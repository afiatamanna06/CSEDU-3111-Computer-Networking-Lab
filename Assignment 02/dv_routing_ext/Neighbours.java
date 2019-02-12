import java.util.HashMap;
import java.util.Vector;

public class Neighbours {
    HashMap<Character,Vector<String>> messagesMap= new HashMap<Character, Vector<String>>();

    //add neighbour to map
    void add(Character c){
        messagesMap.put(c, new Vector<String>());
    }

    //add message to map
    void addMessage(Character c,String message){
        messagesMap.get(c).add(message);
    }

    //check if a neighbour is stabilized
    boolean doneVector(Vector<String> messages){
        if(messages.size()<10){
            return false;
        }
        for(int i=messages.size()-10;i<messages.size();i++){
            if(!messages.get(messages.size()-1).equals(messages.get(i))){
                return false;
            }
        }
        return true;
    }

    //check if all nodes are stabilized
    boolean done(){
        for (char key : messagesMap.keySet()) {
            if(doneVector(messagesMap.get(key))==false){
                return false;
            }
        }
        return true;
    }




}
