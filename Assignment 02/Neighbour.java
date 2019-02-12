import java.util.Vector;

public class Neighbour {
    Vector<String> messages = new Vector<String>();
    boolean done = false;

    //check if this neighbour is stabilized
    public void checkDone(){
        if(messages.size()<10){
            return;
        }
        boolean doneTemp=true;
        for(int i=messages.size()-10;i<messages.size();i++){
            if(!messages.get(i).equals(messages.get(messages.size()-1))){
                doneTemp = false;
            }
        }
        done = doneTemp;

    }

    //add messages
    public void add(Message msg){
        messages.add(msg.toString());
        checkDone();
    }
}
