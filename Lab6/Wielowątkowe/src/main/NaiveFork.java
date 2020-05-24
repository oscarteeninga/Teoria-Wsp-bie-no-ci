package main;

import java.util.concurrent.Semaphore;

public class NaiveFork implements Fork {
    private int id;
    private Semaphore sem;

    public NaiveFork(int id) {
        this.id = id;
        this.sem = new Semaphore(1);
    }

    public void get(int id) throws InterruptedException{
        sem.acquire();
        //System.out.println("Fork " + this.id + " acquired by " + id + ".");
    }

    public void put(int id) {
        //System.out.println("Fork " + this.id + " released by " + id + ".");
        sem.release();
    }

    public synchronized boolean isAcquired() {
        return sem.availablePermits() == 0;
    }
}
