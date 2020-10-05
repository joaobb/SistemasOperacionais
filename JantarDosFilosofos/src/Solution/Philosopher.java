package Solution;

public class Philosopher implements Runnable {
    private final int i;
    private final int timeToEat;
    private final int timeToThink;

    private final int THINKING = 0;
    private final int HUNGRY = 1;
    private final int EATING = 2;

    private final SharedTable table;
    private int state;

    Philosopher(int i, int timeToEat, int thinkTime, SharedTable table) {
        this.i = i;
        this.timeToEat = timeToEat;
        this.timeToThink = thinkTime;
        this.table = table;
        this.state = 0;

        new Thread(this, "Philosopher " + i).start();
    }

    private void think() {
        try {
            System.out.printf("Philosopher %d > Starts THINKING.%n", i + 1);
            this.state = THINKING;
            Thread.sleep(this.timeToThink);
            System.out.printf("Philosopher %d > Stops THINKING and will be HUNGRY.%n", i + 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void eat() {
        try {
            this.state = EATING;
            Thread.sleep(this.timeToEat);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void takeForks() {
        this.state = HUNGRY;
        this.table.takeForks(i);
    }

    private void putForks() {
        this.table.releaseForks(i);
        this.state = THINKING;
    }

    public void run() {
        while (true) {
            try {
                think();
                takeForks();
                eat();
                putForks();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
