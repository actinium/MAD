package mad.database.backend;

import java.io.File;
import java.io.IOException;
import static junit.framework.Assert.assertEquals;
import mad.database.backend.table.ArrayRow;
import mad.database.backend.table.Row;
import mad.database.backend.table.Schema;
import mad.database.backend.table.WritableRow;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class DBUpdateRowTest {

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
     */
    @Test
    public void testRowUpdate() throws IOException, Row.TypeMismatchException, Row.NoSuchColumnException {
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
            assertEquals("Department", row.getTableName(0));
            assertTrue(row.hasNext());

            Row row2 = row.next();
            assertNotNull(row2);
            assertEquals(1, row2.getInteger("id"));
            assertEquals("Finance", row2.getString("name"));
            assertEquals("Department", row2.getTableName(0));
            assertFalse(row2.hasNext());

            Row row3 = row2.next();
            assertNull(row3);
        }
        try (DB db = DB.open(testFile.getAbsolutePath())) {
            Row row = db.getFirstRow(db.getTablePointer("Employee"));
            assertNotNull(row);
            assertEquals(0, row.getInteger("id"));
            assertEquals("Gary", row.getString("name"));
            assertEquals(1, row.getInteger("department_id"));
            assertEquals(50000, row.getInteger("salary"));
            assertEquals("Employee", row.getTableName(0));
            assertTrue(row.hasNext());

            Row row2 = row.next();
            assertNotNull(row2);
            assertEquals(1, row2.getInteger("id"));
            assertEquals("Alice", row2.getString("name"));
            assertEquals(0, row2.getInteger("department_id"));
            assertEquals(35000, row2.getInteger("salary"));
            assertEquals("Employee", row2.getTableName(0));
            assertTrue(row.hasNext());

            Row row3 = row2.next();
            assertNotNull(row3);
            assertEquals(2, row3.getInteger("id"));
            assertEquals("Bob", row3.getString("name"));
            assertEquals(1, row3.getInteger("department_id"));
            assertEquals(36000, row3.getInteger("salary"));
            assertEquals("Employee", row3.getTableName(0));
            assertTrue(row.hasNext());

            Row row4 = row3.next();
            assertNotNull(row4);
            assertEquals(3, row4.getInteger("id"));
            assertEquals("Charlie", row4.getString("name"));
            assertEquals(0, row4.getInteger("department_id"));
            assertEquals(32000, row4.getInteger("salary"));
            assertEquals("Employee", row4.getTableName(0));
            assertTrue(row4.hasNext());

            Row row5 = row4.next();
            assertNotNull(row4);
            assertEquals(4, row5.getInteger("id"));
            assertEquals("Lara", row5.getString("name"));
            assertEquals(0, row5.getInteger("department_id"));
            assertEquals(37000, row5.getInteger("salary"));
            assertEquals("Employee", row5.getTableName(0));
            assertFalse(row5.hasNext());
            Row row6 = row5.next();
            assertNull(row6);
        }
        try (DB db = DB.open(testFile.getAbsolutePath())) {
            WritableRow row = db.getFirstRow(db.getTablePointer("Employee"));
            while(row != null){
                if(row.getInteger("department_id")==1){
                    int salary = row.getInteger("salary");
                    salary += 5000;
                    row.setInteger("salary", salary);
                }
                row = row.next();
            }
        }
        try (DB db = DB.open(testFile.getAbsolutePath())) {
            Row row = db.getFirstRow(db.getTablePointer("Employee"));
            assertNotNull(row);
            assertEquals(0, row.getInteger("id"));
            assertEquals("Gary", row.getString("name"));
            assertEquals(1, row.getInteger("department_id"));
            assertEquals(55000, row.getInteger("salary"));
            assertEquals("Employee", row.getTableName(0));
            assertTrue(row.hasNext());

            Row row2 = row.next();
            assertNotNull(row2);
            assertEquals(1, row2.getInteger("id"));
            assertEquals("Alice", row2.getString("name"));
            assertEquals(0, row2.getInteger("department_id"));
            assertEquals(35000, row2.getInteger("salary"));
            assertEquals("Employee", row2.getTableName(0));
            assertTrue(row.hasNext());

            Row row3 = row2.next();
            assertNotNull(row3);
            assertEquals(2, row3.getInteger("id"));
            assertEquals("Bob", row3.getString("name"));
            assertEquals(1, row3.getInteger("department_id"));
            assertEquals(41000, row3.getInteger("salary"));
            assertEquals("Employee", row3.getTableName(0));
            assertTrue(row.hasNext());

            Row row4 = row3.next();
            assertNotNull(row4);
            assertEquals(3, row4.getInteger("id"));
            assertEquals("Charlie", row4.getString("name"));
            assertEquals(0, row4.getInteger("department_id"));
            assertEquals(32000, row4.getInteger("salary"));
            assertEquals("Employee", row4.getTableName(0));
            assertTrue(row4.hasNext());

            Row row5 = row4.next();
            assertNotNull(row4);
            assertEquals(4, row5.getInteger("id"));
            assertEquals("Lara", row5.getString("name"));
            assertEquals(0, row5.getInteger("department_id"));
            assertEquals(37000, row5.getInteger("salary"));
            assertEquals("Employee", row5.getTableName(0));
            assertFalse(row5.hasNext());
            Row row6 = row5.next();
            assertNull(row6);
        }
    }

}
