// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package mixin;

import java.util.*;
import Jakarta.util.*;
import java.io.*;

abstract public class UnmodifiedTypeDeclaration extends AstNode {

    @mixin.R4Feature(mixin.R4Feature.mixinbase)


    public void extensionOf( String name ) {
        AstNode.override( "UnmodifiedTypeDeclaration.extensionOf", this );
    }

    public boolean isExtension() {
        // override by Ute
        return false;
    }

    public String getName() {
        AstNode.override( "UnmodifiedTypeDeclaration.getName", this );
        return null;
    }

    // mangle class name, and return it

    public String getAndMangleName() {
        AstNode.override( "UnmodifiedTypeDeclaration.getAndMangleName", this );
        return null; // pacify whiney compiler
    }

    // replaces extension TypeDeclaration with Java counterpart
    // this method is overridden only in the case of extensions

    public  UnmodifiedTypeDeclaration prepareReplace( JTSParseTree t ) {
        // OVERRIDE only in the case of extensions
        return ( UnmodifiedTypeDeclaration ) this;
    }

}
