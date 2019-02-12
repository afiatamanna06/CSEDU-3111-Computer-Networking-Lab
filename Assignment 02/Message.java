import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Vector;

public class Message {
    char source_node_id;
    Vector<Node> nodes = new Vector<Node>();

    public Message(char source_node_id, Vector<Node> nodes) {
        this.source_node_id = source_node_id;
        this.nodes = nodes;
    }

    //converts vector to string(packet)
    @Override
    public String toString() {
        String ret = source_node_id + "";
        for(int i=0;i<nodes.size();i++){
            if(nodes.get(i).distance!=Float.POSITIVE_INFINITY)
                ret += " " + nodes.get(i).toString();
        }
        return ret;
    }

    //convert message to vector
    public Message(String str){
        StringTokenizer stringTokenizer = new StringTokenizer(str);
        this.source_node_id = stringTokenizer.nextToken().charAt(0);
        while (stringTokenizer.hasMoreTokens()){
            char node_id = stringTokenizer.nextToken().charAt(0);
            int node_port = -1;
            char parent = stringTokenizer.nextToken().charAt(0);
            float distance = Float.parseFloat(stringTokenizer.nextToken().toString());
            this.nodes.add(new Node(node_id,node_port,distance,parent));
        }
    }
}
