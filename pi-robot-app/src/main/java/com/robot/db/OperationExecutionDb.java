package com.robot.db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.robot.msg.response.OperationTypes;
import com.robot.msg.response.Status;
import org.bson.Document;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by Lechu on 10.10.2016.
 */
public class OperationExecutionDb {
    public static final String OPERATION_STATUS = "OperationStatus";
    public static final String STATUS = "Status";
    private static final String OPERATION_TYPE = "OperationType";
    MongoCollection<Document> collection;

    public OperationExecutionDb() {
        MongoDatabase instance = MongoDb.getInstance();
        collection = instance.getCollection(OPERATION_STATUS);
    }

    public void saveOperationExecution(OperationTypes operationType, Status status) {
        LocalDateTime dataTime = LocalDateTime.now();
        Document doc = new Document(OPERATION_STATUS, toDateTime(dataTime));
        doc.append(OPERATION_TYPE, operationType);
        doc.append(STATUS, status);
        collection.insertOne(doc);
    }

    private Date toDateTime(LocalDateTime dataTime) {
        Instant instant = dataTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }
}
