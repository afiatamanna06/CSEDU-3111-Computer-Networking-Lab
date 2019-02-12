import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

class SendThread extends Thread{
    int port = 0;
    DatagramSocket ds ;

    public SendThread(int port) throws IOException {
        this.port = port;
        ds =  new DatagramSocket();
    }

    //sends vector every 5 seconds
    public void run(){
        while (true){
            String data = dv_routing.getMessage().toString();
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
