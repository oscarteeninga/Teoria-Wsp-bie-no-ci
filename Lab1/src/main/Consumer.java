package main;

public class Consumer implements Runnable {
    private Buffer buffer;
    private int ilosc;
    private int id;

    public Consumer(Buffer buffer, int ile, int id) {
        this.buffer = buffer;
        this.ilosc = ile;
        this.id = id;
    }

    public void run() {
        for(int i = 0; i < this.ilosc; i++) {
            System.out.println(this.buffer.take() + " was received by Consumer " + this.id);
        }
    }
}
