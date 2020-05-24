package main;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FairBuffer implements Buffer {
    private int size;
    private Lock lock;
    private Condition firstProducer;
    private Condition put;
    private Condition firstConsumer;
    private Condition take;

    private boolean firstProducerOccupied;
    private boolean firstConsumerOccupied;

    private int buffer;

    private void initialize(int size) {
        this.lock = new ReentrantLock();
        this.firstProducer = lock.newCondition();
        this.put = lock.newCondition();
        this.firstConsumer = lock.newCondition();
        this.take = lock.newCondition();
        this.size = size;
        this.firstConsumerOccupied = false;
        this.firstProducerOccupied = false;
        this.buffer = 0;
    }

    public FairBuffer(int size) {
        initialize(size);
    }

    public void take(int count) {
        lock.lock();
        try {
            if(firstConsumerOccupied) {
                firstConsumer.await();
            }
            firstConsumerOccupied = true;

            while (getBuffer() < count) {
                take.await();
            }
            //System.out.print("Buffer:c\t" + getBuffer());
            changeBuffer(-count);
            //System.out.println("\t  ->  \t" + getBuffer());

            firstConsumerOccupied = false;

            firstConsumer.signal();
            put.signal();
        } catch(InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void put(int count) {
        lock.lock();
        try {
            if (firstProducerOccupied) {
                firstProducer.await();
            }
            firstProducerOccupied = true;

            while (getBuffer() + count >= size) {
                put.await();
            }
            //System.out.print("Buffer:p\t" + getBuffer());
            changeBuffer(count);
            //System.out.println("\t  ->  \t" + getBuffer());

            firstProducerOccupied = false;

            take.signal();
            firstProducer.signal();
        } catch(InterruptedException ex) {
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

    private boolean getFirstConsumerOccupied() {
        return this.firstConsumerOccupied;
    }

    private boolean getFirstProducerOccupied() {
        return this.firstProducerOccupied;
    }

}
