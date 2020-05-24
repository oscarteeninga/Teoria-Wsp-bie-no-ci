package main;

public class Consumer implements Runnable {
    private Buffer buffer;
    private int id;

    public Consumer(Buffer buffer, int id) {
        this.buffer = buffer;
        this.id = id;
    }
    public void run() {
        for (int i = 0; i < buffer.getSize(); i++) {
            String msg = new String();
            try {
                msg = buffer.take(id, i);
                Thread.sleep((int) Math.random() * 100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.out.println("Consumer " + id + " taken: " + msg);
        }
    }
}
