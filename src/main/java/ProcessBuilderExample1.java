import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.*;
import java.net.*;
import java.util.*;

public class ProcessBuilderExample1 {

public static void main(String[] args) 
    { try{

                System.out.println("befforea"); 
                
                 ProcessBuilder   ps=new ProcessBuilder("java","HelloWorldClient");

                 Process pr = ps.start()
    
                System.out.println("afffte"); 
                
                ServerSocket ss = new ServerSocket(5016); 

                Socket s = null;

                System.out.println("oohfodsfs"); 
                
        
                // s = ss.accept();
                System.out.println("a"); 
                
                System.out.println("bb");
                // ps.redirectErrorStream(true);
                System.out.println("c");
                Process pr = ps.start();  
                System.out.println("aas");
                // BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                // System.out.println("qww");
                // String line;
                // while ((line = in.readLine()) != null) {
                //     System.out.println(line);
                // }
                // pr.waitFor();
                System.out.println("ok!");

                // in.close();
                System.exit(0);
            }

        catch (Exception e) 
            { 
                e.printStackTrace(); 
            } 
    } 
}

