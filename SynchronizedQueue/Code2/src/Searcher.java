// Gai Ashkenazy
// 204459127
// Ex-3

import java.io.File;

public class Searcher implements Runnable {

    private String pattern;
    private SynchronizedQueue<File> directoryQueue;
    private SynchronizedQueue<File> resultQueue;


    public Searcher(String pattern, SynchronizedQueue<File> directoryQueue, SynchronizedQueue<File> resultQueue) {
        this.pattern = pattern;
        this.directoryQueue = directoryQueue;
        this.resultQueue = resultQueue;
    }

    public void run() {


        File dir;
        File file;
        File[] path;

        resultQueue.registerProducer();

        try {
            //run on all the directories in the directory Queue
            while (directoryQueue.getSize() > 0) {
                dir = directoryQueue.dequeue();
                path = dir.listFiles();

                if (path == null) return;

                //insert all files that match the pattern to the result Queue
                for (File item : path) {
                    if (item.isFile() && item.toString().contains(this.pattern)) {
                        resultQueue.enqueue(item);
                    }
                }
            }

        } finally {
            resultQueue.unregisterProducer();
        }
    }
}
