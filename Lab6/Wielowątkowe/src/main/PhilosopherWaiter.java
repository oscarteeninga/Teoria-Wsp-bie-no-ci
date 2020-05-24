package main;

import java.util.concurrent.Semaphore;

public class PhilosopherWaiter implements Runnable {
    private int id;
    private Fork[] forks;
    private Semaphore waiter;
    public long time;

    public PhilosopherWaiter(Semaphore waiter, Fork[] forks, int id) {
        this.forks = forks;
        this.id = id;
        this.waiter = waiter;
        this.time = 0;
    }

    public void run() {
        try {
            for (int i = 0; i < 1000; i++) {
                long start = System.nanoTime();
                waiter.acquire(1);
                forks[id].get(id);
                forks[(id+1)%forks.length].get(id);
                time = (time*i + System.nanoTime() - start)/(i+1);
                //System.out.println("Philosopher " + id + " is eating " + i + " time...");
                Thread.sleep(1);
                forks[id].put(id);
                forks[(id+1)%forks.length].put(id);
                waiter.release(1);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
