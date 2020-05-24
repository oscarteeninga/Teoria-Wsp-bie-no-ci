package main;

import org.jfree.ui.RefineryUtilities;

import java.io.*;
import java.util.Arrays;

public class Runner {
    public static void main (String args[]) throws Exception {
        //tests();
        //test(new NaiveBuffer(2000),1000, 1000, 1000);
        test(new FairBuffer(2000),1000, 1000, 1000);
    }

    private static void testsNaive() {
        //M = 1000
        test(new NaiveBuffer(2000),1000, 10, 10);
        test(new NaiveBuffer(2000),1000, 100, 100);
        test(new NaiveBuffer(2000),1000, 1000, 1000);
        //M = 10k
        test(new NaiveBuffer(20000),10000, 10, 10);
        test(new NaiveBuffer(20000),10000, 100, 100);
        test(new NaiveBuffer(20000),10000, 1000, 1000);
        //M = 100k
        test(new NaiveBuffer(200000),100000, 10, 10);
        test(new NaiveBuffer(200000),100000, 100, 100);
        test(new NaiveBuffer(200000),100000, 1000, 1000);
    }

    private static void testsFair() {
        test(new FairBuffer(2000),1000, 10, 10);
        test(new FairBuffer(2000),1000, 100, 100);
        test(new FairBuffer(2000),1000, 1000, 1000);
        //M = 10k
        test(new FairBuffer(20000),10000, 10, 10);
        test(new FairBuffer(20000),10000, 100, 100);
        test(new FairBuffer(20000),10000, 1000, 1000);
        //M = 100k
        test(new FairBuffer(200000),100000, 10, 10);
        test(new FairBuffer(200000),100000, 100, 100);
        test(new FairBuffer(200000),100000, 1000, 1000);
    }

    private static void test(Buffer buffer, int M, int P, int C) {
        Thread[] threads = new Thread[P+C];
        Producer[] producers = new Producer[P];
        Consumer[] consumers = new Consumer[C];

        for (int i = 0; i < P; i++) {
            producers[i] = new Producer(buffer, M, i);
            threads[i] = new Thread(producers[i]);
        }

        for (int i = 0; i < C; i++) {
            consumers[i] = new Consumer(buffer, M, i);
            threads[i+P] = new Thread(consumers[i]);
        }

        for (int i = 0; i < C+P; i++) {
            threads[i].start();
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        String bufType;
        if (buffer instanceof NaiveBuffer) {
            bufType = "Naive";
        } else {
            bufType = "Fair";
        }
        System.out.println("==================" + bufType + "===================");
        System.out.println("===== M: " + M + ", P: " + P + ", C: " + C + " =====");
        int scale = 1000;
        long []receiveAvg = new long[M];
        long []sendAvg = new long[M];

        for (int i = 0; i < M; i++) {
            receiveAvg[i] = 0;
            for (int j = 0; j < C; j++) {
                receiveAvg[i] += consumers[j].getTime(i);
            }
            receiveAvg[i] = receiveAvg[i]/(C*scale);
            //System.out.println("Rece average time for " + i + " size message\t-\t" + receiveAvg[i]/100000);
        }

        for (int i = 0; i < M; i++) {
            sendAvg[i] = 0;
            for (int j = 0; j < P; j++) {
                sendAvg[i] += producers[j].getAverageTime(i);
            }
            sendAvg[i] = sendAvg[i]/(P*scale);
            //System.out.println("Send average time for " + i + " size message\t-\t" + sendAvg[i]/1000000);
        }

        int receiveEnd = 0, sendEnd = 0;
        for (int j = 0; j < C; j++) {
            if (consumers[j].isEnd()) {
                receiveEnd++;
            }
        }
        for (int j = 0; j < P; j++) {
            if (producers[j].isEnd()) {
                sendEnd++;
            }
        }

        System.out.println(buffer.getBuffer() + " buffer load.");
        System.out.println(C - receiveEnd + " Customers has not ended.");
        System.out.println(P - sendEnd + " Producers has not ended.");
        System.out.println("AVG for receive - " + avgOfTable(receiveAvg) + " µs");
        System.out.println("AVG for send - " + avgOfTable(sendAvg) + " µs");
        plot(bufType, sendAvg, receiveAvg, M, C, P);
    }

    private static void plot(String bufType, long []sendAvg, long []receiveAvg, int M, int C, int P) {
        Plot recplot = new Plot(bufType +" - Recive time(M:" + M + ", C:" + C + ", P:" + P + ")", receiveAvg);
        recplot.pack();
        RefineryUtilities.centerFrameOnScreen(recplot);
        recplot.setVisible(true);
        Plot sndplot = new Plot(bufType +" - Send time(M:" + M + ", C:" + C + ", P:" + P + ")", sendAvg);
        sndplot.pack();
        RefineryUtilities.centerFrameOnScreen(sndplot);
        sndplot.setVisible(true);
    }

    private static double avgOfTable(long []table) {
        double avg = 0;
        for (int i = 0; i < table.length; i++) {
            avg += table[i];
        }
        avg /= table.length;
        return avg;
    }

    private static void saveResultToCSV(long []sendAvg, long []receiveAvg, int M, int C, int P) throws IOException{
        String fileCSV = "result-" + M + "-" + C + "-" + P + ".csv";
        FileWriter writer = new FileWriter(fileCSV);
        int smaller = 1000;
        for (int i = 0; i < M; i++) {
            CSVAdapter.writeLine(writer, Arrays.asList(String.valueOf(i), String.valueOf(sendAvg[i]/smaller), String.valueOf(receiveAvg[i]/smaller)));
        }
        writer.flush();
        writer.close();
    }
}
