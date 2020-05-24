package main;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private final Lock lock = new ReentrantLock();
    final Condition[] conditions;

    private String[] msgs;
    private int size;
    private int[] caller;

    public Buffer(int size) {
        this.size = size;
        conditions = new Condition[size];
        msgs = new String[size];
        caller = new int[size];
        for (int i = 0; i < size; i++) {
            conditions[i] = lock.newCondition();
            caller[i] = 0;
        }
    }

    public void put(int id, int index, String msg) {
        lock.lock();
        try {
            while (caller[index] != id) {
                conditions[index].await();
            }
            caller[index]++;
            msgs[index] = msg;
            conditions[index].signalAll();
        } catch(InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    public String take(int id, int index) {
        lock.lock();
        String result = new String();
        try {
            while (caller[index] != id) {
                conditions[index].await();
            }
            result = msgs[index];
            msgs[index] = "";
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
        return result;
    }

    public int getSize() {
        return this.size;
    }
}
