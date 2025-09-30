
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQL {

    private static final String DELIMITER = ",";
    private final Map<String, Table> namesToTables = new HashMap<>();

    public SQL(List<String> names, List<Integer> columns) {
        for (int i = 0; i < names.size(); ++i) {
            namesToTables.put(names.get(i), new Table(columns.get(i)));
        }
    }

    public boolean ins(String name, List<String> row) {
        if (!namesToTables.containsKey(name) || row.size() != namesToTables.get(name).numberOfColumns) {
            return false;
        }

        int rowID = namesToTables.get(name).rowID;
        namesToTables.get(name).rowsToColumns.put(rowID, row);
        ++namesToTables.get(name).rowID;

        return true;
    }

    public void rmv(String name, int rowID) {
        if (!namesToTables.containsKey(name) || !namesToTables.get(name).rowsToColumns.containsKey(rowID)) {
            return;
        }
        namesToTables.get(name).rowsToColumns.remove(rowID);
    }

    public String sel(String name, int rowID, int columnID) {
        if (!namesToTables.containsKey(name) || !namesToTables.get(name).rowsToColumns.containsKey(rowID)
                || columnID < 1 || columnID > namesToTables.get(name).numberOfColumns) {
            return NOT_FOUND.STRING_FORMAT;
        }
        String value = namesToTables.get(name).rowsToColumns.get(rowID).get(columnID - 1);
        return value;
    }

    public List<String> exp(String name) {
        if (!namesToTables.containsKey(name) || namesToTables.get(name).rowsToColumns.isEmpty()) {
            return NOT_FOUND.LIST_FORMAT;
        }

        List<Integer> sortedRowIDs = new ArrayList<>(namesToTables.get(name).rowsToColumns.keySet());
        Collections.sort(sortedRowIDs);

        List<String> tableFormatCSV = new ArrayList<>();
        for (int rowID : sortedRowIDs) {
            String row = rowID + DELIMITER + String.join(DELIMITER, namesToTables.get(name).rowsToColumns.get(rowID));
            tableFormatCSV.add(row);
        }

        return tableFormatCSV;
    }
}

class Table {

    int rowID = 1;
    int numberOfColumns;
    Map<Integer, List<String>> rowsToColumns = new HashMap<>();

    Table(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }
}

class NOT_FOUND {

    static final List<String> LIST_FORMAT = new ArrayList<>();
    static final String STRING_FORMAT = "<null>";
}
