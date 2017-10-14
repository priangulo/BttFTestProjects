package guidsl;

public class PositionalArg extends CommandLineArg {

    String binding;

    public PositionalArg(String _name, int _layer) {
        name = _name;
        layerID = _layer;
        optional = false;
    }
}
