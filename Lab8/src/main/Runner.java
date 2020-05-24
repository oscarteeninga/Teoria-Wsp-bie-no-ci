package main;

import java.util.concurrent.locks.ReentrantLock;

public class Runner {
    static int readers = 0;
    static int writers = 0;
    static int count = 10;

    static FIFO queue = new FIFO();
    static ReentrantLock mutex = new ReentrantLock();
    static Semaphore S = new Semaphore();


    public static void main(String[] args) {
        articleSolution();
    }

    static public void articleSolution() {
        Thread []threads;
        Thread []threads2;

        threads = new Thread[count];

        for (int i = 0; i < count; i++) {
            threads[i] = new Thread(new Reader(i));
        }
        threads[count/2] = new Thread(new Writer(count/2));
        threads[count-1] = new Thread(new Writer(count-1));

        for (int i = 0; i < count; i++) {
            threads[i].start();
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        System.out.println("Second attempt:");
        threads2 = new Thread[count];

        for (int i = 0; i < count; i++) {
            threads2[i] = new Thread(new Reader(i));
        }
        threads2[count/2] = new Thread(new Writer(count/2));
        threads2[count-1] = new Thread(new Writer(count-1));

        for (int i = 0; i < count; i++) {
            threads2[i].start();
        }
    }
}
