import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class CountingSortByThreadsTest {
    private static final int N_DICTIONARIES = 100;
    private static final char[] dict = getDict();
    private static final char[] bigData = getBigData(dict);

    @Test
    void countSort1Threads() {
        long start = System.nanoTime();
        CountingSortByThreads.countSort(bigData, 1);
        long end = System.nanoTime();

        for (int i = 1; i < bigData.length; ++i) {
            assertTrue(bigData[i] >= bigData[i - 1]);
        }
        System.out.println("RunTime with 1 thread and 100 dictionaries is " + (end - start) / 1_000_000 + " ms");
    }

    @Test
    void countSort2Threads() {
        assertEquals(dict.length * N_DICTIONARIES, bigData.length);
        long start = System.nanoTime();
        CountingSortByThreads.countSort(bigData, 2);
        long end = System.nanoTime();

        for (int i = 1; i < bigData.length; ++i) {
            assertTrue(bigData[i] >= bigData[i - 1]);
        }
        System.out.println("RunTime with 2 threads and 100 dictionaries is " + (end - start) / 1_000_000 + " ms");
    }

    @Test
    void countSort4Threads() {
        assertEquals(dict.length * N_DICTIONARIES, bigData.length);
        long start = System.nanoTime();
        CountingSortByThreads.countSort(bigData, 4);
        long end = System.nanoTime();

        for (int i = 1; i < bigData.length; ++i) {
            assertTrue(bigData[i] >= bigData[i - 1]);
        }
        System.out.println("RunTime with 4 threads and 100 dictionaries is " + (end - start) / 1_000_000 + " ms");
    }

    @Test
    void countSort8Threads() {
        long start = System.nanoTime();
        CountingSortByThreads.countSort(bigData, 8);
        long end = System.nanoTime();

        for (int i = 1; i < bigData.length; ++i) {
            assertTrue(bigData[i] >= bigData[i - 1]);
        }
        System.out.println("RunTime with 8 threads and 100 dictionaries is " + (end - start) / 1_000_000 + " ms");
    }

    @Test
    void countSort12Threads() {
        long start = System.nanoTime();
        CountingSortByThreads.countSort(bigData, 12);
        long end = System.nanoTime();

        for (int i = 1; i < bigData.length; ++i) {
            assertTrue(bigData[i] >= bigData[i - 1]);
        }
        System.out.println("RunTime with 12 threads and 100 dictionaries is " + (end - start) / 1_000_000 + " ms");
    }

    private static char[] getBigData(char[] dict) {
        int startIndex = 0;
        char[] bigData = new char[N_DICTIONARIES * dict.length];
        for (int i = 0; i < N_DICTIONARIES; ++i) {
            startIndex = i * dict.length;
            System.arraycopy(dict, 0, bigData, startIndex, dict.length);
        }
        assertEquals(dict.length * N_DICTIONARIES, bigData.length);

        return bigData;
    }

    private static char[] getDict() {
        String path = "/usr/share/dict/american-english";
        BufferedReader bufferedReader = null;
        FileReader fr = null;
        try {
            fr = new FileReader(path);
            bufferedReader = new BufferedReader(fr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        StringBuilder builder = new StringBuilder();

        try {
            String line = bufferedReader.readLine();

            while (null != line) {
                builder.append(line);
                line = bufferedReader.readLine();
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString().toCharArray();
    }
}