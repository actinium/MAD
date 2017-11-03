package mad.database.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import mad.database.Config;
import mad.database.sql.Statement;

/**
 *
 */
public class REPL {

    BufferedReader in;
    OutputStream out;

    public REPL(InputStream in, OutputStream out){
        this.in = new BufferedReader(new InputStreamReader(System.in));
        this.out = out;
    }
    
    private String readline() {
        String line = null;
        try {
            line = in.readLine();
        } catch (IOException ex) {
            System.err.printf(ex.getMessage());
            System.exit(1);
        }
        return line;
    }

    private MetaCommandResult runMetaCommand(String query) {
        if (query.equals(".exit")) {
            return MetaCommandResult.Exit;
        }
        if (query.equals(".help")){
            System.out.println(".exit    Exit this program.");
            System.out.println(".help    Show available commands.");
            System.out.println(".version Show version number.");
            return MetaCommandResult.Success;
        }
        if(query.equals(".version")){
            System.out.printf("MAD version %s\n", Config.VERSION);
            return MetaCommandResult.Success;
        }
        return MetaCommandResult.UnrecognizedCommand;
    }

    private void run() {
        repl:
        while (true) {
            System.out.print("dbname>");
            String query = readline();
            if(query.charAt(0)=='.'){
                switch(runMetaCommand(query)){
                    case Success:
                        continue repl;
                    case Exit:
                        break repl;
                    case UnrecognizedCommand:
                        System.out.printf("Unrecognized command '%s'. Enter '.help' for help.\n", query);
                }
            }
            
            Statement statement = new Statement(query);
            if(statement.statementIsOK()){
                switch(statement.execute()){
                    case Success:
                        continue repl;
                    case Error:
                        System.out.printf("Error executing command: '%s'. Error: '%s'\n", query, "some error");
                        break;
                }
            }
            else{
                System.out.printf("Unrecognized command '%s'.\n", query);
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.printf("MAD version %s\n", Config.VERSION);
        REPL repl = new REPL(System.in,System.out);
        repl.run();
    }
    
    private enum MetaCommandResult{
        Success,
        Exit,
        UnrecognizedCommand
    }

}
