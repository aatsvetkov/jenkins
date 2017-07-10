package com.epam.cdp.m2.hw2.aggregator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelTaskExecutor<T> {

    private List<Callable<T>> tasks = new ArrayList<>();
    private int threadCount;

    public ParallelTaskExecutor(int threadCount) {
        this.threadCount = threadCount;
    }

    void addTask(final Callable<T> task) {
        this.tasks.add(task);
    }

    public List<T> performAllTasksAndGetResults() {
        if (tasks.isEmpty()) {
            return Collections.emptyList();
        }
        final ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        final List<T> taskResults = new ArrayList<>();
        try {
            final List<Future<T>> taskResultPromises = executor.invokeAll(tasks);
            for (final Future<T> taskResultPromise : taskResultPromises) {
                taskResults.add(taskResultPromise.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        executor.shutdown();
        return taskResults;
    }

}
