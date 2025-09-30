
class SQL {

    static DELIMITER = ",";

    /**
     * @param {string[]} names
     * @param {number[]} columns
     */
    constructor(names, columns) {
        // Map<string, Table>
        this.namesToTables = new Map();
        for (let i = 0; i < names.length; ++i) {
            this.namesToTables.set(names[i], new Table(columns[i]));
        }
    }

    /** 
     * @param {string} name
     * @param {string[]} row
     * @return {boolean}
     */
    ins(name, row) {
        if (!this.namesToTables.has(name) || row.length !== this.namesToTables.get(name).numberOfColumns) {
            return false;
        }

        const rowID = this.namesToTables.get(name).rowID;
        this.namesToTables.get(name).rowsToColumns.set(rowID, row);
        ++this.namesToTables.get(name).rowID;

        return true;
    }

    /** 
     * @param {string} name 
     * @param {number} rowID
     * @return {void}
     */
    rmv(name, rowID) {
        if (!this.namesToTables.has(name) || !this.namesToTables.get(name).rowsToColumns.has(rowID)) {
            return;
        }
        this.namesToTables.get(name).rowsToColumns.delete(rowID);
    }

    /** 
     * @param {string} name
     * @param {number} rowID
     * @param {number} columnID
     * @return {string}
     */
    sel(name, rowID, columnID) {
        if (!this.namesToTables.has(name) || !this.namesToTables.get(name).rowsToColumns.has(rowID)
                || columnID < 1 || columnID > this.namesToTables.get(name).numberOfColumns) {
            return NOT_FOUND.STRING_FORMAT;
        }
        const value = this.namesToTables.get(name).rowsToColumns.get(rowID)[columnID - 1];
        return value;
    }

    /** 
     * @param {string} name
     * @return {string[]}
     */
    exp(name) {
        if (!this.namesToTables.has(name) || this.namesToTables.get(name).rowsToColumns.size === 0) {
            return NOT_FOUND.LIST_FORMAT;
        }

        const sortedRowIDs = [...this.namesToTables.get(name).rowsToColumns.keys()];
        sortedRowIDs.sort((x, y) => x - y);

        const tableFormatCSV = new Array();
        for (let rowID of sortedRowIDs) {
            const row = rowID + DELIMITER + this.namesToTables.get(name).rowsToColumns.get(rowID).join(SQL.DELIMITER);
            tableFormatCSV.push(row);
        }

        return tableFormatCSV;
    }
}

class Table {

    /**
     * @param {number} numberOfColumns
     */
    constructor(numberOfColumns) {
        this.rowID = 1;
        this.numberOfColumns = numberOfColumns;
        // Map<number, string[]> 
        this.rowsToColumns = new Map();
    }
}

class NOT_FOUND {

    static LIST_FORMAT = new Array();
    static STRING_FORMAT = "<null>";
}
