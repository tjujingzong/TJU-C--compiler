package CompilersProject.LexicalAnalysis;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @Author：ljz
 * @Date：2022/10/18 19:07
 * @Description: DFA的构建
 */
public class DFA {


    public static final Map<String, Integer> TYPE_TO_CONTENT_DICT_KW = new HashMap<String, Integer>() {
        {
            put("int", 1);
            put("void", 2);
            put("return", 3);
            put("const", 4);
            put("main", 5);
        }
    };
    private static final Map<String, Integer> TYPE_TO_CONTENT_DICT_OP = new HashMap<String, Integer>() {
        {
            put("+", 6);
            put("-", 7);
            put("*", 8);
            put("/", 9);
            put("%", 10);
            put("=", 11);
            put(">", 12);
            put("<", 13);
            put("==", 14);
            put("<=", 15);
            put(">=", 16);
            put("!=", 17);
            put("&&", 18);
            put("||", 19);
        }
    };
    public static final Map<String, Integer> TYPE_TO_CONTENT_DICT_SE = new HashMap<String, Integer>() {{
        put("(", 20);
        put(")", 21);
        put("{", 22);
        put("}", 23);
        put(";", 24);
        put(",", 25);
    }};
    public ArrayList<Node> nodeList = new ArrayList<>();
    public ArrayList<Edge> edgeList = new ArrayList<>();
    public NFA nfa;
    int nowId;
    int startId;


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
        nodeList = nfa.nodeList;
        edgeList = nfa.edgeList;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        System.out.println("DFA节点数:" + nodeList.size());
        for (Edge e : edgeList) {
            sb.append(e.fromNodeId);
            sb.append("(");
            sb.append(nodeList.get(e.fromNodeId).isLast + ",");
            sb.append(nodeList.get(e.fromNodeId).needRollback + ",");
            sb.append(nodeList.get(e.fromNodeId).type);
            sb.append(")----- ");
            sb.append(e.tag);
            sb.append(" ----->");
            sb.append(e.toNodeId);
            sb.append("(");
            sb.append(nodeList.get(e.toNodeId).isLast + ",");
            sb.append(nodeList.get(e.toNodeId).needRollback + ",");
            sb.append(nodeList.get(e.toNodeId).type + ")");
            sb.append("\n");
        }
        return sb.toString();
    }

    public boolean nextId(String tag) {
        for (Edge edge : edgeList) {
            if (edge.fromNodeId == nowId && Pattern.matches(edge.tag, tag)) {
                // 并将nowId指向新的位置
                nowId = edge.toNodeId;
                // 说明成功找到下一个节点
                return true;
            }
        }
        System.out.println(tag);
        return false;
    }

    public boolean isFinal(int id) {
        return nodeList.get(id).isLast;
    }

    public boolean isBackOff(int id) {
        return nodeList.get(id).needRollback;
    }

    public String getTag(int id) {
        return nodeList.get(id).type;
    }

    public String getTokenType(String token, String node_tag) {
        if (node_tag.equals("OP") || node_tag.equals("SE") || node_tag.equals("INT")) {
            return node_tag;
        } else if (node_tag.equals("IDNorKWorOP")) {
            Set<String> keywords = TYPE_TO_CONTENT_DICT_KW.keySet();
            Set<String> ops = TYPE_TO_CONTENT_DICT_OP.keySet();
            if (keywords.contains(token)) {
                return "KW";
            } else if (ops.contains(token)) {
                return "OP";
            } else {
                return "IDN";
            }
        } else {
            return "ERROR";
        }
    }

    public String getTokenNum(String token, String token_type) {
        if (token_type == "IDN" || token_type == "INT") {
            return token;
        } else if (token_type == "KW") {
            return TYPE_TO_CONTENT_DICT_KW.get(token).toString();
        } else if (token_type == "OP") {
            return TYPE_TO_CONTENT_DICT_OP.get(token).toString();
        } else if (token_type == "SE") {
            //System.out.println(token);
            return TYPE_TO_CONTENT_DICT_SE.get(token).toString();
        }
        return "error";
    }

    public void getStart() {
        nowId = startId;
    }
}
