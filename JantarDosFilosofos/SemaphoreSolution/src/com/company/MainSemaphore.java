package com.company;

public class MainSemaphore {
    private static int tableSize = 2;
    private static int timeToThink = 1000;
    private static int timeToEat = 500;

    public static void main(String[] args) {
        // Creating table
        SharedTableSemaphore table = new SharedTableSemaphore(MainSemaphore.tableSize);


        for (int i = 1; i <= tableSize; i++) {
            new Philosopher(i, MainSemaphore.timeToEat, MainSemaphore.timeToThink, table);
        }
    }

}
