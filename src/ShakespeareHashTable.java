import java.util.LinkedList;

public class ShakespeareHashTable {

    private static class Entry {
        String key;
        int value;

        Entry(String k, int v) {
            key = k;
            value = v;
        }
    }

    private LinkedList<Entry>[] table;
    private int size;
    private long totalCollisions = 0;
    private long maxBucketSize = 0;

    public ShakespeareHashTable(int initialSize) {
        table = new LinkedList[initialSize];
        for (int i = 0; i < initialSize; i++) {
            table[i] = new LinkedList<>();
        }
    }

    private int hash(String key) {
        return (key.hashCode() & 0x7fffffff) % table.length;
    }

    public void put(String key) {
        int index = hash(key);
        LinkedList<Entry> bucket = table[index];

        // A collision means a bucket already has elements
        if (!bucket.isEmpty()) {
            totalCollisions++;
            maxBucketSize = Math.max(maxBucketSize, bucket.size());
        }

        for (Entry e: bucket){
            if (e.key.equals(key)) {
                e.value++;
                return;
            }
        }

        bucket.add(new Entry(key, 1));
        size++;

        // ?
        if (size > table.length * 0.75)
        resize();
    }

    private void resize() {
        LinkedList<Entry>[] old = table;
        table = new LinkedList[old.length * 2];

        for (int i = 0; i < table.length; i++) {
            table[i] = new LinkedList<>();
        }

        size = 0;
        totalCollisions = 0;
        maxBucketSize = 0;

        for (LinkedList<Entry> bucket: old){
            for(Entry e: bucket){
                put(e.key);
            }
        }
    }

    public int get(String key) {
        int index = hash(key);
        for (Entry e: table[index]){
            if (e.key.equals(key)){
                return e.value;
            }
        }
        return 0;
    }

    public long getTotalCollisions() { 
        return totalCollisions;
    }

    public long getMaxBucketSize() {
        return maxBucketSize;
    }

    public int getSize() {
        return size; 
    }

    public int getCapacity() { 
        return table.length; 
    }

}