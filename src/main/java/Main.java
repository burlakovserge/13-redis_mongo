public class Main {
    public static void main(String[] args) throws InterruptedException {
        Mongo students = new Mongo("test");

        parseCSVtoMongoCollection();
        students.printResults();
        students.printCollection();
    }

    private static void parseCSVtoMongoCollection() {
        Runtime r = Runtime.getRuntime();
        Process p = null;
        String command = "mongoimport --db test " +
                "--collection users --type csv " +
                "-f name,age,courses " +
                "--file C:\\IdeaProjects\\Module-13-nosql\\contacts.csv";
        try {
            p = r.exec(command);
            System.out.println("Reading csv into Database");

        } catch (Exception e) {
            System.out.println("Error executing " + command + e.toString());
        }
    }
}