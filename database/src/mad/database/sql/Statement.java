package mad.database.sql;

/**
 *
 */
public class Statement {
    private boolean okStatement = false;
    
    public Statement(String query) {
        // tokenize
        // parse
        
        // temp
        if(query.length() > 5) {
            switch(query.substring(0, 6).toLowerCase()) {
                case "select":
                case "insert":
                case "update":
                case "delete":
                    okStatement = true;
                    break;
            }
        }
    }
    
    public boolean statementIsOK() {
        return okStatement;
    }
    
    public ExecuteStatmentResult execute() {
        if(okStatement)
            return ExecuteStatmentResult.Success;
        else
            return ExecuteStatmentResult.Error;
    }
    
    public enum ExecuteStatmentResult {
        Success,
        Error
    }
}
