
using System;
using System.Collections.Generic;

public class SQL
{
    private static readonly string DELIMITER = ",";
    private readonly Dictionary<string, Table> namesToTables = [];

    public SQL(IList<string> names, IList<int> columns)
    {
        for (int i = 0; i < names.Count; ++i)
        {
            namesToTables.Add(names[i], new Table(columns[i]));
        }
    }

    public bool Ins(string name, IList<string> row)
    {
        if (!namesToTables.ContainsKey(name) || row.Count != namesToTables[name].numberOfColumns)
        {
            return false;
        }

        int rowID = namesToTables[name].rowID;
        namesToTables[name].rowsToColumns.Add(rowID, row);
        ++namesToTables[name].rowID;

        return true;
    }

    public void Rmv(string name, int rowID)
    {
        if (!namesToTables.ContainsKey(name) || !namesToTables[name].rowsToColumns.ContainsKey(rowID))
        {
            return;
        }
        namesToTables[name].rowsToColumns.Remove(rowID);
    }

    public string Sel(string name, int rowID, int columnID)
    {
        if (!namesToTables.ContainsKey(name) || !namesToTables[name].rowsToColumns.ContainsKey(rowID)
              || columnID < 1 || columnID > namesToTables[name].numberOfColumns)
        {
            return NOT_FOUND.STRING_FORMAT;
        }
        string value = namesToTables[name].rowsToColumns[rowID][columnID - 1];
        return value;
    }

    public IList<string> Exp(string name)
    {
        if (!namesToTables.ContainsKey(name) || namesToTables[name].rowsToColumns.Count == 0)
        {
            return NOT_FOUND.LIST_FORMAT;
        }

        List<int> sortedRowIDs = new List<int>(namesToTables[name].rowsToColumns.Keys);
        sortedRowIDs.Sort();

        IList<string> tableFormatCSV = new List<string>();
        foreach (int rowID in sortedRowIDs)
        {
            string row = rowID + DELIMITER + string.Join(DELIMITER, namesToTables[name].rowsToColumns[rowID]);
            tableFormatCSV.Add(row);
        }

        return tableFormatCSV;
    }
}

class Table
{
    public int rowID = 1;
    public int numberOfColumns;
    public Dictionary<int, IList<string>> rowsToColumns = [];

    public Table(int numberOfColumns)
    {
        this.numberOfColumns = numberOfColumns;
    }
}

class NOT_FOUND
{
    public static readonly List<string> LIST_FORMAT = [];
    public static readonly string STRING_FORMAT = "<null>";
}
