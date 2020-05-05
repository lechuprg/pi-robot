package com.robot.db;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.robot.msg.response.OperationTypes;
import com.robot.msg.response.ServiceDetail;
import com.robot.sensor.WeatherInfo;
import com.robot.utils.DateTime;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by Lechu on 04.09.2016.
 */

@Service
public class WeatherDb {
    public static final String WEATHER_COLLECTION = "Weather";
    public static final String TEMPERATURE = "Temperature";
    public static final String HUMIDITY = "Humidity";
    MongoCollection<Document> collection;

    public WeatherDb() {
        MongoDatabase instance = MongoDb.getInstance();
        collection = instance.getCollection(WEATHER_COLLECTION);
    }

    public void saveCurrentWeather(WeatherInfo weatherInfo) {
        LocalDateTime dataTime = LocalDateTime.now();
        Document doc = new Document(WEATHER_COLLECTION, DateTime.toDateTime(dataTime));
        doc.append(TEMPERATURE, weatherInfo.getTemperatureCelsius());
        doc.append(HUMIDITY, weatherInfo.getHumidity());
        collection.insertOne(doc);
    }

    public ServiceDetail getLastWeatherInfo() {
        try {
            ServiceDetail sd = new ServiceDetail(OperationTypes.TEMPERATURE_CHECK);
            FindIterable<Document> tempInfo = collection.find().sort(new Bson() {
                @Override
                public <TDocument> BsonDocument toBsonDocument(Class<TDocument> tDocumentClass, CodecRegistry codecRegistry) {
                    return null;
                }
            }).limit(1);
            Document document = tempInfo.iterator().next();
            sd.addKeyValue(TEMPERATURE, String.valueOf(document.get(TEMPERATURE)));
            sd.addKeyValue(HUMIDITY, String.valueOf(document.get(HUMIDITY)));
            return sd;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ServiceDetail getWeatherStatistics(int days) {
        try {

            LocalDateTime dateTime = LocalDateTime.now();
            LocalDateTime startDate = dateTime.minusDays(days);
            Date date = DateTime.toDate(startDate);
            ServiceDetail sd = new ServiceDetail(OperationTypes.TEMPERATURE_CHECK);

            BasicDBObject query = new BasicDBObject();
            query.put(WEATHER_COLLECTION, new BasicDBObject("$gte", date));
            FindIterable<Document> temperature = collection.find(query).sort(new BasicDBObject(WEATHER_COLLECTION, -1));
            DecimalFormat df = new DecimalFormat("#0.00");
            for (Document doc : temperature) {
                sd.addKeyValue(String.valueOf(doc.get(WEATHER_COLLECTION)), df.format(doc.get(TEMPERATURE)) + "|" + df.format(doc.get(HUMIDITY)));
            }
            return sd;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
