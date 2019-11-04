// Gai Ashkenazy
// 204459127
// Ex-3

import java.io.File;

public class DiskSearcher {

    public static final int DIRECTORY_QUEUE_CAPACITY = 50;
    public static final int RESULTS_QUEUE_CAPACITY = 50;

    public static void main(String[] args) {
        //check for right number of arguments
        if(args.length != 5){
            System.out.println("Usege: the program must run with 5 arguments");
        }

        String pattern = args[0];
        File root = new File(args[1]);
        if(!root.exists() && root.isDirectory()){
            System.out.println("root directory was not opened correctly, make sure to spell correctly");
            System.exit(-1);
        }
        File destination = new File(args[2]);
        if(!destination.exists() && destination.isDirectory()){
            System.out.println("destination directory was not opened correctly, make sure to spell correctly");
            System.exit(-1);
        }

        int numOfSearchThreads = 1;
        int numOfCopyThreads = 1;

        try{
            numOfSearchThreads = Integer.parseInt(args[3]);
            numOfCopyThreads = Integer.parseInt(args[4]);
        } catch (NumberFormatException e){
            System.out.println("The number arguments are not correct");
            System.exit(-1);
        }

        //initialize the queues
        SynchronizedQueue dirQueue = new SynchronizedQueue(DIRECTORY_QUEUE_CAPACITY);
        SynchronizedQueue fileQueue = new SynchronizedQueue(RESULTS_QUEUE_CAPACITY);

        //single thread for scouting
        Scouter scout = new Scouter(dirQueue, root);
        scout.run();

        //create threads for searching
        Thread[] searchThreads = new Thread[numOfSearchThreads];
        for (int i = 0; i < numOfSearchThreads; i++) {
            searchThreads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    Searcher search = new Searcher(pattern, dirQueue, fileQueue);
                    search.run();
                }
            });
            searchThreads[i].start();
        }

        //create threads for copying
        Thread[] copyThreads = new Thread[numOfCopyThreads];
        for (int i = 0; i < numOfCopyThreads; i++) {
            copyThreads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    Copier copy = new Copier(destination, fileQueue);
                    copy.run();
                }
            });
            copyThreads[i].start();
        }
    }
}
