
class SQL(names: List<String>, columns: List<Int>) {

    private companion object {
        const val DELIMITER = ","
    }

    private val namesToTables = mutableMapOf<String, Table>()

    init {
        for (i in names.indices) {
            namesToTables[names[i]] = Table(columns[i])
        }
    }

    fun ins(name: String, row: List<String>): Boolean {
        if (!namesToTables.containsKey(name) || row.size != namesToTables[name]!!.numberOfColumns) {
            return false
        }

        val rowID = namesToTables[name]!!.rowID
        namesToTables[name]!!.rowsToColumns[rowID] = row
        ++namesToTables[name]!!.rowID

        return true
    }

    fun rmv(name: String, rowID: Int) {
        if (!namesToTables.containsKey(name) || !namesToTables[name]!!.rowsToColumns.containsKey(rowID)) {
            return
        }
        namesToTables[name]!!.rowsToColumns.remove(rowID)
    }

    fun sel(name: String, rowID: Int, columnID: Int): String {
        if (!namesToTables.containsKey(name) || !namesToTables[name]!!.rowsToColumns.containsKey(rowID)
            || columnID < 1 || columnID > namesToTables[name]!!.numberOfColumns) {
            return NOT_FOUND.STRING_FORMAT
        }
        val value = namesToTables[name]!!.rowsToColumns[rowID]!![columnID - 1]
        return value
    }

    fun exp(name: String): List<String> {
        if (!namesToTables.containsKey(name) || namesToTables[name]!!.rowsToColumns.isEmpty()) {
            return NOT_FOUND.LIST_FORMAT
        }

        val sortedRowIDs = ArrayList<Int>(namesToTables[name]!!.rowsToColumns.keys)
        sortedRowIDs.sort()

        val tableFormatCSV = mutableListOf<String>()
        for (rowID in sortedRowIDs) {
            val row = rowID.toString() + DELIMITER + namesToTables[name]!!.rowsToColumns[rowID]!!.joinToString(DELIMITER)
            tableFormatCSV.add(row)
        }

        return tableFormatCSV
    }
}

class Table(val numberOfColumns: Int) {

    var rowID = 1
    val rowsToColumns = mutableMapOf<Int, List<String>>()
}

class NOT_FOUND {

    companion object {
        val LIST_FORMAT = mutableListOf<String>()
        const val STRING_FORMAT = "<null>"
    }
}
