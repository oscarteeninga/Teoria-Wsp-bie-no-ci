package main;

import java.util.concurrent.locks.ReentrantLock;

public class Reader implements Runnable {
    private Semaphore S;
    private ReentrantLock mutex;
    private int id;

    public Reader(int id) {
        this.S = Runner.S;
        this.mutex = Runner.mutex;
        this.id = id;
    }

    public void run() {
        mutex.lock();
        if (Runner.writers > 0 || Runner.readers == 0) {
            S.wait_(id);
        }
        Runner.readers += 1;
        mutex.unlock();

        System.out.println("Reader " + this.id + " reading resource.");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("Reader " + this.id + " stopped reading resource.");

        mutex.lock();
        Runner.readers -= 1;
        if (Runner.readers == 0) {
            S.signal_();
        }
        mutex.unlock();
    }
}
