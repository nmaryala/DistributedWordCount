// Java implementation for a client 
// Save file as Client.java 

import java.io.*; 
import java.net.*; 
import java.util.*; 

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
			Socket s = new Socket(ip, 5003); 
	
			// obtaining input and out streams 
			DataInputStream dis = new DataInputStream(s.getInputStream()); 
			DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
	
			// the following loop performs the exchange of 
			// information between client and client handler 

			while (true)
			{ 
				String masterResponse = dis.readUTF();
				System.out.println(masterResponse);
				// System.out.println("hey nikhil-0");
				String filename = "/Users/omarkhursheed/Desktop/CO532/project-2-group-8/src/test/resources/"+masterResponse;
				BufferedReader reader = new BufferedReader(new FileReader(filename));

				StringBuilder builder = new StringBuilder();
				// System.out.println("hey nikhil-0");
				// System.out.println(filename);
				String currentLine = reader.readLine();
				while (currentLine != null) {
			        builder.append(currentLine);
			        currentLine = reader.readLine();
			    }
			    reader.close();
			    String fileContent = builder.toString();
				String[] result = fileContent.split(" ");
				Map<String, Integer> map = new HashMap<String, Integer> ();
				for (String t:result) {   
				    if (!map.containsKey(t)) {  // first time we've seen this string
				      map.put(t, 1);
				    }
				    else {
				      int count = map.get(t);
				      map.put(t, count + 1);
				    }
				 }
				BufferedWriter writer = new BufferedWriter(new FileWriter("temp/samplefile1word" + args[0] + ".txt",true));
				// map.forEach((key, value) -> System.out.println(key + ":" + value));
				for (String key : map.keySet()) {
			        writer.write(key + ":" + map.get(key));
			        writer.newLine();
			        writer.flush();
			    	// dos.writeUTF(key + ":" + map.get(key))
			    }
				writer.close();


				// dos.writeUTF("Done");

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
