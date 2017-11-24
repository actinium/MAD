package mad.database.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import static mad.database.Config.MADVERSION;
import mad.database.backend.old.Statement;

/**
 *
 */
public class REPL implements Runnable {

    BufferedReader in;
    PrintWriter out;

    public REPL(InputStream in, OutputStream out) {
        this.in = new BufferedReader(new InputStreamReader(in));
        this.out = new PrintWriter(out, true);
    }

    private String readline() {
        try {
            return in.readLine();
        } catch (IOException ex) {
            System.err.printf(ex.getMessage());
            System.exit(1);
        }
        return null;
    }

    private int firstNonWhitespace(String str) {
        int index = 0;
        for (char c : str.toCharArray()) {
            if (c != ' ' && c != '\t' && c != '\n') {
                return index;
            }
            index++;
        }
        return index;
    }

    private MetaCommandResult runMetaCommand(String query) {
        query = query.substring(firstNonWhitespace(query));
        if (query.equals(".exit")) {
            return MetaCommandResult.Exit;
        }
        if (query.equals(".help")) {
            out.print(".exit    Exit this program.\n");
            out.print(".help    Show available commands.\n");
            out.print(".version Show version number.\n");
            return MetaCommandResult.Success;
        }
        if (query.equals(".version")) {
            out.printf("MAD version %s\n", MADVERSION);
            return MetaCommandResult.Success;
        }
        return MetaCommandResult.UnrecognizedCommand;
    }

    @Override
    public void run() {
        repl:
        while (true) {
            out.print("dbname>");
            out.flush();
            String query = readline();
            if (query.length() == 0 || firstNonWhitespace(query) == query.length()) {
                continue repl;
            }
            if (query.charAt(firstNonWhitespace(query)) == '.') {
                switch (runMetaCommand(query)) {
                    case Success:
                        continue repl;
                    case Exit:
                        break repl;
                    case UnrecognizedCommand:
                        out.printf("Unrecognized command '%s'. Enter '.help' for help.\n", query);
                        continue repl;
                }
            }

            Statement statement = new Statement(query);
            if (statement.statementIsOK()) {
                switch (statement.execute()) {
                    case Success:
                        continue repl;
                    case Error:
                        out.printf("Error executing command: '%s'. Error: '%s'\n", query, "some error");
                        break;
                }
            } else {
                out.printf("Unrecognized command '%s'.\n", query);
            }
        }
        out.close();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.printf("MAD version %s\n", MADVERSION);
        REPL repl = new REPL(System.in, System.out);
        repl.run();
    }

    private enum MetaCommandResult {

        Success,
        Exit,
        UnrecognizedCommand
    }

}
