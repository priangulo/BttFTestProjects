package guidsl;

public class Switch extends CommandLineArg implements Cloneable {

    String description;
    String[] args; // names or instance bindings

    public Switch(String _id, String _description, String[] argNames,
            boolean _optional, int _layer) {
        name = _id;
        description = _description;
        args = argNames;
        optional = _optional;
        layerID = _layer;
    }

    public Object clone() throws CloneNotSupportedException {
        return (super.clone());
    }
}
