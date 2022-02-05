package com.napier.sem;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class App
{
    public static void main(String[] args)
    {
        //Connecting to MongoDb on Local System - On port 27000
        MongoClient mongoClient = new MongoClient("localhost", 27000);
        //Get a db
        MongoDatabase database = mongoClient.getDatabase("mydb");
        //Get a Collection from the DB
        MongoCollection<Document> collection = database.getCollection("test");
        //Create a Document to store
        Document doc = new Document("name", "Hamza Shabir")
                .append("class", "Software Engineering Methods")
                .append("year", "2022")
                .append("result", new Document("CW", "95").append("EX", 85));
        //Add document to Collection
        collection.insertOne(doc);

        //Check document in collection
        Document myDoc = collection.find().first();
        //Print Doc contents as JSON
        System.out.println(myDoc.toJson());
    }
}
