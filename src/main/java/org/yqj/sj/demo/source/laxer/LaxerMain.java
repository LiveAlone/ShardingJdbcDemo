package org.yqj.sj.demo.source.laxer;

import io.shardingjdbc.core.parsing.lexer.Lexer;
import io.shardingjdbc.core.parsing.lexer.dialect.mysql.MySQLLexer;
import io.shardingjdbc.core.parsing.lexer.token.Token;

/**
 * @author yaoqijun on 2017-12-08.
 */
public class LaxerMain {
    public static void main(String[] args) {
        String sql = "SELECT i.* FROM t_order o JOIN t_order_item i ON o.order_id=i.order_id WHERE o.user_id=? AND o.order_id=?";
        Lexer lexer = new MySQLLexer(sql);

        for (int i=0; i< 20; i++){
            lexer.nextToken();
            printToken(lexer.getCurrentToken());
        }
    }

    public static void printToken(Token token){
        System.out.println(token.getLiterals());
        System.out.println(token.getType());
        System.out.println("-----------------------------------------------------");
    }
}
