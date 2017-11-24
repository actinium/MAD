package mad.database.backend.table;

/**
 *
 */
public interface DeleteableRow extends Row {

    /**
     *
     * @return
     */
    @Override
    WritableRow next();

    /**
     *
     * @return
     */
    WritableRow previous();

    /**
     *
     * @return
     */
    boolean hasPrevious();
}
