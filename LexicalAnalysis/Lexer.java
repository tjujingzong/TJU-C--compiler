package CompilersProject.LexicalAnalysis;

import java.io.File;
import java.util.Scanner;

/**
 * @Author：ljz
 * @Date：2022/11/12 9:59
 * @Description:
 */
class Lexer {
    private String source;
    private TokenTable tokenTable;
    private DFA dfa;

    public Lexer(String source, TokenTable tokenTable, DFA dfa) {
        this.source = source;
        this.tokenTable = tokenTable;
        this.dfa = dfa;
    }

    public void run() {
        String token_now = "";
        String text = source;
        int i = 0;
        int ID = 0;
        while (i < text.length()) {
            // 需要跳过的情况
            Character ch = text.charAt(i);
            if (token_now.equals("") && (ch == '\n' || ch == ' ')) {
                i += 1;
                continue;
            }

            token_now += ch;
            // 匹配成功到下一个节点
            if (dfa.nextId(ch.toString())) {
                ID = dfa.nowId;
                // 判断is_final
                if (dfa.isFinal(ID)) {
                    // 判断is_back_off
                    if (dfa.isBackOff(ID)) {
                        // 指针回退一个
                        token_now = token_now.substring(0, token_now.length() - 1);
                        i -= 1;
                    }

                    // 获得最终节点的tag
                    String node_tag = dfa.getTag(ID);
                    // 这个判断应该是dfa提供的
                    String token_type = dfa.getTokenType(token_now, node_tag);
                    String token_num = dfa.getTokenNum(token_now, token_type);

                    tokenTable.pushToken(new Token(token_now, token_type, token_num));
                    token_now = "";
                    dfa.getStart();
                }
                i += 1;
            }
            // 匹配失败，则抛出异常
            else {
                System.out.println(ID);
                System.out.println("Lexical error: 不符合c--词法！");
                return;
            }
        }

        // 如果最后一个词是属于IDNorKWorOP那么也要加入token_list中

        if (!dfa.isFinal(ID)) {
            System.out.println("Lexical error: 最终一个词不是完整的token");
            return;
        }
    }
}