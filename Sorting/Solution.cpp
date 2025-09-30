
#include <span>
#include <vector>
#include <ranges>
#include <string>
#include <unordered_map>
using namespace std;

class SQL {

    struct Table {

        int rowID = 1;
        int numberOfColumns = 0;
        unordered_map<int, vector<string>>rowsToColumns;

        Table() = default;
        Table(int numberOfColumns) : numberOfColumns{ numberOfColumns } {}
    };

    struct NOT_FOUND {

        inline static const vector<string> LIST_FORMAT = vector<string>();
        inline static const string STRING_FORMAT = "<null>";
    };

    inline static const string DELIMITER = ",";
    unordered_map<string, Table> namesToTables;

public:
    SQL(vector<string>& names, vector<int>& columns) {
        for (int i = 0; i < names.size(); ++i) {
            namesToTables[names[i]] = Table(columns[i]);
        }
    }

    bool ins(string name, vector<string> row) {
        if (!namesToTables.contains(name) || row.size() != namesToTables[name].numberOfColumns) {
            return false;
        }

        int rowID = namesToTables[name].rowID;
        namesToTables[name].rowsToColumns[rowID] = row;
        ++namesToTables[name].rowID;

        return true;
    }

    void rmv(const string& name, int rowID) {
        if (!namesToTables.contains(name) || !namesToTables[name].rowsToColumns.contains(rowID)) {
            return;
        }
        namesToTables[name].rowsToColumns.erase(rowID);
    }

    string sel(const string& name, int rowID, int columnID) {
        if (!namesToTables.contains(name) || !namesToTables[name].rowsToColumns.contains(rowID)
            || columnID < 1 || columnID > namesToTables[name].numberOfColumns) {
            return NOT_FOUND::STRING_FORMAT;
        }
        string value = namesToTables[name].rowsToColumns[rowID][columnID - 1];
        return value;
    }

    vector<string> exp(const string& name) const {
        if (!namesToTables.contains(name) || namesToTables.at(name).rowsToColumns.empty()) {
            return NOT_FOUND::LIST_FORMAT;
        }


        vector<int> sortedRowIDs;
        for (const auto& [rowID, _] : namesToTables.at(name).rowsToColumns) {
            sortedRowIDs.push_back(rowID);
        }
        ranges::sort(sortedRowIDs);

        vector<string> tableFormatCSV;
        for (const auto& rowID : sortedRowIDs) {
            string row = to_string(rowID) + DELIMITER + vectorToString(namesToTables.at(name).rowsToColumns.at(rowID));
            tableFormatCSV.push_back(row);
        }

        return tableFormatCSV;
    }

    string vectorToString(span<const string> columns) const {
        string joinedColumns;
        joinedColumns.append(columns[0]);

        for (int i = 1; i < columns.size(); ++i) {
            joinedColumns.append(DELIMITER).append(columns[i]);
        }
        return joinedColumns;
    }
};
