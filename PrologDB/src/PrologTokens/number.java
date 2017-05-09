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
public class number implements Token {
    dScan str;

    public number() {
    }
    
    public number(dScan x) {
        str = x;
    }

    public boolean canParse() {
        boolean dotNotYetSeen = true;
        int i,j;
        char c = str.peek();
        boolean positive = !str.isNegative(c);
        int start = positive ? 0 : 1;
        for (i = start; i < str.toParse.length(); i++) {
            c = str.toParse.charAt(i);
            if (str.isDot(c) && dotNotYetSeen) {
                dotNotYetSeen = false;
            }
            else
            if (!str.isDigit(c)) {
                break;
            }
        }
        return str.returnResult(i);
    }
    
    public void parse() {
        str.parseStep(canParse(), "number");
    }

    @Override
    public void setScan(dScan x) {
        str = x;
    }
}
