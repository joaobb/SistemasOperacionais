package com.company;

import java.util.concurrent.Semaphore;

public class SharedTableSemaphore {
    private final int THINKING = 0;
    private final int HUNGRY = 1;
    private final int EATING = 2;

    private final int tableSize;
    private final int[] philosophers;

    private final Semaphore mutex;
    private final Semaphore[] forks;

    SharedTableSemaphore(int tableSize) {
        this.tableSize = tableSize;
        this.philosophers = new int[this.tableSize];

        this.mutex = new Semaphore(1);
        this.forks = new Semaphore[]{
                new Semaphore(0),
                new Semaphore(0),
                new Semaphore(0),
                new Semaphore(0),
                new Semaphore(0),
        };
    }

    private int getLeft(int i) {
        return (i + this.tableSize - 1) % this.tableSize;
    }

    private int getRight(int i) {
        return (i + 1) % this.tableSize;
    }

    public void takeForks(int i) {
        try {
            mutex.acquire();
            philosophers[i] = HUNGRY;

            printAction(i, "trying to take them forks", "HUNGRY", "EATING");
            test(i);

            mutex.release();
            forks[i].acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void releaseForks(int i) {
        try {
            mutex.acquire();

            printAction(i, "dropping forks", "EATING", "THINKING");
            philosophers[i] = THINKING;

            test(getLeft(i));
            test(getRight(i));

            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mutex.release();
    }

    private void test(int i) {
        if (philosophers[i] == HUNGRY && philosophers[getLeft(i)] != EATING && philosophers[getRight(i)] != EATING) {
            printAction(i, "successfully taking forks", "HUNGRY", "EATING");
            philosophers[i] = EATING;
            forks[i].release();
        } else if (philosophers[i] == HUNGRY) {
            printAction(i, String.format("failed at taking forks since the philosopher %s is already eating",
                    philosophers[getLeft(i)] == EATING ? getLeft(i) + 1 : getRight(i) + 1), "HUNGRY", "HUNGRY");
        }
    }

    private void printAction(int id, String action, String currentState, String nextState) {
        System.out.printf("Philosopher %d > Is %s %s and will be %s.%n", id + 1, currentState, action, nextState);
    }
}
