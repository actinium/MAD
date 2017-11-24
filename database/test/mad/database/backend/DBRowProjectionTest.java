package mad.database.backend;

import java.io.File;
import java.io.IOException;
import static junit.framework.Assert.assertEquals;
import mad.database.backend.table.ArrayRow;
import mad.database.backend.table.ProjectionRow;
import mad.database.backend.table.Row;
import mad.database.backend.table.Schema;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class DBRowProjectionTest {

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
    public void testProjectionRow() throws IOException, Row.TypeMismatchException, Row.NoSuchColumnException {
        File testFile = File.createTempFile("madtest-", Long.toString(System.nanoTime()));
        testFile.deleteOnExit();
        try (DB db = DB.open(testFile.getAbsolutePath())) {
            db.createTable("Employee", new Schema(
                    new Schema.Field("id", Schema.Field.Type.Integer),
                    new Schema.Field("name", Schema.Field.Type.Varchar, 100),
                    new Schema.Field("department_id", Schema.Field.Type.Integer),
                    new Schema.Field("salary", Schema.Field.Type.Integer)));
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
            Row initialRow = db.getFirstRow(db.getTablePointer("Employee"));
            int[] projection = {1, 0, 1, 3, 2};
            Row row = ProjectionRow.getFirstProjectionRow(initialRow, projection);

            // Gary
            assertNotNull(row);
            assertEquals("Employee", row.getTableName());
            assertEquals("name", row.getName(0));
            assertEquals("id", row.getName(1));
            assertEquals("name", row.getName(2));
            assertEquals("salary", row.getName(3));
            assertEquals("department_id", row.getName(4));

            assertEquals(0, row.getInteger("id"));
            assertEquals("Gary", row.getString("name"));
            assertEquals(1, row.getInteger("department_id"));
            assertEquals(50000, row.getInteger("salary"));

            assertEquals("Gary", row.getString(0));
            assertEquals(0, row.getInteger(1));
            assertEquals("Gary", row.getString(2));
            assertEquals(50000, row.getInteger(3));
            assertEquals(1, row.getInteger(4));
            assertTrue(row.hasNext());

            // Alice
            Row row2 = row.next();
            assertNotNull(row2);
            assertEquals("Employee", row2.getTableName());
            assertEquals("name", row2.getName(0));
            assertEquals("id", row2.getName(1));
            assertEquals("name", row2.getName(2));
            assertEquals("salary", row2.getName(3));
            assertEquals("department_id", row2.getName(4));

            assertEquals(1, row2.getInteger("id"));
            assertEquals("Alice", row2.getString("name"));
            assertEquals(0, row2.getInteger("department_id"));
            assertEquals(35000, row2.getInteger("salary"));

            assertEquals("Alice", row2.getString(0));
            assertEquals(1, row2.getInteger(1));
            assertEquals("Alice", row2.getString(2));
            assertEquals(35000, row2.getInteger(3));
            assertEquals(0, row2.getInteger(4));
            assertTrue(row2.hasNext());

            // Bob
            Row row3 = row2.next();
            assertNotNull(row3);
            assertEquals("Employee", row3.getTableName());
            assertEquals("name", row3.getName(0));
            assertEquals("id", row3.getName(1));
            assertEquals("name", row3.getName(2));
            assertEquals("salary", row3.getName(3));
            assertEquals("department_id", row3.getName(4));

            assertEquals(2, row3.getInteger("id"));
            assertEquals("Bob", row3.getString("name"));
            assertEquals(1, row3.getInteger("department_id"));
            assertEquals(36000, row3.getInteger("salary"));

            assertEquals("Bob", row3.getString(0));
            assertEquals(2, row3.getInteger(1));
            assertEquals("Bob", row3.getString(2));
            assertEquals(36000, row3.getInteger(3));
            assertEquals(1, row3.getInteger(4));
            assertTrue(row3.hasNext());

            // Charlie
            Row row4 = row3.next();
            assertNotNull(row4);
            assertEquals("Employee", row4.getTableName());
            assertEquals("name", row4.getName(0));
            assertEquals("id", row4.getName(1));
            assertEquals("name", row4.getName(2));
            assertEquals("salary", row4.getName(3));
            assertEquals("department_id", row4.getName(4));

            assertEquals(3, row4.getInteger("id"));
            assertEquals("Charlie", row4.getString("name"));
            assertEquals(0, row4.getInteger("department_id"));
            assertEquals(32000, row4.getInteger("salary"));

            assertEquals("Charlie", row4.getString(0));
            assertEquals(3, row4.getInteger(1));
            assertEquals("Charlie", row4.getString(2));
            assertEquals(32000, row4.getInteger(3));
            assertEquals(0, row4.getInteger(4));
            assertTrue(row4.hasNext());

            // Lara
            Row row5 = row4.next();
            assertNotNull(row5);
            assertEquals("Employee", row5.getTableName());
            assertEquals("name", row5.getName(0));
            assertEquals("id", row5.getName(1));
            assertEquals("name", row5.getName(2));
            assertEquals("salary", row5.getName(3));
            assertEquals("department_id", row5.getName(4));

            assertEquals(4, row5.getInteger("id"));
            assertEquals("Lara", row5.getString("name"));
            assertEquals(0, row5.getInteger("department_id"));
            assertEquals(37000, row5.getInteger("salary"));

            assertEquals("Lara", row5.getString(0));
            assertEquals(4, row5.getInteger(1));
            assertEquals("Lara", row5.getString(2));
            assertEquals(37000, row5.getInteger(3));
            assertEquals(0, row5.getInteger(4));
            assertTrue(row5.hasNext());

            // Paul
            Row row6 = row5.next();
            assertNotNull(row6);
            assertEquals("Employee", row6.getTableName());
            assertEquals("name", row6.getName(0));
            assertEquals("id", row6.getName(1));
            assertEquals("name", row6.getName(2));
            assertEquals("salary", row6.getName(3));
            assertEquals("department_id", row6.getName(4));

            assertEquals(5, row6.getInteger("id"));
            assertEquals("Paul", row6.getString("name"));
            assertEquals(1, row6.getInteger("department_id"));
            assertEquals(25000, row6.getInteger("salary"));
            
            assertEquals("Paul", row6.getString(0));
            assertEquals(5, row6.getInteger(1));
            assertEquals("Paul", row6.getString(2));
            assertEquals(25000, row6.getInteger(3));
            assertEquals(1, row6.getInteger(4));
            assertFalse(row6.hasNext());

            Row row7 = row6.next();
            assertNull(row7);
        }
    }

}
