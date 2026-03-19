import java.util.*;

// Perfect hashing using two level hashing.
public class PerfectHashTable {
    private static class BucketTable {
        String[] table;
        int size;
        int seed;

        BucketTable(int size) {
            this.size = size;
            this.table = new String[size];
            this.seed = 0;
        }
    }


    // First level buckets
    private List<String>[] buckets;
    // Second level perfect tables
    private BucketTable[] secondLevel;
    private int totalKeys;

    // Collect data
    private long totalCollisions = 0;
    private long maxCollisions = 0;

    public PerfectHashTable(List<String> keys) {
        totalKeys = keys.size();

        // First level table size = next prime >= n
        int m = nextPrime(keys.size());
        buckets = new List[m];

        for (int i = 0; i < m; i++){
            buckets[i] = new ArrayList<>();
        }

        // First level hashing
        for (String key : keys) {
            int h = hash(key, m);
            buckets[h].add(key);
        }

        // Build second level tables
        secondLevel = new BucketTable[m];

        for (int i = 0; i < m; i++) {
            int k = buckets[i].size();

            if (k == 0) {
                secondLevel[i] = new BucketTable(0);
                continue;
            }

            if (k == 1) {
                BucketTable bt = new BucketTable(1);
                bt.table[0] = buckets[i].get(0);
                secondLevel[i] = bt;
                continue;
            }

            // Allocate table of size k^2
            int size = k * k;
            BucketTable bt = new BucketTable(size);

            boolean success = false;

            // Try random seeds until collision free
            while (!success) {
                success = true;
                Arrays.fill(bt.table, null);

                int seed = new Random().nextInt(Integer.MAX_VALUE);

                for (String key : buckets[i]) {
                    int pos = secondaryHash(key, size, seed);

                    if (bt.table[pos] != null) {
                        totalCollisions++;
                        maxCollisions = Math.max(maxCollisions, 1);
                        success = false;
                        break;
                    }

                    bt.table[pos] = key;
                }

                if (success) {
                    bt.seed = seed;
                }
            }

            secondLevel[i] = bt;
        }
    }

    // Lookup
    public boolean contains(String key) {
        int h = hash(key, buckets.length);
        BucketTable bt = secondLevel[h];

        if (bt.size == 0) {
            return false;
        }

        if (bt.size == 1) {
            return key.equals(bt.table[0]);
        }

        // seed not needed for lookup
        int pos = secondaryHash(key, bt.size, bt.seed);
        return key.equals(bt.table[pos]);
    }

    // Hash functions
    private int hash(String key, int mod) {
        int h = key.hashCode() % mod;
        return h < 0 ? h + mod : h;
    }

    private int secondaryHash(String key, int mod, int seed) {
        int h = (key.hashCode() ^ seed) % mod;
        return h < 0 ? h + mod : h;
    }

    // Data
    public long getTotalCollisions() {
        return totalCollisions;
    }

    public long getMaxCollisions() {
        return maxCollisions;
    }

    public int getEmptySlots() {
        int empty = 0;
        for (BucketTable bt : secondLevel) {
            if (bt.size == 0) {
                empty++;
            }
        }
        return empty;
    }

    public double getLoadFactor() {
        return (double) totalKeys / secondLevel.length;
    }

    // Prime helpers
    private static int nextPrime(int n) {
        if (n % 2 == 0) {
            n++;   
        }
        while (!isPrime(n)) {
            n += 2;
        }
        return n;
    }

    private static boolean isPrime(int n) {
        if (n < 2) {
            return false;
        }  
        if (n == 2 || n == 3) {
            return true;
        }

        if (n % 2 == 0) {
            return false;
        }

        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

}