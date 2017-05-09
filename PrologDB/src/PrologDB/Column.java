package PrologDB;

public class Column { // read only

    /**
     * name of column and does the column contain single-quoted values?
     */
    private final String name;
    private final boolean isQuoted;

    /**
     * create a Column with name and does the column contain single-quoted
     * values
     *
     * @param name of column
     * @param isQuoted is its values single-quoted?
     */
    public Column(String name, boolean isQuoted) {
        this.name = name;
        this.isQuoted = isQuoted;
    }

    /**
     * @return name of column
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return true if column values are single-quoted
     */
    public boolean isQuoted() {
        return isQuoted;
    }

    /**
     *
     * @return the schema spec of this column -- its name is or is not in double
     * quotes
     */
    @Override
    public String toString() {
        String q = isQuoted ? "\"" : "";
        return q + name + q;
    }

    /**
     * asserts that 'this' column must equal the given otherColumn if unequal,
     * an error is thrown
     *
     * @param otherColumn that should be equal to 'this' column
     */
    public void mustEqual(Column otherColumn) {
        if (otherColumn == null) {
            throw new Error("column is empty");
        }
        String tempName = otherColumn.getName();
        if (!name.equals(tempName)) {
            throw new Error("column names (" + name + "," + tempName + ") do not match");
        }
        if (isQuoted != otherColumn.isQuoted()) {
            throw new Error("quotations in column names (" + name + "," + tempName + ") do not match");
        }
    }
}
