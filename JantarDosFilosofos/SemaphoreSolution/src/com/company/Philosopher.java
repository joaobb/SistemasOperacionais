package com.company;

public class Philosopher implements Runnable {
    private final int i;
    private final int timeToEat;
    private final int timeToThink;

    private final int THINKING = 0;
    private final int HUNGRY = 1;
    private final int EATING = 2;

    private SharedTableSemaphore table;
    private int state;

    Philosopher(int i, int timeToEat, int thinkTime, SharedTableSemaphore table) {
        this.i = i;
        this.timeToEat = timeToEat;
        this.timeToThink = thinkTime;
        this.table = table;
        this.state = 0;

        new Thread(this, "Philosopher").start();
    }

    private void think() {
        try {
            this.state = THINKING;
            Thread.sleep(this.timeToThink);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void eat() {
        try {
            if (this.state == EATING) {
                Thread.sleep(this.timeToEat);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void takeForks() {
        if (this.table.takeForks(i)) {
            this.state = EATING;
        }
    }

    private void putForks() {
        if (this.state == EATING) {
            this.table.releaseForks(i);
            this.state = THINKING;
        }
    }

    public void run() {
        while (true) {
            try {
                System.out.printf("Philosopher %d > Starts thinking.%n", i);
                think();
                System.out.printf("Philosopher %d > Stops thinking.%n", i);

                takeForks();
                eat();
                putForks();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
