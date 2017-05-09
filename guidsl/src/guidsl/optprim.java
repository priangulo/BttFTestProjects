package guidsl;import javax.swing.*;public class optprim extends term {

    optprim( String name ) {
      super( name );
        current = this;
        pattern.current.terms.add( this );
    }

    public void visit( GVisitor v ) {
        v.action( this );
    }
    //draw an optional primitive
    public JComponent draw (int several) {
        if (var.hidden)
           return null;
        else{
           JCheckBox t = new JCheckBox(var.disp);
           var.userVisible = true;  // *dsb*
           t.setToolTipText("Optional");
           return t;
           }
    }
}
