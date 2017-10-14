package guidsl;

import javax.swing.*;

public class optprod extends term {

    optprod(String name) {
        super(name);
        current = this;
        pattern.current.terms.add(this);
    }

    public void visit(GVisitor v) {
        v.action(this);
    }
//draw an optional production

    public JComponent draw(int several) {
        if (var.hidden) {
            return null;
        } else {
            return prod.draw(1);
        }
    }
}
