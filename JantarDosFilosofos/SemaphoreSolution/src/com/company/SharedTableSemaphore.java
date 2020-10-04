package com.company;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class SharedTableSemaphore {
    private final int tableSize;
    private final int[] table;

    private final Semaphore mutex;
    private final Semaphore[] forks;

    SharedTableSemaphore(int tableSize) {
        this.tableSize = tableSize;
        this.table = new int[this.tableSize];

        this.mutex = new Semaphore(1);
        this.forks = new Semaphore[]{
                new Semaphore(1),
                new Semaphore(1),
                new Semaphore(1),
                new Semaphore(1),
                new Semaphore(1),
        };
    }

    public boolean takeForks(int i) {
        try {
            mutex.acquire();
            if (table[(i - 1) % this.tableSize] == 0 && table[i % this.tableSize] == 0) {
                System.out.printf("Philosopher %d > Taking forks %d, and %d.%n",
                        i, (i - 1) % this.tableSize, i % this.tableSize);

                forks[(i - 1) % this.tableSize].acquire();
                forks[(i) % this.tableSize].acquire();

                table[(i - 1) % this.tableSize] = 1;
                table[i % this.tableSize] = 1;

                System.out.println(Arrays.toString(table));

                mutex.release();
                return true;
            } else {
                System.out.printf("Philosopher %d > Couldn't grab forks %d and %d. State: %s.%n", i, (i - 1) %
                        this.tableSize, i % this.tableSize, Arrays.toString(table));
            }
        } catch (InterruptedException e) {
            System.out.println("ERROR");
            e.printStackTrace();
        }
        mutex.release();
        return false;
    }

    public void releaseForks(int i) {
        try {
            mutex.acquire();
            System.out.printf("Philosopher %d > Dropping forks %d, and %d.%n",
                    i, (i - 1) % this.tableSize, i % this.tableSize);
            System.out.println(Arrays.toString(table));

            forks[(i - 1) % this.tableSize].release();
            forks[(i) % this.tableSize].release();

            table[(i - 1) % this.tableSize] = 0;
            table[i % this.tableSize] = 0;

            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mutex.release();
    }
}
