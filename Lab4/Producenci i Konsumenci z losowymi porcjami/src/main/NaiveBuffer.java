package main;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NaiveBuffer implements Buffer {
    private Lock lock;
    private int size;
    private int buffer;
    private Condition put;
    private Condition taken;

    public NaiveBuffer(int size) {
        this.size = size;
        this.buffer = 0;
        this.lock = new ReentrantLock();
        this.put = lock.newCondition();
        this.taken = lock.newCondition();
    }

    public void take(int count)  {
        lock.lock();
        try {
            if (count > size/2) {
                throw new IllegalArgumentException("You cannot take more than " + size/2 + " elements.");
            }
            while (getBuffer() < count) {
                put.await();
            }
            //System.out.print("Buffer:c\t" + getBuffer());
            changeBuffer(-count);
            //System.out.println("\t  ->  \t" + getBuffer());
            taken.signalAll();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void put(int count) {
        lock.lock();
        try {
            while (count + getBuffer() >= size) {
                taken.await();
            }
            //System.out.print("Buffer:p\t" + getBuffer());
            changeBuffer(count);
            //System.out.println("\t  ->  \t" + getBuffer());
            put.signalAll();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public synchronized int getBuffer() {
        return this.buffer;
    }

    private synchronized void changeBuffer(int count) {
        this.buffer += count;
    }
}
