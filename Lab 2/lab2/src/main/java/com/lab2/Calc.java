package com.lab2;

public class Calc implements Runnable {
    private SyncronizeQueue<Task> taskQueue;
    private SyncronizeQueue<Answer> resultsList;
    private boolean in_work;

    public Calc(SyncronizeQueue<Task> taskQueue, SyncronizeQueue<Answer> resultsList) {
        this.taskQueue = taskQueue;
        this.resultsList = resultsList;
    }

    @Override
    public void run() {
        Task task;
        Answer answer = new Answer(-1);
        int n=0,N =0;
        in_work = false;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                task = taskQueue.take();
                in_work = true;
                N = task.N;
                answer = new Answer(task.id);
                for (n = 1; n <= task.N;n++) {
                    answer.ans += 4 *  Math.pow(-1,n-1)/(2*n-1);
                    answer.persent_of_completion = (int) (100.0*n/N);
                    Thread.sleep(30);
                }

                answer.is_completed=true;
                resultsList.put(answer);
                System.out.println(answer);
                in_work = false;
            }
        } catch (InterruptedException e) {
            if (in_work) {
                System.out.println(answer);
                resultsList.put(answer);
            }
            Thread.currentThread().interrupt();
        }
    }
}
