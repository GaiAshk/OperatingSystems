// Gai Ashkenazy
// 204459127
// Ex-3

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<String> lines = getLinesFromFile();
        System.out.println("Number of lines found: " + lines.size());
        System.out.println("Starting to process");

        long startTimeWithoutThreads = System.currentTimeMillis();
        workWithoutThreads(lines);
        long elapsedTimeWithoutThreads = (System.currentTimeMillis() - startTimeWithoutThreads);
        System.out.println("Execution time: " + elapsedTimeWithoutThreads);


        long startTimeWithThreads = System.currentTimeMillis();
        workWithThreads(lines);
        long elapsedTimeWithThreads = (System.currentTimeMillis() - startTimeWithThreads);
        System.out.println("Execution time: " + elapsedTimeWithThreads);

    }

    private static void workWithThreads(List<String> lines){
        //Your code:
        //Get the number of available cores
        //Assuming X is the number of cores - Partition the data into x data sets
        //Create X threads that will execute the Worker class
        //Wait for all threads to finish

        //get the number of available cores
        int x = Runtime.getRuntime().availableProcessors();

        //split the list to x sublist
        List<List<String>> partitionArray = new ArrayList<>(x);
        int size = lines.size();

        for (int i = 0; i < x; i++) {
            partitionArray.add( lines.subList(size * i / x, size * (i+1) / x));
        }

        //create the threads and run each one of them
        Thread[] threads = new Thread[x];
        for (int i = 0; i < x; i++) {
            int j = i;
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    Worker worker = new Worker(partitionArray.get(j));
                    worker.run();

                }
            });
            threads[i].start();
        }

        try{
            for (int i = 0; i < x; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void workWithoutThreads(List<String> lines) {
        Worker worker = new Worker(lines);
        worker.run();
    }

    private static List<String> getLinesFromFile() {
        //Your code:
        //Read the shakespeare file provided from C:\Temp\Shakespeare.txt
        //and return an ArrayList<String> that contains each line read from the file.

        BufferedReader reader;
        List<String> lines = new ArrayList<>();

        try{
            reader = new BufferedReader(new FileReader("C:\\Temp\\Shakespeare.txt"));
            String line;

            //add each line to the array list
            while((line = reader.readLine()) != null){
                lines.add(line);
            }

            //close reader
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }
}
