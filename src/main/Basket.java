package main;

public class Basket {
    Product products[];
    int actual;
    int size;

    public Basket(int size) {
        this.size = size;
        this.products = new Product[size];
        this.actual = -1;
    }

    public synchronized Product getProduct() {
        if (actual == -1) {
            System.out.println("Basket empty!");
            return null;
        }
        System.out.println("Product given!");
        return products[actual--];
    }

    public synchronized void addProduct(Product product) {
        if (actual >= size) {
            System.out.println("Basket full!");
            return;
        }
        if (actual == -1) {
            products[++actual] = product;
            System.out.println("Product taken!");
        } else {
            products[actual++] = product;
            System.out.println("Product taken!");
        }
    }
}
