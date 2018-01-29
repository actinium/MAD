package mad.database.sql;

import java.util.ArrayList;
import java.util.List;
import mad.database.sql.Tokenizer.Token.TokenType;
import mad.database.sql.ast.Statement;
import mad.database.sql.ast.Tables.JoinedTables;
import mad.database.sql.ast.Tables.SingleTable;
import mad.database.sql.ast.Tables.SubSelect;
import mad.database.sql.ast.Tables.SubTable;
import mad.database.sql.ast.Tables.Table;
import mad.database.sql.ast.expression.Expression;
import mad.database.sql.ast.selection.Columns.Column;
import mad.database.sql.ast.selection.Columns.ExprColumn;
import mad.database.sql.ast.selection.Columns.StarColumn;
import mad.database.sql.ast.selection.CompundSelectStatement;
import mad.database.sql.ast.selection.Order;
import mad.database.sql.ast.selection.SelectStatement;
import mad.database.sql.ast.selection.SelectionLimitStatement;
import mad.database.sql.ast.selection.SelectionOrderStatement;
import mad.database.sql.ast.selection.SimpleSelectStatement;

/**
 *
 */
public class SelectParser {

    private final Parser parser;

    public SelectParser(Parser parser) {
        this.parser = parser;
    }

    public Statement parse() throws Parser.ParseError {
        return parse(TokenType.Semicolon);
    }

    private SelectStatement parse(TokenType endToken) throws Parser.ParseError {
        SelectStatement statement = null;
        do {
            if (statement == null) {
                statement = parseSelect();
                if (statement == null) {
                    return null;
                }
            } else {
                CompundSelectStatement.Operator op;

                if (parser.accept(TokenType.Union)) {
                    if (parser.accept(TokenType.All)) {
                        op = CompundSelectStatement.Operator.UnionAll;
                    } else {
                        op = CompundSelectStatement.Operator.Union;
                    }
                } else if (parser.accept(TokenType.Intersect)) {
                    if (parser.accept(TokenType.All)) {
                        op = CompundSelectStatement.Operator.IntersectAll;
                    } else {
                        op = CompundSelectStatement.Operator.Intersect;
                    }
                } else if (parser.accept(TokenType.Except)) {
                    op = CompundSelectStatement.Operator.Except;
                } else {
                    throw parser.error("SelectParser.parse: Expected Set Operator");
                }
                SelectStatement secondStatement = parseSelect();
                if (secondStatement == null) {
                    throw parser.error("SelectParser.parse: Expected Select Statement after Set Operator");
                }
                statement = new CompundSelectStatement(statement, op, secondStatement);
            }
            // Order By:
            if (parser.accept(TokenType.Order)) {
                parser.expect(TokenType.By);
                List<Order> order = new ArrayList<>();
                do {
                    Expression exp = parser.parseExpression();
                    if (parser.accept(TokenType.Ascending)) {
                        order.add(new Order(exp, true));
                    } else if (parser.accept(TokenType.Descending)) {
                        order.add(new Order(exp, false));
                    } else {
                        order.add(new Order(exp));
                    }
                } while (parser.accept(TokenType.Comma));
                statement = new SelectionOrderStatement(statement, order);
            }
            // Limit & Offset:
            if (parser.accept(TokenType.Limit)) {
                Expression limit = parser.parseExpression();
                if (parser.accept(TokenType.Offset)) {
                    Expression offset = parser.parseExpression();
                    statement = new SelectionLimitStatement(statement, limit, offset);
                } else {
                    statement = new SelectionLimitStatement(statement, limit);
                }
            }
        } while (!parser.accept(endToken));
        return statement;
    }

    private SelectStatement parseSelect() throws Parser.ParseError {

        if (!parser.accept(TokenType.Select)) {
            return null;
        }

        // Distinct:
        boolean distinct;
        if (parser.accept(TokenType.Distinct)) {
            distinct = true;
        } else if (parser.accept(TokenType.All)) {
            distinct = false;
        } else {
            distinct = false;
        }

        // Result Columns:
        List<Column> results = new ArrayList<>();
        do {
            Column resultColumn = parseResultColumn();
            results.add(resultColumn);
        } while (parser.accept(TokenType.Comma));

        // From Tables:
        Table tables = null;
        if (parser.accept(TokenType.From)) {
            tables = parseTables();
        }

        // Where Condition:
        Expression condition = null;
        if (parser.accept(TokenType.Where)) {
            condition = parser.parseExpression();
        }

        // Group By:
        List<Expression> groupBy = null;
        Expression havingCondition = null;
        if (parser.accept(TokenType.Group)) {
            parser.expect(TokenType.By);
            groupBy = new ArrayList<>();
            do {
                groupBy.add(parser.parseExpression());
            } while (parser.accept(TokenType.Comma));
            if (parser.accept(TokenType.Having)) {
                havingCondition = parser.parseExpression();
            }
        }

        return new SimpleSelectStatement(distinct, results, tables, condition, groupBy, havingCondition);
    }

    private Column parseResultColumn() throws Parser.ParseError {
        if (parser.accept(TokenType.Star)) {
            return new StarColumn();
        }
        int savedIndex = parser.getIndex();
        if (parser.accept(TokenType.ID) || parser.accept(TokenType.StringID)) {
            String colName = parser.value();
            if (parser.accept(TokenType.Dot)) {
                if (parser.accept(TokenType.Star)) {
                    return new StarColumn(colName);
                }
            }
        }
        parser.setIndex(savedIndex);
        Expression columExpression = parser.parseExpression();
        if (parser.accept(TokenType.As)) {
            return new ExprColumn(columExpression, parser.parseIdentifier());
        } else {
            return new ExprColumn(columExpression);
        }
    }

    private Table parseTables() throws Parser.ParseError {
        Table table = null;
        boolean done = false;
        do {
            if (table == null) {
                table = parseTable();
            } else {
                done = true;
                if (parser.accept(TokenType.Comma)
                        || (parser.accept(TokenType.Cross) && parser.expect(TokenType.Join))) {
                    // CrossJoin
                    table = new JoinedTables(table, parseTable(), JoinedTables.Join.CrossJoin);
                    done = false;

                } else if (parser.accept(TokenType.Natural)) {
                    // Natural Join
                    parser.expect(TokenType.Join);
                    table = new JoinedTables(table, parseTable(), JoinedTables.Join.NatrualJoin);
                    done = false;

                } else if (parser.accept(TokenType.Join)
                        || (parser.accept(TokenType.Inner) && parser.expect(TokenType.Join))) {
                    // Inner Join
                    Table table2 = parseTable();
                    if (parser.accept(TokenType.On)) {
                        Expression condition = parser.parseExpression();
                        table = new JoinedTables(table, table2, JoinedTables.Join.InnerJoin, condition);
                        done = false;

                    } else if (parser.accept(TokenType.Using)) {
                        List<String> using = parseUsing();
                        table = new JoinedTables(table, table2, JoinedTables.Join.InnerJoin, using);
                        done = false;

                    } else {
                        table = new JoinedTables(table, table2, JoinedTables.Join.CrossJoin);
                        done = false;
                    }
                } else if (parser.accept(TokenType.Left)) {
                    // Left Outer Join
                    parser.accept(TokenType.Outer);
                    parser.expect(TokenType.Join);
                    Table table2 = parseTable();
                    if (parser.accept(TokenType.On)) {
                        Expression condition = parser.parseExpression();
                        table = new JoinedTables(table, table2, JoinedTables.Join.LeftOuterJoin, condition);
                        done = false;

                    } else if (parser.accept(TokenType.Using)) {
                        List<String> using = parseUsing();
                        table = new JoinedTables(table, table2, JoinedTables.Join.LeftOuterJoin, using);
                        done = false;

                    }

                } else if (parser.accept(TokenType.Right)) {
                    // Right Outer Join
                    parser.accept(TokenType.Outer);
                    parser.expect(TokenType.Join);
                    Table table2 = parseTable();
                    if (parser.accept(TokenType.On)) {
                        Expression condition = parser.parseExpression();
                        table = new JoinedTables(table, table2, JoinedTables.Join.RightOuterJoin, condition);
                        done = false;

                    } else if (parser.accept(TokenType.Using)) {
                        List<String> using = parseUsing();
                        table = new JoinedTables(table, table2, JoinedTables.Join.RightOuterJoin, using);
                        done = false;

                    }

                } else if (parser.accept(TokenType.Full)) {
                    // Full Outer Join
                    parser.accept(TokenType.Outer);
                    parser.expect(TokenType.Join);
                    Table table2 = parseTable();
                    if (parser.accept(TokenType.On)) {
                        Expression condition = parser.parseExpression();
                        table = new JoinedTables(table, table2, JoinedTables.Join.FullOuterJoin, condition);
                        done = false;

                    } else if (parser.accept(TokenType.Using)) {
                        List<String> using = parseUsing();
                        table = new JoinedTables(table, table2, JoinedTables.Join.FullOuterJoin, using);
                        done = false;

                    }

                }
            }
        } while (!done);
        return table;
    }

    private Table parseTable() throws Parser.ParseError {
        Table table = null;
        if (parser.accept(TokenType.LParen)) {
            if (parser.currentToken().type == TokenType.Select) {
                SelectStatement statement = parse(TokenType.RParen);
                if (parser.accept(TokenType.As)) {
                    table = new SubSelect(statement, parser.parseIdentifier());
                } else {
                    table = new SubSelect(statement);
                }
            } else {
                table = parseTables();
                parser.expect(TokenType.RParen);
                if (parser.accept(TokenType.As)) {
                    table = new SubTable(table, parser.parseIdentifier());
                }
            }
        } else {
            String tableName = parser.parseIdentifier();
            if (parser.accept(TokenType.As)) {
                String as = parser.parseIdentifier();
                table = new SingleTable(tableName, as);
            } else {
                table = new SingleTable(tableName);
            }
        }
        return table;
    }

    private List<String> parseUsing() throws Parser.ParseError {
        parser.accept(TokenType.LParen);
        List<String> using = new ArrayList<>();
        do {
            using.add(parser.parseIdentifier());
        } while (parser.accept(TokenType.Comma));
        parser.accept(TokenType.RParen);
        return using;
    }
}
