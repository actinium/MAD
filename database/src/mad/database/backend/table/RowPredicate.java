package mad.database.backend.table;

import java.io.IOException;
import mad.database.backend.table.Row.NoSuchColumnException;
import mad.database.backend.table.Row.TypeMismatchException;

/**
 *
 */
public interface RowPredicate {

    boolean test(Row row) throws NoSuchColumnException, TypeMismatchException, IOException;
}
