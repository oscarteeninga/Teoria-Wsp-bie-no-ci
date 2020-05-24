package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class WordCounter {
    private int threads;
    private int tasks;

    public WordCounter(int threads, int tasks) {
        this.threads = threads;
        this.tasks = tasks;
    }

    public long countMulti(String fileName) throws IOException {
        ExecutorService service = Executors.newFixedThreadPool(threads);
        Callable[] callables = new Callable[tasks];
        List<Future<Long>> futures = new ArrayList<>();

        String text = new String(Files.readAllBytes(Paths.get(fileName)));
        List<String> result = Arrays.asList(text.split(" "));


        int size = result.size();
        int part = size/tasks;
        int p = 0;
        for (; p < tasks-1; p++) {
            final int j = p;
            callables[p] = new Callable() {
                @Override
                public Long call() {
                    return result.subList(j*part, (j+1)*part).stream()
                            .filter(w -> !w.equals(","))
                            .filter(w -> !w.equals(" "))
                            .filter(w -> !w.equals("."))
                            .filter(w -> !w.equals(")"))
                            .filter(w -> !w.equals("("))
                            .count();
                }
            };
        }
        final int j = p;
        callables[j] = new Callable() {
            @Override
            public Long call() {
                return result.subList(j*part, size).stream()
                        .filter(w -> !w.equals(","))
                        .filter(w -> !w.equals(" "))
                        .filter(w -> !w.equals("."))
                        .filter(w -> !w.equals(")"))
                        .filter(w -> !w.equals("("))
                        .count();
            }
        };

        for (int i = 0; i < tasks; i++) {
            Future<Long> future = service.submit(callables[i]);
            futures.add(future);
        }

        long sum = 0;

        for (int i = 0; i < tasks; i++) {
            Future<Long> future = futures.get(i);
            try {
                sum += future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        service.shutdown();

        return sum;
    }

    public long countSingle(String fileName) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(fileName)));
        List<String> result = Arrays.asList(text.split(" "));
        return result.stream()
                .filter(w -> !w.equals(","))
                .filter(w -> !w.equals(" "))
                .filter(w -> !w.equals("."))
                .filter(w -> !w.equals(")"))
                .filter(w -> !w.equals("("))
                .count();
    }
}
