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
public class IsPrimitives {

    public boolean isDigit(char c) {
        return '0' <= c && c <= '9';
    }

    public boolean isDoubleQuote(char c) {
        return c == '"';
    }

    public boolean isLetter(char c) {
        return 'a' <= c && c <= 'z' || 'A' <= c && c <= 'Z';
    }

    public boolean isLetterOrDigitOrUnderScore(char c) {
        return isLetter(c) || isDigit(c) || isUnderScore(c);
    }

    public boolean isNegative(char c) {
        return c == '-';
    }

    public boolean isSingleQuote(char c) {
        return c == '\'';
    }

    public boolean isletter(char c) {
        return 'a' <= c && c <= 'z';
    }
    
    public boolean isDot(char c) {
        return c == '.';
    }
    
    public boolean isUnderScore(char c) {
        return c == '_';
    }
    
}
