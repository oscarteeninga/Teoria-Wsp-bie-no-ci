package main;

public interface Fork {
    void get(int id) throws InterruptedException;
    void put(int id);
    boolean isAcquired();
}
