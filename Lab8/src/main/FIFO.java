package main;

import java.util.LinkedList;

public class FIFO {
    private LinkedList<Integer> queue = new LinkedList<>();

    public void add(Integer obj) {
        queue.add(obj);
    }
    public int poll() {
        return queue.pollLast();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
