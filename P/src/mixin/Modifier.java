// Automatically generated code.  Edit at your own risk!
// Generated by bali2jak v2002.09.03.

package mixin;

import Jakarta.util.FixDosOutputStream;
import Jakarta.util.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashSet;

abstract public class Modifier extends AstNode {

    /** return name of modifier 
     * @layer<Java>
     */

    public String GetName() {
        // generic method, can be overridden
        return tok[0].tokenName();
    }

}