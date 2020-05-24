package main;

import org.jfree.ui.RefineryUtilities;

import java.util.concurrent.Semaphore;

public class Runner {
    private static int N = 250;
    private static Thread philosopher[] = new Thread[N];
    private static Semaphore waiter = new Semaphore(N-1);
    private static Fork forks[] = new NaiveFork[N];
    
    public static void main(String args[]) {
        for (int i = 0; i < N; i++) {
            forks[i] = new NaiveFork(i);
        }
        hierarchy();
    }

    public static void hierarchy() {
        PhilosopherHierarchy[] hierarchies = new PhilosopherHierarchy[N];
        long results[] = new long[N];

        for (int i = 0; i < N; i++) {
            hierarchies[i] = new PhilosopherHierarchy(forks, i);
            philosopher[i] = new Thread(hierarchies[i]);
            philosopher[i].start();
        }
        try {
            for (int i = 0; i < N; i++) {
                philosopher[i].join();
                System.out.println("Time for " + i + "\t-\t" +hierarchies[i].time);
                results[i] = hierarchies[i].time/1000;
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        plot("Hierarchy", results);
    }


    public static void waiter() {
        PhilosopherWaiter[] waiters = new PhilosopherWaiter[N];
        long results[] = new long[N];

        for (int i = 0; i < N; i++) {
            waiters[i] = new PhilosopherWaiter(waiter, forks, i);
            philosopher[i] = new Thread(waiters[i]);
            philosopher[i].start();
        }
        try {
            for (int i = 0; i < N; i++) {
                philosopher[i].join();
                System.out.println("Time for " + i + "\t-\t" +waiters[i].time);
                results[i] = waiters[i].time/1000;
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        plot("Waiter", results);
    }

    public static void asym() {
        PhilosopherAsym[] asyms = new PhilosopherAsym[N];
        long results[] = new long[N];

        for (int i = 0; i < N; i++) {
            asyms[i] = new PhilosopherAsym(forks, i);
            philosopher[i] = new Thread(asyms[i]);
            philosopher[i].start();
        }
        try {
            for (int i = 0; i < N; i++) {
                philosopher[i].join();
                System.out.println("Time for " + i + "\t-\t" +asyms[i].time);
                results[i] = asyms[i].time/1000;
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        plot("Asym", results);
    }

    public static void hungry() {
        PhilosopherHungry[] hungries = new PhilosopherHungry[N];
        long results[] = new long[N];
        for (int i = 0; i < N; i++) {
            hungries[i] = new PhilosopherHungry(forks, i);
            philosopher[i] = new Thread(hungries[i]);
            philosopher[i].start();
        }
        try {
            for (int i = 0; i < N; i++) {
                philosopher[i].join();
                System.out.println("Time for\t" + i + "\t-\t" +hungries[i].time);
                results[i] = hungries[i].time/1000;
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        plot("Hungry", results);
    }

    private static void plot(String bufType, long[] result) {
        Plot plot = new Plot(bufType + " - Acquire time(N:" + N + ")", result);
        plot.pack();
        RefineryUtilities.centerFrameOnScreen(plot);
        plot.setVisible(true);
    }
}
