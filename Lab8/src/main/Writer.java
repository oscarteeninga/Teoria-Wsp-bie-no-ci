package main;

import java.util.concurrent.locks.ReentrantLock;

public class Writer implements Runnable {
    private Semaphore S;
    private ReentrantLock mutex;
    private int id;

    public Writer(int id) {
        this.S = Runner.S;
        this.mutex = Runner.mutex;
        this.id = id;
    }

    public void run() {
        mutex.lock();
        Runner.writers += 1;
        S.wait_(id);
        mutex.unlock();

        System.out.println("Writer " + this.id + " reading resource.");
        try {
            Thread.sleep(0);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("Writer " + this.id + " stopped reading resource.");

        mutex.lock();
        Runner.writers -= 1;
        S.signal_();
        mutex.unlock();
    }
}
