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
        String dir = "src/CompilersProject/Tests/";
        String in = "test5.txt";
        String out = "test5_out.txt";

        ReadTxt r = new ReadTxt();
        NFA nfa = new NFA();
        DFA dfa = new DFA(nfa);
        dfa.determine();
        System.out.println(dfa);
        TokenTable tokenTable = new TokenTable();
        Lexer lexer = new Lexer(r.readTxt(dir + in), tokenTable, dfa);
        lexer.run();
        tokenTable.printTokenTable();
        tokenTable.saveTokenTable(dir + out);
    }
}
