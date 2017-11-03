package mad.database.sql;

/**
 *
 */
public class Statement {
    private boolean okStatement;
    
    public Statement(String query) {
        // tokenize
        // parse
        
        // temp
        switch(query.substring(0, 6).toLowerCase()) {
            case "select":
            case "insert":
            case "update":
            case "delete":
                okStatement = true;
                break;
            default:
                okStatement= false;
                break;
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
