package Solution;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class SharedTableSemaphore implements SharedTable {
    private final int THINKING = 0;
    private final int HUNGRY = 1;
    private final int EATING = 2;

    private final int tableSize;
    private final int[] table;

    private final Semaphore mutex;
    private final Semaphore[] philosophers;

    SharedTableSemaphore(int tableSize) {
        this.tableSize = tableSize;
        this.table = new int[this.tableSize];

        this.mutex = new Semaphore(1);
        this.philosophers = new Semaphore[this.tableSize];
        Arrays.fill(this.philosophers, new Semaphore(0));
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
            table[i] = HUNGRY;

            printAction(i, "trying to take them forks", "HUNGRY", "EATING");
            test(i);

            mutex.release();
            philosophers[i].acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void releaseForks(int i) {
        try {
            mutex.acquire();

            printAction(i, "dropping forks", "EATING", "THINKING");
            table[i] = THINKING;

            test(getLeft(i));
            test(getRight(i));

            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mutex.release();
    }

    private void test(int i) {
        if (table[i] == HUNGRY && table[getLeft(i)] != EATING && table[getRight(i)] != EATING) {
            printAction(i, "successfully taking forks", "HUNGRY", "EATING");
            table[i] = EATING;
            philosophers[i].release();
        } else if (table[i] == HUNGRY) {
            printAction(i, String.format("failed at taking forks since the philosopher %s is already eating",
                    table[getLeft(i)] == EATING ? getLeft(i) + 1 : getRight(i) + 1), "HUNGRY", "HUNGRY");
        }
    }

    private void printAction(int id, String action, String currentState, String nextState) {
        System.out.printf("Philosopher %d > Is %s and is %s and will be %s.%n", id + 1, currentState, action, nextState);
    }
}
