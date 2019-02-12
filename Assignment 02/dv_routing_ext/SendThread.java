import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

class SendThread extends Thread{
    int port = 0;
    char source_id=0;
    DatagramSocket ds ;

    public SendThread(int port,char source_id) throws IOException {
        this.port = port;
        this.source_id=source_id;
        ds =  new DatagramSocket();
    }

    //sends message to the port every 5 seconds
    public void run(){
        while (true){
            String data = new Message(source_id,dv_routing.table[dv_routing.toInt(source_id)]).toString();
            byte[] buf = data.getBytes();

            DatagramPacket DpSend =
                    null;
            try {
                DpSend = new DatagramPacket(buf, buf.length, InetAddress.getLocalHost(), this.port);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            try {
                ds.send(DpSend);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //System.out.println("Sent to:"+port+" "+data);
        }

    }
}
