package main;

public class Counter {
    private int tmp;

    public Counter(int tmp) {
        this.tmp = tmp;
    }

    public synchronized void increment() {
        this.tmp++;
    }

    public synchronized void decrement() {
        this.tmp--;
    }

    public synchronized int getValue() {
        return this.tmp;
    }
}
