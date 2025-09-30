
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SQL {

    class NOT_FOUND {

        static final List<String> LIST_FORMAT = new ArrayList<>();
        static final String STRING_FORMAT = "<null>";
    }

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
        namesToTables.get(name).rowsToComuns.put(rowID, row);
        ++namesToTables.get(name).rowID;

        return true;
    }

    public void rmv(String name, int rowID) {
        if (!namesToTables.containsKey(name) || !namesToTables.get(name).rowsToComuns.containsKey(rowID)) {
            return;
        }
        namesToTables.get(name).rowsToComuns.remove(rowID);
    }

    public String sel(String name, int rowID, int columnID) {
        if (!namesToTables.containsKey(name) || !namesToTables.get(name).rowsToComuns.containsKey(rowID)
                || columnID < 1 || columnID > namesToTables.get(name).numberOfColumns) {
            return NOT_FOUND.STRING_FORMAT;
        }
        String value = namesToTables.get(name).rowsToComuns.get(rowID).get(columnID - 1);
        return value;
    }

    public List<String> exp(String name) {
        if (!namesToTables.containsKey(name) || namesToTables.get(name).rowsToComuns.isEmpty()) {
            return NOT_FOUND.LIST_FORMAT;
        }

        List<String> tableFormatCSV = new ArrayList<>();
        for (int rowID : namesToTables.get(name).rowsToComuns.keySet()) {
            String row = rowID + DELIMITER + String.join(DELIMITER, namesToTables.get(name).rowsToComuns.get(rowID));
            tableFormatCSV.add(row);
        }
        return tableFormatCSV;
    }
}

class Table {

    int rowID = 1;
    int numberOfColumns;
    TreeMap<Integer, List<String>> rowsToComuns = new TreeMap<>();

    Table(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }
}
