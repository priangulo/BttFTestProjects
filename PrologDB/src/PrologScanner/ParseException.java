/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PrologScanner;

/**
 *
 * @author don
 */
public class ParseException extends RuntimeException {
    public ParseException(int lineno, String msg) {
        super(((lineno<0)?"":"at line "+lineno+" ")+msg);
    }
}
