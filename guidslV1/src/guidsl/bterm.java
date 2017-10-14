package guidsl;

import Jakarta.util.*;
import java.util.*;

public class bterm extends node {

    public String name;

    public bterm(String n) {
        name = n;
    }

    public node klone() {
        return new bterm(name);
    }

    public node simplify() {
        return this;
    }

    public String toString() {
        return name;
    }

    public String cnf2String() {
        return name;
    }

    public node cnf() {
        return this;
    }

    public void reduce(cterm t) {
        variable v = variable.find(name);
        if (v == null) {
            Util.error("variable " + name + " undeclared");
        }
        t.setVar(v);
    }

    public void reduce(ArrayList terms) {
        cnfClause c = new cnfClause();
        reduce(c);
        terms.add(c);
    }

    public void reduce(cnfClause c) {
        cterm t = new cterm(false);
        reduce(t);
        c.add(t);
    }

    // print number of variable
    public void toCnfFormat(cnfout out) throws CNFException {
        try {
            out.print(variable.findNumber(name) + " ");
        } catch (Exception e) {
            throw new CNFException(e.getMessage());
        }
    }

    public String toXMLString() {
        return "<term>" + name + "</term>";
    }
}
