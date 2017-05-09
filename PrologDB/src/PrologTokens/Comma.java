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
public class Comma implements Token {
    
    dScan str;

    public Comma() {
    }
    
    public Comma(dScan x) {
        str = x;
    }

    public boolean canParse() {
        return str.parseChar(',');
    }

    public void parse() {
        str.parseSkip(canParse(), "comma");
    }

    public void setScan(dScan x) {
        str = x;
    }
}
