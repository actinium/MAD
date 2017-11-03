package mad.database.sql;

import com.sun.media.jfxmedia.effects.EqualizerBand;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 */
public class Tokenizer implements Iterable<Tokenizer.Token>{

    private ArrayList<Token> tokens;
    private int index;
    
    public Tokenizer(String tokenStr){
        tokens = new ArrayList<>();
        index = 0;
        while( index < tokenStr.length() ){
            char c = tokenStr.charAt(index);
            if(c == ' ' || c == '\t' || c == '\n' ){
                index++;
            }else if("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".indexOf(c) != -1){
                if(index+6 <= tokenStr.length() && "select".equalsIgnoreCase(tokenStr.substring(index,index+6))){
                    tokens.add(new Token(Token.Type.Select, null));
                    index += 6;
                }
                if(index+6 <= tokenStr.length() && "insert".equalsIgnoreCase(tokenStr.substring(index,index+6))){
                    tokens.add(new Token(Token.Type.Insert, null));
                    index += 6;
                }
                if(index+6 <= tokenStr.length() && "delete".equalsIgnoreCase(tokenStr.substring(index,index+6))){
                    tokens.add(new Token(Token.Type.Delete, null));
                    index += 6;
                }
                if(index+6 <= tokenStr.length() && "update".equalsIgnoreCase(tokenStr.substring(index,index+6))){
                    tokens.add(new Token(Token.Type.Update, null));
                    index += 6;
                }
                if(index+4 <= tokenStr.length() && "true".equalsIgnoreCase(tokenStr.substring(index,index+4))){
                    tokens.add(new Token(Token.Type.Boolean, "true"));
                    index += 4;
                }
                if(index+5 <= tokenStr.length() && "false".equalsIgnoreCase(tokenStr.substring(index,index+5))){
                    tokens.add(new Token(Token.Type.Boolean, "false"));
                    index += 5;
                }
                parseId();
            }else if(c == '"'){
                parseString();
            }else if("0123456789".indexOf(c) != -1){
                parseNumber();
            }else if(c == ';'){
                tokens.add(new Token(Token.Type.Semicolon, null));
                index++;
            }
            
        }
    }
    
    private void parseId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private void parseString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private void parseNumber(){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private void parseFloat(){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private void parseInteger(){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Iterator<Token> iterator() {
        return tokens.iterator();
    }

    

    
    
    public static class Token{
        
        public final Type type;
        public final String value;
        
        public Token(Type type){
            this.type = type;
            this.value = null;
        }
        
        public Token(Type type,String value){
            this.type = type;
            this.value = value;
        }
        
        
        public enum Type{
            Select,  // 'select'
            Insert,  // 'insert'
            Delete,  // 'delete'
            Update,  // 'update'
            ID,      // [A-Za-z][A-Za-z0-9_]*
            Integer, // '0' | [1-9][0-9]*
            Float,   // [0-9][0-9]*.[0-9]*
            Boolean, // 'true'|'false'
            Text,    // Text surrounded by '"'
            Semicolon
        }
        
        @Override
        public boolean equals(Object obj){
            if(obj instanceof Token){
                Token tobj = (Token)obj;
                if(tobj.type == this.type){
                    if(tobj.value == null && this.value == null){
                        return true;
                    }
                    if(tobj.value != null && this.value != null && tobj.value.equals(this.value)){
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
