package Solution;

public class MainSemaphore {
    private static final int TABLE_SIZE = 5;
    private static final int TIME_TO_THINK = 1000;
    private static final int TIME_TAKEN_TO_EAT = 500;

    public static void main(String[] args) {
        // Creating table
        SharedTableSemaphore table = new SharedTableSemaphore(MainSemaphore.TABLE_SIZE);

        // Creating N philosophers
        for (int i = 0; i < MainSemaphore.TABLE_SIZE; i++) {
            new Philosopher(i, MainSemaphore.TIME_TAKEN_TO_EAT, MainSemaphore.TIME_TO_THINK, table);
        }
    }
}
