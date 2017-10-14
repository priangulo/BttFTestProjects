package guidsl;

import java.util.Vector;

/**
 * ArgList: encapsulates a list of either Switch or PositionalArg objects.
 *
 * @layer<kernel>
 */
public class ArgList extends Vector {

    // Constants used to constrain find(), first(), and next()
    static public final int NO_LAYER = -1;
    static public final Class NO_CLASS = null;

    // Current filter values
    int layerFilter = NO_LAYER;
    Class classFilter = NO_CLASS;

    // Acts as a cursor for current position.
    int csrIndex;

    private CommandLineArg locate(int start) {
        CommandLineArg arg;

        for (int i = start; i < elementCount; i++) {
            arg = (CommandLineArg) elementData[i];
            if ((layerFilter != NO_LAYER)
                    && (arg.layerID != layerFilter)) {
                continue;
            }
            if ((classFilter != NO_CLASS)
                    && (arg.getClass() != classFilter)) {
                continue;
            }
            csrIndex = i;
            return (arg);
        }
        return (null);
    }

    //**************************************************
    // Return first CommandLineArg with class and layerID possible filter
    // criteria.
    //**************************************************
    public CommandLineArg first() {
        return (locate(0));
    }

    public CommandLineArg first(Class cls) {
        classFilter = cls;
        return (locate(0));
    }

    public CommandLineArg first(int _layer) {
        layerFilter = _layer;
        return (locate(0));
    }

    public CommandLineArg first(Class cls, int _layer) {
        classFilter = cls;
        layerFilter = _layer;
        return (locate(0));
    }

    //**************************************************
    // Return next CommandLineArg with class and layerID possible filter
    // criteria.
    //**************************************************
    public CommandLineArg next() {
        return (locate(csrIndex + 1));
    }

    public CommandLineArg next(Class cls) {
        classFilter = cls;
        return (locate(csrIndex + 1));
    }

    public CommandLineArg next(int _layer) {
        layerFilter = _layer;
        return (locate(csrIndex + 1));
    }

    public CommandLineArg next(Class cls, int _layer) {
        classFilter = cls;
        layerFilter = _layer;
        return (locate(csrIndex + 1));
    }

    //**************************************************
    // Locate an argument by name with class and layerID possible
    // filter criteria.
    //**************************************************
    public CommandLineArg find(String name) {
        CommandLineArg arg;

        for (arg = locate(0); arg != null; arg = locate(csrIndex + 1)) {
            if (name.compareTo(arg.name) == 0) {
                return (arg);
            }
        }
        return (null);
    }

    public CommandLineArg find(String name, Class cls) {
        classFilter = cls;
        return (find(name));
    }

    public CommandLineArg find(String name, int _layer) {
        layerFilter = _layer;
        return (find(name));
    }

    public CommandLineArg find(String name, Class cls, int _layer) {
        classFilter = cls;
        layerFilter = _layer;
        return (find(name));
    }
}
