package mad.database.backend;

import mad.database.backend.table.ArrayRow;
import mad.database.backend.table.Row;
import mad.database.backend.table.Schema;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import static mad.database.Config.PAGESIZE;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class DBinsertRowTest {

    public DBinsertRowTest() {
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
     * @throws java.lang.Exception
     */
    @Test
    public void testInsertRow() throws Exception {
        File testFile = File.createTempFile("madtest-", Long.toString(System.nanoTime()));
        testFile.deleteOnExit();
        {
            DB db = DB.open(testFile.getAbsolutePath());
            db.createTable("AwesomeTable", new Schema(
                    new Schema.Field("aCol1", Schema.Field.Type.Integer),
                    new Schema.Field("aCol2", Schema.Field.Type.Integer)));
            db.createTable("SecondTable", new Schema(
                    new Schema.Field("sCol1", Schema.Field.Type.Integer),
                    new Schema.Field("sCol2", Schema.Field.Type.Integer)));
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            List<String> tableNames = db.getTableNames();
            ArrayList<String> expTableNames = new ArrayList<>(Arrays.asList(
                    "AwesomeTable", "SecondTable"));
            assertEquals(expTableNames.size(), tableNames.size());
            for (int i = 0; i < expTableNames.size() && i < tableNames.size(); i++) {
                assertEquals(expTableNames.get(i), tableNames.get(i));
            }
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            Schema schema = db.getSchema("AwesomeTable");
            assertNotNull(schema);
            assertEquals(2, schema.size());
            assertEquals("aCol1", schema.get(0).name);
            assertEquals("aCol2", schema.get(1).name);
            assertEquals(Schema.Field.Type.Integer, schema.get(0).type);
            assertEquals(Schema.Field.Type.Integer, schema.get(1).type);
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            Schema schema = db.getSchema("SecondTable");
            assertNotNull(schema);
            assertEquals(2, schema.size());
            assertEquals("sCol1", schema.get(0).name);
            assertEquals("sCol2", schema.get(1).name);
            assertEquals(Schema.Field.Type.Integer, schema.get(0).type);
            assertEquals(Schema.Field.Type.Integer, schema.get(1).type);
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            ArrayRow row = new ArrayRow().addIntegerColumn("aCol1", 3141).addIntegerColumn("aCol2", 8848);
            int tablePointer = db.getTablePointer("AwesomeTable");
            db.insertRow(tablePointer, row);
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            int tablePointer = db.getTablePointer("AwesomeTable");
            Row row = db.getFirstRow(tablePointer);
            assertNotNull(row);
            assertEquals(3141, row.getInteger(0));
            assertEquals(8848, row.getInteger(1));
            assertFalse(row.hasNext());
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            ArrayRow row = new ArrayRow().addIntegerColumn("aCol1", 111).addIntegerColumn("aCol2", 222);
            int tablePointer = db.getTablePointer("AwesomeTable");
            db.insertRow(tablePointer, row);
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            int tablePointer = db.getTablePointer("AwesomeTable");
            Row row = db.getFirstRow(tablePointer);
            assertNotNull(row);
            assertEquals(3141, row.getInteger(0));
            assertEquals(8848, row.getInteger(1));
            assertTrue(row.hasNext());
            row = row.next();
            assertNotNull(row);
            assertEquals(111, row.getInteger(0));
            assertEquals(222, row.getInteger(1));
            assertFalse(row.hasNext());
            db.close();
        }
    }

    /**
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testInsertRow2() throws Exception {
        File testFile = File.createTempFile("madtest-", Long.toString(System.nanoTime()));
        testFile.deleteOnExit();
        {
            DB db = DB.open(testFile.getAbsolutePath());
            db.createTable("Employee", new Schema(
                    new Schema.Field("id", Schema.Field.Type.Integer),
                    new Schema.Field("name", Schema.Field.Type.Varchar, 100),
                    new Schema.Field("boss", Schema.Field.Type.Boolean),
                    new Schema.Field("salary", Schema.Field.Type.Integer)));
            db.createTable("Department", new Schema(
                    new Schema.Field("id", Schema.Field.Type.Integer),
                    new Schema.Field("name", Schema.Field.Type.Varchar, 45)));
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            List<String> tableNames = db.getTableNames();
            ArrayList<String> expTableNames = new ArrayList<>(Arrays.asList(
                    "Employee", "Department"));
            assertEquals(expTableNames.size(), tableNames.size());
            for (int i = 0; i < expTableNames.size() && i < tableNames.size(); i++) {
                assertEquals(expTableNames.get(i), tableNames.get(i));
            }
            Schema schema1 = db.getSchema("Employee");
            assertNotNull(schema1);
            assertEquals(4, schema1.size());
            assertEquals("id", schema1.get(0).name);
            assertEquals("name", schema1.get(1).name);
            assertEquals("boss", schema1.get(2).name);
            assertEquals("salary", schema1.get(3).name);
            assertEquals(Schema.Field.Type.Integer, schema1.get(0).type);
            assertEquals(Schema.Field.Type.Varchar, schema1.get(1).type);
            assertEquals(100, schema1.get(1).length);
            assertEquals(Schema.Field.Type.Boolean, schema1.get(2).type);
            assertEquals(Schema.Field.Type.Integer, schema1.get(3).type);

            Schema schema2 = db.getSchema("Department");
            assertNotNull(schema2);
            assertEquals(2, schema2.size());
            assertEquals("id", schema2.get(0).name);
            assertEquals("name", schema2.get(1).name);
            assertEquals(Schema.Field.Type.Integer, schema2.get(0).type);
            assertEquals(Schema.Field.Type.Varchar, schema2.get(1).type);
            assertEquals(45, schema2.get(1).length);
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            Row row = db.getFirstRow(db.getTablePointer("Employee"));
            assertNull(row);
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            Row row = new ArrayRow().addIntegerColumn("id", 1)
                    .addStringColumn("name", "Boss Bossman")
                    .addBooleanColumn("boss", true)
                    .addIntegerColumn("salary", 100000);
            db.insertRow("Employee", row);
            Row row2 = new ArrayRow().addIntegerColumn("id", 2)
                    .addStringColumn("name", "Alice")
                    .addBooleanColumn("boss", false)
                    .addIntegerColumn("salary", 35000);
            db.insertRow("Employee", row2);
            Row row3 = new ArrayRow().addIntegerColumn("id", 3)
                    .addStringColumn("name", "Bob")
                    .addBooleanColumn("boss", false)
                    .addIntegerColumn("salary", 32000);
            db.insertRow("Employee", row3);
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            Row row = new ArrayRow().addIntegerColumn("id", 1)
                    .addStringColumn("name", "RnD");
            db.insertRow("Department", row);
            Row row2 = new ArrayRow().addIntegerColumn("id", 2)
                    .addStringColumn("name", "Finance");
            db.insertRow("Department", row2);
            Row row3 = new ArrayRow().addIntegerColumn("id", 4)
                    .addStringColumn("name", "Charlie")
                    .addBooleanColumn("boss", false)
                    .addIntegerColumn("salary", 36000);
            db.insertRow("Employee", row3);
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());

            Row row = db.getFirstRow(db.getTablePointer("Department"));
            assertNotNull(row);
            assertEquals(1, row.getInteger(0));
            assertEquals(1, row.getInteger("id"));
            assertEquals("RnD", row.getString(1));
            assertEquals("RnD", row.getString("name"));
            assertTrue(row.hasNext());

            Row row2 = row.next();
            assertNotNull(row2);
            assertEquals(2, row2.getInteger(0));
            assertEquals(2, row2.getInteger("id"));
            assertEquals("Finance", row2.getString(1));
            assertEquals("Finance", row2.getString("name"));
            assertFalse(row2.hasNext());

            Row row3 = row2.next();
            assertNull(row3);
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());

            Row row = db.getFirstRow(db.getTablePointer("Employee"));
            assertNotNull(row);
            assertEquals(1, row.getInteger(0));
            assertEquals(1, row.getInteger("id"));
            assertEquals("Boss Bossman", row.getString(1));
            assertEquals("Boss Bossman", row.getString("name"));
            assertEquals(true, row.getBoolean(2));
            assertEquals(true, row.getBoolean("boss"));
            assertEquals(100000, row.getInteger(3));
            assertEquals(100000, row.getInteger("salary"));
            assertTrue(row.hasNext());

            Row row2 = row.next();
            assertNotNull(row2);
            assertEquals(2, row2.getInteger(0));
            assertEquals(2, row2.getInteger("id"));
            assertEquals("Alice", row2.getString(1));
            assertEquals("Alice", row2.getString("name"));
            assertEquals(false, row2.getBoolean(2));
            assertEquals(false, row2.getBoolean("boss"));
            assertEquals(35000, row2.getInteger(3));
            assertEquals(35000, row2.getInteger("salary"));
            assertTrue(row.hasNext());

            Row row3 = row2.next();
            assertNotNull(row3);
            assertEquals(3, row3.getInteger(0));
            assertEquals(3, row3.getInteger("id"));
            assertEquals("Bob", row3.getString(1));
            assertEquals("Bob", row3.getString("name"));
            assertEquals(false, row3.getBoolean(2));
            assertEquals(false, row3.getBoolean("boss"));
            assertEquals(32000, row3.getInteger(3));
            assertEquals(32000, row3.getInteger("salary"));
            assertTrue(row.hasNext());

            Row row4 = row3.next();
            assertNotNull(row4);
            assertEquals(4, row4.getInteger(0));
            assertEquals(4, row4.getInteger("id"));
            assertEquals("Charlie", row4.getString(1));
            assertEquals("Charlie", row4.getString("name"));
            assertEquals(false, row4.getBoolean(2));
            assertEquals(false, row4.getBoolean("boss"));
            assertEquals(36000, row4.getInteger(3));
            assertEquals(36000, row4.getInteger("salary"));
            assertFalse(row4.hasNext());
            Row row5 = row4.next();
            assertNull(row5);

            db.close();
        }
    }

    /**
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testInsertRow3() throws Exception {
        File testFile = File.createTempFile("madtest-", Long.toString(System.nanoTime()));
        testFile.deleteOnExit();
        {
            DB db = DB.open(testFile.getAbsolutePath());
            db.createTable("Person", new Schema(
                    new Schema.Field("id", Schema.Field.Type.Integer),
                    new Schema.Field("name", Schema.Field.Type.Varchar, 100)));
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            List<String> tableNames = db.getTableNames();
            ArrayList<String> expTableNames = new ArrayList<>(Arrays.asList(
                    "Person"));
            assertEquals(expTableNames.size(), tableNames.size());
            for (int i = 0; i < expTableNames.size() && i < tableNames.size(); i++) {
                assertEquals(expTableNames.get(i), tableNames.get(i));
            }
            Schema schema = db.getSchema("Person");
            assertNotNull(schema);
            assertEquals(2, schema.size());
            assertEquals("id", schema.get(0).name);
            assertEquals("name", schema.get(1).name);
            assertEquals(Schema.Field.Type.Integer, schema.get(0).type);
            assertEquals(Schema.Field.Type.Varchar, schema.get(1).type);
            assertEquals(100, schema.get(1).length);
            assertEquals(PAGESIZE / (4 + 4 + 8 + 4 + 100), schema.rowsPerPage());
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            Row row = db.getFirstRow(db.getTablePointer("Person"));
            assertNull(row);
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            for (int i = 0; i < 100; i++) {
                Row row = new ArrayRow().addIntegerColumn("id", i)
                        .addStringColumn("name", "Person-" + i);
                db.insertRow("Person", row);
            }
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());

            Row row = db.getFirstRow(db.getTablePointer("Person"));
            for (int i = 0; i < 100; i++) {
                assertNotNull(row);
                assertEquals(i, row.getInteger(0));
                assertEquals(i, row.getInteger("id"));
                assertEquals("Person-" + i, row.getString(1));
                assertEquals("Person-" + i, row.getString("name"));
                if (i < 99) {
                    assertTrue(row.hasNext());
                    row = row.next();
                } else {
                    assertFalse(row.hasNext());
                }
            }
            db.close();
        }
    }

    /**
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testInsertRow4() throws Exception {
        File testFile = File.createTempFile("madtest-", Long.toString(System.nanoTime()));
        testFile.deleteOnExit();
        {
            DB db = DB.open(testFile.getAbsolutePath());
            db.createTable("Person", new Schema(
                    new Schema.Field("id", Schema.Field.Type.Integer),
                    new Schema.Field("name", Schema.Field.Type.Varchar, 100)));

            List<String> tableNames = db.getTableNames();
            ArrayList<String> expTableNames = new ArrayList<>(Arrays.asList("Person"));
            assertEquals(expTableNames.size(), tableNames.size());
            for (int i = 0; i < expTableNames.size() && i < tableNames.size(); i++) {
                assertEquals(expTableNames.get(i), tableNames.get(i));
            }

            Schema schema = db.getSchema("Person");
            assertNotNull(schema);
            assertEquals(2, schema.size());
            assertEquals("id", schema.get(0).name);
            assertEquals("name", schema.get(1).name);
            assertEquals(Schema.Field.Type.Integer, schema.get(0).type);
            assertEquals(Schema.Field.Type.Varchar, schema.get(1).type);
            assertEquals(100, schema.get(1).length);
            assertEquals(PAGESIZE / (4 + 4 + 8 + 4 + 100), schema.rowsPerPage());

            Row row = db.getFirstRow(db.getTablePointer("Person"));
            assertNull(row);

            for (int i = 0; i < 100; i++) {
                Row irow = new ArrayRow().addIntegerColumn("id", i)
                        .addStringColumn("name", "Person-" + i);
                db.insertRow("Person", irow);
            }

            row = db.getFirstRow(db.getTablePointer("Person"));
            for (int i = 0; i < 100; i++) {
                assertNotNull(row);
                assertEquals(i, row.getInteger(0));
                assertEquals(i, row.getInteger("id"));
                assertEquals("Person-" + i, row.getString(1));
                assertEquals("Person-" + i, row.getString("name"));
                if (i < 99) {
                    assertTrue(row.hasNext());
                    row = row.next();
                } else {
                    assertFalse(row.hasNext());
                }
            }
            db.close();
        }
    }

    /**
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testInsertRow5() throws Exception {
        File testFile = File.createTempFile("madtest-", Long.toString(System.nanoTime()));
        testFile.deleteOnExit();
        {
            DB db = DB.open(testFile.getAbsolutePath());
            db.createTable("Person", new Schema(
                    new Schema.Field("id", Schema.Field.Type.Integer),
                    new Schema.Field("name", Schema.Field.Type.Varchar, 100)));
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            List<String> tableNames = db.getTableNames();
            ArrayList<String> expTableNames = new ArrayList<>(Arrays.asList(
                    "Person"));
            assertEquals(expTableNames.size(), tableNames.size());
            for (int i = 0; i < expTableNames.size() && i < tableNames.size(); i++) {
                assertEquals(expTableNames.get(i), tableNames.get(i));
            }
            Schema schema = db.getSchema("Person");
            assertNotNull(schema);
            assertEquals(2, schema.size());
            assertEquals("id", schema.get(0).name);
            assertEquals("name", schema.get(1).name);
            assertEquals(Schema.Field.Type.Integer, schema.get(0).type);
            assertEquals(Schema.Field.Type.Varchar, schema.get(1).type);
            assertEquals(100, schema.get(1).length);
            assertEquals(PAGESIZE / (4 + 4 + 8 + 4 + 100), schema.rowsPerPage());
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            Row row = db.getFirstRow(db.getTablePointer("Person"));
            assertNull(row);
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            for (int i = 0; i < 400; i++) {
                Row row = new ArrayRow().addIntegerColumn("id", i)
                        .addStringColumn("name", "Person-" + i);
                db.insertRow("Person", row);
            }
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());

            Row row = db.getFirstRow(db.getTablePointer("Person"));
            for (int i = 0; i < 400; i++) {
                assertNotNull(row);
                assertEquals(i, row.getInteger(0));
                assertEquals(i, row.getInteger("id"));
                assertEquals("Person-" + i, row.getString(1));
                assertEquals("Person-" + i, row.getString("name"));
                if (i < 399) {
                    assertTrue(row.hasNext());
                    row = row.next();
                } else {
                    assertFalse(row.hasNext());
                }
            }
            db.close();
        }
    }

    /**
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testInsertRow6() throws Exception {
        File testFile = File.createTempFile("madtest-", Long.toString(System.nanoTime()));
        testFile.deleteOnExit();
        {
            DB db = DB.open(testFile.getAbsolutePath());
            db.createTable("Person", new Schema(
                    new Schema.Field("id", Schema.Field.Type.Integer),
                    new Schema.Field("name", Schema.Field.Type.Varchar, 40)));
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            Row row = db.getFirstRow(db.getTablePointer("Person"));
            assertNull(row);
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            for (int i = 0; i < 100; i++) {
                Row row = new ArrayRow().addIntegerColumn("id", i)
                        .addStringColumn("name", "Person-" + i);
                db.insertRow("Person", row);
            }
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            db.createTable("Country", new Schema(
                    new Schema.Field("id", Schema.Field.Type.Integer),
                    new Schema.Field("name", Schema.Field.Type.Varchar, 120),
                    new Schema.Field("capital", Schema.Field.Type.Varchar, 120)));
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            Row row = db.getFirstRow(db.getTablePointer("Country"));
            assertNull(row);
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());

            Row row = db.getFirstRow(db.getTablePointer("Person"));
            for (int i = 0; i < 100; i++) {
                assertNotNull(row);
                assertEquals(i, row.getInteger(0));
                assertEquals(i, row.getInteger("id"));
                assertEquals("Person-" + i, row.getString(1));
                assertEquals("Person-" + i, row.getString("name"));
                if (i < 99) {
                    assertTrue(row.hasNext());
                    row = row.next();
                } else {
                    assertFalse(row.hasNext());
                }
            }
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            for (int i = 100; i < 200; i++) {
                Row row = new ArrayRow().addIntegerColumn("id", i)
                        .addStringColumn("name", "Person-" + i);
                db.insertRow("Person", row);
            }
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            for (int i = 0; i < 100; i++) {
                Row row = new ArrayRow().addIntegerColumn("id", i)
                        .addStringColumn("name", "Country-" + i)
                        .addStringColumn("capital", "Capital-" + i);
                db.insertRow("Country", row);
            }
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());

            Row row = db.getFirstRow(db.getTablePointer("Person"));
            for (int i = 0; i < 200; i++) {
                assertNotNull(row);
                assertEquals(i, row.getInteger(0));
                assertEquals(i, row.getInteger("id"));
                assertEquals("Person-" + i, row.getString(1));
                assertEquals("Person-" + i, row.getString("name"));
                if (i < 199) {
                    assertTrue(row.hasNext());
                    row = row.next();
                } else {
                    assertFalse(row.hasNext());
                }
            }
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());

            Row row = db.getFirstRow(db.getTablePointer("Country"));
            for (int i = 0; i < 100; i++) {
                assertNotNull(row);
                assertEquals(i, row.getInteger(0));
                assertEquals(i, row.getInteger("id"));
                assertEquals("Country-" + i, row.getString(1));
                assertEquals("Country-" + i, row.getString("name"));
                assertEquals("Capital-" + i, row.getString(2));
                assertEquals("Capital-" + i, row.getString("capital"));
                if (i < 99) {
                    assertTrue(row.hasNext());
                    row = row.next();
                } else {
                    assertFalse(row.hasNext());
                }
            }
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            for (int i = 100; i < 300; i++) {
                Row row = new ArrayRow().addIntegerColumn("id", i)
                        .addStringColumn("name", "Country-" + i)
                        .addStringColumn("capital", "Capital-" + i);
                db.insertRow("Country", row);
            }
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            for (int i = 200; i < 400; i++) {
                Row row = new ArrayRow().addIntegerColumn("id", i)
                        .addStringColumn("name", "Person-" + i);
                db.insertRow("Person", row);
            }
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            for (int i = 300; i < 400; i++) {
                Row row = new ArrayRow().addIntegerColumn("id", i)
                        .addStringColumn("name", "Country-" + i)
                        .addStringColumn("capital", "Capital-" + i);
                db.insertRow("Country", row);
            }
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());

            Row row = db.getFirstRow(db.getTablePointer("Person"));
            for (int i = 0; i < 400; i++) {
                assertNotNull(row);
                assertEquals(i, row.getInteger(0));
                assertEquals(i, row.getInteger("id"));
                assertEquals("Person-" + i, row.getString(1));
                assertEquals("Person-" + i, row.getString("name"));
                if (i < 399) {
                    assertTrue(row.hasNext());
                    row = row.next();
                } else {
                    assertFalse(row.hasNext());
                }
            }
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());

            Row row = db.getFirstRow(db.getTablePointer("Country"));
            for (int i = 0; i < 400; i++) {
                assertNotNull(row);
                assertEquals(i, row.getInteger(0));
                assertEquals(i, row.getInteger("id"));
                assertEquals("Country-" + i, row.getString(1));
                assertEquals("Country-" + i, row.getString("name"));
                assertEquals("Capital-" + i, row.getString(2));
                assertEquals("Capital-" + i, row.getString("capital"));
                if (i < 399) {
                    assertTrue(row.hasNext());
                    row = row.next();
                } else {
                    assertFalse(row.hasNext());
                }
            }
            db.close();
        }
    }

    /**
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testInsertRow7() throws Exception {
        File testFile = File.createTempFile("madtest-", Long.toString(System.nanoTime()));
        testFile.deleteOnExit();
        {
            DB db = DB.open(testFile.getAbsolutePath());
            db.createTable("Employee", new Schema(
                    new Schema.Field("id", Schema.Field.Type.Integer),
                    new Schema.Field("name", Schema.Field.Type.Varchar, 100),
                    new Schema.Field("boss", Schema.Field.Type.Boolean),
                    new Schema.Field("salary", Schema.Field.Type.Integer)));
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            Row row = new ArrayRow().addIntegerColumn("id", 1)
                    .addStringColumn("name", "Boss Bossman")
                    .addBooleanColumn("boss", true)
                    .addIntegerColumn("salary", 100000);
            db.insertRow("Employee", row);
            Row row2 = new ArrayRow().addIntegerColumn("id", 2)
                    .addStringColumn("name", "Alice")
                    .addNullBooleanColumn("boss")
                    .addIntegerColumn("salary", 35000);
            db.insertRow("Employee", row2);
            Row row3 = new ArrayRow().addIntegerColumn("id", 3)
                    .addStringColumn("name", "Bob")
                    .addBooleanColumn("boss", false)
                    .addNullIntegerColumn("salary");
            db.insertRow("Employee", row3);
            Row row4 = new ArrayRow().addIntegerColumn("id", 4)
                    .addStringColumn("name", "Charlie")
                    .addNullBooleanColumn("boss")
                    .addNullIntegerColumn("salary");
            db.insertRow("Employee", row4);
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());

            Row row = db.getFirstRow(db.getTablePointer("Employee"));
            assertNotNull(row);
            assertFalse(row.isNull(0));
            assertFalse(row.isNull("id"));
            assertFalse(row.isNull(1));
            assertFalse(row.isNull("name"));
            assertFalse(row.isNull(2));
            assertFalse(row.isNull("boss"));
            assertFalse(row.isNull(3));
            assertFalse(row.isNull("salary"));
            assertTrue(row.hasNext());

            Row row2 = row.next();
            assertNotNull(row2);
            assertFalse(row2.isNull(0));
            assertFalse(row2.isNull("id"));
            assertFalse(row2.isNull(1));
            assertFalse(row2.isNull("name"));
            assertTrue(row2.isNull(2));
            assertTrue(row2.isNull("boss"));
            assertFalse(row2.isNull(3));
            assertFalse(row2.isNull("salary"));
            assertTrue(row.hasNext());

            Row row3 = row2.next();
            assertNotNull(row3);
            assertFalse(row3.isNull(0));
            assertFalse(row3.isNull("id"));
            assertFalse(row3.isNull(1));
            assertFalse(row3.isNull("name"));
            assertFalse(row3.isNull(2));
            assertFalse(row3.isNull("boss"));
            assertTrue(row3.isNull(3));
            assertTrue(row3.isNull("salary"));
            assertTrue(row.hasNext());

            Row row4 = row3.next();
            assertNotNull(row4);
            assertFalse(row4.isNull(0));
            assertFalse(row4.isNull("id"));
            assertFalse(row4.isNull(1));
            assertFalse(row4.isNull("name"));
            assertTrue(row4.isNull(2));
            assertTrue(row4.isNull("boss"));
            assertTrue(row4.isNull(3));
            assertTrue(row4.isNull("salary"));
            assertFalse(row4.hasNext());
            Row row5 = row4.next();
            assertNull(row5);

            db.close();
        }
    }
}
