package main;

public class Client implements Runnable {
    private int id;
    private Waiter waiter;

    public Client(int id, Waiter waiter) {
        this.id = id;
        this.waiter = waiter;
    }

    public void run() {
        try {
            for (; ; ) {
                Thread.sleep((id+1)*2000);
                waiter.order(id);
                Thread.sleep(1000);
                waiter.free();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
