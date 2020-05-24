package main;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Waiter {
    private final Lock lock;
    private final Condition []pair;
    private final Condition available;
    private int []request;
    private int atTable;

    public Waiter(int N) {
        lock = new ReentrantLock();
        pair = new Condition[N];
        available = lock.newCondition();
        request = new int[N];
        for (int i = 0; i < N; i++) {
            pair[i] = lock.newCondition();
            request[i] = 0;
        }
        atTable = 0;
    }

    public void order(int person) throws InterruptedException {
        lock.lock();
        try {
            System.out.println("Klient " + person + " chce otrzymać stolik...");
            request[person]++;
            if (request[person] < 2) {
                pair[person].await();
            } else {
                while (atTable > 0) {
                    available.await();
                }
                atTable = 2;
                System.out.println("Para " + person + " dostała stolik.");
                pair[person].signal();
                request[person] = 0;
            }
        } finally {
            lock.unlock();
        }
    }

    public void free() throws InterruptedException {
        lock.lock();
        try {
            atTable--;
            System.out.println("Osoba zwolniła stolik...");
            if (atTable == 0) {
                available.signal();
                System.out.println("Stolik zwolniony!");
            }
        } finally {
            lock.unlock();
        }
    }
}
