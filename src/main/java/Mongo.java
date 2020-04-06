import com.mongodb.client.*;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Accumulators.*;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.set;

public class Mongo {
    private MongoDatabase database;
    private MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");

    public Mongo(String databaseName) {
        database = mongoClient.getDatabase(databaseName);
    }

    public void printCollection(String collectionName) {
        FindIterable<Document> list = database.getCollection(collectionName).find();
        for (Document elem : list) {
            System.out.println(elem);
        }
    }

    public void addMarket(String name) {
        Document market = new Document("name", name).append("productsList", new ArrayList());
        database.getCollection("stores").insertOne(market);
    }

    public void addProduct(String name, int price) {
        Document product = new Document("name", name).append("price", price);
        database.getCollection("products").insertOne(product);
    }

    public void delete(String collectionName, String key, String value) {
        database.getCollection(collectionName).deleteOne(eq(key, value));
    }

    public void addProductToMarket(String marketName, String productName) {
        database.getCollection("stores").
                updateOne(eq("name", marketName),
                        Updates.addToSet("productsList", productName));

    }

    public void createCollection(String collectionName) {
        database.createCollection(collectionName);
    }

    public void getCountOfProductsByAllStores() {
        Bson lookup = new Document("$lookup",
                new Document("from", "stores")
                        .append("localField", "name")
                        .append("foreignField", "productsList")
                        .append("as", "products_list"));

        Bson unwind = unwind("$products_list");
        Bson group = group(null, sum("Количество товаров", 1));

        List<Bson> filters = new ArrayList<>();
        filters.add(lookup);
        filters.add(unwind);
        filters.add(group);

        AggregateIterable<Document> result = database.getCollection("products")
                .aggregate(filters);
        for (Document res : result) {
            System.out.println("Количество товаров во всех магазинах: " + res.get("Количество товаров"));
        }
    }

    public void storeStatistics(String storeName) {
        Bson lookup = new Document("$lookup",
                new Document("from", "stores")
                        .append("localField", "name")
                        .append("foreignField", "productsList")
                        .append("as", "products_list"));

        Bson unwind = unwind("$products_list");
        Bson matchEq = match(eq("products_list.name", storeName));
        Bson matchLt = match(lt("price", 100));
        Bson group = group("products_list", sum("Количество товаров", 1),
                max("maxPrice", "$price"), min("minPrice", "$price"),
                avg("avgPrice", "$price"));

        List<Bson> filters1 = new ArrayList<>();
        filters1.add(lookup);
        filters1.add(unwind);
        filters1.add(matchEq);
        filters1.add(group);

        List<Bson> filters2 = new ArrayList<>();
        filters2.add(lookup);
        filters2.add(unwind);
        filters2.add(matchEq);
        filters2.add(matchLt);
        filters2.add(group);

        AggregateIterable<Document> result1 = database.getCollection("products")
                .aggregate(filters1);
        for (Document res : result1) {
            System.out.println("Статистика магазина: " + storeName + ": \n" +
                    "Количество товаров " + ": " + res.get("Количество товаров") + "\n" +
                    "\t Самый дорогой товар " + res.get("maxPrice") + "\n" +
                    "\t Самый дешевый товар " + res.get("minPrice") + "\n" +
                    "\t Средняя стоимость товаров " + res.get("avgPrice"));

            AggregateIterable<Document> result2 = database.getCollection("products").aggregate(filters2);
            for (Document res2 : result2) {
                    System.out.println("\t Количество товаров дешевле 100 руб: " + res2.get("Количество товаров"));
            }
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

    //---------
    //        Bson bson = Filters.gt("age", 40);
//        FindIterable<Document> resultWithBson = usersList.find(bson);
//        for (Document element : resultWithBson) {
//            System.out.println("ему больше 40 лет " + element);
//        }
//        System.out.println(" количество студентов старше 40 лет: " + resultWithBson);
//-----------
//    public void parseCSVtoMongoCollection() {
//        Runtime r = Runtime.getRuntime();
//        Process p = null;
//        String command = "mongoimport --db test " +
//                "--collection users --type csv " +
//                "-f name,age,courses " +
//                "--file C:\\IdeaProjects\\redis\\contacts.csv";
//        try {
//            p = r.exec(command);
//            System.out.println("Reading csv into Database");
//
//        } catch (Exception e) {
//            System.out.println("Error executing " + command + e.toString());
//        }
//    }
    //--------- Использование запросов
//    Bson unwind = unwind("$products_list");
//    Bson match = match(eq("products_list.name", "Lenta"));
//    Bson group = group("products_list", sum("Количество товаров", 1));
//    Bson projection = project(fields(include("Количество товаров"), excludeId()));
    //-------------
    //Добавить в коллекцию один объект, в поле с типом массив
    //Document score = new Document().append("name", "blinchik").append("price", 500);
}
