
class SQL {

    static DELIMITER = ",";
    namesToTables: Map<string, Table>;

    constructor(names: string[], columns: number[]) {
        this.namesToTables = new Map<string, Table>();
        for (let i = 0; i < names.length; ++i) {
            this.namesToTables.set(names[i], new Table(columns[i]));
        }
    }

    ins(name: string, row: string[]): boolean {
        if (!this.namesToTables.has(name) || row.length !== this.namesToTables.get(name).numberOfColumns) {
            return false;
        }

        const rowID = this.namesToTables.get(name).rowID;
        this.namesToTables.get(name).rowsToColumns.set(rowID, row);
        ++this.namesToTables.get(name).rowID;

        return true;
    }

    rmv(name: string, rowID: number): void {
        if (!this.namesToTables.has(name) || !this.namesToTables.get(name).rowsToColumns.has(rowID)) {
            return;
        }
        this.namesToTables.get(name).rowsToColumns.delete(rowID);
    }

    sel(name: string, rowID: number, columnID: number): string {
        if (!this.namesToTables.has(name) || !this.namesToTables.get(name).rowsToColumns.has(rowID)
            || columnID < 1 || columnID > this.namesToTables.get(name).numberOfColumns) {
            return NOT_FOUND.STRING_FORMAT;
        }
        const value = this.namesToTables.get(name).rowsToColumns.get(rowID)[columnID - 1];
        return value;
    }

    exp(name: string): string[] {
        if (!this.namesToTables.has(name) || this.namesToTables.get(name).rowsToColumns.size === 0) {
            return NOT_FOUND.LIST_FORMAT;
        }

        const sortedRowIDs = [...this.namesToTables.get(name).rowsToColumns.keys()];
        sortedRowIDs.sort((x, y) => x - y);

        const tableFormatCSV = new Array();
        for (let rowID of sortedRowIDs) {
            const row = rowID + SQL.DELIMITER + this.namesToTables.get(name).rowsToColumns.get(rowID).join(SQL.DELIMITER);
            tableFormatCSV.push(row);
        }

        return tableFormatCSV;
    }
}

class Table {

    rowID: number;
    numberOfColumns: number;
    rowsToColumns: Map<number, string[]>

    constructor(numberOfColumns: number) {
        this.rowID = 1;
        this.numberOfColumns = numberOfColumns;
        this.rowsToColumns = new Map<number, string[]>();
    }
}

class NOT_FOUND {

    static LIST_FORMAT = new Array();
    static STRING_FORMAT = "<null>";
}
