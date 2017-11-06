package mad.database.sql;

import mad.database.sql.Tokenizer.Token;

/**
 *
 */
public class Parser {
    
    public Parser(){
        
    }
    
    private void parse(Token token){
        // TODO
    }
    
    public void parse(Tokenizer tokenizer){
        while(tokenizer.hasNext()){
            parse(tokenizer.next());
        }
    }
}
