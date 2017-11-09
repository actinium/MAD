package mad.database.sql;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
import mad.database.sql.Tokenizer.Token;

/**
 *
 */
public class Tokenizer implements Iterator<Token> {

    private Queue<Token> tokens;
    private int index;
    private String tokenStr;

    public Tokenizer() {
        tokens = new ArrayDeque<>();
        this.tokenStr = null;
        index = 0;
    }

    public void tokenize(String tokenStr) throws TokenizeException {
        this.tokenStr = tokenStr;
        index = 0;
        while (index < length()) {
            char c = charAt(index);
            if (c == ' ' || c == '\t' || c == '\n') {
                index++;
            } else if (isAZazUnderscore(c)) {
                if (parseKeywords()) {
                    continue;
                }
                if (index + 4 <= length() && "true".equalsIgnoreCase(tokenStr.substring(index, index + 4))) {
                    tokens.add(new Token(Token.Type.Boolean, "true"));
                    index += 4;
                    continue;
                }
                if (index + 5 <= length() && "false".equalsIgnoreCase(tokenStr.substring(index, index + 5))) {
                    tokens.add(new Token(Token.Type.Boolean, "false"));
                    index += 5;
                    continue;
                }
                parseId();
            } else if (c == '\"' || c == '\'') {
                parseString();
            } else if (isDigit(c)) {
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
            } else if (c == '=') {
                tokens.add(new Token(Token.Type.Equals, null));
                index++;
            } else if (c == ',') {
                tokens.add(new Token(Token.Type.Comma, null));
                index++;
            } else if (c == '.') {
                tokens.add(new Token(Token.Type.Dot, null));
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
            } else {
                throw new TokenizeException("Unknow character '" + charAt(index) + "'.", index);
            }

        }
    }

    private boolean parseKeywords() {
        String[] keywords = {
            "by",
            "delete",
            "from",
            "group",
            "having",
            "insert",
            "order",
            "select",
            "update",
            "where",};
        for (String keyword : keywords) {
            if (parseKeyword(keyword)) {
                return true;
            }
        }
        return false;
    }

    private boolean parseKeyword(String keyword) {
        int length = keyword.length();
        if (index + length <= length()
                && keyword.equalsIgnoreCase(tokenStr.substring(index, index + length))
                && !isAZazDigitUnderscore(charAt(index + length))) {
            tokens.add(new Token(tokenTypeFromKeyword(keyword), null));
            index += keyword.length();
            return true;
        }
        return false;
    }

    private Token.Type tokenTypeFromKeyword(String keyword) {
        keyword = keyword.toLowerCase();
        switch (keyword) {
            case "by":
                return Token.Type.By;
            case "delete":
                return Token.Type.Delete;
            case "from":
                return Token.Type.From;
            case "group":
                return Token.Type.Group;
            case "having":
                return Token.Type.Having;
            case "insert":
                return Token.Type.Insert;
            case "order":
                return Token.Type.Order;
            case "select":
                return Token.Type.Select;
            case "update":
                return Token.Type.Update;
            case "where":
                return Token.Type.Where;
        }
        return null;
    }

    private void parseId() {
        int start = index;
        while (index < length() && isAZazDigitUnderscore(charAt(index))) {
            index++;
        }
        tokens.add(new Token(Token.Type.ID, tokenStr.substring(start, index)));
    }

    private void parseString() throws TokenizeException {
        char mark = charAt(index);
        index++;
        StringBuilder builder = new StringBuilder();

        while (true) {
            if (index + 1 < length() && charAt(index) == mark && charAt(index + 1) != mark) {
                index++;
                break;
            } else if (index + 1 == length() && charAt(index) == mark) {
                index++;
                break;
            } else if (index == length()) {
                throw new TokenizeException("Failed to tokenize string!", index);
            } else {
                if (charAt(index) == mark) {
                    index++;
                }
                builder.append(charAt(index));
                index++;
            }
        }
        if (mark == '"') {
            tokens.add(new Token(Token.Type.StringID, builder.toString()));
        } else {
            tokens.add(new Token(Token.Type.Text, builder.toString()));
        }
    }

    private void parseNumber() throws TokenizeException {
        StringBuilder builder = new StringBuilder();
        boolean dotFound = false;

        if (charAt(index) == '0') {
            if (index + 1 == length() || charAt(index + 1) != '.') {
                tokens.add(new Token(Token.Type.Integer, "0"));
            } else if (index + 2 < length() && charAt(index + 1) == '.' && isDigit(charAt(index + 2))) {
                builder.append("0.");
                dotFound = true;
                builder.append(charAt(index + 2));
                index += 3;
            } else {
                throw new TokenizeException("Could not parse number!", index);
            }
        }

        while (true) {
            if (charAt(index) == '.') {
                if (dotFound) {
                    throw new TokenizeException("Could not parse number!", index);
                } else {
                    builder.append('.');
                    dotFound = true;
                    index++;
                    continue;
                }
            }
            if (isDigit(charAt(index))) {
                builder.append(charAt(index));
                index++;
            } else {
                break;
            }
        }

        if (dotFound) {
            tokens.add(new Token(Token.Type.Float, builder.toString()));
        } else {
            tokens.add(new Token(Token.Type.Integer, builder.toString()));
        }
    }

    private boolean isDigit(char c) {
        return 0x30 <= c && c <= 0x39;
    }

    private boolean isAZaz(char c) {
        boolean large = 0x41 <= c && c <= 0x5A;
        boolean small = 0x61 <= c && c <= 0x7A;
        return small || large;
    }

    private boolean isAZazUnderscore(char c) {
        return isAZaz(c) || c == '_';
    }

    private boolean isAZazDigitUnderscore(char c) {
        return isAZazUnderscore(c) || isDigit(c);
    }

    private char charAt(int index) {
        return tokenStr.charAt(index);
    }

    private int length() {
        return tokenStr.length();
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
            By,
            Delete,
            From,
            Group,
            Having,
            Insert,
            Order,
            Select,
            Update,
            Where,
            // Types
            ID, // [A-Za-z][A-Za-z0-9_]*
            StringID, // Text surrounded or '"'
            Integer, // '0' | [1-9][0-9]*
            Float, // [0-9][0-9]*.[0-9]*
            Boolean, // 'true'|'false'
            Text, // Text surrounded or '''

            // Symbols
            Semicolon, // ';'
            LParen, // '('
            RParen, // ')'
            Comma, // ','
            Dot, // '.'
            Equals, // '='
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
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("(");
            builder.append(type.toString());
            if (value != null) {
                builder.append(",").append(value);
            }
            builder.append(")");
            return builder.toString();
        }
    }

    public static class TokenizeException extends Exception {

        private final int index;

        private TokenizeException(String message, int index) {
            super(message);
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

}
