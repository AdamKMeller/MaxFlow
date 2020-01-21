import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
public class MatchingViewer extends JPanel implements ActionListener {
    public static final int DIM_X = 800;
    public static final int DIM_Y = 800;
    public static final int POINT_RADIUS = 4;
    private JButton b1, b2, b3;
    private JPanel bottomButtons = new JPanel();
    
    //these three edge lists are used for drawing
    //note that "edges" is always a superset of the other tow
    private ArrayList<Edge> edges = new ArrayList<>();
    private ArrayList<Edge> matching = new ArrayList<>();
    private ArrayList<Edge> path = new ArrayList<>();
    private BipartiteGraph bg;
    private int nLeft = 3;
    private int nRight = 3;
    public void actionPerformed(ActionEvent e){
        if (e.getActionCommand().equals("fp")){
            //new augmenting paths are stored in the list "path"
            ArrayList<Edge> augPath = bg.findAugmentingPath();
            if (augPath != null){
                path = augPath;
            }
        }
        else if (e.getActionCommand().equals("am")){
            //if the path has already been computed, update the matching
            bg.augmentFlow(path);
            recomputeMatching();
            path = new ArrayList<>();
        }
        else if (e.getActionCommand().equals("mm")){
            ArrayList<Edge> augPath;
            while ((augPath = bg.findAugmentingPath()) != null){
                bg.augmentFlow(augPath);
            }
            recomputeMatching();
            path = new ArrayList<>();
        }
        repaint();
    }
    public void recomputeMatching(){
        //whether an edge is in the current matching is the only
        //instance variable we have in the Edge class
        matching = new ArrayList<>();
        for (Edge e : edges){
            if (e.isInMatching()){
                matching.add(e);
            }
        }
    }
    public MatchingViewer(String filename) throws Exception {
        ArrayList<int[]> pairs = new ArrayList<>();
        Scanner r = new Scanner(new File(filename));
        String str = r.nextLine();
        String[] tokens = str.split("[ ]+");
        nLeft = Integer.parseInt(tokens[0]);
        nRight = Integer.parseInt(tokens[1]);
        while (r.hasNext()){
            str = r.nextLine();
            tokens = str.split("[ ]+");
            int u = Integer.parseInt(tokens[0]);
            int v = Integer.parseInt(tokens[1]);
            pairs.add(new int[]{u, v});
        }
        bg = new BipartiteGraph(nLeft, nRight, pairs);
        edges = bg.getEdges();
        
        setPreferredSize(new Dimension(DIM_X, DIM_Y));
        setLayout(new BorderLayout());
        bottomButtons.setLayout(new GridLayout(1,3));
        b1 = new JButton("Find Path");
        b1.setActionCommand("fp");
        b2 = new JButton("Augment Matching");
        b2.setActionCommand("am");
        b3 = new JButton("Maximum Matching");
        b3.setActionCommand("mm");
        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        bottomButtons.add(b1);
        bottomButtons.add(b2);
        bottomButtons.add(b3);
    }
    public JPanel getButtons(){
        return bottomButtons;
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        drawPoints(g);
        drawLines(g, path, Color.BLUE, 5);
        drawLines(g, edges, Color.BLACK, 1);
        drawLines(g, matching, Color.RED, 3);
    }
    public double getX(Vertex v){
        return (v.isLeftVertex() ? 0.166 : 0.833);
    }
    public double getY(Vertex v){
        double gap = 1./(Math.max(nLeft, nRight)+1);
        double index = ((double)v.getLabel())+0.5-(v.isLeftVertex() ? nLeft : nRight)/2.0;
        return 0.5+index*gap;
    }
    public void drawPoints(Graphics g){
        g.setColor(Color.BLACK);
        for (Vertex v : bg.getVertices()){
            int ix = (int)(getX(v)*getWidth());
            int iy = (int)(getY(v)*getHeight());
            g.fillOval(ix-POINT_RADIUS, iy-POINT_RADIUS, POINT_RADIUS*2, POINT_RADIUS*2);
            g.drawString(v.toString(), ix+40*(v.isLeftVertex() ? -1 : 1), iy);
        }
    }
    public void drawLines(Graphics g, ArrayList<Edge> edgeList, Color c, int weight){
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(c);
        g2.setStroke(new BasicStroke(weight));
        for (Edge e : edgeList){
            Vertex u = e.getHead();
            Vertex v = e.getTail();
            int ix1 = (int)(getX(u)*getWidth());
            int iy1 = (int)(getY(u)*getHeight());
            int ix2 = (int)(getX(v)*getWidth());
            int iy2 = (int)(getY(v)*getHeight());
            g2.drawLine(ix1, iy1, ix2, iy2);
        }
    }
    public static void main(String[] args) throws Exception {
        JFrame j = new JFrame();
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        j.setLayout(new BorderLayout());
        MatchingViewer mv = new MatchingViewer(args[0]);
        j.add(mv, BorderLayout.CENTER);
        j.add(mv.getButtons(), BorderLayout.PAGE_END);
        j.setVisible(true);
        j.setResizable(true);
        j.setTitle("Matching Viewer");
        j.pack();
    }
}
