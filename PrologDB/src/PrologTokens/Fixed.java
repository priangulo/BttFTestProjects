/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PrologTokens;

import PrologScanner.dScan;

/**
 *
 * for fixed (no variability) string patterns
 */
public class Fixed implements Token {
    
    dScan str;

    String pattern;
    
    public Fixed(String value, dScan x) {
        pattern = value;
        str = x;
    }

    public Fixed(String value) {
        this.pattern = value;
    }

    public boolean canParse() {
        return str.toParse.startsWith(pattern) ? str.returnSkip(pattern.length()) : false;
    }

    public void parse() {
        str.parseSkip(canParse(), "string \"" + pattern+"\"");
    }

    @Override
    public void setScan(dScan x) {
        str = x;
    }
}
