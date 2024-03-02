package com.lab2;

import java.util.LinkedList;
import java.util.Queue;

public class SyncronizeQueue<T> {
    private Queue<T> queue = new LinkedList<T>();
    private boolean isStopped = false;

    public synchronized void put(T element) throws InterruptedException {
        queue.add(element);
        notify();
    }

    public synchronized T take() throws InterruptedException {
        while(queue.isEmpty()) {
            wait(1000);
            if (isStopped && queue.isEmpty()) return null;
        }
        T item = queue.remove();
        notify();
        return item;
    }
    
    public void stop() {
        isStopped = true;
    }

    public void print() {
        for (T item : queue) {
            System.out.println(item);
        }
    }
}
