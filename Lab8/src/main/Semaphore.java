package main;

import java.util.concurrent.locks.Condition;

public class Semaphore {
    private int value;
    private FIFO queue;
    private Integer[] users;
    private Integer first;
    private Condition []cond;

    public Semaphore() {
        this.users = new Integer[Runner.count];
        this.cond = new Condition[Runner.count];
        for (int i = 0; i < Runner.count; i++) {
            this.users[i] = i;
            this.cond[i] = Runner.mutex.newCondition();
        }

        this.value = 1;
        this.first = -1;
        this.queue = Runner.queue;
    }

    public void wait_(int id) {
        if (this.value == 0) {
            queue.add(id);
            if (first != id) {
                try {
                    this.cond[id].await();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            this.value = 0;
        }
    }

    public void signal_() {
        if (!this.queue.isEmpty()) {
            first = queue.poll();
            this.cond[first].signalAll();
        } else {
            this.value = 1;
        }
    }
}
