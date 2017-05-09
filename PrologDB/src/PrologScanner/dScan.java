/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PrologScanner;

import PrologTokens.*;
import java.util.LinkedList;

public class dScan extends IsPrimitives {

    // these are the single-line sentences that can be parsed
    static public Token[] dbaseStmt
            = {new Fixed("dbase"), new LeftParen(), new Name(), new Comma(), new LeftBracket(), new NameList(), new RightBracket(), new RightParen(), new Dot(), new End()};
    static public Token[] subTableStmt
            = {new Fixed("subtable"), new LeftParen(), new Name(), new Comma(), new LeftBracket(), new NameList(), new RightBracket(),
                new RightParen(), new Dot(), new End()};
    static public Token[] tableStmt
            = {new Fixed("table"), new LeftParen(), new Name(), new Comma(), new LeftBracket(), new ColumnDecls(), new RightBracket(),
                new RightParen(), new Dot(), new End()};
    static public Token[] tupleStmt
            = {new Name(), new LeftParen(), new TupleValues(), new RightParen(), new Dot(), new End()};

    public String toParse;
    public String token;
    public LinkedList<String> parseList;
    public int lineno;

    public dScan(String x, int lineNumber) {
        toParse = x;
        token = null;
        parseList = new LinkedList<>();
        lineno = lineNumber;
    }

    protected dScan() {
    }

    public char peek() {
        return toParse.charAt(0);
    }

    public boolean parseChar(char k) {
        if (toParse.equals("")) {
            return false;
        }
        return (peek() == k) ? returnResult(1) : false;
    }

    public void more() {
        if (toParse.equals("")) {
            throw new ParseException(lineno, "End Of String Reached");
        }
    }

    public boolean returnResult(int i) {
        token = toParse.substring(0, i);
        toParse = toParse.substring(i).trim();
        return true;
    }

    public boolean returnSkip(int i) {
        toParse = toParse.substring(i).trim();
        return true;
    }

    public void parseStep(boolean b, String emsg) {
        if (b) {
            parseList.add(token);
        } else {
            throw new ParseException(lineno, emsg + " expected at >>" + toParse);
        }
    }

    public void parseSkip(boolean b, String emsg) {
        if (b) {
        } else {
            throw new ParseException(lineno, emsg + " expected at >>" + toParse);
        }
    }

    public boolean parser(Token[] actions) {
        for (Token t : actions) {
            t.setScan(this);
            t.parse();
        }
        return true;
    }

}
