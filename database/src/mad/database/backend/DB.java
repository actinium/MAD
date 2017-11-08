package mad.database.backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 */
public class DB {

    private Pager pager;

    private DB(String filename) throws FileNotFoundException, IOException {
        File file = new File(filename);
        if(file.exists() && (!file.canRead() || !file.canWrite())){
            throw new IOException("Can not access file: " + filename + "!");
        }
        if(!file.exists() || file.length() == 0){
            initDBFile(file);
        }
        pager = new Pager(file);
    }

    public static DB open(String filename) throws FileNotFoundException, IOException {
        return new DB(filename);
    }
    
    public void close() throws IOException{
        pager.close();
    }

    // - Create Table
    // - Alter Table
    // - Drop Table
    //
    // - Insert Row/Rows
    // - Update Row/Rows
    // - Delete Row/Rows
    // 
    // - Select

    private void initDBFile(File file) throws FileNotFoundException, IOException {
        try(FileOutputStream writer = new FileOutputStream(file)){
            byte[] initBytes = {0,0,0,0,0,0,0,0,0,0,0,0};
            writer.write(initBytes);
        }
    }

}
