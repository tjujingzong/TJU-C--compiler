package CompilersProject.LexicalAnalysis;

/**
 * @Author：ljz
 * @Date：2022/10/14 20:57
 * @Description: NFA中的边
 */
public class Edge {
    public Node fromNode;
    public Node toNode;
    public String tag;//边上的符号标记

    public Edge(Node fromNode, Node toNode, String tag) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.tag = tag;
    }
}
