package opl;

public class Plus extends Exp {
    Exp l,r;
    Plus(Exp L, Exp R) { l=L; r=R; }
    String print() {
      return l.print() + "+" + r.print();
    }
    int eval() {
        return l.eval() + r.eval();
    }
}
