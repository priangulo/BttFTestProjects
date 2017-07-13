// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package bali2jak;

public class OptionalNode extends Primitive {

    final public static int ARG_LENGTH = 2 ;
    final public static int TOK_LENGTH = 2 ;

    public Lookahead getLookahead () {
        
        AstNode node = arg[0].arg [0] ;
        return (node != null) ? (Lookahead) node : null ;
    }

    public Terminal getTerminal () {
        
        return (Terminal) arg [1] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {true, false, false, true} ;
    }

    public OptionalNode setParms
    (AstToken tok0, AstOptNode arg0, Terminal arg1, AstToken tok1) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        tok [0] = tok0 ;            /* "[" */
        arg [0] = arg0 ;            /* [Lookahead] */
        arg [1] = arg1 ;            /* Terminal */
        tok [1] = tok1 ;            /* "]" */
        
        InitChildren () ;
        return (OptionalNode) this ;
    }

}