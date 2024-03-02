package com.lab2;

public class Calculator implements Runnable {
    private SyncronizeQueue<Integer> taskQueue;
    private SyncronizeQueue<String> resultsList;

    public Calculator(SyncronizeQueue<Integer> taskQueue, SyncronizeQueue<String> resultsList) {
        this.taskQueue = taskQueue;
        this.resultsList = resultsList;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Integer task = taskQueue.take();
                if (task == null) break;
                String result = String.format("%d * %d = %d", task, task, task * task);
                resultsList.put(result);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
