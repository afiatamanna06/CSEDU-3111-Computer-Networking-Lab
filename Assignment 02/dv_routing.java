import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;


public class dv_routing {

    public static Node[] nodes = new Node[26];
    public static char source_node_id;

    public static int toInt(char c){
        int ch = c - 'A';
        return ch;
    }
    public static char toChar(int i){
        return (char)('A'+i);
    }

    public static Vector<Node> getNodes(){
        Vector<Node> ret= new Vector<Node>();
        for (int i=0;i<nodes.length;i++){
            if(nodes[i].distance!=Float.POSITIVE_INFINITY){
                ret.add(nodes[i]);
            }
        }
        return ret;
    }

    public static Message getMessage(){
        return new Message(source_node_id,getNodes());
    }

    public static String getStr(byte[] a)
    {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0)
        {
            ret.append((char) a[i]);
            i++;
        }
        return ret.toString();
    }

    public static void main(String args[]){
        int source_port=0;
        String file="";
        //Parsing arguments
        try {
            source_node_id = args[0].charAt(0);
            source_port = Integer.parseInt(args[1]);
            file = args[2];
        }catch (Exception e){
            System.out.println("Incorrect argument");
            System.exit(0);
        }

        //Initialize nodes
        for(int i=0;i<26;i++){
            nodes[i]=new Node(toChar(i),-1,Float.POSITIVE_INFINITY,'0');
        }

        nodes[toInt(source_node_id)] = new Node(source_node_id,source_port,0,source_node_id);

        //initialize neighbours
        Neighbours neighbours= new Neighbours();

        //Open file
        FileReader fin = null;
        try {
            fin = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }

        //update neighbours from file
        Scanner sc = new Scanner(fin);
        sc.nextLine();
        while(sc.hasNext()){
            String processing = sc.nextLine();
            StringTokenizer stringTokenizer = new StringTokenizer(processing);
            char node_id = stringTokenizer.nextToken().charAt(0);
            float distance = Float.parseFloat(stringTokenizer.nextToken());
            int node_port = Integer.parseInt(stringTokenizer.nextToken());
            nodes[toInt(node_id)] = new Node(node_id,node_port,distance,node_id);
            neighbours.add(node_id);
        }

        //Start thread for all neighbours to send vector
        for (int i=0;i<26;i++){
            if(nodes[i].node_port!=-1&& nodes[i].node_id!=source_node_id){
                try {
                    new SendThread(nodes[i].node_port).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //Loop to receive message and update the vector
        DatagramSocket ds = null;
        DatagramPacket DpReceive = null;
        try {
            ds = new DatagramSocket(source_port);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        while (true){
            byte[] receive = new byte[65535];
            DpReceive = new DatagramPacket(receive, receive.length);

            try {
                ds.receive(DpReceive);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String data = getStr(receive);
            Message message = new Message(data);

            //Relaxation
            for(int i=0;i<message.nodes.size();i++){
                Node node =message.nodes.get(i);
                if(nodes[toInt(node.node_id)].distance>nodes[toInt(message.source_node_id)].distance+node.distance){
                    nodes[toInt(node.node_id)].distance=nodes[toInt(message.source_node_id)].distance+node.distance;
                    nodes[toInt(node.node_id)].parent=nodes[toInt(message.source_node_id)].parent;
                }
            }
            neighbours.addMessage(message);

            //If all neighbours stabilized, print vector
            if(neighbours.checkDone()){
                Vector<Node> nodeVector = getNodes();
                for(int i=0;i<nodeVector.size();i++){
                    if(nodeVector.get(i).node_id!=source_node_id)
                        System.out.println("shortest path to node "+nodeVector.get(i).node_id+": the next hop is "
                                +nodeVector.get(i).parent+" and the cost is " +nodeVector.get(i).distance);
                }
                break;
            }
        }
    }

}
