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
public class LeftParen implements Token {
    
    dScan str;

    public LeftParen() {
    }
    
    public LeftParen(dScan x) {
        str = x;
    }
    
    public boolean canParse() {
        return str.parseChar('(');
    }

    @Override
    public void parse() {
        str.parseSkip(canParse(), "left paren");
    }

    @Override
    public void setScan(dScan x) {
        str = x;
    }
}
