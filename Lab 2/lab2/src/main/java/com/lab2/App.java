package com.lab2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws InterruptedException {
        SyncronizeQueue<String> resultQueue = new SyncronizeQueue<>();
        SyncronizeQueue<Integer> taskQueue = new SyncronizeQueue<>();

        if (args.length != 1) {
            System.out.println("Doesn't have enough arguments");
            return;
        }

        int n_threads = Integer.parseInt(args[0]);
        List<Calculator> workerList = new ArrayList<>();
        List<Thread> threadList = new ArrayList<>();

        for (int i = 0; i < n_threads; i++) {
            Calculator calculator = new Calculator(taskQueue, resultQueue);
            workerList.add(calculator);
            Thread thread = new Thread(calculator);
            threadList.add(thread);
            thread.start();
        }

        Scanner scanner = new Scanner(System.in);
        String currentTask;
        while (!(currentTask = scanner.nextLine()).equals("exit")) {
            if (!isNumeric(currentTask))
                continue;
            int currentValue = Integer.parseInt(currentTask);
            taskQueue.put(currentValue);
        }

        taskQueue.stop();

        for (Thread thread : threadList) {
            thread.join();
        }
        scanner.close();
        resultQueue.print();
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
