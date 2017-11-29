package mad.database.sql;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Objects;
import java.util.Queue;
import mad.database.sql.Tokenizer.Token;

/**
 *
 */
public class Tokenizer implements Iterator<Token> {

    private Queue<Token> tokens;
    private int index;
    private String tokenStr;

    /**
     *
     */
    public Tokenizer() {
        tokens = new ArrayDeque<>();
        this.tokenStr = null;
        index = 0;
    }

    /**
     *
     * @param tokenStr
     * @throws mad.database.sql.Tokenizer.TokenizeException
     */
    public void tokenize(String tokenStr) throws TokenizeException {
        this.tokenStr = tokenStr;
        index = 0;
        while (index < length()) {
            char c = charAt(index);
            if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                index++;
            } else if (c == '-' && index + 1 < length() && charAt(index + 1) == '-') {
                parseComment();
            } else if (isAZazUnderscore(c)) {
                if (parseKeywords()) {
                    continue;
                }
                if (index + 4 <= length() && "true".equalsIgnoreCase(tokenStr.substring(index, index + 4))) {
                    tokens.add(new Token(Token.TokenType.Boolean, "true"));
                    index += 4;
                    continue;
                }
                if (index + 5 <= length() && "false".equalsIgnoreCase(tokenStr.substring(index, index + 5))) {
                    tokens.add(new Token(Token.TokenType.Boolean, "false"));
                    index += 5;
                    continue;
                }
                parseId();
            } else if (c == '\"' || c == '\'') {
                parseString();
            } else if (isDigit(c)) {
                parseNumber();
            } else if (index + 1 < length() && parseDoubleCharOperators(c, charAt(index + 1))) {
                index += 2;
            } else if (parseSingleCharOperators(c)) {
                index++;
            } else {
                throw new TokenizeException("Unknow character '" + charAt(index) + "'.", index);
            }

        }
    }

    /**
     *
     * @return
     */
    @Override
    public boolean hasNext() {
        return !tokens.isEmpty();
    }

    /**
     *
     * @return
     */
    @Override
    public Token next() {
        return tokens.poll();
    }

    private boolean parseDoubleCharOperators(char c1, char c2) {
        if (c1 == '=' && c2 == '=') {
            tokens.add(new Token(Token.TokenType.DoubleEquals));
            return true;
        }
        if (c1 == '!' && c2 == '=') {
            tokens.add(new Token(Token.TokenType.NotEquals));
            return true;
        }
        if (c1 == '<' && c2 == '>') {
            tokens.add(new Token(Token.TokenType.NotEquals));
            return true;
        }
        if (c1 == '>' && c2 == '=') {
            tokens.add(new Token(Token.TokenType.GreaterThanOrEquals));
            return true;
        }
        if (c1 == '<' && c2 == '=') {
            tokens.add(new Token(Token.TokenType.LessThanOrEquals));
            return true;
        }
        if (c1 == '<' && c2 == '<') {
            tokens.add(new Token(Token.TokenType.LeftShift));
            return true;
        }
        if (c1 == '>' && c2 == '>') {
            tokens.add(new Token(Token.TokenType.RightShift));
            return true;
        }
        if (c1 == '|' && c2 == '|') {
            tokens.add(new Token(Token.TokenType.Concat));
            return true;
        }
        return false;
    }

    private boolean parseSingleCharOperators(char c) {
        switch (c) {
            case ';':
                tokens.add(new Token(Token.TokenType.Semicolon));
                return true;
            case '(':
                tokens.add(new Token(Token.TokenType.LParen));
                return true;
            case ')':
                tokens.add(new Token(Token.TokenType.RParen));
                return true;
            case '=':
                tokens.add(new Token(Token.TokenType.Equals));
                return true;
            case '>':
                tokens.add(new Token(Token.TokenType.GreaterThan));
                return true;
            case '<':
                tokens.add(new Token(Token.TokenType.LessThan));
                return true;
            case '&':
                tokens.add(new Token(Token.TokenType.And));
                return true;
            case '|':
                tokens.add(new Token(Token.TokenType.Or));
                return true;
            case ',':
                tokens.add(new Token(Token.TokenType.Comma));
                return true;
            case '.':
                tokens.add(new Token(Token.TokenType.Dot));
                return true;
            case '*':
                tokens.add(new Token(Token.TokenType.Star));
                return true;
            case '/':
                tokens.add(new Token(Token.TokenType.Slash));
                return true;
            case '+':
                tokens.add(new Token(Token.TokenType.Plus));
                return true;
            case '-':
                tokens.add(new Token(Token.TokenType.Minus));
                return true;
            case '%':
                tokens.add(new Token(Token.TokenType.Modulo));
                return true;
            default:
                return false;
        }
    }

    private void parseId() {
        int start = index;
        while (index < length() && isAZazDigitUnderscore(charAt(index))) {
            index++;
        }
        tokens.add(new Token(Token.TokenType.ID, tokenStr.substring(start, index)));
    }

    private void parseComment() {
        while (index < length() && charAt(index) != '\n') {
            index++;
        }
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
            tokens.add(new Token(Token.TokenType.StringID, builder.toString()));
        } else {
            tokens.add(new Token(Token.TokenType.Text, builder.toString()));
        }
    }

    private void parseNumber() throws TokenizeException {
        StringBuilder builder = new StringBuilder();
        boolean dotFound = false;

        if (charAt(index) == '0') {
            if (index + 1 == length() || charAt(index + 1) != '.') {
                tokens.add(new Token(Token.TokenType.Integer, "0"));
                index++;
                return;
            } else if (index + 2 < length() && charAt(index + 1) == '.' && isDigit(charAt(index + 2))) {
                builder.append("0.");
                dotFound = true;
                builder.append(charAt(index + 2));
                index += 3;
            } else {
                throw new TokenizeException("Could not parse number!", index);
            }
        }

        while (index < length()) {
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
            tokens.add(new Token(Token.TokenType.Float, builder.toString()));
        } else {
            tokens.add(new Token(Token.TokenType.Integer, builder.toString()));
        }
    }

    private boolean parseKeywords() {
        if (parseKeyword("is not")) {
            // must run first to prevent matching with 'is'.
            return true;
        }
        String[] keywords = {
            "add",
            "all",
            "alter",
            "and",
            "as",
            "asc",
            "ascending",
            "between",
            "by",
            "case",
            "coalesce",
            "column",
            "create",
            "cross",
            "data",
            "delete",
            "desc",
            "descending",
            "distinct",
            "drop",
            "else",
            "end",
            "except",
            "exists",
            "from",
            "full",
            "group",
            "having",
            "in",
            "inner",
            "insert",
            "intersect",
            "into",
            "is",
            "join",
            "left",
            "like",
            "limit",
            "natural",
            "not",
            "null",
            "on",
            "or",
            "order",
            "outer",
            "rename",
            "right",
            "select",
            "set",
            "table",
            "then",
            "truncate",
            "type",
            "union",
            "using",
            "update",
            "values",
            "when",
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
        if (index + length > length()) {
            return false;
        }
        if (index + length < length() && isAZazDigitUnderscore(charAt(index + length))) {
            return false;
        }
        if (keyword.equalsIgnoreCase(tokenStr.substring(index, index + length))) {
            tokens.add(new Token(tokenTypeFromKeyword(keyword)));
            index += keyword.length();
            return true;
        }
        return false;
    }

    private Token.TokenType tokenTypeFromKeyword(String keyword) {
        keyword = keyword.toLowerCase();
        for (Token.TokenType type : Token.TokenType.values()) {
            if (type.stringValue().equals(keyword)) {
                return type;
            }
        }
        if ("asc".equals(keyword)) {
            return Token.TokenType.Ascending;
        }
        if ("desc".equals(keyword)) {
            return Token.TokenType.Descending;
        }
        return null;
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

    //----------------------------------------------------------------------------------------------
    // Token Class
    //----------------------------------------------------------------------------------------------
    /**
     *
     */
    public static class Token {

        public final TokenType type;
        public final String value;

        public Token(TokenType type) {
            this.type = type;
            this.value = null;
        }

        public Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }

        public enum TokenType {

            // Keywords:
            Add("add"),
            All("all"),
            Alter("alter"),
            And("and"),
            As("as"),
            Ascending("ascending"),
            Between("between"),
            By("by"),
            Case("case"),
            Coalesce("coalesce"),
            Column("column"),
            Create("create"),
            Cross("cross"),
            Data("data"),
            Delete("delete"),
            Descending("descending"),
            Distinct("distinct"),
            Drop("drop"),
            Else("else"),
            End("end"),
            Except("except"),
            Exists("exists"),
            From("from"),
            Full("full"),
            Group("group"),
            Having("having"),
            In("in"),
            Inner("inner"),
            Insert("insert"),
            Intersect("intersect"),
            Into("into"),
            Is("is"),
            IsNot("is not"),
            Join("join"),
            Left("left"),
            Like("like"),
            Limit("limit"),
            Natural("natural"),
            Not("not"),
            Null("null"),
            On("on"),
            Or("or"),
            Order("order"),
            Outer("outer"),
            Rename("rename"),
            Right("right"),
            Select("select"),
            Set("set"),
            Table("table"),
            Then("then"),
            Truncate("truncate"),
            Type("type"),
            Union("union"),
            Update("update"),
            Using("using"),
            Values("values"),
            When("when"),
            Where("where"),
            // Types
            ID, // [A-Za-z][A-Za-z0-9_]*
            StringID, // Text surrounded or '"'
            Integer, // '0' | [1-9][0-9]*
            Float, // [0-9][0-9]*.[0-9]*
            Boolean, // 'true'|'false'
            Text, // Text surrounded or '''
            // Symbols
            Semicolon(";"),
            LParen("("),
            RParen(")"),
            Comma(","),
            Dot("."),
            Equals("="),
            DoubleEquals("=="),
            NotEquals("<>"),
            GreaterThan(">"),
            GreaterThanOrEquals(">="),
            LessThan("<"),
            LessThanOrEquals("<="),
            Star("*"),
            Slash("/"),
            Plus("+"),
            Minus("-"),
            Modulo("%"),
            LeftShift("<<"),
            RightShift(">>"),
            Concat("||");

            private final String string;

            private TokenType() {
                this.string = "";
            }

            private TokenType(String string) {
                this.string = string;
            }

            public String stringValue() {
                return string;
            }
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
        public int hashCode() {
            int hash = 7;
            hash = 71 * hash + Objects.hashCode(this.type);
            hash = 71 * hash + Objects.hashCode(this.value);
            return hash;
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

    //----------------------------------------------------------------------------------------------
    // Exception
    //----------------------------------------------------------------------------------------------
    /**
     *
     */
    public static class TokenizeException extends Exception {

        private final int index;

        /**
         *
         * @param message
         * @param index
         */
        private TokenizeException(String message, int index) {
            super(message);
            this.index = index;
        }

        /**
         *
         * @return
         */
        public int getIndex() {
            return index;
        }
    }

}
