// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package mixin;

public class MDecl extends InterfaceMemberDeclaration {

    final public static int ARG_LENGTH = 1 ;
    final public static int TOK_LENGTH = 1 /* Kludge! */ ;

    public MethodDeclaration getMethodDeclaration () {
        
        return (MethodDeclaration) arg [0] ;
    }

    public boolean[] printorder () {
        
        return new boolean[] {false} ;
    }

    public MDecl setParms (MethodDeclaration arg0) {
        
        arg = new AstNode [ARG_LENGTH] ;
        tok = new AstTokenInterface [TOK_LENGTH] ;
        
        arg [0] = arg0 ;            /* MethodDeclaration */
        
        InitChildren () ;
        return (MDecl) this ;
    }

}
