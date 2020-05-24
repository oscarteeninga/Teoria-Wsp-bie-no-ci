package main;

public class MonitorRunner {
    public static void main(String args[]) {
        int N = 11;
        int M = 3;
        if (N <= M) {
            System.out.println("N must be greater than M");
            return;
        }
        Thread threads[] = new Thread[N];
        Printer printers[] = new Printer[M];
        BoundedBuffer printersBuffer = new BoundedBuffer();
        for (int i = 0; i < M; i++) {
            printers[i] = new Printer();
            try {
                printersBuffer.put(i);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        MonitorPrinters monitorPrinters = new MonitorPrinters(M, printersBuffer);
        for (int i = 0; i < N; i++) {
            final int id = i;
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (;;) {
                        try {
                            int printer_nr = monitorPrinters.order();
                            String napis = "Zadanie watku " + id + " przez drukarkę " + printer_nr + " drukowanie...";
                            printers[printer_nr].print(napis);
                            Thread.sleep(1000);
                            printers[printer_nr].print("Drukowanie przez wątek " + id + " przez drukarkę " + printer_nr + " zakończone.");
                            monitorPrinters.give(printer_nr);
                            Thread.sleep(10000/(id+1));
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
        }

        for (int i = 0; i < N; i++) {
            threads[i].start();
        }
    }
}
