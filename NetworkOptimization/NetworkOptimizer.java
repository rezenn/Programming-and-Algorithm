import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class NetworkOptimizer extends JFrame {

    private Panel panel = new Panel();
    private EdgeList<Edge> edgeList = new EdgeList<>();
    private DotList<Dot> dotList = new DotList<>();

    private JRadioButton rbtnAddVertex = new JRadioButton("Add Vertex");
    private JRadioButton rbtnAddEdge = new JRadioButton("Add Edge");
    private JRadioButton rbtnMoveVertex = new JRadioButton("Move Vertex");
    private JRadioButton rbtnShortestPath = new JRadioButton("Shortest Path");
    private JButton btnAddAllEdges = new JButton("Add All Edges");
    private JButton btnRandomWeights = new JButton("Random Weights");
    private JButton btnOptimize = new JButton("Optimize Network");
    private JButton btnSave = new JButton("Save Network");
    private JLabel lblCostWeight = new JLabel("Cost Weight:");
    private JLabel lblLatencyWeight = new JLabel("Latency Weight:");
    private JSlider costWeightSlider = new JSlider(0, 100, 50);
    private JSlider latencyWeightSlider = new JSlider(0, 100, 50);
    private JTextArea txtOutput = new JTextArea(5, 20);

    private double costWeight = 0.5;
    private double latencyWeight = 0.5;

    public NetworkOptimizer() {
        super("Network Optimizer");
        setFrame();
    }

    private void setFrame() {
        this.setSize(800, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(null);
        this.setBackground(Color.WHITE);

        panel.setBounds(250, 10, 520, 600);
        this.add(panel);

        addButtons();
        addSliders();
        addOutputArea();

        this.setVisible(true);
    }

    private void addButtons() {
        rbtnAddVertex.setBounds(10, 50, 200, 30);
        rbtnAddEdge.setBounds(10, 90, 200, 30);
        rbtnMoveVertex.setBounds(10, 130, 200, 30);
        rbtnShortestPath.setBounds(10, 170, 200, 30);
        btnAddAllEdges.setBounds(10, 210, 200, 30);
        btnRandomWeights.setBounds(10, 250, 200, 30);
        btnOptimize.setBounds(10, 290, 200, 30);
        btnSave.setBounds(10, 330, 200, 30);

        this.add(rbtnAddVertex);
        this.add(rbtnAddEdge);
        this.add(rbtnMoveVertex);
        this.add(rbtnShortestPath);
        this.add(btnAddAllEdges);
        this.add(btnRandomWeights);
        this.add(btnOptimize);
        this.add(btnSave);

        rbtnAddVertex.addActionListener(e -> panel.setMode(Panel.Mode.ADD_VERTEX));
        rbtnAddEdge.addActionListener(e -> panel.setMode(Panel.Mode.ADD_EDGE));
        rbtnMoveVertex.addActionListener(e -> panel.setMode(Panel.Mode.MOVE_VERTEX));
        rbtnShortestPath.addActionListener(e -> panel.setMode(Panel.Mode.SHORTEST_PATH));
        btnAddAllEdges.addActionListener(e -> addAllEdges());
        btnRandomWeights.addActionListener(e -> randomizeWeights());
        btnOptimize.addActionListener(e -> optimizeNetwork());
        btnSave.addActionListener(e -> saveNetwork());
    }

    private void addSliders() {
        lblCostWeight.setBounds(10, 370, 100, 20);
        costWeightSlider.setBounds(120, 370, 100, 20);
        lblLatencyWeight.setBounds(10, 400, 100, 20);
        latencyWeightSlider.setBounds(120, 400, 100, 20);

        this.add(lblCostWeight);
        this.add(costWeightSlider);
        this.add(lblLatencyWeight);
        this.add(latencyWeightSlider);

        costWeightSlider.addChangeListener(e -> {
            costWeight = costWeightSlider.getValue() / 100.0;
            latencyWeight = 1.0 - costWeight;
            latencyWeightSlider.setValue((int) (latencyWeight * 100));
        });

        latencyWeightSlider.addChangeListener(e -> {
            latencyWeight = latencyWeightSlider.getValue() / 100.0;
            costWeight = 1.0 - latencyWeight;
            costWeightSlider.setValue((int) (costWeight * 100));
        });
    }

    private void addOutputArea() {
        txtOutput.setBounds(10, 430, 230, 200);
        txtOutput.setEditable(false);
        this.add(txtOutput);
    }

    private void addAllEdges() {
        edgeList.clear();
        for (int i = 0; i < dotList.size(); i++) {
            for (int j = i + 1; j < dotList.size(); j++) {
                Edge edge = new Edge(dotList.get(i), dotList.get(j));
                edge.setWeight((int) (Math.random() * 100) + 1);
                edge.setBandwidth((int) (Math.random() * 100) + 1); 
                edgeList.add(edge);
            }
        }
        panel.repaint();
    }

    private void randomizeWeights() {
        for (Edge edge : edgeList) {
            edge.setWeight((int) (Math.random() * 100) + 1);
            edge.setBandwidth((int) (Math.random() * 100) + 1);
        }
        panel.repaint();
    }

    private void optimizeNetwork() {
        if (dotList.size() < 2) {
            txtOutput.setText("Not enough nodes to optimize.");
            return;
        }
    
        for (Edge edge : edgeList) {
            edge.setColor(Color.BLUE);
        }
        for (Dot dot : dotList) {
            dot.setColor(Color.RED);
        }
    
        Dijkstra dijkstra = new Dijkstra();
        dijkstra.execute(dotList.get(0)); 
    
        for (int i = 1; i < dotList.size(); i++) {
            dijkstra.getPath(dotList.get(0), dotList.get(i));
        }
    
        evaluateTopology();
    }

    private void evaluateTopology() {
        int totalCost = 0;
        double totalLatency = 0;
        for (Edge edge : edgeList) {
            totalCost += edge.getWeight();
            totalLatency += 1.0 / edge.getBandwidth(); 
        }
        txtOutput.setText("Total Cost: " + totalCost + "\nTotal Latency: " + totalLatency);
    }

    private void saveNetwork() {
        try (FileWriter writer = new FileWriter("network.txt")) {
            for (Edge edge : edgeList) {
                writer.write(edge.getA().getX() + "," + edge.getA().getY() + "," +
                            edge.getB().getX() + "," + edge.getB().getY() + "," +
                            edge.getWeight() + "," + edge.getBandwidth() + "\n");
            }
            txtOutput.setText("Network saved to network.txt");
        } catch (IOException e) {
            txtOutput.setText("Error saving network: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new NetworkOptimizer();
    }

    protected class Panel extends JPanel {
        public enum Mode { ADD_VERTEX, ADD_EDGE, MOVE_VERTEX, SHORTEST_PATH }
        private Mode mode = Mode.ADD_VERTEX;
        private Dot selectedDot = null;
        private Dot startNode = null;
        private Dot endNode = null;

        public Panel() {
            this.setBackground(Color.WHITE);
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleClick(e.getX(), e.getY());
                }
            });
        }

        private void handleClick(int x, int y) {
            switch (mode) {
                case ADD_VERTEX:
                    dotList.add(new Dot(x, y));
                    break;
                case ADD_EDGE:
                    Dot dot = findDot(x, y);
                    if (dot != null) {
                        if (selectedDot == null) {
                            selectedDot = dot;
                        } else {
                            edgeList.add(new Edge(selectedDot, dot));
                            selectedDot = null;
                        }
                    }
                    break;
                case SHORTEST_PATH:
                    Dot clickedDot = findDot(x, y);
                    if (clickedDot != null) {
                        if (startNode == null) {
                            startNode = clickedDot;
                        } else if (endNode == null) {
                            endNode = clickedDot;
                            calculateShortestPath();
                        }
                    }
                    break;
            }
            repaint();
        }

        private void calculateShortestPath() {
            if (startNode != null && endNode != null) {
                Dijkstra dijkstra = new Dijkstra();
                dijkstra.execute(startNode);
                dijkstra.getPath(startNode, endNode);
                startNode = null;
                endNode = null;
            }
        }

        private Dot findDot(int x, int y) {
            for (Dot dot : dotList) {
                if (Math.abs(dot.getX() - x) < 10 && Math.abs(dot.getY() - y) < 10) {
                    return dot;
                }
            }
            return null;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (Dot dot : dotList) {
                dot.draw(g);
            }
            for (Edge edge : edgeList) {
                edge.draw(g);
            }
        }

        public void setMode(Mode mode) {
            this.mode = mode;
        }
    }

    protected class Dot {
        private int x, y;
        private Color color = Color.RED;

        public Dot(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() { return x; }
        public int getY() { return y; }
        public void setColor(Color color) { this.color = color; }

        public void draw(Graphics g) {
            g.setColor(color);
            g.fillOval(x - 5, y - 5, 10, 10);
        }
    }

    protected class Edge {
        private Dot a, b;
        private int weight;
        private int bandwidth;
        private Color color = Color.BLUE;

        public Edge(Dot a, Dot b) {
            this.a = a;
            this.b = b;
        }

        public void setWeight(int weight) { this.weight = weight; }
        public int getWeight() { return weight; }
        public void setBandwidth(int bandwidth) { this.bandwidth = bandwidth; }
        public int getBandwidth() { return bandwidth; }
        public Dot getA() { return a; }
        public Dot getB() { return b; }
        public void setColor(Color color) { this.color = color; }

        public void draw(Graphics g) {
            g.setColor(color);
            g.drawLine(a.getX(), a.getY(), b.getX(), b.getY());
            g.drawString("C:" + weight + ", B:" + bandwidth, (a.getX() + b.getX()) / 2,
             (a.getY() + b.getY()) / 2);
        }
    }

    protected class Dijkstra {
        private Map<Dot, Integer> distance = new HashMap<>();
        private Map<Dot, Dot> previous = new HashMap<>();
        private Set<Dot> visited = new HashSet<>();

        public void execute(Dot start) {
            for (Dot dot : dotList) {
                distance.put(dot, Integer.MAX_VALUE);
            }
            distance.put(start, 0);

            while (visited.size() != dotList.size()) {
                Dot current = getClosestUnvisitedNode();
                visited.add(current);

                for (Edge edge : edgeList) {
                    if (edge.getA() == current || edge.getB() == current) {
                        Dot neighbor = (edge.getA() == current) ? edge.getB() : edge.getA();
                        int newDistance = distance.get(current) + edge.getWeight();
                        if (newDistance < distance.get(neighbor)) {
                            distance.put(neighbor, newDistance);
                            previous.put(neighbor, current);
                        }
                    }
                }
            }
        }

        private Dot getClosestUnvisitedNode() {
            Dot closest = null;
            int minDistance = Integer.MAX_VALUE;
            for (Dot dot : dotList) {
                if (!visited.contains(dot) && distance.get(dot) < minDistance) {
                    minDistance = distance.get(dot);
                    closest = dot;
                }
            }
            return closest;
        }
        public void getPath(Dot start, Dot end) {
            LinkedList<Dot> path = new LinkedList<>();
            for (Dot at = end; at != null; at = previous.get(at)) {
                path.addFirst(at);
            }

            for (int i = 0; i < path.size() - 1; i++) {
                Dot current = path.get(i);
                Dot next = path.get(i + 1);
                for (Edge edge : edgeList) {
                    if ((edge.getA() == current && edge.getB() == next) ||
                        (edge.getA() == next && edge.getB() == current)) {
                        edge.setColor(Color.RED);
                        current.setColor(Color.GREEN);
                        next.setColor(Color.GREEN);
                    }
                }
            }
            panel.repaint();
        }
    }

    protected class DotList<T> extends ArrayList<T> {}
    protected class EdgeList<T> extends ArrayList<T> {}
}