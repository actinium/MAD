package mad.database.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import static mad.database.Config.MADVERSION;
import mad.database.sql.Parser;
import mad.database.sql.Tokenizer;
import mad.database.sql.Tokenizer.Token.TokenType;
import mad.database.sql.ast.StatementList;

/**
 *
 */
public class REPL implements Runnable {

    private final BufferedReader in;
    private final PrintWriter out;

    public REPL(InputStream in, OutputStream out) {
        this.in = new BufferedReader(new InputStreamReader(in));
        this.out = new PrintWriter(out, true);
    }

    private String readline() {
        try {
            return in.readLine();
        } catch (IOException ex) {
            out.printf(ex.getMessage());
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

    private void runSqlStatement(String query) {
        Tokenizer tokenizer = new Tokenizer();
        Parser parser = new Parser(tokenizer);
        try {
            tokenizer.tokenize(query);
            parser.readTokens();
            while (parser.lastSymbol() != TokenType.Semicolon) {
                System.out.print("   ...>");
                query = readline();
                tokenizer.tokenize(query);
                parser.readTokens();
            }
            StatementList sl = new StatementList();
            while (!parser.done()) {
                sl.add(parser.parse());
            }
            System.out.println(sl.toString());
        } catch (Tokenizer.TokenizeException ex) {
            out.println("Error tokenizing input:" + ex.getMessage() + " (" + ex.getIndex() + ")");
        } catch (Parser.ParseError ex) {
            out.println(ex.getMessage());
        }

    }

    @Override
    public void run() {
        repl:
        while (true) {
            out.print("dbname>");
            out.flush();
            String query = readline();
            if (query.length() == 0 || firstNonWhitespace(query) == query.length()) {
                // If line was empty when enter was pressed, do nothing.
                continue repl;
            }
            if (query.charAt(firstNonWhitespace(query)) == '.') {
                // If first non whitespace character was a '.' run metacommand.
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
            runSqlStatement(query);
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
