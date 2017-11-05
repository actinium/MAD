package mad.database.sql;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class TableTest {
    
    public TableTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testConstructor_ShouldSetInitialColumns() {
        String[] columnNames = {
            "Col_1",
            "Col_2",
            "Col_3",
            "Col_4"
        };
        DataType[] columnTypes = {
            DataType.INTEGER,
            DataType.DOUBLE,
            DataType.BOOLEAN,
            DataType.VARCHAR
        };
        
        Table table = new Table(columnNames, columnTypes);
        
        assertEquals("Col_1", table.getColumnName(0));
        assertEquals("Col_2", table.getColumnName(1));
        assertEquals("Col_3", table.getColumnName(2));
        assertEquals("Col_4", table.getColumnName(3));
        
        assertEquals(DataType.INTEGER, table.getColumnType(0));
        assertEquals(DataType.DOUBLE, table.getColumnType(1));
        assertEquals(DataType.BOOLEAN, table.getColumnType(2));
        assertEquals(DataType.VARCHAR, table.getColumnType(3));
    }
    
    @Test
    public void testConstructor_ShouldStoreCopiesOfInput() {
        String[] columnNames = { "Col_1" };
        DataType[] columnTypes = { DataType.INTEGER };
        
        Table table = new Table(columnNames, columnTypes);
        columnNames[0] = "Upd_1";
        columnTypes[0] = DataType.BOOLEAN;
        
        assertEquals("Col_1", table.getColumnName(0));
        assertEquals(DataType.INTEGER, table.getColumnType(0));
    }
    
    @Test
    public void testConstructor_ShouldOnlyStoreLeastNumberOfNamesOrTypes() {
        String[] columnNames1 = { "Col_1" };
        DataType[] columnTypes1 = { DataType.INTEGER, DataType.VARCHAR };
        
        String[] columnNames2 = { "Col_1", "Col_2" };
        DataType[] columnTypes2 = { DataType.INTEGER };
        
        Table table1 = new Table(columnNames1, columnTypes1);
        Table table2 = new Table(columnNames2, columnTypes2);
        
        assertEquals(1, table1.getColumnCount());
        assertEquals(1, table2.getColumnCount());
    }
    
    @Test
    public void testConstructor_UsingCollections() {
        ArrayList<String> columnNames = new ArrayList<>();
        columnNames.add( "Col_1" );
        columnNames.add( "Col_2" );
        ArrayList<DataType> columnTypes = new ArrayList<>();
        columnTypes.add(DataType.INTEGER);
        columnTypes.add(DataType.VARCHAR);
        
        Table table = new Table(columnNames, columnTypes);
        columnNames.set(0, "Upd_1");
        columnTypes.set(0, DataType.BOOLEAN);
        
        assertEquals(2, table.getColumnCount());
        assertEquals("Col_1", table.getColumnName(0));
        assertEquals("Col_2", table.getColumnName(1));
        assertEquals(DataType.INTEGER, table.getColumnType(0));
        assertEquals(DataType.VARCHAR, table.getColumnType(1));
    }
    
    @Test
    public void testInsertRow() {
        String[] columnNames = { "Col_1", "Col_2" };
        DataType[] columnTypes = { DataType.INTEGER, DataType.VARCHAR };
        Table table = new Table(columnNames, columnTypes);
        
        int result1 = table.insertRow(new Row(new DataType[] { DataType.INTEGER, DataType.VARCHAR }));
        int result2 = table.insertRow(new Row(new DataType[] { DataType.INTEGER, DataType.BOOLEAN }));
        
        assertEquals(1, result1);
        assertEquals(0, result2);
        assertEquals(1, table.getRowCount());
    }
    
    @Test
    public void testInsertRows() {
        String[] columnNames = { "Col_1", "Col_2" };
        DataType[] columnTypes = { DataType.INTEGER, DataType.VARCHAR };
        Table table = new Table(columnNames, columnTypes);
        
        Row[] rows = {
            new Row(new DataType[] { DataType.INTEGER, DataType.VARCHAR }),
            new Row(new DataType[] { DataType.INTEGER, DataType.DOUBLE }),
            new Row(new DataType[] { DataType.INTEGER, DataType.VARCHAR, DataType.VARCHAR }),
            new Row(new DataType[] { DataType.INTEGER, DataType.VARCHAR }),
        };
        int result = table.insertRows(rows);
        
        assertEquals(2, result);
        assertEquals(2, table.getRowCount());
    }
    
    @Test
    public void testInsertRows_UsingCollections() {
        ArrayList<String> columnNames = new ArrayList<>();
        columnNames.add("Col_1");
        columnNames.add("Col_2");
        ArrayList<DataType> columnTypes = new ArrayList<>();
        columnTypes.add(DataType.INTEGER);
        columnTypes.add(DataType.VARCHAR);
        Table table = new Table(columnNames, columnTypes);
        
        ArrayList<Row> rows = new ArrayList<>();
        rows.add(new Row(new DataType[] { DataType.INTEGER, DataType.VARCHAR }));
        rows.add(new Row(new DataType[] { DataType.INTEGER, DataType.DOUBLE }));
        rows.add(new Row(new DataType[] { DataType.INTEGER, DataType.VARCHAR, DataType.VARCHAR }));
        rows.add(new Row(new DataType[] { DataType.INTEGER, DataType.VARCHAR }));
        
        int result = table.insertRows(rows);
        
        assertEquals(2, result);
        assertEquals(2, table.getRowCount());
    }
    
    @Test
    public void testDeleteRowsLike() {
        String[] columnNames = { "Col_1", "Col_2" };
        DataType[] columnTypes = { DataType.INTEGER, DataType.VARCHAR };
        Table table = new Table(columnNames, columnTypes);
        Row[] rows = {
            new Row(new DataType[] { DataType.INTEGER, DataType.VARCHAR }, new String[] { "1", "Val_1" }),
            new Row(new DataType[] { DataType.INTEGER, DataType.VARCHAR }, new String[] { "1", "Val_2" }),
            new Row(new DataType[] { DataType.INTEGER, DataType.VARCHAR }, new String[] { "2", "Val_1" }),
            new Row(new DataType[] { DataType.INTEGER, DataType.VARCHAR }, new String[] { "2", "Val_2" }),
            new Row(new DataType[] { DataType.INTEGER, DataType.VARCHAR }, new String[] { "1", "Val_1" })
        };
        table.insertRows(rows);
        
        int result = table.deleteRowsLike(new Row(new DataType[] { DataType.INTEGER, DataType.VARCHAR }, new String[] { "1", "Val_1" }));
        
        assertEquals(2, result);
        assertEquals(3, table.getRowCount());
    }
}
