package mad.database.backend;

import java.io.File;
import java.io.IOException;
import static junit.framework.Assert.assertEquals;
import mad.database.backend.table.ArrayRow;
import mad.database.backend.table.Row;
import mad.database.backend.table.Schema;
import mad.database.backend.table.SelectionRow;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class SelectionRowTest {

    public SelectionRowTest() {
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

    /**
     *
     * @throws java.io.IOException
     * @throws Row.TypeMismatchException
     */
    @Test
    public void testSelectionRow() throws IOException, Row.TypeMismatchException, Row.NoSuchColumnException {
        File testFile = File.createTempFile("madtest-", Long.toString(System.nanoTime()));
        testFile.deleteOnExit();
        try (DB db = DB.open(testFile.getAbsolutePath())) {
            db.createTable("Employee", new Schema(
                    new Schema.Field("id", Schema.Field.Type.Integer),
                    new Schema.Field("name", Schema.Field.Type.Varchar, 100),
                    new Schema.Field("department_id", Schema.Field.Type.Integer),
                    new Schema.Field("salary", Schema.Field.Type.Integer)));
            db.createTable("Department", new Schema(
                    new Schema.Field("id", Schema.Field.Type.Integer),
                    new Schema.Field("name", Schema.Field.Type.Varchar, 45)));
        }
        try (DB db = DB.open(testFile.getAbsolutePath())) {
            Row row = db.getFirstRow(db.getTablePointer("Employee"));
            assertNull(row);
        }
        try (DB db = DB.open(testFile.getAbsolutePath())) {
            Row row = new ArrayRow()
                    .addIntegerColumn("id", 0)
                    .addStringColumn("name", "Gary")
                    .addIntegerColumn("department_id", 1)
                    .addIntegerColumn("salary", 50000);
            db.insertRow("Employee", row);
            Row row2 = new ArrayRow()
                    .addIntegerColumn("id", 1)
                    .addStringColumn("name", "Alice")
                    .addIntegerColumn("department_id", 0)
                    .addIntegerColumn("salary", 35000);
            db.insertRow("Employee", row2);
            Row row3 = new ArrayRow()
                    .addIntegerColumn("id", 2)
                    .addStringColumn("name", "Bob")
                    .addIntegerColumn("department_id", 1)
                    .addIntegerColumn("salary", 36000);
            db.insertRow("Employee", row3);
            Row row4 = new ArrayRow()
                    .addIntegerColumn("id", 3)
                    .addStringColumn("name", "Charlie")
                    .addIntegerColumn("department_id", 0)
                    .addIntegerColumn("salary", 32000);
            db.insertRow("Employee", row4);
            Row row5 = new ArrayRow()
                    .addIntegerColumn("id", 4)
                    .addStringColumn("name", "Lara")
                    .addIntegerColumn("department_id", 0)
                    .addIntegerColumn("salary", 37000);
            db.insertRow("Employee", row5);
            Row row6 = new ArrayRow()
                    .addIntegerColumn("id", 5)
                    .addStringColumn("name", "Paul")
                    .addIntegerColumn("department_id", 1)
                    .addIntegerColumn("salary", 25000);
            db.insertRow("Employee", row6);
        }
        try (DB db = DB.open(testFile.getAbsolutePath())) {
            Row row = new ArrayRow()
                    .addIntegerColumn("id", 0)
                    .addStringColumn("name", "RnD");
            db.insertRow("Department", row);
            Row row2 = new ArrayRow()
                    .addIntegerColumn("id", 1)
                    .addStringColumn("name", "Finance");
            db.insertRow("Department", row2);

        }
        try (DB db = DB.open(testFile.getAbsolutePath())) {
            Row row = db.getFirstRow(db.getTablePointer("Department"));
            assertNotNull(row);
            assertEquals(0, row.getInteger("id"));
            assertEquals("RnD", row.getString("name"));
            assertEquals("Department", row.getTableName());
            assertTrue(row.hasNext());

            Row row2 = row.next();
            assertNotNull(row2);
            assertEquals(1, row2.getInteger("id"));
            assertEquals("Finance", row2.getString("name"));
            assertEquals("Department", row2.getTableName());
            assertFalse(row2.hasNext());

            Row row3 = row2.next();
            assertNull(row3);
        }
        try (DB db = DB.open(testFile.getAbsolutePath())) {
            Row initialRow = db.getFirstRow(db.getTablePointer("Employee"));
            Row row = SelectionRow.getFirstMatchingRow(initialRow, r -> {
                try {
                    // Select only rows where department_id == 0.
                    return r.getInteger("department_id") == 0;
                } catch (Row.NoSuchColumnException | Row.TypeMismatchException | IOException ex) {
                    return false;
                }
            });

            assertNotNull(row);
            assertEquals(1, row.getInteger("id"));
            assertEquals("Alice", row.getString("name"));
            assertEquals(0, row.getInteger("department_id"));
            assertEquals(35000, row.getInteger("salary"));
            assertEquals("Employee", row.getTableName());
            assertTrue(row.hasNext());

            Row row2 = row.next();
            assertNotNull(row2);
            assertEquals(3, row2.getInteger("id"));
            assertEquals("Charlie", row2.getString("name"));
            assertEquals(0, row2.getInteger("department_id"));
            assertEquals(32000, row2.getInteger("salary"));
            assertEquals("Employee", row2.getTableName());
            assertTrue(row2.hasNext());

            Row row3 = row2.next();
            assertNotNull(row3);
            assertEquals(4, row3.getInteger("id"));
            assertEquals("Lara", row3.getString("name"));
            assertEquals(0, row3.getInteger("department_id"));
            assertEquals(37000, row3.getInteger("salary"));
            assertEquals("Employee", row3.getTableName());
            assertFalse(row3.hasNext());
            Row row4 = row3.next();
            assertNull(row4);
        }
    }
}
