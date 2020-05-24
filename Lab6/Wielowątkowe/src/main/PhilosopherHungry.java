package main;

public class PhilosopherHungry implements Runnable {
    private int id;
    private Fork[] forks;
    public long time;

    public PhilosopherHungry(Fork[] forks, int id) {
        this.forks = forks;
        this.id = id;
        this.time = 0;
    }

    public void run() {
        try {
            for (int i = 0; i < 1000; i++) {
                long start = System.nanoTime();
                while (forks[id].isAcquired() || forks[(id+1)%forks.length].isAcquired()) {
                }
                forks[id].get(id);
                forks[(id+1)%forks.length].get(id);
                time = (i*time + System.nanoTime() - start)/(i+1);
                //System.out.println("Philosopher " + id + " is eating " + i + " time...");
                Thread.sleep(1);
                forks[id].put(id);
                forks[(id+1)%forks.length].put(id);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
