// Gai Ashkenazy
// 204459127
// Ex-3

import java.io.*;

public class Copier implements Runnable {

    private File destination;
    private SynchronizedQueue<File> resultQueue;
    public static final int COPY_BUFFER_SIZE = 4096;

    public Copier(File destination, SynchronizedQueue<File> resultQueue){
        this.destination = destination;
        this.resultQueue = resultQueue;
    }

    public void run(){

        File file;
        byte[] buff = new byte[COPY_BUFFER_SIZE];
        BufferedInputStream reader;
        BufferedOutputStream writer;

        try{
            //run on all the file in the result Queue
            while (resultQueue.getSize() > 0){
                //dequeue each file from the result queue
                file = resultQueue.dequeue();
                //get the path of the new file
                String fullName = destination.toString() + "\\" + file.getName();
                //open reader
                reader = new BufferedInputStream(new FileInputStream(file));
                //open the writer to the destination
                writer = new BufferedOutputStream( new FileOutputStream(fullName,false));

                int c;
                //coping the file
                while((c = reader.read(buff, 0, buff.length)) != -1){
                    writer.write(buff, 0, c);
                }

                //close the reader
                reader.close();
                //close the writer
                writer.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
