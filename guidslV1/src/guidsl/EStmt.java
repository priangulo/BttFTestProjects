// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.
package guidsl;

public class EStmt extends ExprStmt {

    final public static int ARG_LENGTH = 1;
    final public static int TOK_LENGTH = 1;

    public Expr getExpr() {

        return (Expr) arg[0];
    }

    public boolean[] printorder() {

        return new boolean[]{false, true};
    }

    public EStmt setParms(Expr arg0, AstToken tok0) {

        arg = new AstNode[ARG_LENGTH];
        tok = new AstTokenInterface[TOK_LENGTH];

        arg[0] = arg0;
        /* Expr */
        tok[0] = tok0;
        /* ";" */

        InitChildren();
        return (EStmt) this;
    }

    public void visit(Visitor v) {

        v.action(this);
    }

    public node eharvest() {
        Expr e = (Expr) arg[0];
        node n = e.eharvest();
        ESList.CTable.add(n);
        return null;
    }

}
