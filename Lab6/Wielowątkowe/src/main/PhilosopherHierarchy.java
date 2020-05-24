package main;

public class PhilosopherHierarchy implements Runnable {
    private int id;
    private Fork[] forks;
    public long time;

    public PhilosopherHierarchy(Fork[] forks, int id) {
        this.forks = forks;
        this.id = id;
        this.time = 0;
    }

    public void run() {
        try {
            for (int i = 0; i < 50; i++) {
                long start = System.nanoTime();
                int f1 = id;
                int f2 = (id+1)%forks.length;
                if (f1 > f2) {
                    int tmp = f2;
                    f2 = f1;
                    f1 = tmp;
                }
                forks[f1].get(id);
                forks[f2].get(id);
                time = (i*time + System.nanoTime() - start)/(i+1);
                //System.out.println("Philosopher " + id + " is eating " + i + " time...");
                Thread.sleep(1);
                forks[f2].put(id);
                forks[f1].put(id);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
