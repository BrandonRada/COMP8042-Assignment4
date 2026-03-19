import java.nio.file.*;
import java.util.*;

public class NamesBenchmark {

    public static void main(String[] args) throws Exception {

        // Load names
        List<String> firstNames = Files.readAllLines(Paths.get("first.txt"));
        List<String> lastNames = Files.readAllLines(Paths.get("last.txt"));

        List<String> allNames = new ArrayList<>();
        allNames.addAll(firstNames);
        allNames.addAll(lastNames);

        System.out.println("Loaded " + allNames.size() + " names\n");

        // Separate Chaining
        SeparateChainingHashTable<String> sc = new SeparateChainingHashTable<>(20000);
        runTest("Separate Chaining", sc, allNames);

        // Quadratic Probing
        QuadraticProbingHashTable<String> qp = new QuadraticProbingHashTable<>(20000);
        runTest("Quadratic Probing", qp, allNames);

        // Double Hashing
        DoubleHashingHashTable<String> dh = new DoubleHashingHashTable<>(20000);
        runTest("Double Hashing", dh, allNames);

        // Perfect Hashing
        // PerfectHashTable ph = new PerfectHashTable(allNames);
        // runPerfect("Perfect Hashing", ph, allNames);

        List<String> smallList = allNames.subList(0, 5000);
        PerfectHashTable ph = new PerfectHashTable(smallList);
        runPerfect("Perfect Hashing", ph, smallList);

    }

    private static void runTest(String name, Object table, List<String> names) {

        long startInsert = System.nanoTime();

        for (String n : names) {
            if (table instanceof SeparateChainingHashTable) {
                ((SeparateChainingHashTable<String>) table).insert(n);
            } else if (table instanceof QuadraticProbingHashTable) {
                ((QuadraticProbingHashTable<String>) table).insert(n);
            } else if (table instanceof DoubleHashingHashTable) {
                ((DoubleHashingHashTable<String>) table).insert(n);
            }
        }

        long endInsert = System.nanoTime();

        long startQuery = System.nanoTime();

        for (String n : names) {
            if (table instanceof SeparateChainingHashTable) {
                ((SeparateChainingHashTable<String>) table).contains(n);
            } else if (table instanceof QuadraticProbingHashTable) {
                ((QuadraticProbingHashTable<String>) table).contains(n);
            } else if (table instanceof DoubleHashingHashTable) {
                ((DoubleHashingHashTable<String>) table).contains(n);
            }
        }

        long endQuery = System.nanoTime();

        System.out.println("=== " + name + " ===");

        if (table instanceof SeparateChainingHashTable) {
            SeparateChainingHashTable<String> sc = (SeparateChainingHashTable<String>) table;
            System.out.println("Total collisions: " + sc.getTotalCollisions());
            System.out.println("Max collisions: " + sc.getMaxCollisions());
            System.out.println("Empty slots: " + sc.getEmptySlots());
            System.out.println("Load factor: " + sc.getLoadFactor());
        } else if (table instanceof QuadraticProbingHashTable) {
            QuadraticProbingHashTable<String> qp = (QuadraticProbingHashTable<String>) table;
            System.out.println("Total collisions: " + qp.getTotalCollisions());
            System.out.println("Max collisions: " + qp.getMaxCollisions());
            System.out.println("Empty slots: " + qp.getEmptySlots());
            System.out.println("Load factor: " + qp.getLoadFactor());
        } else if (table instanceof DoubleHashingHashTable) {
            DoubleHashingHashTable<String> dh = (DoubleHashingHashTable<String>) table;
            System.out.println("Total collisions: " + dh.getTotalCollisions());
            System.out.println("Max collisions: " + dh.getMaxCollisions());
            System.out.println("Empty slots: " + dh.getEmptySlots());
            System.out.println("Load factor: " + dh.getLoadFactor());
        }

        System.out.println("Insert time: " + (endInsert - startInsert) / 1_000_000 + " ms");
        System.out.println("Avg query time: " + (endQuery - startQuery) / names.size() + " ns\n");
    }

    private static void runPerfect(String name, PerfectHashTable ph, List<String> names) {

        long startQuery = System.nanoTime();

        for (String n : names) {
            ph.contains(n);
        }

        long endQuery = System.nanoTime();

        System.out.println("=== Perfect Hashing ===");
        System.out.println("Total collisions (during build): " + ph.getTotalCollisions());
        System.out.println("Max collisions: " + ph.getMaxCollisions());
        System.out.println("Empty slots: " + ph.getEmptySlots());
        System.out.println("Load factor: " + ph.getLoadFactor());
        System.out.println("Avg query time: " + (endQuery - startQuery) / names.size() + " ns\n");
    }
}
