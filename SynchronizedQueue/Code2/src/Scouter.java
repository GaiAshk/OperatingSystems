// Gai Ashkenazy
// 204459127
// Ex-3

import java.io.File;

public class Scouter implements Runnable {

    private SynchronizedQueue<File> directoryQueue;
    private File root;

    public Scouter(SynchronizedQueue<File> directoryQueue, File root) {
        this.directoryQueue = directoryQueue;
        this.root = root;
    }

    @Override
    public void run() {
        //register as producer
        directoryQueue.registerProducer();
        //add all the directories to the queue
        directoryQueue.enqueue(this.root);
        addFilesFromFolder(this.root);
        //unregister
        directoryQueue.unregisterProducer();
    }

    public void addFilesFromFolder (File dir){

        File[] paths;

        try {
            // returns pathname's for files and directory
            paths = dir.listFiles();

            // for each pathname in pathname array add to the queue if it is a file or call recursively on its files
            for (int i = 0; i < paths.length; i++) {
                //if the item is a directory add to queue
                if(paths[i].isDirectory()){
                    this.directoryQueue.enqueue(paths[i]);
                    addFilesFromFolder(paths[i]);
                }
            }
        } catch(Exception e) {
            // if any error occurs
            e.printStackTrace();
        }
        return;
    }
}
