// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.
package guidsl;

public class VarDef extends ExprStmt {

    final public static int ARG_LENGTH = 1;
    final public static int TOK_LENGTH = 4;

    public Expr getExpr() {

        return (Expr) arg[0];
    }

    public AstToken getIDENTIFIER() {

        return (AstToken) tok[1];
    }

    public boolean[] printorder() {

        return new boolean[]{true, true, true, false, true};
    }

    public VarDef setParms(AstToken tok0, AstToken tok1, AstToken tok2, Expr arg0, AstToken tok3) {

        arg = new AstNode[ARG_LENGTH];
        tok = new AstTokenInterface[TOK_LENGTH];

        tok[0] = tok0;
        /* "let" */
        tok[1] = tok1;
        /* IDENTIFIER */
        tok[2] = tok2;
        /* "iff" */
        arg[0] = arg0;
        /* Expr */
        tok[3] = tok3;
        /* ";" */

        InitChildren();
        return (VarDef) this;
    }

    public node eharvest() {
        // Step 1: define variable first

        String name = tok[1].getTokenName();
        variable.define(name, variable.Prim, null, false);

        // Step 2: now define expression
        Expr e = (Expr) arg[0];
        node n = e.eharvest();
        ESList.CTable.add(new iff(new bterm(name), n));
        return null;
    }

}
