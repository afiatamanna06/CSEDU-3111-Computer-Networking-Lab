import java.util.Date;

public class updateThread extends Thread{

    //detects if a neighbour is dead and update vector accordingly
    public void run(){
        while (true){
            for(int i =0;i<dv_routing.neighbours.size();i++){
                if(new Date().getTime()-dv_routing.nodeTime[dv_routing.neighbours.get(i).id].getTime()>15009){
                    dv_routing.table[dv_routing.toInt(dv_routing.source_node_id)][dv_routing.neighbours.get(i).id]=new Node(Float.POSITIVE_INFINITY,-1);
                    dv_routing.messageQueue.messagesMap.remove(dv_routing.toChar(dv_routing.neighbours.get(i).id));
                    for(int j=0;j<26;j++){
                        if(dv_routing.table[dv_routing.toInt(dv_routing.source_node_id)][j].nextHop==dv_routing.neighbours.get(i).id&&
                                dv_routing.table[dv_routing.toInt(dv_routing.source_node_id)][j].distance<Float.POSITIVE_INFINITY){
                            dv_routing.table[dv_routing.toInt(dv_routing.source_node_id)][j]=new Node(Float.POSITIVE_INFINITY,-1);
                                for(int k =0;k<dv_routing.neighbours.size();k++){
                                    if(dv_routing.neighbours.get(k).id==j){
                                        dv_routing.table[dv_routing.toInt(dv_routing.source_node_id)][j]=new Node(dv_routing.neighbours.get(k).distance,j);

                                    }
                                }

                        }
                    }
                }
            }
        }

    }
}
