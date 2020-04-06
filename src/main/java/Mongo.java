import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import org.bson.Document;
import com.mongodb.client.model.Filters;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Filters.*;

public class Mongo {
    private MongoDatabase database;
    private MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    private MongoCollection<Document> collection;

    public Mongo(String databaseName) {
        database = mongoClient.getDatabase(databaseName);
        collection = database.getCollection("users");
    }

    public void printCollection() {
        FindIterable<Document> list = collection.find();
        for (Document elem : list) {
            System.out.println(elem);
        }
    }

    public void printResults(){
        long countStudents = collection.countDocuments();
        long countStudentsGreatherThan40Years = collection.countDocuments(gt("age", 40));
        String nameYoungStudent = collection.find().sort(new BasicDBObject("age",1)).limit(1).first().get("name").toString();
        String coursesOldStudent = collection.find().sort(new BasicDBObject("age",-1)).limit(1).first().get("courses").toString();

        System.out.println("Общее количество студентов в базе: " + countStudents + "\n" +
                "Количество студентов старше 40 лет: " + countStudentsGreatherThan40Years + "\n" +
        "Имя самого молодого студента: " + nameYoungStudent + "\n" +
                "Список курсов самого старого студента: " + coursesOldStudent);
    }

}


//Методы, которые "пригодятся в жизни"
//    public void updateObject() {
//        database.getCollection("stores").updateOne(
//                eq("name", "DNS"),
//                set("goodsList", Arrays.asList("qq", "dd")));
//    }

//Поиск продукта по названию. Возвращает объект со всеми полями. Ранее ложил его в список магазина.
//    private Document findProduct(String key) {
//Найти объект по полю и значению, взять первый (если их будет несколько). В результате будет без поля id
//        return database.getCollection("products").find(eq("name", key)).projection(Projections.excludeId()).first();
//Найти объект по полю и значению, взять первый (если их будет несколько). В результате будут все поля.
//return database.getCollection(collectionName).find(eq("name", key)).first();
//    }

//--------- Использование запросов
//    Bson unwind = unwind("$products_list");
//    Bson match = match(eq("products_list.name", "Lenta"));
//    Bson group = group("products_list", sum("Количество товаров", 1));
//    Bson projection = project(fields(include("Количество товаров"), excludeId()));
//-------------
//Добавить в коллекцию один объект, в поле с типом массив
//Document score = new Document().append("name", "blinchik").append("price", 500);
