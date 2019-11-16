import java.io.*;
import java.util.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;

class ClientHandler extends Thread 
{ 
	final DataInputStream dis; 
	final DataOutputStream dos; 
	final Socket s; 
	final int clientNumber;
	Queue<String> inputQueue;
	Dictionary<Integer, ArrayList<String>> clientWorkDictionary;
	

	// Constructor 
	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, int clientNumber, Queue<String> inputQueue, Dictionary<Integer, ArrayList<String>> clientWorkDictionary)
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
		String currentFileName = this.inputQueue.poll();
		ArrayList<String> list = this.clientWorkDictionary.get(this.clientNumber);
		if(list == null){
			this.clientWorkDictionary.put(this.clientNumber, new ArrayList<String>());
			list = this.clientWorkDictionary.get(this.clientNumber);
		}

		while (true) 
		{ 
			if (currentFileName == null){
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

				while(received.equals("Heartbeat")){
					System.out.println("Heartbeat received from Client:"+ s);
					// Thread.sleep(3000);
					received = dis.readUTF();
				}
				
				//If client succeeds, give it next file name
				if(received.equals("Done")) 
				{
					list.add(currentFileName);
					System.out.println("Client" + s+" returned success for file: "+currentFileName);
					currentFileName = this.inputQueue.poll();
				}
				else{
					this.inputQueue.add(currentFileName);
					currentFileName = null;
				}
				
			}
			catch(SocketTimeoutException ex){
				this.inputQueue.add(currentFileName);
				currentFileName = null;
			} 
			catch (IOException e) { 
				// e.printStackTrace(); 
			}
		} 
		
	} 
} 