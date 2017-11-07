package mad.database.backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/**
 *
 */
public class Pager {
    
    private final RandomAccessFile dbFile;
    
    public Pager(File file) throws FileNotFoundException{
        dbFile = new RandomAccessFile(file, "rwd");
    }
    
}
