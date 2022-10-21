package CompilersProject.LexicalAnalysis;


/**
 * @Author：ljz
 * @Date：2022/10/14 19:16
 * @Description: 词法分析器的程序入口
 */
public class Main {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        //默认是当前工程文件夹
        String path = "src/CompilersProject/Tests/test0.txt";
        ReadTxt r = new ReadTxt();
        System.out.println(r.readTxt(path));
        NFA nfa = new NFA();
        System.out.println(nfa);
        DFA dfa = new DFA(nfa);
        dfa.determine();
        System.out.println(dfa);
    }
}
