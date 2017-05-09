package opl;

public class Times extends Exp {
    Exp l,r;
    Times(Exp L, Exp R) { l=L; r=R; }
    String print() {
      return "("+l.print() + ")*(" + r.print()+")";
    }
    int eval() {
        return l.eval() * r.eval();
    }
}
