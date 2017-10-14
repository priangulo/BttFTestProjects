package guidsl;
import javax.swing.*;
import Jakarta.util.*;// this class is used as a way to classify
// all gspec classes as subclasses of a single class

public abstract class gObj {
    public String name;
    public variable var;
    public gObj( String name ) {
        this.name = name;
    }
    public JComponent draw (int several) {
       Util.fatalError( "gObj.draw should never be called");
       return null;
    }

   JComponent setWidget( JComponent w ) {
      variable v = (variable) variable.Vtable.get(name);
      if (v==null)
         Util.fatalError("term " + name + " undefined");
      v.widget = w;
      return w;
   }
}
