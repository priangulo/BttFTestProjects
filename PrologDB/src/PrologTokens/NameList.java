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
public class NameList implements Token { // comma separated names

    dScan str; 
    
    public NameList() {
    }
    
    public NameList(dScan x) {
        str = x;
    }

    public boolean canParse() {
        str.more();
        char c = str.peek();
        return  (str.isLetter(c));
    }

    public void parse() {
        while (true) {
            str.more();

            str.parseStep( new Name(str).canParse(), "name");

            if (str.peek() == ',') {
                str.parseSkip(new Comma(str).canParse(), "comma");
            } else {
                break;
            }
        }
    }

    @Override
    public void setScan(dScan x) {
        str = x;
    }
}
