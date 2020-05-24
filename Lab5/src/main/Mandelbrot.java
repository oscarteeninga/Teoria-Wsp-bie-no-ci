package main;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import javax.swing.JFrame;

public class Mandelbrot extends JFrame {

    private final int MAX_ITER = 2570;
    private final double ZOOM = 500;
    private BufferedImage I;
    private static long []results = new long[10];

    public Mandelbrot(int threads, int tasks, int iteration) {
        super("Mandelbrot Set");

        ExecutorService service = Executors.newFixedThreadPool(threads);
        Callable []callables = new Callable[tasks];
        List<Future<Object>> futures = new ArrayList<>();

        setBounds(200, 200, 800, 800);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        I = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        if (tasks == 640000) {
            System.out.println("fdsfdsdfsfds");
            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    final int xd = x;
                    final int yd = y;
                    callables[y+getHeight()*x] = new Callable() {
                        @Override
                        public Object call() throws Exception {
                            double tmp, cX, cY, zy, zx;
                            zx = 0;
                            zy = 0;
                            cX = (xd - 400) / ZOOM;
                            cY = (yd - 300) / ZOOM;
                            int iter = MAX_ITER;
                            while (zx * zx + zy * zy < 4 && iter > 0) {
                                tmp = zx * zx - zy * zy + cX;
                                zy = 2.0 * zx * zy + cY;
                                zx = tmp;
                                iter--;
                            }
                            I.setRGB(xd, yd, iter | (iter << 8));
                            return null;
                        }
                    };
                }
            }
        } else {
            for (int i = 0; i < tasks; i++) {
                final int j = i;
                callables[i] = new Callable() {
                    @Override
                    public Object call() throws Exception {
                        double tmp, cX, cY, zy, zx;
                        for (int y = j * getHeight() / tasks; y < (j + 1) * getHeight() / tasks; y++) {
                            for (int x = 0; x < getWidth(); x++) {
                                zx = 0;
                                zy = 0;
                                cX = (x - 400) / ZOOM;
                                cY = (y - 300) / ZOOM;
                                int iter = MAX_ITER;
                                while (zx * zx + zy * zy < 4 && iter > 0) {
                                    tmp = zx * zx - zy * zy + cX;
                                    zy = 2.0 * zx * zy + cY;
                                    zx = tmp;
                                    iter--;
                                }
                                I.setRGB(x, y, iter | (iter << 8));
                            }
                        }
                        return null;
                    }
                };
            }
        }

        long start = System.nanoTime();

        for (int i = 0; i < tasks; i++) {
            Future<Object> future = service.submit(callables[i]);
            futures.add(future);
        }

        for (int i = 0; i < tasks; i++) {
            Future<Object> future = futures.get(i);
            try {
                 future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        long time = (System.nanoTime() - start);
        results[iteration] = time;
        //System.out.println("Time for " + threads + " threads, " + tasks + " tasks, time: " + time/1000000);
        service.shutdown();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(I, 0, 0, this);
    }

    public static void printResults(int threads, int tasks) {
        long sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += results[i];
        }
        long avg = sum/10;
        long odchylenie = 0;
        for (int i = 0; i < 10; i++) {
            long tmp = (long) Math.pow((results[i] - avg), 2);
            odchylenie += tmp;
        }
        System.out.println(threads + " Threads, " + tasks + " tasks - AVG: " + avg + ", σ = " + Math.sqrt(odchylenie));
    }

    public static void test(int threads) {
        //for (int i = 0; i < 10; i++ ) { new Mandelbrot(threads, 1*threads, i); }
        //printResults(threads, 1*threads);
        //for (int i = 0; i < 10; i++ ) { new Mandelbrot(threads, 10*threads, i); }
        //printResults(threads, 10*threads);
        for (int i = 0; i < 10; i++ ) { new Mandelbrot(threads, 640000, i).setVisible(true); }
        printResults(threads, 640000);
    }

    public static void main(String[] args) {
        test(1);
        test(2);
        test(4);
    }
}

//        1 Threads, 1      tasks - AVG: 3322994836,    σ = 1.6002276218951344E9
//        1 Threads, 10     tasks - AVG: 3002480415,    σ = 6.913286346675304E7
//        1 Threads, 640000 tasks - AVG: 3595987342,    σ = 1.6365222269592813E8
//        2 Threads, 2      tasks - AVG: 1966414447,    σ = 4.082661567184306E7
//        2 Threads, 20     tasks - AVG: 1496230021,    σ = 1.614714347445761E7
//        2 Threads, 640000 tasks - AVG: 1787521501,    σ = 2.0082848749000093E8
//        4 Threads, 4      tasks - AVG: 1097494991,    σ = 3.637386502921624E7
//        4 Threads, 40     tasks - AVG: 789237388,     σ = 9242086.410266353
//        4 Threads, 640000 tasks - AVG: 1038215129,    σ = 2.5359066204582947E8
