package mad.database.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import mad.database.Config;

/**
 *
 */
public class REPL {

    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

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
            System.exit(0);
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
        while (true) {
            System.out.print("dbname>");
            String query = readline();
            if(query.charAt(0)=='.'){
                switch(runMetaCommand(query)){
                    case Success:
                        continue;
                    case UnrecognizedCommand:
                        System.out.printf("Unrecognized command '%s'. Enter '.help' for help.\n", query);
                }
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.printf("MAD version %s\n", Config.VERSION);
        REPL repl = new REPL();
        repl.run();
    }
    
    private enum MetaCommandResult{
        Success,
        UnrecognizedCommand
    }

}
