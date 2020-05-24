package main;

public class Process implements Runnable {
    private Buffer buffer;
    private final int id;

    public Process(Buffer buffer, int id) {
        this.buffer = buffer;
        this.id = id;
    }

    public void run() {
        for (int i = 0; i < buffer.getSize(); i++) {
            try {
                String msg = buffer.take(id, i);
                System.out.println("Process " + id + " take buffer " + i + ": " + msg);
                Thread.sleep((int) (Math.random() * 50));
                int index = Integer.parseInt(msg.split(":")[2]) + 1;
                String newmsg = msg.split(":")[0] + ":" + msg.split(":")[1] +  ":" + index;
                buffer.put(id, i, newmsg);
                System.out.println("Process " + id + " put buffer " + i + ": " + newmsg);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
