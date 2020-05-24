package main;

public class Basket {
    private Product products[];
    private int actual;
    private int size;
    private CountSemaphore countSem;
    private BinarySemaphore binSem;

    public Basket(int size) {
        this.size = size;
        this.products = new Product[size];
        this.actual = -1;
        this.countSem = new CountSemaphore(size-1);
        this.binSem = new BinarySemaphore();
    }

    public int size() { return this.size; }

    public Product getProduct() {
        binSem.P();
        if (actual == -1) {
            System.out.println("Basket empty!");
            return null;
        }
        System.out.println("Product given!");
        Product tmp = products[actual];
        actual--;
        binSem.V();
        countSem.V();
        return tmp;
    }

    public void addProduct(Product product) {
        countSem.P();
        binSem.P();
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
        binSem.V();
    }
}
