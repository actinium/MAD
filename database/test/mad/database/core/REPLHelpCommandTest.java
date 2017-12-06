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
                + ".exit    Exit this program.\n"
                + ".help    Show available commands.\n"
                + ".pwd     Print working directory.\n"
                + ".version Show version number.\n"
                + "dbname>";

        assertEquals(expResult, result);

    }

}
