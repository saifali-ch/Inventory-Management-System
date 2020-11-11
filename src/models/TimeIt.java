package models;

public class TimeIt {
    public static void code(Runnable block) {
        long start = System.nanoTime();
        try {
            block.run();
        } finally {
            long end = System.nanoTime();
            System.out.printf("Time Taken : sec=%f - nano-sec=%d%n", (end - start) / 1.0e9 , (end - start));
        }

    }
}
