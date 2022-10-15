package CompilersProject.LexicalAnalysis;


import java.io.File;

/**
 * @Author：ljz
 * @Date：2022/10/14 19:16
 * @Description: 词法分析器的程序入口
 */
public class Main {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        //默认是当前工程文件夹
        String path = "src/CompilersProject/LexicalAnalysis/Tests/test0.txt";
        File file = new File(path);
        System.out.println(ReadTxt.readTxt(path));
    }
}
