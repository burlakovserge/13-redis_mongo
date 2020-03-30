public class Main {
    public static void main(String[] args) throws InterruptedException {
        JedisStorage js = new JedisStorage();

        //Первая часть задания
        js.CitiesAndFlyghtCost();

        //Вторая часть задания
        js.createUsersList("DaringSite");
        js.printResult("DaringSite");
    }
}