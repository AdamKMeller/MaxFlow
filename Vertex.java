public class Vertex {
    private boolean leftVertex = false;
    private int label = -1;
    public Vertex(int label, boolean left){ 
        leftVertex = left; 
        this.label = label;
    }
    public boolean isLeftVertex(){ return leftVertex; }
    public int getLabel(){ return label; } 
    public String toString(){
        return label + (leftVertex ? "L" : "R");
    }
}
