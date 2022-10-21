package CompilersProject.LexicalAnalysis;

import java.util.ArrayList;

/**
 * @Author：ljz
 * @Date：2022/10/15 19:03
 * @Description: 构造NFA
 */
public class NFA {
    public ArrayList<Node> nodeList = new ArrayList<>();
    public ArrayList<Edge> edgeList = new ArrayList<>();
    public String[] tags = {
            "+", "-", "*", "/", "%", "=", "(", ")", "{", "}", ";", ",",
            ">", "^=", "<", "|", "&", "_a-zA-Z", "_0-9a-zA-Z", "^_0-9a-zA-Z",
            "1-9", "0-9", "^0-9"
    };

    public NFA() {
        String[] directOP = {"+", "-", "*", "/", "%", "="};
        String[] SE = {"(", ")", "{", "}", ";", ","};
        nodeList.add(new Node(0, false, false, "epsilon"));
        edgeList.add(new Edge(0, 0, " "));
        for (int i = 1; i <= 6; i++) {
            nodeList.add(new Node(i, true, false, "OP"));
            edgeList.add(new Edge(0, i, directOP[i - 1]));
        }
        for (int i = 1; i <= 6; i++) {
            nodeList.add(new Node(i + 6, true, false, "SE"));
            edgeList.add(new Edge(0, i + 6, SE[i - 1]));
        }
        nodeList.add(new Node(13, false, false, ""));
        edgeList.add(new Edge(0, 13, ">"));
        nodeList.add(new Node(14, true, false, "OP"));
        edgeList.add(new Edge(13, 14, "="));
        nodeList.add(new Node(15, true, true, "OP"));
        edgeList.add(new Edge(13, 15, "epsilon"));

        nodeList.add(new Node(16, false, false, ""));
        edgeList.add(new Edge(0, 16, "<"));
        nodeList.add(new Node(17, true, false, "OP"));
        edgeList.add(new Edge(16, 17, "="));
        nodeList.add(new Node(18, true, true, "OP"));
        edgeList.add(new Edge(16, 18, "^="));

        nodeList.add(new Node(19, false, false, ""));
        nodeList.add(new Node(20, true, false, "OP"));
        edgeList.add(new Edge(0, 19, "|"));
        edgeList.add(new Edge(19, 20, "|"));

        nodeList.add(new Node(21, false, false, ""));
        nodeList.add(new Node(22, true, false, "OP"));
        edgeList.add(new Edge(0, 21, "&"));
        edgeList.add(new Edge(21, 22, "&"));

        nodeList.add(new Node(23, false, false, ""));
        nodeList.add(new Node(24, true, true, "KWorIDN"));
        edgeList.add(new Edge(0, 23, "_a-zA-Z"));
        edgeList.add(new Edge(23, 23, "_0-9a-zA-Z"));
        edgeList.add(new Edge(23, 24, "^_0-9a-zA-Z"));

        nodeList.add(new Node(25, false, false, ""));
        nodeList.add(new Node(26, true, true, "INT"));
        edgeList.add(new Edge(0, 25, "1-9"));
        edgeList.add(new Edge(25, 25, "0-9"));
        edgeList.add(new Edge(25, 26, "^0-9"));
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (Edge e : edgeList) {
            sb.append(e.fromNodeId);
            sb.append("----- ");
            sb.append(e.tag);
            sb.append(" ----->");
            sb.append(e.toNodeId);
            sb.append("\n");
        }
        return sb.toString();
    }
}
