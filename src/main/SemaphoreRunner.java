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
        int size = 3;
        Product tmpProduct = new Product(0);
        Basket basket = new Basket(size);
        CountSemaphore countSem = new CountSemaphore(size);

        Thread threads[] = new Thread[2*size];

        for (int i = 0; i < 2*size; i++) {
            final int id = i;
            threads[i] = new Thread(new Runnable() {
                public void run() {
                    for (int j = 0; j < 10; j++) {
                        countSem.P();
                        System.out.println("Thread " + id + " loop " + j);
                        basket.addProduct(tmpProduct);
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        Product tmp = basket.getProduct();
                        System.out.println("End of thread " + id + " loop " + j);
                        countSem.V();
                    }
                }
            });
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
