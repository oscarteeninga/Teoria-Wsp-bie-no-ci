package main;

public class Consumer implements Runnable {
    private Buffer buffer;
    private int size;
    private int id;
    private long []time;
    private int []receive;
    private boolean end;

    public Consumer(Buffer buffer, int size, int id) {
        this.size = size;
        this.buffer = buffer;
        this.id = id;
        this.time = new long[size];
        this.receive = new int[size];
        for (int i = 0; i < size; i++) {
            time[i] = 0;
            receive[i] = 0;
        }
        this.end = false;
    }

    public void run() {
        for (int k = 0;k < 100;k++) {
            int count = (int) (Math.random() * size);
            long start = System.nanoTime();
            buffer.take(count);
            time[count] += System.nanoTime() - start;
            receive[count]++;
            //System.out.println("Consumer\t " + id + "\ttaken:\t" + count);
            try {
                Thread.sleep((long) Math.random() * 0);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            this.end = true;
        }
    }

    public double getTime(int index) {
        if (receive[index] == 0) {
            return 0;
        } else {
            return (double) (time[index] / receive[index]);
        }
    }

    public boolean isEnd() {
        return this.end;
    }
}
