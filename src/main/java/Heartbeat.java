import java.io.*; 
import java.net.*; 
import java.util.*; 

class Heartbeat extends Thread{
    DataOutputStream dos;

    public Heartbeat(DataOutputStream dos){
        this.dos = dos;
    }

    @Override
    public void run() {
		try
		{ 
            while(true){
                this.dos.writeUTF("Heartbeat");
                Thread.sleep(3000);     
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}