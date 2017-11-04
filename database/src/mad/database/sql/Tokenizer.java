package mad.database.sql;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 */
public class Tokenizer implements Iterable<Tokenizer.Token> {

    private ArrayList<Token> tokens;
    private int index;
    private final String tokenStr;

    public Tokenizer(String tokenStr) {
        tokens = new ArrayList<>();
        this.tokenStr = tokenStr;
        index = 0;
        while (index < tokenStr.length()) {
            char c = tokenStr.charAt(index);
            if (c == ' ' || c == '\t' || c == '\n') {
                index++;
            } else if ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_".indexOf(c) != -1) {
                if(parseKeyword("select"))continue;
                if(parseKeyword("insert"))continue;
                if(parseKeyword("update"))continue;
                if(parseKeyword("delete"))continue;
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
            } else if (c == '"') {
                parseString();
            } else if ("0123456789".indexOf(c) != -1) {
                parseNumber();
            } else if (c == ';') {
                tokens.add(new Token(Token.Type.Semicolon, null));
                index++;
            }else if (c == '(') {
                tokens.add(new Token(Token.Type.LParen, null));
                index++;
            }else if (c == ')') {
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
        while(index < tokenStr.length() && "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".indexOf(tokenStr.charAt(index)) != -1){
            index++;
        }
        tokens.add(new Token(Token.Type.ID,tokenStr.substring(start, index)));
    }

    private void parseString() {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public Iterator<Token> iterator() {
        return tokens.iterator();
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
}
