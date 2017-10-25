

package Application;

/// MsP:  untoggle the commmented import below and comment D.Gui to switch
// between implementations

//import BI.Gui;
import D.Gui;

public class Main {
    public Gui g;
    
    public Main() {
        g = new Gui("Calc");
        g.display();
    }
    
    public static void main(String args[]) {
        Main m = new Main();
        m.g=new Gui("Calc");
        m.g.display();
    }
}

