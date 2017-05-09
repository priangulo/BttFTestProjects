package opl;

public class Int extends Exp {
    int v;
    Int(int a) { v=a; }
    String print() { return ""+v; }
    int eval() { return v; }
}
