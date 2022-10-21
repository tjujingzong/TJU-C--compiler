package CompilersProject.LexicalAnalysis;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.TreeSet;

/**
 * @Author：ljz
 * @Date：2022/10/18 19:07
 * @Description: DFA的构建
 */
public class DFA {
    public ArrayList<Node> nodeList = new ArrayList<>();
    public ArrayList<Edge> edgeList = new ArrayList<>();
    public NFA nfa;

    public DFA(NFA nfa) {
        this.nfa = nfa;
    }

    public TreeSet<Node> epsilonClosure(TreeSet<Node> nodeSet) {
        //使用bfs方法计算epsilon闭包,即经过若干条epsilon边可到达的边
        Queue<Node> q = new ArrayDeque<>();
        for (Node n : nodeSet) q.add(n);
        while (!q.isEmpty()) {
            Node top = q.poll();
            for (Edge e : nfa.edgeList) {
                if (top.id == e.fromNodeId && e.tag.equals("epsilon")) {
                    if (nodeSet.contains(nfa.nodeList.get(e.toNodeId)))
                        continue;
                    nodeSet.add(nfa.nodeList.get(e.toNodeId));
                    q.add(nfa.nodeList.get(e.toNodeId));
                }
            }
        }
        return nodeSet;
    }

    public TreeSet<Node> move(TreeSet<Node> nodeSet, String a) {
        //move计算经过一次a边可以到达的点
        TreeSet<Node> res = new TreeSet<>();
        for (Node n : nodeSet) {
            for (Edge e : nfa.edgeList) {
                if (n.id == e.fromNodeId && e.tag.equals(a)) {
                    res.add(nfa.nodeList.get(e.toNodeId));
                }
            }
        }
        return res;
    }

    public void determine() {
        nodeList.add(new Node(0, false, false, ""));
        TreeSet<Node> startNodeSet = new TreeSet<>();
        startNodeSet.add(nfa.nodeList.get(0));
        startNodeSet = epsilonClosure(startNodeSet);//
        ArrayList<TreeSet<Node>> nodeArrayList = new ArrayList<>();
        nodeArrayList.add(startNodeSet);
        int pointer = 0;
        int nowId = 0;
        while (pointer < nodeArrayList.size()) {
            TreeSet<Node> nodeSet = nodeArrayList.get(pointer);
            for (String tag : nfa.tags) {
                TreeSet<Node> moveNodeSet = move(epsilonClosure(nodeSet), tag);
                if (moveNodeSet.isEmpty())
                    continue;
                if (!nodeArrayList.contains(moveNodeSet)) {
                    nodeArrayList.add(moveNodeSet);
                    Node firstInSet = nodeSet.first();
                    nowId++;
                    nodeList.add(new Node(nowId, firstInSet.isLast, firstInSet.needRollback, firstInSet.type));
                    edgeList.add(new Edge(pointer, nowId, tag));
                } else {
                    int toNewNodeId = nodeArrayList.indexOf(moveNodeSet);
                    edgeList.add(new Edge(pointer, toNewNodeId, tag));
                }
            }
            pointer++;
        }
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
