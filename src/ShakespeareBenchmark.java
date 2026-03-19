import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class ShakespeareBenchmark {

    public static void main(String[] args) throws Exception {

        // Load all words from all .txt files inside shakespeare-dataset-main/text/
        List<String> words = loadWords("shakespeare-dataset-main/text");

        System.out.println("Loaded " + words.size() + " words\n");

        // Custom Hashtable
        ShakespeareHashTable custom = new ShakespeareHashTable(50000);

        long start = System.nanoTime();
        for (String w : words) {
            custom.put(w);
        }
        long end = System.nanoTime();

        long customTime = end - start;

        // Java HashMap
        HashMap<String, Integer> map = new HashMap<>();

        start = System.nanoTime();
        for (String w : words) {
            map.put(w, map.getOrDefault(w, 0) + 1);
        }
        end = System.nanoTime();

        long javaTime = end - start;

        // Results
        System.out.println("=== Custom Hashtable ===");
        System.out.println("Time: " + customTime / 1_000_000 + " ms");
        System.out.println("Total collisions: " + custom.getTotalCollisions());
        System.out.println("Max bucket size: " + custom.getMaxBucketSize());
        System.out.println("Load factor: " + (double) custom.getSize() / custom.getCapacity());

        System.out.println("\n=== Java HashMap ===");
        System.out.println("Time: " + javaTime / 1_000_000 + " ms");
        System.out.println("Load factor (approx): 0.75");
    }

    private static List<String> loadWords(String folder) throws Exception {

        List<String> allWords = new ArrayList<>();

        try {
            Files.walk(Paths.get(folder))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".txt"))
                .forEach(path -> {
                    try {
                        Files.lines(path)
                            .flatMap(line -> Arrays.stream(line
                                .toLowerCase()
                                .split("[^a-z]+")))
                            .filter(s -> !s.isEmpty())
                            .forEach(allWords::add);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return allWords;
    }
}
