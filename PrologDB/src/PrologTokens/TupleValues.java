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
public class TupleValues implements Token { // comma separated tuple values

    dScan str;

    public TupleValues() {
    }

    public TupleValues(dScan x) {
        str = x;
    }

    public boolean canParse() {
        str.more();
        char c = str.peek();
        return  ((c == '\'') || (str.isDigit(c)) || (str.isLetter(c)));
    }

    public void parse() {
        while (true) {
            str.more();
            char c = str.peek();

            if (c == '\'') {
                str.parseStep(new SingleQuotedString(str).canParse(), "single quoted string");
            } else if (str.isDigit(c)) {
                str.parseStep(new number(str).canParse(), "integer");
            } else {
                str.parseStep(new Name(str).canParse(), "name");
            }
            if (str.peek() == ',') {
                str.parseSkip(new Comma(str).canParse(), "comma");
            } else {
                break;
            }
        }
    }

    public void setScan(dScan x) {
        str = x;
    }
}
