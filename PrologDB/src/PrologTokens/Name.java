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
public class Name implements Token {
    
    dScan str;

    public Name() {
    }
    
    public Name(dScan x) {
        str = x;
    }

    public boolean canParse() {
        int i;
        if (!str.isletter(str.peek())) {
            return false;
        }
        for (i = 1; i < str.toParse.length(); i++) {
            if (!str.isLetterOrDigitOrUnderScore(str.toParse.charAt(i))) {
                break;
            }
        }
        return str.returnResult(i);
    }

    public void parse() {
        str.parseStep(canParse(), "name");
    }

    @Override
    public void setScan(dScan x) {
        str = x;
    }
}
