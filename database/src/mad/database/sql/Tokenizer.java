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
                throw new TokenizeException("Failed to tokenize string!");
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

    private void parseNumber() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void parseFloat() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void parseInteger() {
        throw new UnsupportedOperationException("Not supported yet.");
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

            Select, // 'select'
            Insert, // 'insert'
            Delete, // 'delete'
            Update, // 'update'
            ID, // [A-Za-z][A-Za-z0-9_]*
            Integer, // '0' | [1-9][0-9]*
            Float, // [0-9][0-9]*.[0-9]*
            Boolean, // 'true'|'false'
            Text, // Text surrounded by '"'
            Semicolon, // ';'
            LParen, // '('
            RParen // ')'
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
    }

    public static class TokenizeException extends Exception {

        private TokenizeException(String message) {
            super(message);
        }
    }

}
