package main;

import java.util.concurrent.ForkJoinPool;

public class Runner {
    public static void main(String args[]) {
        int N = 500000000;
        ForkJoinPool fjp = new ForkJoinPool();
        int[] table = generateTable(N);
        long start = System.nanoTime();
        System.out.println(sum(table) + " - " + (System.nanoTime() - start));
        start = System.nanoTime();
        System.out.println(fjp.invoke(new TableCounter(table, 0, N)) + " - " + (System.nanoTime() - start));
    }

    public static int[] generateTable(int N) {
        int[] table = new int[N];
        for (int i = 0; i < N; i++) {
            table[i] = (int)(Math.random() *2000000000);
        }
        return table;
    }

    public static long sum(int []table) {
        long sum = 0;
        for (int i = 0; i < table.length; i++) {
            sum += table[i];
        }
        return sum;
    }
}
