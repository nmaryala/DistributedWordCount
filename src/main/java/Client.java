// Java implementation for a client 
// Save file as Client.java 

import java.io.*; 
import java.net.*; 
import java.util.Scanner; 

// Client class 
public class Client 
{ 
	public static void main(String[] args) throws IOException 
	{ 
		try
		{ 
			System.out.println("hey nikhil");
			Scanner scn = new Scanner(System.in); 
			
			// getting localhost ip 
			InetAddress ip = InetAddress.getByName("localhost"); 
	
			// establish the connection with server port 5056 
			Socket s = new Socket(ip, 5056); 
	
			// obtaining input and out streams 
			DataInputStream dis = new DataInputStream(s.getInputStream()); 
			DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
	
			// the following loop performs the exchange of 
			// information between client and client handler 
			while (true)
			{ 
				String masterResponse = dis.readUTF();
				System.out.println(masterResponse);
				
				String fileContent = "Hello Learner !! Welcome to howtodoinjava.com.";
	
				BufferedWriter writer = new BufferedWriter(new FileWriter("temp/samplefile1"+args[0]+".txt"));
				writer.write(fileContent);
				writer.close();

				dos.writeUTF("Done");

				if(masterResponse == "Over"){
					break;
				}
			} 
			System.out.println("Closing this connection : " + s);
			s.close(); 
			System.out.println("Connection closed"); 
	
			dis.close(); 
			dos.close(); 
		}catch(Exception e){ 
			e.printStackTrace(); 
		} 
	} 
} 
