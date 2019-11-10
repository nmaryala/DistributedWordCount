import java.io.*;
import java.util.*;

class AssignWorkers extends Thread {
    final Integer numberOfWorkers;
    ArrayList<Process> processes;

	public AssignWorkers(Integer numberOfWorkers, ArrayList<Process> processes) {
        this.numberOfWorkers = numberOfWorkers;
        this.processes = processes;
	}

	@Override
	public void run() {
		try {
			ProcessBuilder pb;
			for(Integer i=1;i <= this.numberOfWorkers; i++){
				pb = new ProcessBuilder("java", "Client", i.toString());
				Process process = pb.start();
				processes.add(process);
			}

			for(Integer i=1;i <= this.numberOfWorkers; i++){
				System.out.println("Echo Output:\n" + output(processes.get(i-1).getInputStream()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

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
