package main;

public class Customer implements Runnable {
    Basket basket;
    Product tmpProduct;
    int id;
    CountSemaphore countSem;

    public Customer(Basket basket, Product product, int id) {
        this.basket = basket;
        this.tmpProduct = product;
        this.id = id;
        this.countSem = new CountSemaphore(basket.size());
    }
    public void run() {
        for (int j = 0; j < 10; j++) {
            basket.addProduct(tmpProduct);
            System.out.println("Customer " + id + " loop " + j);
            try {
                Thread.sleep(2);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            Product tmp = basket.getProduct();
            System.out.println("End of customer " + id + " loop " + j);
        }
    }
}