package main;

public class Runner2 {
    public static void main(String args[]) {
        Buffer buffer = new Buffer();

        Thread cr1 = new Thread(new Consumer(buffer, 1000, 1));
        Thread cr2 = new Thread(new Consumer(buffer, 1000, 2));
        Thread pr1 = new Thread(new Producer(buffer, 1000, 1));
        Thread pr2 = new Thread(new Producer(buffer, 1000, 2));

        pr1.start();
        cr1.start();
        pr2.start();
        cr2.start();

        try {
             pr1.join();
             cr1.join();
             pr2.join();
             cr2.join();
        } catch(InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
