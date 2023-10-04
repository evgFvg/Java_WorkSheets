import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {

    @Test
    void size() {
        HashTable<String, Integer> gg = new HashTable<>();
        assertEquals(0, gg.size());
        gg.put("hi", 11);
        gg.put("Ben", 34);
        assertEquals(2, gg.size());

    }

    @Test
    void isEmpty() {
        HashTable<String, Integer> gg = new HashTable<>();
        assertTrue(gg.isEmpty());
        gg.put("hi", 11);
        gg.put("Ben", 34);
        assertFalse(gg.isEmpty());


    }

    @Test
    void containsKey() {
        HashTable<String, Integer> ht = new HashTable<>();
        ht.put("hello", 35);
        ht.put("hi", 11);
        ht.put("Ben", 34);
        ht.put("hello", 12);

        assertTrue(ht.containsKey("hello"));
        assertTrue(ht.containsKey("Ben"));
        assertTrue(ht.containsKey("hi"));
        assertFalse(ht.containsKey("Ban"));
    }

    @Test
    void containsValue() {
        HashTable<String, Integer> ht = new HashTable<>();
        ht.put("hello", 35);
        ht.put("hi", 11);
        ht.put("Ben", 34);

        assertTrue( ht.containsValue(35));
        assertTrue( ht.containsValue(11));
        assertTrue( ht.containsValue(34));
        assertFalse( ht.containsValue(100));
        assertFalse( ht.containsValue(4));
    }

    @Test
    void put() {
        HashTable<String, Integer> ht = new HashTable<>();
        assertEquals(null, ht.put("hello", 35));
        assertEquals(35, ht.get("hello"));
        assertEquals(35, ht.put("hello", 100));
        assertEquals(100, ht.get("hello"));
    }

    @Test
    void remove() {
        HashTable<String, Integer> ht = new HashTable<>();
        ht.put("hello", 35);
        ht.put("hi", 11);
        ht.put("Ben", 34);
        ht.put("hello", 12);

        assertEquals(12, ht.remove("hello"));
        assertEquals(34, ht.remove("Ben"));
        assertEquals(null, ht.remove("Alan"));
        assertEquals(1, ht.size());
    }

    @Test
    void putAll() {
        HashTable<String, Integer> ht = new HashTable<>();
        ht.put("hello", 35);
        ht.put("hi", 11);
        ht.put("Ben", 34);

        Map<String, Integer> map = new HashMap<>();
        map.put("tanya", 56);
        map.put("alex", 18);
        map.put("Hanna", 67);
        ht.putAll(map);

        assertEquals(6, ht.size());
        assertEquals(18, ht.remove("alex"));
        assertEquals(67, ht.get("Hanna"));
    }

    @Test
    void clear() {
        HashTable<String, Integer> ht = new HashTable<>();
        ht.put("hello", 10);
        ht.put("hi", 11);
        ht.put("bb", 110);
        ht.clear();
        assertNull(ht.get("hi"));
        assertNull(ht.get("hello"));
        assertNull(ht.get("bb"));
        assertEquals(0, ht.size());
    }

    @Test
    void keySet() {
        HashTable<String, Integer> ht = new HashTable<>();
        ht.put("hello", 35);
        ht.put("hi", 11);
        ht.put("Ben", 34);
        ht.put("hello2", 35);
        ht.put("hi2", 11);
        ht.put("Ben2", 34);
        assertEquals(ht.keySet().size() , ht.size());
        for (String s : ht.keySet()) {
            System.out.println(s);
        }
    }

    @Test
    void values() {
        HashTable<String, Integer> ht = new HashTable<>();
        ht.put("hello", 35);
        ht.put("hi", 11);
        ht.put("Ben", 34);
        ht.put("hello2", 35);
        ht.put("hi2", 11);
        ht.put("Ben2", 34);
        ht.put("hello3", 12);
        assertEquals(ht.values().size() , ht.size());
        for (Integer i : ht.values()) {
            System.out.println(i);
        }
    }

    @Test
    void entrySet() {
        HashTable<String, Integer> ht = new HashTable<>();
        ht.put("hello", 35);
        ht.put("hi", 11);
        ht.put("Ben", 34);
        ht.put("hello2", 35);
        ht.put("hi2", 11);
        ht.put("Ben2", 34);
        ht.put("hello3", 12);
        assertEquals(ht.entrySet().size() , ht.size());
        for (Map.Entry<String, Integer> e : ht.entrySet()) {
            System.out.println(e);
        }
    }

    @Test
    void checkExceptions() {
        HashTable<String, Integer> ht = new HashTable<>();
        ht.put("hello", 35);
        ht.put("hi", 11);
        ht.put("Ben", 34);

        Exception ex = assertThrows(ConcurrentModificationException.class, () -> {
            for (Map.Entry<String, Integer> e : ht.entrySet()) {
                ht.put("NewOne", 77);
            }
        });
        assertEquals("Illegal use of Iterator", ex.getMessage());
    }

    @Test
    void loadDictionary() {
        String path  = "/usr/share/dict/american-english";
        int i = 0;
        HashTable<String, Integer> dict = new HashTable<>();

        try {
            File f = new File(path);
            Scanner reader = new Scanner(f);
            while(reader.hasNextLine()) {
                String gg = reader.nextLine();
                dict.put(gg, i++);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File doesnt exist");
            e.printStackTrace();
        }

        assertEquals(104334, dict.size());
    }

    @Test
    void exceptionsTest(){
        Map<String, Integer> testMap = new HashTable<>(10);
        final Iterator<String> keyIter;
        final Iterator<Integer> valIter;
        final Iterator<Map.Entry<String, Integer>> entryIter;

        keyIter = testMap.keySet().iterator();
        testMap.put("s", 3);
        assertThrows(ConcurrentModificationException.class, keyIter::next);

        valIter = testMap.values().iterator();
        testMap.remove("s");

        assertThrows(ConcurrentModificationException.class, valIter::next);
        testMap.put("g", 3);
        testMap.put("d", 3);
        entryIter = testMap.entrySet().iterator();
        entryIter.next();
        entryIter.next();

        testMap.clear();
        assertThrows(ConcurrentModificationException.class, entryIter::next);

    }

}