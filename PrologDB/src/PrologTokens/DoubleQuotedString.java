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
public class DoubleQuotedString implements Token {
    dScan str;

    DoubleQuotedString() {
    }
    
    DoubleQuotedString(dScan x) {
        str = x;
    }

    public boolean canParse() {
        int i;
        char c = str.peek();

        if (!str.isDoubleQuote(c)) {
            return false;
        }
        for (i = 1; i < str.toParse.length(); i++) {
            c = str.toParse.charAt(i);
            if (str.isDoubleQuote(c)) {
                break;
            }
        }
        return str.isDoubleQuote(c) ? str.returnResult(i + 1) : false;
    }

    public void parse() {
        str.parseStep(canParse(), "double quoted string");
    }

    @Override
    public void setScan(dScan x) {
        str = x;
    }

}
