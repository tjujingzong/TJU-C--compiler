#### 需要实现的单词符号

在下表中列出需要实现的**单词符号**
**关键字（KW）**

| 类别    | 语法关键词                                               |
|-------| ------------------------------------------------|
| 关键字   | (1) int (2) void (3) return (4) const (5) main|

**运算符（OP）**

| 运算符类型 | 语法关键词                                             |
|-------|---------------------------------------------------|
| 计算运算符 | (6) + (7) - (8) * (9) / (10) %                    |
| 比较运算符 | (11) = (12) > (13) < (14) == (15) <= (16) >= (17) != |
| 逻辑运算符 | (18) && (19) \|\| |

**界符（SE）**

| 类型 | 语法关键词 |
| ---- | ---------- |
| 界符 |(20)（  (21) ）  (22) {  (23) }  (24)；  (25) ,     |

**标识符（IDN）** 定义与 C 语言保持相同，为字母、数字和下划线（_）组成的不以数字开头的串

**整数（INT）** 的定义与 C 语言类似，整数由数字串表示
### 一、NFA的构建与确定化算法的实现

#### 状态转化图

如下图：



### 算法实现

运用<font color="red">**面向对象**</font>的编程思想，将NFA,DFA和词法分析器等抽象成一个个类。

由简到繁，先将类的定义规划好，再逐渐扩充完善细节。

#### 3.1 NFA的定义与实现

​	根据NFA定义依据五元组$$M=(S, \sum, \sigma, S_0, F)$$构造成一个**类**，使用面向对象的编程方式来进行词法分析器的编写。

##### 定义的类：

1. **Node**

   表示图中的一个节点。

   * `id`是节点的编号；

   * `isLast`用来判断是否是终止节点；
   * `needRollback`用来判断是否需要回退；
   * `tag`用于判定最终得到的type类别。
   * `Node()`节点的构造函数
   * `compareTo(Node o)`实现`Comparable<Node>`接口，保证在后续算法中的集合中节点是有序的。
```java
public class Node implements Comparable<Node> {
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


    @Override
    public int compareTo(Node o) {
        return this.id - o.id;
    }
}
```

2. **Edge**

	表示图中的边。
	
    * `fromNodeId`表示有向边的出发节点；
    * `toNodeId`使用集合表示通过`tag`可以抵达的节点；
    * `tag`表示获得`tag`可以从`fromNodeId`转化为`toNodeIds`。
```java
public class Edge {
    public int fromNodeId;
    public int toNodeId;
    public String tag;//边上的符号标记

    public Edge(int fromNodeId, int toNodeId, String tag) {
        this.fromNodeId = fromNodeId;
        this.toNodeId = toNodeId;
        this.tag = tag;
    }
}
```
3. **NFA**

	根据点集和边集构造NFA。
	
    * `nodeList`用于存储点的ArrayList
    * `edgeList`用于存储点的ArrayList
    * `tags`存储状态转移的标签
```java
public class NFA {
    public ArrayList<Node> nodeList = new ArrayList<>();
    public ArrayList<Edge> edgeList = new ArrayList<>();
    public String[] tags = {
            "+", "-", "*", "/", "%", "=", "(", ")", "{", "}", ";", ",",
            ">", "^=", "<", "|", "&", "_a-zA-Z", "_0-9a-zA-Z", "^_0-9a-zA-Z",
            "1-9", "0-9", "^0-9"
    };

    public NFA() {//构造函数
		...
    }

    public String toString() {//实现该方法后可在控制台打印，便于调试
		...
    }
}

```
4. DFA
	DFA的数据结构与NFA类似，此处不再赘述。
	NFA到DFA的转变需要我们实现确定化算法。
	首先从$$s_0$$出发，仅经过任意条$$\epsilon$$箭弧能够到达的状态组成的集合$$I$$作为$$M'$$的初态$$q_0$$ .
	分别把从$$q_0$$(对应于$$M$$的状态子集$$I$$)出发，经过任意$$a\in \sum$$的$$a$$弧转换$$I_a$$所组成的集合作为$$M'$$的状态，如此继续，直到不再有新的状态为止。
```java
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
```