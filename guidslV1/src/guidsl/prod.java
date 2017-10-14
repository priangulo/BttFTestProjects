package guidsl;

import javax.swing.*;

public class prod extends term {

    prod(String name) {
        super(name);
        current = this;
        pattern.current.terms.add(this);
    }

    public void visit(GVisitor v) {
        v.action(this);
    }

    public JComponent draw(int several) {
        if (var.hidden) {
            return null;
        } else {
            return prod.draw(several);
        }
    }
}
