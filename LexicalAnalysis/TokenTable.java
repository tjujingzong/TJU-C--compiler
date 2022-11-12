package CompilersProject.LexicalAnalysis;

import java.io.FileWriter;
import java.util.ArrayList;

/**
 * @Author：ljz
 * @Date：2022/11/12 9:51
 * @Description:
 */
public class TokenTable {
    private ArrayList<Token> tokens;

    public TokenTable() {
        tokens = new ArrayList<Token>();
    }

    public void printTokenTable() {
        for (Token token : tokens) {
            System.out.println(token.getLexeme() + "   <" + token.getTokenType() + "," + token.getTokenNum() + ">");
        }
    }

    public void pushToken(Token token) {
        tokens.add(token);
    }

    public void saveTokenTable(String path) {
        try {
            FileWriter f = new FileWriter(path);
            for (Token token : tokens) {
                if (token != tokens.get(tokens.size() - 1)) {
                    f.write(token.getLexeme() + "   <" + token.getTokenType() + "," + token.getTokenNum() + ">\n");
                } else {
                    f.write(token.getLexeme() + "   <" + token.getTokenType() + "," + token.getTokenNum() + ">");
                }
            }
            f.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
