
class Node{
    float distance;
    int nextHop;
    public Node(float distance, int nextHop) {
        this.distance = distance;
        this.nextHop = nextHop;
    }

    @Override
    public String toString() {
        return distance+" "+nextHop;
    }
}