package Solution;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class SharedTableMonitor implements SharedTable {
    private final int THINKING = 0;
    private final int HUNGRY = 1;
    private final int EATING = 2;

    private final int tableSize;

    private final int[] table;
    private final Integer[] philosophers;

    SharedTableMonitor(int tableSize) {
        this.tableSize = tableSize;

        this.philosophers = new Integer[this.tableSize];
        this.table = new int[this.tableSize];

        Arrays.fill(this.philosophers, 0);
        Arrays.fill(this.philosophers, this.THINKING);
    }

    private int getLeft(int i) {
        return (i + this.tableSize - 1) % this.tableSize;
    }

    private int getRight(int i) {
        return (i + 1) % this.tableSize;
    }

    public void takeForks(int i) {
        synchronized (philosophers[i]) {
            table[i] = HUNGRY;

            printAction(i, "trying to take forks", "HUNGRY", "EATING");
            test(i);

            while (table[i] != EATING) {
                try {
                    philosophers[i].wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void releaseForks(int i) {
        synchronized (philosophers[i]) {
            printAction(i, "dropping forks", "EATING", "THINKING");
            table[i] = THINKING;
            test(getLeft(i));
            test(getRight(i));
        }
    }

    private void test(int i) {
        if (table[i] == HUNGRY && table[getLeft(i)] != EATING && table[getRight(i)] != EATING) {
            printAction(i, "successfully taking forks", "HUNGRY", "EATING");
            table[i] = EATING;

            synchronized (philosophers[i]) {
                philosophers[i].notify();
            }

        } else if (table[i] == HUNGRY) {
            printAction(i, String.format("failed at taking forks since the philosopher %s is already eating",
                    table[getLeft(i)] == EATING ? getLeft(i) + 1 : getRight(i) + 1), "HUNGRY", "HUNGRY");
        }
    }

    private void printAction(int id, String action, String currentState, String nextState) {
        System.out.printf("Philosopher %d > Is %s and is %s and will be %s.%n", id + 1, currentState, action, nextState);
    }
}
