package main;

public class Producer implements Runnable {
    private Buffer buffer;
    private int ilosc;
    private int id;

    public Producer(Buffer buffer, int ile, int id) {
        this.buffer = buffer;
        this.ilosc = ile;
        this.id = id;
    }

    public void run() {
        for(int i = 0; i < this.ilosc; i++) {
            this.buffer.put("Message " + i + " send by Producer " + this.id);
        }
    }
}
