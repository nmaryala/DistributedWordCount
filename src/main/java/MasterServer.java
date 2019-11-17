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
    Queue<Integer> faultQueue;
    Dictionary<Integer, ArrayList<String>> clientWorkDictionary;
    ArrayList<Integer> ports;
    Queue<Boolean> allQueue;

    public MasterServer(Integer workers, Queue<String> inputQueue, Queue<Integer> faultQueue,
            Dictionary<Integer, ArrayList<String>> clientWorkDictionary, ArrayList<Integer> ports, Queue<Boolean> allQueue) {
        this.workerNum = workers;
        this.inputQueue = inputQueue;
        this.clientWorkDictionary = clientWorkDictionary;
        this.faultQueue = faultQueue;
        this.ports = ports;
        this.allQueue = allQueue;
    }

    @Override
    public void run() {
        try {
            // running infinite loop for getting
            // client request
            // server is listening on port 5056
            ServerSocket ss = new ServerSocket(0);
            ports.add(ss.getLocalPort());
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
                    Thread t = new ClientHandler(s, dis, dos, i, inputQueue, faultQueue,clientWorkDictionary);

                    // Invoking the start() method
                    t.start();

                } catch (Exception e) {
                    s.close();
                    e.printStackTrace();
                }
            }
 
            Thread t = new FaultHandler(inputQueue, faultQueue, clientWorkDictionary, this.ports, ss, this.allQueue);
            t.start();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


class FaultHandler extends Thread {
    Integer workerNum;
    Queue<String> inputQueue;
    Queue<Integer> faultQueue;
    Dictionary<Integer, ArrayList<String>> clientWorkDictionary;
    ArrayList<Integer> ports;
    ServerSocket ss;
    Queue<Boolean> allQueue;

    public FaultHandler(Queue<String> inputQueue, Queue<Integer> faultQueue,
            Dictionary<Integer, ArrayList<String>> clientWorkDictionary, ArrayList<Integer> ports, ServerSocket ss, Queue<Boolean> allQueue) {
        this.inputQueue = inputQueue;
        this.clientWorkDictionary = clientWorkDictionary;
        this.faultQueue = faultQueue;
        this.ports = ports;
        this.ss = ss;
        this.allQueue = allQueue;
    }

    @Override
    public void run() {
        try {
            // running infinite loop for getting
            // client request
            // server is listening on port 5056

            while(true){
                // System.out.println("Ready to create if someone died");
                Thread.sleep(500);
				// System.out.println("inputSize:"+ this.inputQueue.size());
				// System.out.println("faultSize:"+ this.faultQueue.size());
                if (this.allQueue.size() != 0){
                    break;
                }

                for (Integer i = 1; i <= this.faultQueue.size(); i++) {
                    Integer workerNum = this.faultQueue.peek();
                    Socket s = null;

                    try {
                        // socket object to receive incoming client requests
                        s = ss.accept();
                        s.setSoTimeout(3000);

                        System.out.println("A new client is connected from fault tolerance : " + s);

                        // obtaining input and out streams
                        DataInputStream dis = new DataInputStream(s.getInputStream());
                        DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                        System.out.println("Assigning new thread for this client in fault tolerance");

                        // create a new thread object
                        Thread t = new ClientHandler(s, dis, dos, workerNum, inputQueue, faultQueue,clientWorkDictionary);

                        // Invoking the start() method
                        t.start();
                        this.faultQueue.poll();

                    } catch (Exception e) {
                        s.close();
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

