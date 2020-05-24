package main;

public class CountSemaphore {
    int counter;
    int max;

    public CountSemaphore(int max) {
        this.counter = 0;
        this.max = max;
    }

    public synchronized void V() {
        try {
            while (counter <= 0) {
                wait();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        counter--;
        System.out.println("Semaphore given!");
        notifyAll();
    }

    public synchronized void P() {
        try {
            while (counter >= max) {
                wait();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        counter++;
        System.out.println("Semaphore taken!");
        notifyAll();
    }
}
