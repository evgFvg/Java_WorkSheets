import java.util.ArrayList;
import java.util.List;

public class CountingSortByThreads {
    public static final int ASCII_SIZE = 256;

    public static void countSort(char[] arr, int numOfThreads) {
        List<Edge> edges = fillThreadsEdges(arr.length, numOfThreads);
        List<ThreadInstance> instances = new ArrayList<>();
        List<Thread> threadsArr = new ArrayList<>();
        int[] resCount = new int[ASCII_SIZE];

        for (Edge e : edges) {
            instances.add(new ThreadInstance(e.getStartIndex(), e.getEndIndex(), resCount, arr));
        }

        for (ThreadInstance t : instances) {
            Thread th = new Thread(t);
            threadsArr.add(th);
            th.start();
        }
        for (Thread t : threadsArr) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        getUnionArray(arr, resCount);
    }

    private static void getUnionArray(char[] arr, int[] resCount) {
        int j = 0;
        for(char i = 0; i < resCount.length; ++i) {
            while(resCount[i] > 0) {
                arr[j] = i;
                ++j;
                --resCount[i];
            }
        }
    }


    private static List<Edge> fillThreadsEdges(int length, int numOfThreads) {
        List<Edge> resList = new ArrayList<>();
        int minChunkSize = length / numOfThreads;
        int reminder = length % numOfThreads;
        int startIndex = 0;
        int endIndex = 0;
        for (int i = 0; i < numOfThreads; ++i) {
            int chunkLength = minChunkSize + (reminder > 0 ? 1 : 0);
            endIndex += chunkLength;
            resList.add(new Edge(startIndex, endIndex));
            startIndex = endIndex;
            --reminder;
        }
        return resList;
    }

    private static class Edge {
        private int startIndex = 0;
        private int endIndex = 0;

        Edge(int startIndex, int endIndex) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        int getStartIndex() {
            return this.startIndex;
        }

        int getEndIndex() {
            return this.endIndex;
        }
    }

}

class ThreadInstance implements Runnable {
    private final int[] localArr = new int[CountingSortByThreads.ASCII_SIZE];
    private final int[] resCount;
    private final char[] arrToSort;
    private final int startIndex;
    private final int endIndex;

    ThreadInstance(int startIndex, int endIndex, int[] resCount, char[] arrToSort) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.resCount = resCount;
        this.arrToSort = arrToSort;
    }

    @Override
    public void run() {
        fillLocalArr();
        fillResCountArr();
    }

    private void fillResCountArr() {
        synchronized (resCount) {
            for(int i = 0; i < CountingSortByThreads.ASCII_SIZE; ++i) {
                resCount[i] += localArr[i];
            }
        }
    }

    private void fillLocalArr() {
        for (int i = startIndex; i < endIndex; ++i) {
            ++localArr[arrToSort[i]];
        }
    }
}


