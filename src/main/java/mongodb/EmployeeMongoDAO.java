package mongodb;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import ifaces.EmployeeDAO;
import models.Employee;
import org.bson.BsonDocument;
import org.bson.Document;
import org.mortbay.util.ajax.JSON;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;

public class EmployeeMongoDAO implements EmployeeDAO {

  public Employee getEmployee(String id) {
    return null;
  }

  public void printDatabases() {
     MongoIterable<String> databaseNames = MongoDBClient.mongoClient.listDatabaseNames();
     for (String database : databaseNames) System.out.println(database);

  }

  public void printDBCollections(String dbName) {
    MongoDatabase database = MongoDBClient.mongoClient.getDatabase(dbName);
    MongoIterable<String> collections = database.listCollectionNames();
    for (String collection : collections) System.out.println(collection);
  }

  public void insert(String dbName) {
    MongoDatabase database = MongoDBClient.mongoClient.getDatabase(dbName);
    Document document = new Document();
    document.append("name", "abhay").append("age", 24);
    MongoCollection<Document> users = database.getCollection("users");
    users.insertOne(document);

    // using insertOne
    MongoCollection < Document > books = database.getCollection("books");
    books.insertOne(
            new Document("authors", new Document().append("author_id", "1").append("name", "Chetan Bhagat"))
                    .append("book_id", "1").append("title", "One Indian Girl").append("isbn", "8129142147")
                    .append("price", "$14.99"));

    List< Document > booksList = new ArrayList<>();

    booksList
            .add(new Document("authors", new Document().append("author_id", "2").append("name", "Sarina Singh"))
                    .append("book_id", "2").append("title", "Lonely Planet India").append("isbn", "1743216769")
                    .append("price", "$34.99"));

    booksList.add(new Document("authors", new Document().append("author_id", "3").append("name", "John Keay"))
            .append("book_id", "3").append("title", "India: A History").append("isbn", "0802145582")
            .append("price", "$20.00"));
    // using insertMany
    books.insertMany(booksList);
  }

  public void printAllDocuments(String dbName) {
    MongoDatabase database = MongoDBClient.mongoClient.getDatabase(dbName);
    MongoCollection < Document > books = database.getCollection("books");
    MongoCursor < Document > cursor = books.find().iterator();
    try {
      while (cursor.hasNext()) {
        Document book = cursor.next();
        System.out.println(book);
      }
    } finally {
      cursor.close();
    }
  }

  public void delete(String dbName) {
    MongoDatabase database = MongoDBClient.mongoClient.getDatabase(dbName);
    MongoCollection < Document > books = database.getCollection("books");
    DeleteResult deleteResult = books.deleteOne(eq("book_id", "1"));
    System.out.println("deleteOne() - # of records deleted - " + deleteResult.getDeletedCount());

    deleteResult = books.deleteMany(gte("book_id", "3"));
    System.out.println("deleteOne() - # of records deleted - " + deleteResult.getDeletedCount());
  }

  public void update(String dbName) {
    MongoDatabase database = MongoDBClient.mongoClient.getDatabase(dbName);
    MongoCollection < Document > books = database.getCollection("books");
    UpdateResult updateResult = books.updateOne(eq("book_id", "1"),
            new Document("$set", new Document("title", "**Updated** - One Indian Girl")
                    .append("price", "$18.00").append("authors.name", "**Update** - Chetan Bhagat")));
    System.out.println("Number of record updated:- " + updateResult.getModifiedCount());
  }

}
