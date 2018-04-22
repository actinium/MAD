package mad.database.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import static mad.database.Config.MADVERSION;
import mad.database.backend.DB;
import mad.database.backend.StatementProcessor;
import mad.database.backend.expression.StaticExpressionProcessor;
import mad.database.backend.table.Row;
import mad.database.backend.table.Schema;
import mad.database.backend.table.Schema.Field;
import mad.database.sql.Parser;
import mad.database.sql.Tokenizer;
import mad.database.sql.Tokenizer.Token.TokenType;
import mad.database.sql.ast.Statement;
import mad.database.sql.ast.StatementList;
import mad.database.sql.ast.selection.SelectStatement;

/**
 *
 */
public class REPL implements Runnable {

    private final BufferedReader in;
    private final PrintWriter out;

    private File pwd;

    private final DB db;
    private final StatementProcessor processor;

    public REPL(String dbFilename, InputStream in, OutputStream out) throws IOException {
        this.in = new BufferedReader(new InputStreamReader(in));
        this.out = new PrintWriter(out, true);
        this.pwd = Paths.get("").toFile();
        db = DB.open(dbFilename);
        processor = new StatementProcessor(db);

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

    private int lastNonWhitespace(String str) {
        char[] strArray = str.toCharArray();
        for (int i = strArray.length - 1; i > 0; i--) {
            if (strArray[i] != ' ' && strArray[i] != '\t' && strArray[i] != '\n') {
                return i;
            }
        }
        return 0;
    }

    private boolean matchesCommand(String query, String command) {
        if (query.length() < command.length()) {
            return false;
        }
        if (!query.substring(0, command.length()).equals(command)) {
            return false;
        }
        if (query.length() > command.length()) {
            return query.charAt(command.length()) == ' ' || query.charAt(command.length()) == '\t';
        } else {
            return true;
        }
    }

    private void cd(String query) {
        query = query.substring(3);
        if (query.length() == 0) {
            return;
        }
        query = query.substring(firstNonWhitespace(query));
        query = query.substring(0, lastNonWhitespace(query) + 1);
        if (query.charAt(0) == '"' && query.charAt(query.length() - 1) == '"') {
            query = query.substring(1, query.length() - 1);
        }
        if (query.equals("..")) {
            pwd = pwd.getAbsoluteFile().getParentFile();
            return;
        }
        if (query.equals("~")) {
            pwd = new File(System.getProperty("user.home"));
            return;
        }
        File[] files = pwd.getAbsoluteFile().listFiles();
        for (File file : files) {
            if (file.getName().equals(query)) {
                if (file.isDirectory()) {
                    pwd = file;
                    return;
                } else {
                    out.print("'" + query + "' is not a directory.\n");
                    return;
                }
            }
        }
        out.print("No directory with name '" + query + "'.\n");
    }

    private void ls() {
        File[] files = pwd.getAbsoluteFile().listFiles();
        for (File file : files) {
            if (!file.isHidden()) {
                if (file.isFile()) {
                    out.print("F - ");
                } else if (file.isDirectory()) {
                    out.print("D - ");
                } else {
                    out.print("? - ");
                }
                out.println(file.getName());
            }
        }
    }

    private void schema(String query) {
        String tableName = query.substring(7);
        if (tableName.length() == 0) {
            return;
        }
        tableName = tableName.substring(firstNonWhitespace(tableName));
        tableName = tableName.substring(0, lastNonWhitespace(tableName) + 1);
        if (tableName.charAt(0) == '"' && tableName.charAt(tableName.length() - 1) == '"') {
            tableName = tableName.substring(1, tableName.length() - 1);
        }
        try {
            Schema schema = db.getSchema(tableName);
            if (schema != null) {
                out.println(tableName + "(");
                for (Field f : schema) {
                    out.print("    ");
                    out.print(f.name);
                    out.print(" ");
                    out.print(f.type);
                    if (f.type == Field.Type.Varchar) {
                        out.print("(" + f.length + ")");
                    }
                    out.print(",\n");
                }
                out.print(");\n");
            } else {
                out.print("Error: Could not find table!\n");
            }
        } catch (IOException ex) {
            out.print("Error: Could not read schema!\n");
        }

    }

    private void tables() {
        try {
            for (String tablename : db.getTableNames()) {
                out.println(tablename);
            }
        } catch (IOException ex) {
            out.println(ex.getMessage());
        }
    }

    private MetaCommandResult runMetaCommand(String query) {
        query = query.substring(firstNonWhitespace(query));
        if (matchesCommand(query, ".cd")) {
            cd(query);
            return MetaCommandResult.Correct;
        }
        if (matchesCommand(query, ".exit")) {
            return MetaCommandResult.Exit;
        }
        if (matchesCommand(query, ".help")) {
            out.print(".cd     DIRECTORY     Change working directory.\n");
            out.print(".exit                 Exit this program.\n");
            out.print(".help                 Show available commands.\n");
            out.print(".ls                   List files in directory.\n");
            out.print(".pwd                  Print working directory.\n");
            out.print(".schema TABLENAME     Show schema for table.\n");
            out.print(".tables               List tables in database.\n");
            out.print(".version              Show version number.\n");
            return MetaCommandResult.Correct;
        }
        if (matchesCommand(query, ".ls")) {
            ls();
            return MetaCommandResult.Correct;
        }
        if (matchesCommand(query, ".pwd")) {
            out.printf(pwd.getAbsolutePath() + "%n");
            return MetaCommandResult.Correct;
        }
        if (matchesCommand(query, ".schema")) {
            schema(query);
            return MetaCommandResult.Correct;
        }
        if (matchesCommand(query, ".tables")) {
            tables();
            return MetaCommandResult.Correct;
        }
        if (matchesCommand(query, ".version")) {
            out.printf("MAD version %s\n", MADVERSION);
            return MetaCommandResult.Correct;
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
                out.print("   ...>");
                out.flush();
                query = readline();
                tokenizer.tokenize(query);
                parser.readTokens();
            }
            StatementList sl = new StatementList();
            while (!parser.done()) {
                sl.add(parser.parse());
            }
            //System.out.println(sl.toString());
            for (Statement statement : sl.getList()) {
                if (statement instanceof SelectStatement) {
                    runSelectStatement(statement);
                } else {
                    try {
                        processor.executeUpdate(statement);
                    } catch (IOException | StatementProcessor.CreateTableException |
                            Row.TypeMismatchException |
                            StaticExpressionProcessor.NonStaticVariableException |
                            StatementProcessor.NoSuchTableException ex) {
                        out.println(ex.getMessage());
                    }
                }
            }
        } catch (Tokenizer.TokenizeException ex) {
            out.println("Error tokenizing input:" + ex.getMessage() + " (" + ex.getIndex() + ")");
        } catch (Parser.ParseError ex) {
            out.println(ex.getMessage());
        }

    }

    /**
     *
     * @param statement
     */
    public void runSelectStatement(Statement statement) {
        try {
            Row row = processor.executeQuery((SelectStatement) statement);
            if (row == null) {
                return;
            }
            while (true) {
                int columns = row.columns();
                for (int i = 0; i < columns; i++) {
                    try {
                        if (row.isNull(i)) {
                            out.print("NULL");
                        } else {
                            switch (row.getType(i)) {
                                case Boolean:
                                    out.print(row.getBoolean(i));
                                    break;
                                case Float:
                                    out.print(row.getFloat(i));
                                    break;
                                case Integer:
                                    out.print(row.getInteger(i));
                                    break;
                                case Varchar:
                                    out.print(row.getString(i));
                                    break;
                            }
                        }
                        if (i != row.columns() - 1) {
                            out.print(", ");
                        }
                    } catch (Row.NoSuchColumnException | Row.TypeMismatchException ex) {
                        out.print(ex.getMessage());
                    }
                }
                out.print("\n");
                out.flush();
                if (row.hasNext()) {
                    row = row.next();
                } else {
                    break;
                }
            }
        } catch (IOException ex) {
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
                    case Correct:
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
        if (args.length != 1) {
            System.out.print("Missing db filename argument!\n");
            System.exit(1);
        }
        System.out.printf("MAD version %s\n", MADVERSION);
        try {
            REPL repl = new REPL(args[0], System.in, System.out);
            repl.run();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }

    private enum MetaCommandResult {

        Correct,
        Exit,
        UnrecognizedCommand
    }

}
