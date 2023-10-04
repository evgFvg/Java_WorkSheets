import java.time.LocalTime;
import java.util.*;


public class HashTable<K extends Comparable<K>, V> implements Map<K, V> {

    /*************************Class Fields*******************************/
    private static final float DEFAULT_LF = 0.75F;
    private static final int HASH_TO_POSITIVE = 0x7fffffff;
    private static final int GROWTH_FACTOR = 2;
    private static final int THRESH_HOLD = 8;
    private static final int DEFAULT_TABLE_SIZE = 3;
    private Collection<Entry<?, ?>>[] table = null;
    private float loadFactor = 0.0F;
    private LocalTime modCount = null;
    private int size = 0;

    /********************Constructors*****************************/
    public HashTable() {
        this(DEFAULT_TABLE_SIZE, DEFAULT_LF);
    }

    public HashTable(int capacity) {
        this(capacity, DEFAULT_LF);
    }

    public HashTable(float loadFactor) {
        this(DEFAULT_TABLE_SIZE, loadFactor);
    }

    public HashTable(int capacity, float loadFactor) {
        this.loadFactor = loadFactor;
        this.table = new Collection[capacity];
        modCount = LocalTime.now();
    }

    /***************************Getters / Setters**************************/

    private void setTable(Collection<Entry<?, ?>>[] table) {
        this.table = table;
    }

    private Collection<Entry<?, ?>>[] getTable() {
        return table;
    }

    private void setModCount() {
        this.modCount = LocalTime.now();
    }

    private LocalTime getModCount() {
        return this.modCount;
    }

    /****************************API functions********************************/

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        Collection<Entry<?, ?>> col = getCollectionByKey(key);
        if (null != col) {
            Entry<?, ?> ent = getEntryByKey(col, key);
            return null != ent;
        }
        return false;
    }


    @Override
    public boolean containsValue(Object value) {
        for (V v : values()) {
            if (v.equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        Collection<Entry<?, ?>> col = getCollectionByKey(key);
        if (null != col) {
            Entry<?, ?> ent = getEntryByKey(col, key);
            if (null != ent) {
                return (V) ent.getValue();
            }
        }
        return null;
    }

    @Override
    public V put(K key, V val) {
        if (Float.compare((this.loadFactor * getTable().length), size()) <= 0) {
            reHash();
        }
        int index = getIndexByKey(key);
        Collection<Entry<?, ?>> col = getCollectionByKey(key);
        Entry<K, V> ent = null;
        Entry<K, V> newEntry = new Entry<>(key, val);
        V res = null;
        if (null == col) {
            getTable()[index] = new LinkedList<>();
            getTable()[index].add(newEntry);
            ++this.size;
        } else if ((ent = getEntryByKey(col, key)) != null) {
            res = ent.setValue(val);
        } else {
            col.add(newEntry);
            ++this.size;
            if (col.size() >= THRESH_HOLD && !(col instanceof TreeSet)) {
                listToTree(index, col);
            }
        }
        setModCount();
        return res;
    }

    @Override
    public V remove(Object key) {
        Collection<Entry<?, ?>> col = getCollectionByKey(key);
        V res = null;
        if (null != col) {
            Entry<?, ?> ent = getEntryByKey(col, key);
            if (null != ent) {
                res = (V) ent.getValue();
                col.remove(ent);
                --this.size;
            }
        }
        setModCount();
        return res;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Map.Entry<? extends K, ? extends V> e : map.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public void clear() {
        Arrays.fill(getTable(), null);
        this.size = 0;
        setModCount();
    }

    @Override
    public Set<K> keySet() {
        return new KeySetTable();
    }

    @Override
    public Collection<V> values() {
        return new ValuesCollection();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new EntrySet();
    }


    /**********************************Utility Functions****************************/
    private Collection<Entry<?, ?>> getCollectionByKey(Object key) {
        return getTable()[getIndexByKey(key)];
    }

    private int getIndexByKey(Object key) {
        return (key.hashCode() & HASH_TO_POSITIVE) % getTable().length;

    }

    private void listToTree(int index, Collection<Entry<?, ?>> col) {
        getTable()[index] = new TreeSet<>(col);
    }

    private void reHash() {
        int newSize = getTable().length * GROWTH_FACTOR;
        HashTable<K, V> newTable = new HashTable<>(newSize);
        newTable.putAll(this);
        this.setTable(newTable.getTable());
    }

    private Entry<K, V> getEntryByKey(Collection<Entry<?, ?>> col, Object key) {
        for (Entry<?, ?> ent : col) {
            if (ent.getKey().equals(key)) {
                return (Entry<K, V>) ent;
            }
        }
        return null;
    }

    /**********************Inner Classes******************************/
    private static class Entry<K extends Comparable<K>, V> implements Map.Entry<K, V>, Comparable<Entry<K, V>> {
        private final K key;
        private V value = null;

        public Entry(K k, V val) {
            this.key = k;
            this.value = val;
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue(V v) {
            V res = getValue();
            this.value = v;
            return res;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry<?, ?> entry = (Entry<?, ?>) o;
            return entry.key.equals(key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }


        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("Entry { key = ");
            sb.append(key).append(", value= ").append(value).append("}");
            return sb.toString();
        }

        @Override
        public int compareTo(Entry<K, V> entry) {
            return key.compareTo(entry.key);
        }
    }

    private class KeySetTable extends AbstractSet<K> {

        @Override
        public Iterator<K> iterator() {
            return new KeyIterator(getFirstEntry());
        }

        @Override
        public int size() {
            return HashTable.this.size();
        }

        private class KeyIterator implements Iterator<K> {
            private Entry<K, V> entry = null;
            private final LocalTime keyModCount;

            KeyIterator(Entry<K, V> entry) {
                this.entry = entry;
                keyModCount = HashTable.this.getModCount();
            }

            private void setEntry(Entry<K, V> entry) {
                this.entry = entry;
            }

            @Override
            public boolean hasNext() {
                checkModification(keyModCount);
                return null != this.entry;
            }

            @Override
            public K next() {
                checkModification(keyModCount);
                K res = this.entry.getKey();
                setEntry(getNextEntry(this.entry));
                return res;
            }
        }
    }

    private class ValuesCollection extends AbstractCollection<V> {

        @Override
        public Iterator<V> iterator() {
            return new ValuesIterator(getFirstEntry());
        }

        @Override
        public int size() {
            return HashTable.this.size();
        }

        private class ValuesIterator implements Iterator<V> {
            Entry<K, V> entry = null;
            private final LocalTime valModCount;

            ValuesIterator(Entry<K, V> entry) {
                this.entry = entry;
                valModCount = HashTable.this.getModCount();
            }

            private void setEntry(Entry<K, V> entry) {
                this.entry = entry;
            }

            @Override
            public boolean hasNext() {
                checkModification(valModCount);
                return null != this.entry;
            }

            @Override
            public V next() {
                checkModification(valModCount);
                V res = this.entry.getValue();
                setEntry(getNextEntry(this.entry));
                return res;
            }
        }
    }

    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntriesIterator(getFirstEntry());
        }

        @Override
        public int size() {
            return HashTable.this.size();
        }

        private class EntriesIterator implements Iterator<Map.Entry<K, V>> {
            Map.Entry<K, V> entry = null;
            private final LocalTime entryModCount;

            EntriesIterator(Map.Entry<K, V> entry) {
                entryModCount = HashTable.this.getModCount();
                this.entry = entry;
            }

            private void setEntry(Entry<K, V> entry) {
                this.entry = entry;
            }

            @Override
            public boolean hasNext() {
                checkModification(entryModCount);
                return null != this.entry;
            }

            @Override
            public Map.Entry<K, V> next() {
                checkModification(entryModCount);
                Map.Entry<K, V> res = this.entry;
                setEntry(getNextEntry((Entry<K, V>) this.entry));
                return res;
            }
        }
    }

    /*********************Utility Iterator Functions***************************/
    private void checkModification(LocalTime iteratorModCounter) {
        if (!iteratorModCounter.equals(getModCount())) {
            throw new ConcurrentModificationException("Illegal use of Iterator");
        }
    }

    private Entry<K, V> getNextEntry(Entry<K, V> current) {
        Entry<K, V> res = getEntrySameCollection(current);
        if (null != res) {
            return res;
        } else {
            return getEntryOtherCollection(current);
        }
    }

    private Entry<K, V> getEntryOtherCollection(Entry<K, V> current) {
        int currIndex = getIndexByKey(current.getKey());
        Entry<?, ?> retVal = null;
        for (int i = currIndex + 1; i < getTable().length; ++i) {
            if (null != getTable()[i]) {
                Iterator<Entry<?, ?>> iter = getTable()[i].iterator();
                if (iter.hasNext()) {
                    retVal = iter.next();
                    break;
                }
            }
        }
        return (Entry<K, V>) retVal;
    }

    private Entry<K, V> getEntrySameCollection(Entry<K, V> current) {
        Collection<Entry<?, ?>> col = getCollectionByKey(current.getKey());
        Entry<?, ?> entry = null;
        Iterator<Entry<?, ?>> iter = col.iterator();
        while (iter.hasNext()) {
            if (iter.next().equals(current)) {
                if (iter.hasNext()) {
                    entry = iter.next();
                }
                break;
            }
        }
        return (Entry<K, V>) entry;
    }

    private Entry<K, V> getFirstEntry() {
        for (Collection<Entry<?, ?>> col : getTable()) {
            if (null != col && !col.isEmpty()) {
                return (Entry<K, V>) col.iterator().next();
            }
        }
        return null;
    }




}