/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PrologTokens;

import PrologScanner.dScan;

/**
 *
 * @author don
 */
public class SingleQuotedString implements Token {
    
    dScan str;

    public SingleQuotedString() {
    }
    
    public SingleQuotedString(dScan x) {
        str = x;
    }

    public boolean canParse() {
        int i;
        char c = str.peek();
        if (!str.isSingleQuote(c)) {
            return false;
        }
        for (i = 1; i < str.toParse.length(); i++) {
            c = str.toParse.charAt(i);
            if (str.isSingleQuote(c)) {
                break;
            }
        }
        return str.isSingleQuote(c) ? str.returnResult(i + 1) : false;
    }

    public void parse() {
        str.parseStep(canParse(), "single quoted string");
    }

    public void setScan(dScan x) {
       str = x;
    }
}
