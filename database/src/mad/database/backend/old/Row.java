package mad.database.backend.old;

import java.util.Arrays;

/**
 *
 */
public class Row {
    class Cell {
        public Cell(DataType type, String value) {
            this.type = type;
            this.value = value;
        }
        
        DataType type;
        String value;
    }
    
    private final Cell[] cells;
    
    public Row(DataType[] definition) {
        cells = new Cell[definition.length];
        for(int i = 0; i < definition.length; i++)
            cells[i] = new Cell(definition[i], null);
    }
    
    public Row(DataType[] definition, String[] values) {
        cells = new Cell[definition.length];
        for(int i = 0; i < definition.length; i++) {
            if(i >= values.length)
                cells[i] = new Cell(definition[i], null);
            else
                cells[i] = new Cell(definition[i], values[i]);
        }
    }
    
    public boolean updateColumn (int idx, String newValue) {
        if(idx < 0 || idx >= getColumnCount())
            return false;
        if(cells[idx].type != DataType.VARCHAR)
            return false;
        
        cells[idx].value = newValue;
        return true;
    }
    
    public boolean updateColumn (int idx, int newValue) {
        if(idx < 0 || idx >= getColumnCount())
            return false;
        if(cells[idx].type != DataType.INTEGER)
            return false;
        
        cells[idx].value = Integer.toString(newValue);
        return true;
    }
    
    public boolean updateColumn (int idx, boolean newValue) {
        if(idx < 0 || idx >= getColumnCount())
            return false;
        if(cells[idx].type != DataType.BOOLEAN)
            return false;
        
        cells[idx].value = newValue ? "1" : "0";
        return true;
    }
    
    public boolean updateColumn (int idx, double newValue) {
        if(idx < 0 || idx >= getColumnCount())
            return false;
        if(cells[idx].type != DataType.DOUBLE)
            return false;
        
        cells[idx].value = Double.toString(newValue);
        return true;
    }
    
    public DataType getColumnDataType(int idx) {
        if(idx < 0 || idx >= getColumnCount())
            return DataType.INVALID_TYPE;
        return cells[idx].type;
    }
    
    private boolean indexOk(int idx, DataType okType) {
        if(idx < 0 || idx >= getColumnCount())
            return false;
        if(cells[idx].value == null)
            return false;
        return cells[idx].type == okType;
    }
    
    public String getValueAsString(int idx) {
        if(!indexOk(idx, DataType.VARCHAR))
            return null;
        return cells[idx].value;
    }
        
    public int getValueAsInteger(int idx) {
        if(!indexOk(idx, DataType.INTEGER))
            return -1;
        return Integer.parseInt(cells[idx].value);
    }
    
    public boolean getValueAsBoolean(int idx) {
        if(!indexOk(idx, DataType.BOOLEAN))
            return false;
        return cells[idx].value.equals("1");
    }
    
    public double getValueAsDouble(int idx) {
        if(!indexOk(idx, DataType.DOUBLE))
            return .0;
        return Double.parseDouble(cells[idx].value);
    }
    
    public int getColumnCount() {
        return cells.length;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Row) {
            Row that = (Row)obj;
            if(this.cells.length != that.cells.length)
                return false;
            for(int i = 0; i < this.cells.length; i++) {
                if(this.cells[i].type != that.cells[i].type || !this.cells[i].value.equals(that.cells[i].value))
                    return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Arrays.deepHashCode(this.cells);
        return hash;
    }
}
