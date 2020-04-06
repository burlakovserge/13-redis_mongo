public class Main {
    public static void main(String[] args) throws InterruptedException {
        Mongo sales = new Mongo("sales");
        //Заполним коллекции магазинов и товаров
//        initData(sales);

        //Добавим товары из списка товаров в соответствующие магазины
//        storesAreNotEmpty(sales);

        sales.getCountOfProductsByAllStores();
        sales.storeStatistics("Lenta");
        sales.storeStatistics("DNS");
        sales.storeStatistics("Alkomarket");

        sales.printCollection("stores");
        sales.printCollection("products");
    }

    public static void initData(Mongo sales) {
        sales.createCollection("products");
        sales.createCollection("stores");
        sales.addMarket("DNS");
        sales.addMarket("Lenta");
        sales.addMarket("Alkomarket");
        sales.addProduct("milk", 50);
        sales.addProduct("beer", 100);
        sales.addProduct("pelmeni", 140);
        sales.addProduct("tea", 80);
        sales.addProduct("potato", 30);
        sales.addProduct("monitor", 4000);
        sales.addProduct("CPU", 10000);
        sales.addProduct("videocard", 30000);
        sales.addProduct("mouse", 99);
        sales.addProduct("memory", 6000);
        sales.addProduct("cheese", 280);
        sales.addProduct("wine", 500);
        sales.addProduct("rum", 1300);
    }

    public static void storesAreNotEmpty(Mongo sales) {
        sales.addProductToMarket("Lenta", "milk");
        sales.addProductToMarket("Lenta", "beer");
        sales.addProductToMarket("Lenta", "pelmeni");
        sales.addProductToMarket("Lenta", "tea");
        sales.addProductToMarket("Lenta", "potato");
        sales.addProductToMarket("Lenta", "rum");
        sales.addProductToMarket("DNS", "mouse");
        sales.addProductToMarket("DNS", "monitor");
        sales.addProductToMarket("DNS", "CPU");
        sales.addProductToMarket("DNS", "memory");
        sales.addProductToMarket("DNS", "videocard");
        sales.addProductToMarket("Alkomarket", "rum");
        sales.addProductToMarket("Alkomarket", "wine");
        sales.addProductToMarket("Alkomarket", "cheese");
        sales.addProductToMarket("Alkomarket", "beer");
        sales.addProductToMarket("Alkomarket", "tea");

    }
}