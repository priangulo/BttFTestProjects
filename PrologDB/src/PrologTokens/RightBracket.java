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
public class RightBracket implements Token {
    
    dScan str;

    public RightBracket() {
    }
    
    public RightBracket(dScan x) {
    }

    public boolean canParse() {
        return str.parseChar(']');
    }

    @Override
    public void parse() {
        str.parseSkip(canParse(), "right bracket");
    }

    @Override
    public void setScan(dScan x) {
        str = x;
    }
}
