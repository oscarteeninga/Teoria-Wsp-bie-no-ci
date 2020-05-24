package main;

import java.util.ArrayList;
import java.util.List;

public class Runner {
    public static void main(String args[]) {
        Buffer buffer = new Buffer(10);

        List<Thread> threads = new ArrayList<>();
        threads.add(new Thread(new Producer(buffer, 0)));

        for (int i = 0; i < 5; i++) {
            threads.add(new Thread(new Process(buffer, i+1)));
        }

        threads.add(new Thread(new Consumer(buffer, 6)));

        for (Thread thread : threads) {
            thread.start();
        }

        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
