package main;

public class Producer implements Runnable {
    private BoundedBuffer buffer;
    private int ilosc;
    private int id;

    public Producer(BoundedBuffer buffer, int ile, int id) {
        this.buffer = buffer;
        this.ilosc = ile;
        this.id = id;
    }

    public void run() {
        for(int i = 0; i < this.ilosc; i++) {
            try {
                this.buffer.put("Message " + i + " send by Producer " + this.id);
            } catch(InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
