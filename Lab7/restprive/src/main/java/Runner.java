import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Runner {
    private static List<String> urls = Arrays.asList("https://ekipatonosi.pl/strona/dostawa-i-platnosci",
            "https://ekipatonosi.pl/kategoria/t-shirty/t-shirt-kolor",
            "https://ekipatonosi.pl/kategoria/longsleeve",
            "https://ekipatonosi.pl/kategoria/longsleeve/longsleeve-ekipatonosi-02",
            "https://ekipatonosi.pl/kolekcja/nowosci/czapka-zimowa-ekipa-rozowa",
            "https://ekipatonosi.pl/kolekcja/nowosci/czapka-zimowa-ekipa-czarna",
            "https://ekipatonosi.pl/kolekcja/nowosci/zeszyt-ekipa",
            "https://ekipatonosi.pl/kategoria/gadzety/pokemon-tcg-hidden-fates",
            "https://ekipatonosi.pl/kategoria/gadzety/brelok-wersow",
            "https://ekipatonosi.pl/kategoria/gadzety/opaska-ekipatonosi-kasia",
            "https://ekipatonosi.pl/kategoria/gadzety/kubek-friz",
            "https://ekipatonosi.pl/kategoria/gadzety/opaska-ekipatonosi-mini-majk",
            "https://ekipatonosi.pl/kategoria/t-shirty/zestaw-kolor",
            "https://ekipatonosi.pl/kolekcja/nowosci/t-shirt-eki-pa",
            "https://ekipatonosi.pl/kategoria/t-shirty/t-shirt-ekipa-02",
            "https://ekipatonosi.pl/kategoria/gadzety/brelok-mini-majk",
            "https://ekipatonosi.pl/kategoria/gadzety/brelok-mixer",
            "https://ekipatonosi.pl/kategoria/gadzety/brelok-friz",
            "https://ekipatonosi.pl/kategoria/gadzety/brelok-wersow",
            "https://ekipatonosi.pl/kategoria/gadzety/brelok-marta",
            "https://ekipatonosi.pl/kategoria/gadzety/brelok-tromba"
    );
    public static void main(String argv[]) {
        notParallel();
        parallel();
    }

    public static void notParallel() {
        int tasks = urls.size();
        PriceGetter []tasksTable = new PriceGetter[tasks];
        for (int i = 0; i < tasks; i++) {
            tasksTable[i] = new PriceGetter(i, urls.get(i % urls.size()));
        }
        double sum = 0;
        long start = System.nanoTime();
        for (int i = 0; i < tasks; i++) {
            sum += tasksTable[i].getPrice();
        }
        System.out.println("Time: " + (System.nanoTime() - start)/1000000);
        System.out.println("Not parallel result is: " + sum);
    }

    public static void parallel() {
        double sum = 0;
        int threads = urls.size();
        int tasks = urls.size();
        ExecutorService service = Executors.newFixedThreadPool(threads);
        Callable[] tasksTable = new Callable[tasks];
        List<Future<Float>> futures = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < tasks; i++) {
            tasksTable[i] = new PriceGetter(i, urls.get(i % urls.size()));
        }
        for (int i = 0; i < tasks; i++) {
            Future<Float> future = service.submit(tasksTable[i]);
            futures.add(future);
        }
        try {
            for (int i = 0; i < tasks; i++) {
                sum += futures.get(i).get();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Time: " + (System.nanoTime() - start)/1000000);
        System.out.println("Parallel result is: " + sum);

    }
}
