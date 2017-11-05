package mad.database.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 */
public class Table {
    private String[] _columnNames;
    private DataType[] _dataTypes;
    private ArrayList<Row> _rows;
    
    public Table(String[] columnNames, DataType[] columnTypes){
        createTable(columnNames, columnTypes);
    }
    
    public Table(Collection<String> columnNames, Collection<DataType> columnTypes){
        String[] colNames = columnNames.toArray(new String[0]);
        DataType[] colTypes = columnTypes.toArray(new DataType[0]);
        
        createTable(colNames, colTypes);
    }
    
    /*
     * Should only be called by the constructors
     */
    private void createTable(String[] columnNames, DataType[] columnTypes) {
        if (columnNames.length > columnTypes.length) {
            _columnNames = new String[columnTypes.length];
            System.arraycopy(columnNames, 0, _columnNames, 0, columnTypes.length);
        }
        else {
            _columnNames = columnNames.clone();
        }
        
        _dataTypes = new DataType[_columnNames.length];
        System.arraycopy(columnTypes, 0, _dataTypes, 0, _columnNames.length);
        
        _rows = new ArrayList<>();
    }
    
    private boolean correctRowTypes(Row row) {
        if(row.getColumnCount() != _dataTypes.length)
            return false;
        for(int i = 0; i < _dataTypes.length; i++)
            if(_dataTypes[i] != row.getColumnDataType(i))
                return false;
        return true;
    }
    
    public int insertRow(Row rowToInsert) {
        if(!correctRowTypes(rowToInsert))
            return 0;
        _rows.add(rowToInsert);
        return 1;
    }
    
    public int insertRows(Collection<Row> rows) {
        Row[] rowArray = rows.toArray(new Row[0]);
        return insertRows(rowArray);
    }
    
    public int insertRows(Row[] rows) {
        int inserted = 0;
        for(Row row : rows)
            inserted += insertRow(row);
        return inserted;
    }
    
    public int deleteRow(int idx) {
        _rows.remove(idx);
        return 1;
    }
    
    public int deleteRowsLike(Row rowToDelete) {
        HashSet<Row> deleteList = new HashSet<>();
        for(Row row : _rows) {
            if(rowToDelete.equals(row))
                deleteList.add(row);
        }
        _rows.removeAll(deleteList);
        return deleteList.size();
    }
    
    public int getColumnCount() {
        return _columnNames.length;
    }
    
    public int getRowCount() {
        return _rows.size();
    }
    
    public String getColumnName(int idx) {
        if(idx < _columnNames.length)
            return _columnNames[idx];
        return null;
    }
    
    public DataType getColumnType(int idx) {
        if(idx < _dataTypes.length)
            return _dataTypes[idx];
        return null;
    }
    
    public Row getRow(int idx) {
        if(idx >= 0 && idx < _rows.size())
            return _rows.get(idx);
        return null;
    }
    
    public Collection<Row> getRows() {
        return _rows;
    }
}
