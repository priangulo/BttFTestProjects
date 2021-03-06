// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package bali2jak;

public class RegexDefinitionNode extends RegexTokenDefinition {

    final public static int ARG_LENGTH = 4 ;
    final public static int TOK_LENGTH = 3 ;

    public CaseFlag getCaseFlag () {
        
        AstNode node = arg[2].arg [0] ;
        return (node != null) ? (CaseFlag) node : null ;
    }

    public REKind getREKind () {
        
        return (REKind) arg [1] ;
    }

    public REList getREList () {
        
        return (REList) arg [3] ;
    }

    public StateSet getStateSet () {
        
        AstNode node = arg[0].arg [0] ;
        return (node != null) ? (StateSet) node : null ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {false, false, false, true, true, false, true} ;
    }

    public RegexDefinitionNode setParms
    (AstOptNode arg0, REKind arg1, AstOptNode arg2, AstToken tok0, AstToken tok1, REList arg3, AstToken tok2)
    {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        arg [0] = arg0 ;            /* [StateSet] */
        arg [1] = arg1 ;            /* REKind */
        arg [2] = arg2 ;            /* [CaseFlag] */
        tok [0] = tok0 ;            /* ":" */
        tok [1] = tok1 ;            /* "{" */
        arg [3] = arg3 ;            /* REList */
        tok [2] = tok2 ;            /* "}" */
        
        InitChildren () ;
        return (RegexDefinitionNode) this ;
    }

}
