package main;

public class Buffer {
    private String str;

    public Buffer() {
        str = null;
    }

    public synchronized void put(String msg) {
        while (str != null) {
            try {
                System.out.println("Buffer is busy, waiting for receive element!");
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("Putting element...");
        str = msg;
        notifyAll();
    }

    public synchronized String take() {
        while (str == null) {
            try {
                System.out.println("Buffer is empty, waiting for put element");
                wait();
            } catch(InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("Taking element...");
        String tmp = str;
        str = null;
        notifyAll();
        return tmp;
    }
}
