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
public class RightParen implements Token {
    
    dScan str;

    public RightParen() {
    }
    
    public RightParen(dScan x) {
        str = x;
    }

    public boolean canParse() {
        return str.parseChar(')');
    }

    @Override
    public void parse() {
        str.parseSkip(canParse(), "right paren");
    }

    @Override
    public void setScan(dScan x) {
        str = x;
    }
}
