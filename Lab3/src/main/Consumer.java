package main;

public class Consumer implements Runnable {
    private BoundedBuffer buffer;
    private int ilosc;
    private int id;

    public Consumer(BoundedBuffer buffer, int ile, int id) {
        this.buffer = buffer;
        this.ilosc = ile;
        this.id = id;
    }

    public void run() {
        for(int i = 0; i < this.ilosc; i++) {
            try {
                System.out.println(this.buffer.take() + " was received by Consumer " + this.id);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
