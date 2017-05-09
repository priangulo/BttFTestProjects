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
public class End implements Token {
    
    dScan str;

    public End() {
    }
    
    public End(dScan x) {
    }

    public boolean canParse() {
        return str.toParse.equals("");
    }

    @Override
    public void parse() {
        str.parseSkip(canParse(),"end of line");
    }

    @Override
    public void setScan(dScan x) {
        str = x;
    }
}
