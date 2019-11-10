import java.io.*;
import java.util.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;

// Server class 
public class Server {
	public static void main(String[] args) throws IOException {
		Queue<Integer> inputQueue = new LinkedList<>();
		Dictionary<Integer, ArrayList<Integer>> clientWorkDictionary = new Hashtable();
		Scanner scn = new Scanner(System.in);

		System.out.println("How many files do you have?");
		int maxFiles = scn.nextInt();

		for (Integer i = 1; i <= maxFiles; i++) {
			inputQueue.add(i);
		}

		System.out.println("How many workers do you want to create?");

		int maxClients = scn.nextInt();
		scn.close();

		// server is listening on port 5056
		ServerSocket ss = new ServerSocket(5056);

		// create a new thread object
		try{
			Thread t = new AssignWorkers(maxClients);
			t.start();
		}
		catch(Exception e){
			e.printStackTrace();
		}

		// running infinite loop for getting
		// client request
		for (Integer i = 1; i <= maxClients; i++) {
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

		ss.close();
	}
}


// ClientHandler class 
class ClientHandler extends Thread 
{ 
	final DataInputStream dis; 
	final DataOutputStream dos; 
	final Socket s; 
	final int clientNumber;
	Queue<Integer> inputQueue;
	Dictionary<Integer, ArrayList<Integer>> clientWorkDictionary;
	

	// Constructor 
	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, int clientNumber, Queue<Integer> inputQueue, Dictionary<Integer, ArrayList<Integer>> clientWorkDictionary)
	{ 
		this.s = s; 
		this.dis = dis; 
		this.dos = dos;
		this.clientNumber = clientNumber;
		this.inputQueue = inputQueue;
		this.clientWorkDictionary = clientWorkDictionary;
	} 

	@Override
	public void run() 
	{ 
		String received; 
		Integer nextFileNumber = this.inputQueue.poll();
		ArrayList<Integer> list = this.clientWorkDictionary.get(this.clientNumber);
		if(list == null){
			this.clientWorkDictionary.put(this.clientNumber, new ArrayList<Integer>());
			list = this.clientWorkDictionary.get(this.clientNumber);
		}
		String currentFileName = "random"+nextFileNumber+".txt";
		while (true) 
		{ 
			if (nextFileNumber == null){
				try
				{ 
					// closing resources 
					this.s.close();
					this.dis.close(); 
					this.dos.close(); 
					
				}catch(IOException e){ 
					// e.printStackTrace(); 
				} 	
				break;
			}
	

			try { 

				// Send file name to the client 
				dos.writeUTF(currentFileName); 
				
				// receive response from the client 
				received = dis.readUTF(); 
				
				//If client succeeds, give it next file name
				if(received.equals("Done")) 
				{
					list.add(nextFileNumber);
					System.out.println("Client" + s+" returned success for file number "+nextFileNumber);
					nextFileNumber = this.inputQueue.poll();
					currentFileName = "random"+nextFileNumber+".txt";
				}
				else{
					this.inputQueue.add(nextFileNumber);
					nextFileNumber = null;
				}
				
			} catch (IOException e) { 
				// e.printStackTrace(); 
			} 
		} 
		
	} 
} 
