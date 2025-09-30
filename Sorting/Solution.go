
package main

import (
    "slices"
    "strconv"
    "strings"
)

var NOT_FOUND_LIST_FORMAT = []string{}

const NOT_FOUND_STRING_FORMAT = "<null>"
const DELIMITER = ","

type SQL struct {
    namesToTables map[string]*Table
}

func Constructor(names []string, columns []int) SQL {
    sql := SQL{namesToTables: map[string]*Table{}}
    for i := range names {
        sql.namesToTables[names[i]] = NewTable(columns[i])
    }
    return sql
}

func (this *SQL) Ins(name string, row []string) bool {
    if _, has := this.namesToTables[name]; !has || len(row) != this.namesToTables[name].numberOfColumns {
        return false
    }

    rowID := this.namesToTables[name].rowID
    this.namesToTables[name].rowsToColumns[rowID] = &row
    this.namesToTables[name].rowID++

    return true
}

func (this *SQL) Rmv(name string, rowID int) {
    if _, has := this.namesToTables[name]; !has {
        return
    }
    if _, has := this.namesToTables[name].rowsToColumns[rowID]; !has {
        return
    }
    delete(this.namesToTables[name].rowsToColumns, rowID)
}

func (this *SQL) Sel(name string, rowID int, columnID int) string {
    if _, has := this.namesToTables[name]; !has {
        return NOT_FOUND_STRING_FORMAT
    }
    if _, has := this.namesToTables[name].rowsToColumns[rowID]; !has ||
        columnID < 1 ||
        columnID > this.namesToTables[name].numberOfColumns {
        return NOT_FOUND_STRING_FORMAT
    }

    value := (*this.namesToTables[name].rowsToColumns[rowID])[columnID-1]
    return value
}

func (this *SQL) Exp(name string) []string {
    if _, has := this.namesToTables[name]; !has || len(this.namesToTables[name].rowsToColumns) == 0 {
        return NOT_FOUND_LIST_FORMAT
    }

    index := 0
    sortedRowIDs := make([]int, len(this.namesToTables[name].rowsToColumns))
    for rowID := range this.namesToTables[name].rowsToColumns {
        sortedRowIDs[index] = rowID
        index++
    }
    slices.Sort(sortedRowIDs)

    index = 0
    tableFormatCSV := make([]string, len(this.namesToTables[name].rowsToColumns))
    for _, rowID := range sortedRowIDs {

        row := strconv.Itoa(rowID) + DELIMITER + strings.Join(*this.namesToTables[name].rowsToColumns[rowID], DELIMITER)
        tableFormatCSV[index] = row
        index++
    }

    return tableFormatCSV
}

type Table struct {
    rowID           int
    numberOfColumns int
    rowsToColumns   map[int]*[]string
}

func NewTable(numberOfColumns int) *Table {
    table := &Table{
        rowID:           1,
        numberOfColumns: numberOfColumns,
        rowsToColumns:   map[int]*[]string{},
    }
    return table
}
