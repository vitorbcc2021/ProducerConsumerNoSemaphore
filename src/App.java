public class App {
    public static void main(String[] args) {
        Runnable producer = new Buffer();
        Runnable consumer = new Buffer();

        Thread t1 = new Thread(producer, "Producer");
        Thread t2 = new Thread(consumer, "Consumer");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
