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
    Queue<String> inputQueue = new LinkedList<>();
    Dictionary<Integer, ArrayList<String>> clientWorkDictionary = new Hashtable();
    Integer currentCount;
    PrintStream printStream;
    OutputStream fout;

    public WordCount(int workerNum, String[] filenames) throws IOException {
        this.workerNum = workerNum;
        this.filenames = filenames;
        this.processes = new ArrayList<Process>();
        this.currentCount = 0;
        for (Integer i = 0; i < filenames.length; i++) {
            this.inputQueue.add(filenames[i]);
        }
    }

    public void setOutputStream(PrintStream out) {
        this.printStream = out;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("started");
        String[] filenames = { "simple.txt", "random.txt" };
        WordCount wordCount = new WordCount(1, filenames);
        System.out.println("starting");
        wordCount.run();
    }

    public static ArrayList<String> listFilesForFolder(final File folder) {
        ArrayList<String> lis = new ArrayList<String>();
        for (final File fileEntry : folder.listFiles()) {
            lis.add("temp/immediate/" + fileEntry.getName());
        }
        return lis;
    }

    public void run() {
        try {
            // create a new thread object
            try {
                Thread t = new MasterServer(this.workerNum, this.inputQueue, this.clientWorkDictionary);
                t.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

            ProcessBuilder pb;
            for (Integer i = 1; i <= this.workerNum; i++) {
                createWorker();
            }

            for (Integer i = 1; i <= this.workerNum; i++) {
                System.out.println("Echo Output:\n" + output(processes.get(i - 1).getInputStream()));
            }

            try {
                int exitCode = processes.get(0).waitFor();
                if (exitCode != 0) {
                    System.out.println("\nExited with error code : " + exitCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
                }
            //this.printStream.println("Omar");
            //counter();

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("nikhil");
    }

    public Collection<Process> getActiveProcess() {
        return processes;
    }

    public void createWorker() throws IOException {
        ProcessBuilder pb;
        pb = new ProcessBuilder("java", "Client", this.currentCount.toString());
        Process process = pb.start();
        processes.add(process);
    }

    public void counter() {
        try {

            HashMap<String, Integer> map1 = new HashMap<String, Integer>();

            String line;
            final File folder = new File("temp/immediate");
            List<String> filenames = listFilesForFolder(folder);
            for (int i = 0; i < filenames.size(); i++) {
                BufferedReader reader1 = new BufferedReader(new FileReader(filenames.get(i)));
                while ((line = reader1.readLine()) != null) {
                    String[] parts = line.split(":", 2);
                    if (!map1.containsKey(parts[0])) {
                        String key = parts[0];
                        int value = Integer.parseInt(parts[1]);
                        map1.put(key, value);
                    } else {
                        int count = map1.get(parts[0]);
                        map1.put(parts[0], count + Integer.parseInt(parts[1]));
                    }
                }

                reader1.close();
            }
            HashMap<String, Integer> hm1 = sortByValue(map1);
            
            //BufferedWriter writer = new BufferedWriter(new FileWriter("temp/result.txt"));
            // map.forEach((key, value) -> System.out.println(key + ":" + value));
            
            //PrintStream out =  new PrintStream(System.out);
            //this.setOutputStream(out);
            for (String key : hm1.keySet()) {
                //writer.write(hm1.get(key) + ":" + key);
                
                this.printStream.println(hm1.get(key) + ":" + key );
                printStream.flush();
                //writer.flush();
                // dos.writeUTF(key + ":" + map.get(key))
            }
            //cwriter.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) 
    { 
        // Create a list from elements of HashMap 
        List<Map.Entry<String, Integer> > list = 
               new LinkedList<Map.Entry<String, Integer> >(hm.entrySet()); 
  
        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() { 
            public int compare(Map.Entry<String, Integer> o1,  
                               Map.Entry<String, Integer> o2) 
            { 
                return (o2.getValue()).compareTo(o1.getValue()); 
            } 
        }); 
          
        // put data from sorted list to hashmap  
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>(); 
        for (Map.Entry<String, Integer> aa : list) { 
            temp.put(aa.getKey(), aa.getValue()); 
        } 
        return temp; 
    } 

    private static String output(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + System.getProperty("line.separator"));
            }
        } finally {
            br.close();
        }
        return sb.toString();
    }

}

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
