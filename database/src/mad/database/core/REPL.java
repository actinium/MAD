package mad.database.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class REPL {
    
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    
    private String readline(){
        String line = null;
        try {
            line = in.readLine();
        } catch (IOException ex) {
            System.err.printf(ex.getMessage());
        }
        return line;
    }
    
    private void run(){
        while(true){
            System.out.print("dbname>");
            String query = readline();
            if(query.equals(".exit")){
                System.exit(0);
            }else{
                System.out.printf("Unrecognized command '%s'\n", query);
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        REPL repl = new REPL();
        repl.run();
    }
    
}
