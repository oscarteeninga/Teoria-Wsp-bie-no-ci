package main;

public class SemaphoreRunner {

    public static void binary() {
        Counter counter = new Counter(0);
        BinarySemaphore binSem = new BinarySemaphore();
        Thread incThread = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 100; i++) {
                    binSem.P();
                    counter.increment();
                    binSem.V();
                }
            }
        });
        Thread decThread = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 100; i++) {
                    binSem.P();
                    counter.decrement();
                    binSem.V();
                }
            }
        });

        System.out.println("Binary semaphore counter test" + counter.getValue());
        decThread.start();
        incThread.start();

        try {
            decThread.join();
            incThread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("Value of counter: " + counter.getValue());
    }

    public static void main(String []args) {
        binary();
        counter();
    }

    public static void counter() {
        int size = 10;
        Product tmpProduct = new Product(0);
        Basket basket = new Basket(size);

        Thread threads[] = new Thread[2*size];

        for (int i = 0; i < 2*size; i++) {
            final int id = i;
            threads[i] = new Thread(new Customer(basket, tmpProduct, id));
        }

        System.out.println("Count semaphore basket-products test");
        for (int i = 0; i < 2*size; i++) {
            threads[i].start();
        }

        try {
            for (int i = 0; i < 2*size; i++) {
                threads[i].join();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
