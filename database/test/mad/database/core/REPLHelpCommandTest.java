package mad.database.core;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 */
public class REPLHelpCommandTest extends REPLTestBase {

    /**
     * Test of help command
     */
    @Test
    public void testHelpCommand() {

        writer.println(".help");
        writer.println(".exit");

        String result = read(reader);
        String expResult
                = "dbname>"
                + ".cd     DIRECTORY     Change working directory.\n"
                + ".exit                 Exit this program.\n"
                + ".help                 Show available commands.\n"
                + ".ls                   List files in directory.\n"
                + ".pwd                  Print working directory.\n"
                + ".schema TABLENAME     Show schema for table.\n"
                + ".tables               List tables in database.\n"
                + ".version              Show version number.\n"
                + "dbname>";

        assertEquals(expResult, result);

    }

}
