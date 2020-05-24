package main;

public class Producer implements Runnable {
    private Buffer buffer;
    private int id;

    public Producer(Buffer buffer, int id) {
        this.id = id;
        this.buffer = buffer;
    }

    public void run() {
        for (int i = 0; i < buffer.getSize(); i++) {
            String msg = id + ":" + i + ":0";
            try {
                buffer.put(id, i, msg);
                System.out.println("Producer " + id + " put: " + msg);
                Thread.sleep((long) (Math.random() * 100));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
