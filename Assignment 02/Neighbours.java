import java.util.HashMap;

public class Neighbours {
    HashMap<Character,Neighbour> neighbours = new HashMap<Character, Neighbour>();

    public void add(char node_id){
        neighbours.put(node_id,new Neighbour());
    }

    public void addMessage(Message msg){
        Neighbour neighbour = neighbours.get(msg.source_node_id);
        neighbour.add(msg);
        neighbours.put(msg.source_node_id,neighbour);
    }


    //check if all neighbours stabilized
    public boolean checkDone(){
        for (char key : neighbours.keySet()) {
            if(neighbours.get(key).done==false){
                return false;
            }
        }
        return true;
    }

}
