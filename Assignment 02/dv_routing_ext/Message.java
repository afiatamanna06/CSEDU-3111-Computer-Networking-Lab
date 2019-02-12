import java.util.Arrays;
import java.util.StringTokenizer;

public class Message {
    char source_node_id;
    Node [] nodes = new Node[26];

    public Message(char source_node_id, Node[] nodes) {
        this.source_node_id = source_node_id;
        this.nodes = nodes;
    }


    //convert message to string(packet)
    @Override
    public String toString() {
        String output = source_node_id+"";
        for(int i=0;i<26;i++){
            if(nodes[i].distance<Float.POSITIVE_INFINITY)
                output+=" "+dv_routing.toChar(i)+" "+nodes[i].toString();
        }
        return output;
    }


    //parse message to vector
    public Message(String str){
        StringTokenizer stringTokenizer = new StringTokenizer(str);
        this.source_node_id = stringTokenizer.nextToken().charAt(0);
        for(int i=0;i<26;i++)
            this.nodes[i]=new Node(Float.POSITIVE_INFINITY,-1);
        while (stringTokenizer.hasMoreTokens()){
            char node_id = stringTokenizer.nextToken().charAt(0);
            float distance = Float.parseFloat(stringTokenizer.nextToken().toString());
            int nextHop = Integer.parseInt(stringTokenizer.nextToken().toString());
            this.nodes[dv_routing.toInt(node_id)]=new Node(distance,nextHop);
        }
    }
}
