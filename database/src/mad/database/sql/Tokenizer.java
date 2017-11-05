package mad.database.sql;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
import mad.database.sql.Tokenizer.Token;

/**
 *
 */
public class Tokenizer implements Iterator<Token>{

    private Queue<Token> tokens;
    private int index;
    private String tokenStr;

    public Tokenizer(){
        tokens = new ArrayDeque<>();
        this.tokenStr = null;
        index = 0;
    }
        
    public void tokenize(String tokenStr) throws TokenizeException {
        this.tokenStr = tokenStr;
        index = 0;
        while (index < tokenStr.length()) {
            char c = tokenStr.charAt(index);
            if (c == ' ' || c == '\t' || c == '\n') {
                index++;
            } else if ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_".indexOf(c) != -1) {
                if (parseKeyword("select")) {
                    continue;
                }
                if (parseKeyword("insert")) {
                    continue;
                }
                if (parseKeyword("update")) {
                    continue;
                }
                if (parseKeyword("delete")) {
                    continue;
                }
                if (index + 4 <= tokenStr.length() && "true".equalsIgnoreCase(tokenStr.substring(index, index + 4))) {
                    tokens.add(new Token(Token.Type.Boolean, "true"));
                    index += 4;
                    continue;
                }
                if (index + 5 <= tokenStr.length() && "false".equalsIgnoreCase(tokenStr.substring(index, index + 5))) {
                    tokens.add(new Token(Token.Type.Boolean, "false"));
                    index += 5;
                    continue;
                }
                parseId();
            } else if (c == '\"' || c == '\'') {
                parseString();
            } else if ("0123456789".indexOf(c) != -1) {
                parseNumber();
            } else if (c == ';') {
                tokens.add(new Token(Token.Type.Semicolon, null));
                index++;
            } else if (c == '(') {
                tokens.add(new Token(Token.Type.LParen, null));
                index++;
            } else if (c == ')') {
                tokens.add(new Token(Token.Type.RParen, null));
                index++;
            } else if (c == '*') {
                tokens.add(new Token(Token.Type.Star, null));
                index++;
            } else if (c == '/') {
                tokens.add(new Token(Token.Type.Slash, null));
                index++;
            } else if (c == '+') {
                tokens.add(new Token(Token.Type.Plus, null));
                index++;
            } else if (c == '-') {
                tokens.add(new Token(Token.Type.Minus, null));
                index++;
            }

        }
    }

    private boolean parseKeyword(String keyword) {
        int length = keyword.length();
        if (index + length <= tokenStr.length()
                && keyword.equalsIgnoreCase(tokenStr.substring(index, index + length))
                && "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".indexOf(tokenStr.charAt(index + length)) == -1) {
            tokens.add(new Token(tokenTypeFromKyeword(keyword), null));
            index += 6;
            return true;
        }
        return false;
    }

    private Token.Type tokenTypeFromKyeword(String keyword) {
        keyword = keyword.toLowerCase();
        switch (keyword) {
            case "select":
                return Token.Type.Select;
            case "insert":
                return Token.Type.Insert;
            case "delete":
                return Token.Type.Delete;
            case "update":
                return Token.Type.Update;
        }
        return null;
    }

    private void parseId() {
        int start = index;
        while (index < tokenStr.length() && "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".indexOf(tokenStr.charAt(index)) != -1) {
            index++;
        }
        tokens.add(new Token(Token.Type.ID, tokenStr.substring(start, index)));
    }

    private void parseString() throws TokenizeException {
        char mark = tokenStr.charAt(index);
        index++;
        StringBuilder builder = new StringBuilder();
        
        while (true) {
            if (index + 1 < tokenStr.length() && tokenStr.charAt(index) == mark && tokenStr.charAt(index + 1) != mark) {
                index++;
                break;
            } else if (index + 1 == tokenStr.length() && tokenStr.charAt(index) == mark) {
                index++;
                break;
            } else if (index == tokenStr.length()) {
                throw new TokenizeException("Failed to tokenize string!",index);
            }else{
                if(tokenStr.charAt(index)==mark){
                    index++;
                }
                builder.append(tokenStr.charAt(index));
                index++;
            }
        }
        
        tokens.add(new Token(Token.Type.Text,builder.toString()));
    }

    private void parseNumber() throws TokenizeException {
        StringBuilder builder = new StringBuilder();
        boolean dotFound = false;
        
        if(tokenStr.charAt(index) == '0'){
            if(index+1 == tokenStr.length() || tokenStr.charAt(index+1) != '.'){
                tokens.add(new Token(Token.Type.Integer, "0"));
            }else if(index +2 <tokenStr.length() && tokenStr.charAt(index+1) == '.' && "0123456789".indexOf(tokenStr.charAt(index+2)) != -1){
                builder.append("0.");
                dotFound = true;
                builder.append(tokenStr.charAt(index+2));
                index+=3;
            }else{
                throw new TokenizeException("Could not parse number!",index);
            }
        }
        
        while(true){
            if(tokenStr.charAt(index) == '.'){
                if(dotFound){
                    throw new TokenizeException("Could not parse number!", index);
                }else{
                    builder.append('.');
                    dotFound = true;
                    index++;
                    continue;
                }
            }
            if("0123456789".indexOf(tokenStr.charAt(index)) != -1){
                builder.append(tokenStr.charAt(index));
                index++;
            }else{
                break;
            }
        }
        
        if(dotFound){
            tokens.add(new Token(Token.Type.Float, builder.toString()));
        }else{
            tokens.add(new Token(Token.Type.Integer, builder.toString()));
        }
    }

    @Override
    public boolean hasNext() {
        return !tokens.isEmpty();
    }

    @Override
    public Token next() {
        return tokens.poll();
    }

    public static class Token {

        public final Type type;
        public final String value;

        public Token(Type type) {
            this.type = type;
            this.value = null;
        }

        public Token(Type type, String value) {
            this.type = type;
            this.value = value;
        }

        public enum Type {
            // Keywords:
            Select, // 'select'
            Insert, // 'insert'
            Delete, // 'delete'
            Update, // 'update'

            // Types
            ID, // [A-Za-z][A-Za-z0-9_]*
            Integer, // '0' | [1-9][0-9]*
            Float, // [0-9][0-9]*.[0-9]*
            Boolean, // 'true'|'false'
            Text, // Text surrounded by '"'

            // Symbols
            Semicolon, // ';'
            LParen, // '('
            RParen, // ')'
            Star, // '*'
            Slash, // '/'
            Plus, // '+'
            Minus, // '-'

        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Token) {
                Token tobj = (Token) obj;
                if (tobj.type == this.type) {
                    if (tobj.value == null && this.value == null) {
                        return true;
                    }
                    if (tobj.value != null && this.value != null && tobj.value.equals(this.value)) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        @Override
        public String toString(){
            StringBuilder builder = new StringBuilder();
            builder.append("(");
            builder.append(type.toString());
            if(value != null){
                builder.append(",").append(value);
            }
            builder.append(")");
            return builder.toString();
        }
    }

    public static class TokenizeException extends Exception {

        private final int index;
        
        private TokenizeException(String message,int index) {
            super(message);
            this.index = index;
        }
        
        public int getIndex(){
            return index;
        }
    }

}
