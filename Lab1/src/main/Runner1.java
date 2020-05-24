package main;

public class Runner1 {
    public static void main(String args[]) {
        int size = 100000000;
        Counter tmp = new Counter(0);

        Thread th1 = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < size; i++) tmp.increment();
            }
        });
        Thread th2 = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < size; i++) tmp.decrement();
            }
        });

        th1.start();
        th2.start();

        try {
            th1.join();
            th2.join();
        } catch(InterruptedException ex) {
            ex.printStackTrace();
        }

        System.out.println("Value of tmp: " + tmp.getValue());
    }
}
