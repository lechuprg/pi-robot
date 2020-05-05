package com.robot.db;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * Created by Lechu on 31.08.2016.
 */
public class MongoDb {
    static final MongoClient mongoClient = new MongoClient();
    static final MongoDatabase db = mongoClient.getDatabase("pirobot");

    private MongoDb() {}

    public static MongoDatabase  getInstance() {
        return db;
    }
}
