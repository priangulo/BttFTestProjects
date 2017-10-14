/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package BI;

//import java.math.*;
import java.math.BigInteger;

/**
 *
 * @author dsb
 */
public class Calc{
    final static BigInteger zero = new  BigInteger("0");
    BigInteger value = zero;

    void sub(String n) { value = value.subtract( new BigInteger(n)); }
    void mul(String n) { value = value.multiply( new BigInteger(n)); }
    void add(String n) { value = value.add( new BigInteger(n)); }
    void div(String n) { value = value.divide( new BigInteger(n)); }
    String get() { return value.toString(); }
    void set(String n) { value = new BigInteger(n); }
    void clear() { value = zero; }
}

