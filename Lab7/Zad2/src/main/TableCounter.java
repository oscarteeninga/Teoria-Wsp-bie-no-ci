package main;

import java.util.concurrent.RecursiveTask;

public class TableCounter extends RecursiveTask<Long> {
    private int table[];
    private int begin;
    private int end;

    public TableCounter(int table[], int begin, int end) {
        this.table = table;
        this.begin = begin;
        this.end = end;
    }

    public Long compute() {
        long sum = 0;
        if (end - begin < 50000000) {
            for (int i = begin; i < end; i++) {
                sum += table[i];
            }
        } else {
            int middle = (begin+end)/2;

            TableCounter task1 = new TableCounter(table, begin, middle);
            TableCounter task2 = new TableCounter(table, middle, end);

            task1.fork();
            task2.fork();

            sum += task1.join() + task2.join();
        }
        return sum;
    }
}
