package Solution;

public class MainMonitor {
    private static final int TABLE_SIZE = 5;
    private static final int TIME_TO_THINK = 1000;
    private static final int TIME_TAKEN_TO_EAT = 500;

    public static void main(String[] args) {
        // Creating table
        SharedTable table = new SharedTableMonitor(MainMonitor.TABLE_SIZE);

        // Creating N philosophers
        for (int i = 0; i < MainMonitor.TABLE_SIZE; i++) {
            new Philosopher(i, MainMonitor.TIME_TAKEN_TO_EAT, MainMonitor.TIME_TO_THINK, table);
        }
    }
}
