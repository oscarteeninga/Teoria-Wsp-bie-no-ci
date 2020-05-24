package main;

public class BinarySemaphore {
    LogicalState state;

    public BinarySemaphore() {
        this.state = LogicalState.GIVEN;
    }

    public synchronized void V() {
        try {
            while (this.state.equals(LogicalState.GIVEN)) {
                wait();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        this.state = LogicalState.GIVEN;
        System.out.println("Semaphore given!");
        notifyAll();
    }

    public synchronized void P() {
        try {
            while (this.state.equals(LogicalState.TAKEN)) {
                wait();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        this.state = LogicalState.TAKEN;
        System.out.println("Semaphore taken!");
        notifyAll();
    }
}
