package com.lab2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws InterruptedException {
        SyncronizeQueue<Answer> resultQueue = new SyncronizeQueue<>();
        SyncronizeQueue<Task> taskQueue = new SyncronizeQueue<>();

        if (args.length != 1) {
            System.out.println("Doesn't have enough arguments");
            return;
        }

        int n_threads = Integer.parseInt(args[0]);
        List<Calc> workerList = new ArrayList<>();
        List<Thread> threadList = new ArrayList<>();

        for (int i = 0; i < n_threads; i++) {
            Calc calculator = new Calc(taskQueue, resultQueue);
            workerList.add(calculator);
            Thread thread = new Thread(calculator);
            threadList.add(thread);
            thread.start();
        }
        int i = 1;
        Scanner scanner = new Scanner(System.in);
        String currentTask;
        while (!(currentTask = scanner.nextLine()).equals("exit")) {
            if (!isNumeric(currentTask))
                continue;
            int currentValue = Integer.parseInt(currentTask);
            taskQueue.put(new Task(currentValue, i));
            i++;
        }

        taskQueue.stop();

        for (Thread thread : threadList) {
            thread.interrupt();
        }
        scanner.close();
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
