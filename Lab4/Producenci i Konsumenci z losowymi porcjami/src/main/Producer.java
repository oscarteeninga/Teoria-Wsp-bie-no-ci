package main;

public class Producer implements Runnable {
    private Buffer buffer;
    private int size;
    private int id;
    private long []time;
    private int []send;
    private boolean end;

    public Producer(Buffer buffer, int size, int id) {
        this.buffer = buffer;
        this.size = size;
        this.id = id;
        this.time = new long[size];
        this.send = new int[size];
        for (int i = 0; i < size; i++) {
            time[i] = 0;
        }
        this.end = false;
    }

    public void run() {
        for (int k = 0;k < 100;k++) {
            int count = (int) (Math.random() * size);
            long start = System.nanoTime();
            buffer.put(count);
            time[count] += System.nanoTime() - start;
            send[count]++;
            //System.out.println("Producer\t " + id + "\tput:\t" + count);
            try {
                Thread.sleep((long) Math.random() * 0);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        this.end = true;
    }

    public double getAverageTime(int index) {
        if (send[index] == 0) {
            return 0;
        } else {
            return (double) (time[index] / send[index]);
        }
    }

    public boolean isEnd() {
        return this.end;
    }
}
