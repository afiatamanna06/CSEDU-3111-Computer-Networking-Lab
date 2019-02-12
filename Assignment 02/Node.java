import java.util.StringTokenizer;

class Node{
    char node_id;
    int node_port;
    float distance;
    char parent;
    public Node(char node_id, int node_port, float distance, char parent) {
        this.node_id = node_id;
        this.node_port = node_port;
        this.distance = distance;
        this.parent = parent;
    }

    @Override
    public String toString() {
        return  node_id +
                " " + parent +
                " " + distance ;
    }

    public Node(String str, int node_port){
        StringTokenizer stringTokenizer = new StringTokenizer(str);
        this.node_id = stringTokenizer.nextToken().charAt(0);
        this.parent = stringTokenizer.nextToken().charAt(0);
        this.distance = Float.parseFloat(stringTokenizer.nextToken().toString());
        this.node_port = node_port;
    }
}