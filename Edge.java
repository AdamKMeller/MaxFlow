public class Edge {
    private Vertex u;
    private Vertex v;
    private boolean inMatching;
    public Edge(Vertex u, Vertex v){
        this.u = u;
        this.v = v;
    }
    public Vertex getHead(){ return u; }
    public Vertex getTail(){ return v; }
    public Vertex opposite(Vertex z){
        if (z == u) return v;
        else if (z == v) return u;
        return null;
    }
    public boolean isInMatching(){ return inMatching; }
    public void setInMatching(boolean b){ inMatching = b; }
    public String toString(){
        return "(" + u + ", " + v + ")";
    }
}
