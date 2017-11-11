package mad.database.sql.ast;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class StatementList implements Statement{
    List<Statement> statements;
    
    public StatementList(){
        statements = new ArrayList<>();
    }
    
    public void append(Statement s){
        statements.add(s);
    }
}
