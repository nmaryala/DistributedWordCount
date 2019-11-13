import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.LinkedList;
import java.io.*;
import java.util.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;


public class WordCount implements Master {
    ArrayList<Process> processes;
    Integer workerNum;
    String[] filenames;
    Queue<Integer> inputQueue = new LinkedList<>();
    Dictionary<Integer, ArrayList<Integer>> clientWorkDictionary = new Hashtable();

    public WordCount(int workerNum, String[] filenames) throws IOException {
        this.workerNum = workerNum;
        this.filenames = filenames;
        this.processes = new ArrayList<Process>();
        for (Integer i = 1; i <= filenames.length; i++) {
			this.inputQueue.add(i);
		}
    }

    public void setOutputStream(PrintStream out) {

    }

    public static void main(String[] args) throws Exception {
        System.out.println("started");
        String[] filenames = {"temp/samplefile1word1.txt", "temp/samplefile1word2.txt"};
        WordCount wordCount = new WordCount(2, filenames);
        System.out.println("starting");
        wordCount.run();
    }

    public static ArrayList<String> listFilesForFolder(final File folder) {
		ArrayList<String> lis = new ArrayList<String>();
		for (final File fileEntry : folder.listFiles()) {
			lis.add("temp/immediate/"+fileEntry.getName());
		}
		return lis;
	}

    public void run() {
        try{
            // server is listening on port 5056
            ServerSocket ss = new ServerSocket(5056);
            // create a new thread object
            try{
                Thread t = new AssignWorkers(this.workerNum, processes);
                t.start();
            }
            catch(Exception e){
                e.printStackTrace();
            }

            // running infinite loop for getting
            // client request
            for (Integer i = 1; i <= this.workerNum; i++) {
                Socket s = null;

                try {
                    // socket object to receive incoming client requests
                    s = ss.accept();

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
        }
        catch(Exception e){
            e.printStackTrace();    
        }
    
        System.out.println("nikhil");
            
            // HashMap<String, Integer> map1 = new HashMap<String, Integer>();

            // String line;
            // final File folder = new File("temp/immediate");
            // List<String> filenames = listFilesForFolder(folder);
            // for (int i = 0; i < filenames.size(); i++){
            //     BufferedReader reader1 = new BufferedReader(new FileReader(filenames.get(i)));
            // while ((line = reader1.readLine()) != null)
            // {
            //     String[] parts = line.split(":", 2);

            //     if (!map1.containsKey(parts[0]))
            //     {
            //         String key = parts[0];
            //         int value = Integer.parseInt(parts[1]);
            //         map1.put(key, value);
            //     } else {
            //         int count = map1.get(parts[0]);
            //             map1.put(parts[0], count + Integer.parseInt(parts[1]) );
            //     }
            // }


            // reader1.close();
        
    }

    public Collection<Process> getActiveProcess() {
        return processes;
    }

    public void createWorker() throws IOException {

    }
}

