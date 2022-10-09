package com.manquius;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    private static int numThreads = 10;
    private static boolean printInternals = true;
    private static List<Integer> intList = IntStream.rangeClosed(1, numThreads).boxed().collect(Collectors.toList());
    private static List<Runnable> heavyList = intList.stream().map(x -> new HeavyProcessing("heavy" + x, 1000, printInternals)).collect(Collectors.toList());
    private static List<Runnable> sleepList = intList.stream().map(x -> new SleepProcessing("sleep" + x, 1000, printInternals)).collect(Collectors.toList());


    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Starting heavy ===");
        simpleThreads(heavyList);

        System.out.println("Starting sleep");
        simpleThreads(sleepList);
    }



    private static void simpleThreads(List<Runnable> list) {
        long startTime = System.currentTimeMillis();
        List<Thread> threads = list.parallelStream().map(x -> Thread.startVirtualThread(x)).collect(Collectors.toList());
        System.out.println("== continue ==");
        waitForAll(threads);
        long endTime = System.currentTimeMillis();
        System.out.println("=== Took: " + (endTime-startTime) + " ===");
    }

    private static void waitForAll(List<Thread> list) {
        list.forEach(x -> {
            try {
                x.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
