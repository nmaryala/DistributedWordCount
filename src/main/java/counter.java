// Java implementation for a client 
// Save file as Client.java 

import java.io.*; 
import java.net.*; 
import java.util.Scanner; 
import java.util.*;

// Client class 
public class counter 
{ 
	public static void main(String[] args) throws IOException 
	{ 
		try
		{ 
			
			// the following loop performs the exchange of 
			// information between client and client handler 
				String filename = "/Users/omarkhursheed/Desktop/CO532/project-2-group-8/src/test/resources/random.txt";
				List<String> records = new ArrayList<String>();
				BufferedReader reader = new BufferedReader(new FileReader(filename));
				StringBuilder builder = new StringBuilder();
				String currentLine = reader.readLine();
				while (currentLine != null) {
			        builder.append(currentLine);
			        builder.append("n");
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
				BufferedWriter writer = new BufferedWriter(new FileWriter("temp/samplefile1word1" + args[0] + ".txt",true));
				//map.forEach((key, value) -> System.out.println(key + ":" + value));
				for (String key : map.keySet()) {
			        writer.write(key + ":" + map.get(key));
			        writer.newLine();
			        writer.flush();
			    }
				writer.close();

				HashMap<String, String> map1 = new HashMap<String, String>();

			    String line;
			    BufferedReader reader1 = new BufferedReader(new FileReader("temp/samplefile1word1" + args[0] + ".txt"));
			    while ((line = reader1.readLine()) != null)
			    {
			        String[] parts = line.split(":", 2);
			        if (parts.length >= 2)
			        {
			            String key = parts[0];
			            String value = parts[1];
			            map1.put(key, value);
			        } else {
			            System.out.println("ignoring line: " + line);
			        }
			    }

			    for (String key : map1.keySet())
			    {
			        System.out.println(key + ":" + map1.get(key));
			    }
			    reader.close();
		}
		catch(Exception e){ 
			e.printStackTrace(); 
		} 
	} 
} 
