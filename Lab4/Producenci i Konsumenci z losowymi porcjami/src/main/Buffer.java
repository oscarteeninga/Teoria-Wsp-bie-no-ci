package main;

public interface Buffer {
    void take(int count);
    void put(int count);
    int getBuffer();
}
