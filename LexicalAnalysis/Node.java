package CompilersProject.LexicalAnalysis;

/**
 * @Author：ljz
 * @Date：2022/10/14 20:47
 * @Description: NFA图中的点
 */
public class Node {
    public int id;
    public boolean isLast;
    public boolean needRollback;
    public String type;

    public Node(int id, boolean isLast, boolean needRollback, String type) {
        this.id = id;
        this.isLast = isLast;
        this.needRollback = needRollback;
        this.type = type;
    }
}
