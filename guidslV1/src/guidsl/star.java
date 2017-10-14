package guidsl;

import javax.swing.*;

public class star extends term {

    star(String name) {
        super(name);
        pattern.current.terms.add(this);
    }

    public void visit(GVisitor v) {
        v.action(this);
    }

    public JComponent draw(int several) {
        if (var.hidden) {
            return null;
        } else {
            return prod.draw(3);
        }
    }
}
