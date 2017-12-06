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

    private File pwd;

    public REPL(InputStream in, OutputStream out) {
        this.in = new BufferedReader(new InputStreamReader(in));
        this.out = new PrintWriter(out, true);
        this.pwd = Paths.get("").toFile();
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
        for (int i=strArray.length-1; i >0;i--) {
            if (strArray[i] != ' ' && strArray[i] != '\t' && strArray[i] != '\n') {
                return i;
            }
        }
        return 0;
    }

    private boolean matchesCommand(String query, String command){
        if(query.length() < command.length()){
            return false;
        }
        if(!query.substring(0, command.length()).equals(command)){
            return false;
        }
        if(query.length() >command.length()){
            return query.charAt(command.length()) == ' ' || query.charAt(command.length()) == '\t';
        }else{
            return true;
        }
    }

    private void cd(String query){
        query = query.substring(3);
        if(query.length()==0){
            return;
        }
        query = query.substring(firstNonWhitespace(query));
        query = query.substring(0,lastNonWhitespace(query)+1);
        if(query.charAt(0) == '"' && query.charAt(query.length()-1) == '"'){
            query = query.substring(1, query.length()-1);
        }
        if(query.equals("..")){
            pwd = pwd.getAbsoluteFile().getParentFile();
            return;
        }
        if(query.equals("~")){
            pwd = new File(System.getProperty("user.home"));
            return;
        }
        File[] files = pwd.getAbsoluteFile().listFiles();
        for(File file: files){
            if(file.getName().equals(query)){
                if(file.isDirectory()){
                    pwd = file;
                    return;
                }else{
                    out.println("'" + query + "' is not a directory.");
                    return;
                }
            }
        }
        out.println("No directory with name '" + query + "'.");
    }

    private void ls(){
        File[] files = pwd.getAbsoluteFile().listFiles();
        for(File file: files){
            if(!file.isHidden()){
                out.println(file.getName());
            }
        }
    }

    private MetaCommandResult runMetaCommand(String query) {
        query = query.substring(firstNonWhitespace(query));
        if (matchesCommand(query,".cd")) {
            cd(query);
            return MetaCommandResult.Correct;
        }
        if (matchesCommand(query,".exit")) {
            return MetaCommandResult.Exit;
        }
        if (matchesCommand(query,".help")) {
            out.print(".cd      Change working directory.\n");
            out.print(".exit    Exit this program.\n");
            out.print(".help    Show available commands.\n");
            out.print(".ls      List files in directory.\n");
            out.print(".pwd     Print working directory.\n");
            out.print(".version Show version number.\n");
            return MetaCommandResult.Correct;
        }
        if (matchesCommand(query,".ls")) {
            ls();
            return MetaCommandResult.Correct;
        }
        if (matchesCommand(query,".pwd")) {
            out.printf(pwd.getAbsolutePath()+ "%n");
            return MetaCommandResult.Correct;
        }
        if (matchesCommand(query,".version")) {
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
        System.out.printf("MAD version %s\n", MADVERSION);
        REPL repl = new REPL(System.in, System.out);
        repl.run();
    }

    private enum MetaCommandResult {

        Correct,
        Exit,
        UnrecognizedCommand
    }

}
