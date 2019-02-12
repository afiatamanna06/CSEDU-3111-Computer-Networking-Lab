import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;


public class dv_routing {

    public static Node[][] table = new Node[26][];
    public static Date[] nodeTime = new Date[26];
    public static char source_node_id;

    public static Vector<Neighbour> neighbours = new Vector<Neighbour>();
    public static Neighbours messageQueue = new Neighbours();


    public static int toInt(char c) {
        int ch = c - 'A';
        return ch;
    }

    public static char toChar(int i) {
        return (char) ('A' + i);
    }

    public static String getStr(byte[] a) {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0) {
            ret.append((char) a[i]);
            i++;
        }
        return ret.toString();
    }

    public static void main(String args[]) {
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
        for (int i = 0; i < 26; i++) {
            table[i] = new Node[26];
            for (int j = 0; j < 26; j++) {
                if (i == j)
                    table[i][j] = new Node(0, i);
                else
                    table[i][j] = new Node(Float.POSITIVE_INFINITY, -1);
            }
        }

        //Open file
        FileReader fin = null;
        try {
            fin = new FileReader(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not Found");
            ;
            System.exit(0);
        }


        //Update neighbour nodes
        Scanner sc = new Scanner(fin);
        sc.nextLine();
        while (sc.hasNext()) {
            String processing = sc.nextLine();
            StringTokenizer stringTokenizer = new StringTokenizer(processing);
            char node_id = stringTokenizer.nextToken().charAt(0);
            float distance = Float.parseFloat(stringTokenizer.nextToken());
            int node_port = Integer.parseInt(stringTokenizer.nextToken());
            neighbours.add(new Neighbour(node_port, toInt(node_id), distance));
            messageQueue.add(node_id);
            table[toInt(source_node_id)][toInt(node_id)] = new Node(distance, toInt(node_id));
        }

        //Start thread for all neighbours to send vector
        for (int i = 0; i < neighbours.size(); i++) {
            try {
                new SendThread(neighbours.get(i).port, source_node_id).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Initialize the timer
        for(int i=0;i<26;i++){
            nodeTime[i]=new Date();
        }

        //Start thread to detect dead neighbours and update accordingly
        new updateThread().start();

        //Initialize reciever
        DatagramSocket ds = null;
        DatagramPacket DpReceive = null;
        try {
            ds = new DatagramSocket(source_port);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        boolean printed = false;

        //Loop to receive message and update the vector
        while (true) {


            byte[] receive = new byte[65535];
            DpReceive = new DatagramPacket(receive, receive.length);

            try {
                ds.receive(DpReceive);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String data = getStr(receive);

            Message message=new Message(data);

            //update timer
            nodeTime[toInt(message.source_node_id)] = new Date();

            //update vector
            table[toInt(message.source_node_id)]=message.nodes;

            //add messgae to queue
            messageQueue.addMessage(message.source_node_id,message.toString());

            //Relaxation
            for(int i=0;i<26;i++){
                if(table[toInt(source_node_id)][i].distance>table[toInt(source_node_id)][toInt(message.source_node_id)].distance+message.nodes[i].distance)
                {
                    table[toInt(source_node_id)][i].distance=table[toInt(source_node_id)][toInt(message.source_node_id)].distance+message.nodes[i].distance;
                    table[toInt(source_node_id)][i].nextHop=table[toInt(source_node_id)][toInt(message.source_node_id)].nextHop;

                }
                else if(table[toInt(source_node_id)][i].nextHop==toInt(message.source_node_id)){
                    table[toInt(source_node_id)][i].distance=table[toInt(source_node_id)][toInt(message.source_node_id)].distance+message.nodes[i].distance;
                }
            }

            //print vector if all neighbours just stabilized
            if((messageQueue.done()&&!printed)||false){
                for(int i=0;i<26;i++){
                    if(table[toInt(source_node_id)][i].distance<Float.POSITIVE_INFINITY&&i!=toInt(source_node_id))
                        System.out.println("shortest path to node "+toChar(i)+": the next hop is "
                                +toChar(table[toInt(source_node_id)][i].nextHop)+" and the cost is "
                                +table[toInt(source_node_id)][i].distance);
                }
                System.out.println();
                printed = true;
            }
            else if(!messageQueue.done()){
                printed = false;
            }

        }
    }
}
