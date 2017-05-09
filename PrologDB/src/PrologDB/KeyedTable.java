package PrologDB;

import java.util.HashMap;

public class KeyedTable extends Table {

    final public String key;
    private final HashMap<String, Tuple> tuples;

    public KeyedTable(TableSchema schema, String key) {
        super(schema);
        this.key = key;
        tuples = new HashMap<>();
    }

    public Tuple findkey(String value) {
        Tuple t = tuples.get(value);
        if (t != null) {
            return t;
        }
        throw new Error("can't find tuple in relation " + schema.getName() + " where " + key + "=" + value);
    }

    public KeyedTable copy() {
        KeyedTable kt = new KeyedTable(schema, key);
        kt.tuples.putAll(tuples);
        return kt;
    }
}
