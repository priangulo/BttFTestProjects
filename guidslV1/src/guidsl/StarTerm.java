// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.
package guidsl;

public class StarTerm extends GTerm {

    final public static int ARG_LENGTH = 1 /* Kludge! */;
    final public static int TOK_LENGTH = 2;

    public AstToken getIDENTIFIER() {

        return (AstToken) tok[0];
    }

    public boolean[] printorder() {

        return new boolean[]{true, true};
    }

    public StarTerm setParms(AstToken tok0, AstToken tok1) {

        arg = new AstNode[ARG_LENGTH];
        tok = new AstTokenInterface[TOK_LENGTH];

        tok[0] = tok0;
        /* IDENTIFIER */
        tok[1] = tok1;
        /* "*" */

        InitChildren();
        return (StarTerm) this;
    }

    public void visit(Visitor v) {

        v.action(this);
    }

}
