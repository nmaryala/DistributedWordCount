import java.io.IOException;
import java.io.PrintStream;
import java.io.*;
import java.util.*;
import java.net.*;

public class WordCount implements Master {
    ArrayList<Process> processes;
    Integer workerNum;
    String[] filenames;
    Queue<String> inputQueue = new LinkedList<>();
    Dictionary<Integer, ArrayList<String>> clientWorkDictionary = new Hashtable();
    Integer currentCount;
    PrintStream printStream;
    OutputStream fout;
    ArrayList<Integer> ports = new ArrayList<Integer>();

    public WordCount(int workerNum, String[] filenames) throws IOException {
        this.workerNum = workerNum;
        this.filenames = filenames;
        this.processes = new ArrayList<Process>();
        this.currentCount = 0;
        for (Integer i = 0; i < filenames.length; i++) {
            this.inputQueue.add(filenames[i]);
        }

        //Comment the below part before running tests
        FileOutputStream fout=new FileOutputStream("temp/result.txt");
        setOutputStream(new PrintStream(fout));
        }

    public void setOutputStream(PrintStream out) {
        this.printStream = out;
    }

    public static void main(String[] args) throws Exception {
        String[] filenames = { "../../test/resources/simple.txt", "../../test/resources/random.txt" };
        WordCount wordCount = new WordCount(1, filenames);
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
            System.out.println("Hosting Master server......");
            // create a new Master Server thread to accept client request
            Thread t = new MasterServer(this.workerNum, this.inputQueue, this.clientWorkDictionary, this.ports);
            t.start();
            Thread.sleep(100);
            System.out.println("Master server hosted !!");

            for (Integer i = 1; i <= this.workerNum; i++) {
                System.out.println("Creating worker:"+i);
                createWorker();
                System.out.println("Worker:"+i+" created successfully !!");
            }

            for (Integer i = 1; i <= this.workerNum; i++) {
                System.out.println("Worker" + i + "\tOutput:\n" + output(processes.get(i - 1).getInputStream()));
            }

            int exitCode = processes.get(0).waitFor();
            if (exitCode != 0) {
                System.out.println("Worker 0 exited with error code : " + exitCode);
            }

            // printStream.println("Nikhjil");
            counter();

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("All done !!");
    }

    public Collection<Process> getActiveProcess() {
        return processes;
    }

    public void createWorker() throws IOException {
        ProcessBuilder pb;
        pb = new ProcessBuilder("java", "Client", ports.get(0).toString());
        Process process = pb.start();
        processes.add(process);
    }

    private void counter() {
        try {

            HashMap<String, Integer> map1 = new HashMap<String, Integer>();

            String line;
            final File folder = new File("temp/immediate");
            List<String> filenames = new ArrayList<>();
            filenames.add("count_1.txt");
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

            System.out.println("Came til here");
            
            for (String key : hm1.keySet()) {
                //writer.write(hm1.get(key) + ":" + key);
                
                this.printStream.println(hm1.get(key) + ":" + key );
                this.printStream.flush();
                //writer.flush();
                // dos.writeUTF(key + ":" + map.get(key))
            }

            this.printStream.close();
 

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) 
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

