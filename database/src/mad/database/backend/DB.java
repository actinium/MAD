package mad.database.backend;

import java.io.File;
import java.io.FileNotFoundException;

/**
 *
 */
public class DB {
    
    private Pager pager;
    
    private DB(String filename) throws FileNotFoundException{
        File file = new File(filename);
        pager = new Pager(file);
    }
    
    public static DB open(String filename) throws FileNotFoundException{
        return new DB(filename);
    }
}
