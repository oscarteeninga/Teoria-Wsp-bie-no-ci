package main;

public class Restaurant {
    public static void main(String args[]) {
        int N = 10;
        Waiter waiter = new Waiter(N);
        for (int i = 0; i < N; i++) {
            new Thread(new Client(i, waiter)).start();
            new Thread(new Client(i, waiter)).start();
        }
    }
}
