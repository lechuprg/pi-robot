package com.robot.db;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import com.robot.property.PiProperty;
import com.robot.utils.DateTime;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by Lechu on 10.10.2016.
 */
@Service
public class PropertiesDb {
    Logger logger = LoggerFactory.getLogger(PropertiesDb.class);
    public static final String PROPERTIES = "Properties";
    private static final String NAME = "Name";
    private static final String VALUE = "Value";

    MongoCollection<Document> collection;

    public PropertiesDb() {
        MongoDatabase instance = MongoDb.getInstance();
        collection = instance.getCollection(PROPERTIES);
    }

    public void updateProperty(PiProperty property, String value) {
        Document doc = new Document(PROPERTIES, DateTime.now());
        doc.append(NAME, property.toString());
        doc.append(VALUE, value);
        UpdateResult updateResult = collection.updateOne(eq(NAME, property.toString()), new Document("$set", doc));
        logger.info("Update result " + updateResult);
    }

    public Map<String, String> getAllProperty() {
        FindIterable<Document> documents = collection.find();
        Map<String,String> properties = new HashMap<>();
        for(Document doc: documents){
            properties.put(doc.getString(NAME), doc.getString(VALUE));
        }
        return properties;
    }

    public String readProperty(PiProperty piProperty) {
        BasicDBObject query = new BasicDBObject();
        query.append(NAME, piProperty.toString());
        FindIterable<Document> documents = collection.find(query);
        Document doc = documents.iterator().next();
        return (String)doc.get(VALUE);
    }

    public void addProperty(PiProperty property, String value) {
        Document doc = new Document(PROPERTIES, DateTime.now());
        doc.append(NAME, property.toString());
        doc.append(VALUE, value);
        collection.insertOne(doc);
    }

    public void removeProperty(String key) {
        BasicDBObject query = new BasicDBObject();
        query.append(NAME, key);
        collection.deleteOne(query);
    }

    public Map<String, String> getWritableProperty() {
        FindIterable<Document> documents = collection.find();
        Map<String,String> properties = new HashMap<>();
        for(Document doc: documents){
            properties.put(doc.getString(NAME), doc.getString(VALUE));
        }
        Map<String, String> sortedProps = properties.entrySet().stream()
            .filter(entry -> PiProperty.fromString(entry.getKey()).isWritable())
            .sorted(Map.Entry.comparingByKey())
            .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        return sortedProps;
    }
}
