package main;

import java.io.IOException;

public class Runner {
    public static void main(String args[]) {
        WordCounter wordCounter = new WordCounter(4, 40);
        try {
            long start = System.nanoTime();
            System.out.println(wordCounter.countSingle("src/main/files/quo_vadis.txt"));
            System.out.println(wordCounter.countSingle("src/main/files/don_kichot_z_la_manchy.txt"));
            System.out.println("Time for single:\t" + (System.nanoTime() - start));
            start = System.nanoTime();
            System.out.println(wordCounter.countMulti("src/main/files/quo_vadis.txt"));
            System.out.println(wordCounter.countMulti("src/main/files/don_kichot_z_la_manchy.txt"));
            System.out.println("Time for multi:\t\t" + (System.nanoTime() - start));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
