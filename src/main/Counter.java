package main;

public class Counter {
    private int tmp;

    public Counter(int tmp) {
        this.tmp = tmp;
    }

    public void increment() {
        this.tmp = this.tmp + 1;
        try {
            Thread.sleep(1);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void decrement() {
        this.tmp = this.tmp - 1;
        try {
            Thread.sleep(1);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public int getValue() {
        return this.tmp;
    }
}
