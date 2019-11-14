import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.LinkedList;
import java.io.*;
import java.util.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;

class MasterServer extends Thread {
    Integer workerNum;
    Queue<String> inputQueue;
    Dictionary<Integer, ArrayList<String>> clientWorkDictionary;

    public MasterServer(Integer workers, Queue<String> inputQueue,
            Dictionary<Integer, ArrayList<String>> clientWorkDictionary) {
        this.workerNum = workers;
        this.inputQueue = inputQueue;
        this.clientWorkDictionary = clientWorkDictionary;
    }

    @Override
    public void run() {
        try {
            // running infinite loop for getting
            // client request
            // server is listening on port 5056
            ServerSocket ss = new ServerSocket(5300);
            for (Integer i = 1; i <= this.workerNum; i++) {
                Socket s = null;

                try {
                    // socket object to receive incoming client requests
                    s = ss.accept();
                    s.setSoTimeout(3000);

                    System.out.println("A new client is connected : " + s);

                    // obtaining input and out streams
                    DataInputStream dis = new DataInputStream(s.getInputStream());
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                    System.out.println("Assigning new thread for this client");

                    // create a new thread object
                    Thread t = new ClientHandler(s, dis, dos, i, inputQueue, clientWorkDictionary);

                    // Invoking the start() method
                    t.start();

                } catch (Exception e) {
                    s.close();
                    // e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
