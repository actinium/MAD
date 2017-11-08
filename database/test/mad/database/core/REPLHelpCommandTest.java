package mad.database.core;

import org.junit.Test;
import static org.junit.Assert.*;

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
                + ".version Show version number.\n"
                + "dbname>";

        assertEquals(expResult, result);

    }

}
