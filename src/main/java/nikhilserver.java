import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
 
public class nikhilserver {
    public static void main(String[] args) throws InterruptedException,
            IOException {
        ProcessBuilder pb = new ProcessBuilder("java", "Client", "1");
        Process process = pb.start();
        ProcessBuilder pb2 = new ProcessBuilder("java", "Client","2");
        Process process2 = pb2.start();
        // int errCode = process.waitFor();
        // System.out.println("Echo command executed, any errors? " + (errCode == 0 ? "No" : "Yes") + errCode);
        System.out.println("Echo Output:\n" + output(process.getInputStream()));    
        System.out.println("Echo Output:\n" + output(process2.getInputStream()));    
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