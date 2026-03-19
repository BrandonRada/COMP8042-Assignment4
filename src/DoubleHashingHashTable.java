public class DoubleHashingHashTable<AnyType> {

    private static class HashEntry<AnyType> {
        AnyType element;
        boolean isActive;
        HashEntry(AnyType e, boolean a) { element = e; isActive = a; }
    }

    private HashEntry<AnyType>[] array;
    private int theSize;
    private long totalCollisions = 0;
    private long maxCollisions = 0;

    public DoubleHashingHashTable(int size) {
        allocateArray(size);
        makeEmpty();
    }

    public boolean insert(AnyType x) {
        int currentPos = findPos(x);
        if (isActive(currentPos)) {
            return false;
        }

        array[currentPos] = new HashEntry<>(x, true);
        theSize++;

        if (theSize > array.length / 2)
            rehash();

        return true;
    }

    private void rehash() {
        HashEntry<AnyType>[] oldArray = array;

        allocateArray(2 * oldArray.length);
        theSize = 0;
        totalCollisions = 0;
        maxCollisions = 0;

        for (HashEntry<AnyType> entry : oldArray) {
            if (entry != null && entry.isActive) {
                insert(entry.element);
            }
        }
    }      

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
        if (n == 2 || n == 3) {
            return true;
        }

        if (n == 1 || n % 2 == 0) {
            return false;
        }
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public boolean contains(AnyType x) {
        int pos = findPos(x);
        return isActive(pos);
    }


    private int findPos(AnyType x) {
        int hash1 = myhash(x);
        int hash2 = secondHash(x);

        int currentPos = hash1;
        long collisions = 0;

        while (array[currentPos] != null &&
               !array[currentPos].element.equals(x)) {

            collisions++;
            currentPos = (currentPos + hash2) % array.length;
        }

        totalCollisions += collisions;
        maxCollisions = Math.max(maxCollisions, collisions);

        return currentPos;
    }


    private boolean isActive(int pos) {
        return array[pos] != null && array[pos].isActive;
    }

    private int myhash(AnyType x) {
        int hashVal = x.hashCode() % array.length;
        return hashVal < 0 ? hashVal + array.length : hashVal;
    }

    // Second hash function
    private int secondHash(AnyType x) {
        int hashVal = x.hashCode();
        hashVal %= array.length;
        if (hashVal < 0) {
            hashVal += array.length;
        }

        return 1 + (hashVal % (array.length - 2));
    }
    
    private void allocateArray(int size) {
        array = new HashEntry[nextPrime(size)];
    }

    public void makeEmpty() {
        theSize = 0;
        for (int i = 0; i < array.length; i++) {
            array[i] = null;
        }
    }

    public long getTotalCollisions() { return totalCollisions; }
    public long getMaxCollisions() { return maxCollisions; }
    public int getEmptySlots() {
        int count = 0;
        for (HashEntry<AnyType> e : array) {
            if (e == null) {
                count++;
            }
        }
        return count;
    }
    public double getLoadFactor() {
        return (double) theSize / array.length;
    }
}
