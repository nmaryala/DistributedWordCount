import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.*;

public class ProcessBuilderExample1 {

public static void main(String[] args) 
    { 
        try
        { 
        
        ProcessBuilder   ps=new ProcessBuilder("java","-version");

        ps.redirectErrorStream(true);

        Process pr = ps.start();  

        BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
        pr.waitFor();
        System.out.println("ok!");

        in.close();
        System.exit(0);








            // create a process and execute google-chrome 
            //Process process = Runtime.getRuntime().exec("java Server");
            //Process process = Runtime.getRuntime().exec("java Client.java"); 
            //System.out.println("Google Chrome successfully started"); 
            //Process process = new ProcessBuilder("java", "-version").start();
            //List<String> results = readOutput(process.getInputStream());
 
        //assertThat("Results should not be empty", results, is(not(empty())));
        //assertThat("Results should contain java version: ", results, hasItem(containsString("java version")));
 
        //int exitCode = process.waitFor();
        //assertEquals("No errors should be detected", 0, exitCode);
        } 
        catch (Exception e) 
        { 
            e.printStackTrace(); 
        } 
    } 
}

