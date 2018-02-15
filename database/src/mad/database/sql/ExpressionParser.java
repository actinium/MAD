package mad.database.sql;

import java.util.ArrayList;
import java.util.List;
import mad.database.sql.Tokenizer.Token.TokenType;
import mad.database.sql.ast.expression.BetweenExpression;
import mad.database.sql.ast.expression.BinaryExpression;
import mad.database.sql.ast.expression.CaseExpression;
import mad.database.sql.ast.expression.ColumnExpression;
import mad.database.sql.ast.expression.Expression;
import mad.database.sql.ast.expression.FunctionExpression;
import mad.database.sql.ast.expression.InSelectExpression;
import mad.database.sql.ast.expression.InValuesExpression;
import mad.database.sql.ast.expression.IsNullExpression;
import mad.database.sql.ast.expression.LikeExpression;
import mad.database.sql.ast.expression.NotExpression;
import mad.database.sql.ast.expression.RegexpExpression;
import mad.database.sql.ast.expression.SubExpression;
import mad.database.sql.ast.expression.UnaryExpression;
import mad.database.sql.ast.expression.ValueExpression;
import mad.database.sql.ast.expression.ValueExpression.Value;
import mad.database.sql.ast.selection.SelectStatement;

/**
 *
 */
public class ExpressionParser {

    private final Parser parser;
    private final SelectParser selectParser;

    public ExpressionParser(Parser parser, SelectParser selectParser) {
        this.parser = parser;
        this.selectParser = selectParser;
    }

    public Expression parse() throws Parser.ParseError {
        Expression expression = null;

        if (parser.accept(TokenType.LParen)) {
            expression = new SubExpression(this.parse());
            parser.expect(TokenType.RParen);
        }

        int savedIndex = parser.getIndex();
        if (expression == null) {
            expression = litteralValue();
        }
        if (expression == null) {
            parser.setIndex(savedIndex);
            expression = function();
        }
        if (expression == null) {
            parser.setIndex(savedIndex);
            expression = column();
        }
        if (expression == null) {
            parser.setIndex(savedIndex);
            expression = unaryOperation();
        }
        if (expression == null) {
            parser.setIndex(savedIndex);
            expression = cases();
        }

        if (expression != null) {
            savedIndex = parser.getIndex();
            Expression combinedExpression = concatOperation(expression);
            if (combinedExpression == null) {
                parser.setIndex(savedIndex);
                BinaryExpression binexp = binaryOperation(expression);
                if (binexp != null) {
                    binexp = binexp.adjustedForPrecedence();
                    combinedExpression = binexp;
                }
            }
            if (combinedExpression == null) {
                parser.setIndex(savedIndex);
                combinedExpression = isNull(expression);
            }
            if (combinedExpression == null) {
                parser.setIndex(savedIndex);
                combinedExpression = isNotNull(expression);
            }
            if (combinedExpression == null) {
                parser.setIndex(savedIndex);
                combinedExpression = between(expression);
            }
            if (combinedExpression == null) {
                parser.setIndex(savedIndex);
                combinedExpression = like(expression);
            }
            if (combinedExpression == null) {
                parser.setIndex(savedIndex);
                combinedExpression = regexp(expression);
            }
            if (combinedExpression == null) {
                parser.setIndex(savedIndex);
                combinedExpression = inValue(expression);
            }
            if (combinedExpression == null) {
                parser.setIndex(savedIndex);
                combinedExpression = inSelect(expression);
            }
            if (combinedExpression != null) {
                expression = combinedExpression;
            }
        }
        if (expression == null) {
            throw parser.error("ExpressionParser.parse: Couldn't parse Expression");
        }
        return expression;
    }

    private void adjustOperatorPrecedence(Expression expression) {

    }

    private ValueExpression litteralValue() {
        if (parser.accept(TokenType.Integer)) {
            return new ValueExpression(Value.Type.Integer, parser.value());
        } else if (parser.accept(TokenType.Float)) {
            return new ValueExpression(Value.Type.Float, parser.value());
        } else if (parser.accept(TokenType.Boolean)) {
            return new ValueExpression(Value.Type.Boolean, parser.value());
        } else if (parser.accept(TokenType.Text)) {
            return new ValueExpression(Value.Type.Text, parser.value());
        } else if (parser.accept(TokenType.Null)) {
            return ValueExpression.nullValue();
        } else {
            return null;
        }
    }

    private FunctionExpression function() throws Parser.ParseError {
        if (parser.accept(TokenType.ID)) {
            String funcName = parser.value();
            if (parser.accept(TokenType.LParen)) {
                List<Expression> argList = new ArrayList<>();
                do {
                    Expression exp = this.parse();
                    argList.add(exp);
                } while (parser.accept(TokenType.Comma));
                parser.expect(TokenType.RParen);
                return new FunctionExpression(funcName, argList);
            }
        }
        return null;
    }

    private ColumnExpression column() throws Parser.ParseError {
        if (parser.accept(TokenType.StringID) || parser.accept(TokenType.ID)) {
            String firstArg = parser.value();
            if (parser.accept(TokenType.Dot)) {
                if (parser.accept(TokenType.StringID) || parser.accept(TokenType.ID)) {
                    String secondArg = parser.value();
                    return new ColumnExpression(firstArg, secondArg);
                } else {
                    throw parser.error("expression.column: Could not parse column name.");
                }
            }
            if (parser.token().type == TokenType.StringID) {
                return new ColumnExpression(firstArg, true);
            }
            return new ColumnExpression(firstArg);
        }
        return null;
    }

    private UnaryExpression unaryOperation() throws Parser.ParseError {
        UnaryExpression.Operator op = null;
        if (parser.accept(TokenType.Plus)) {
            op = UnaryExpression.Operator.Plus;
        } else if (parser.accept(TokenType.Minus)) {
            op = UnaryExpression.Operator.Minus;
        } else if (parser.accept(TokenType.Tilde)) {
            op = UnaryExpression.Operator.BitwiseNot;
        } else if (parser.accept(TokenType.Exclamation)) {
            op = UnaryExpression.Operator.Not;
        }
        if (op != null) {
            Expression exp = this.parse();
            return new UnaryExpression(op, exp);
        }
        return null;
    }

    private Expression cases() throws Parser.ParseError {
        if (parser.accept(TokenType.Case)) {
            Expression expression = null;
            if (!parser.accept(TokenType.When)) {
                expression = parse();
                parser.expect(TokenType.When);
            }
            List<CaseExpression.Case> cases = new ArrayList<>();
            do {
                Expression condition = parse();
                parser.expect(TokenType.Then);
                Expression then = parse();
                cases.add(new CaseExpression.Case(condition, then));
            } while (parser.accept(TokenType.When));
            Expression defaultCase = null;
            if (parser.accept(TokenType.Else)) {
                defaultCase = parse();
            }
            parser.expect(TokenType.End);
            return new CaseExpression(expression, cases, defaultCase);
        }
        return null;
    }

    private Expression concatOperation(Expression leftExpression) throws Parser.ParseError {
        if (parser.accept(TokenType.Concat)) {
            Expression rightExpression = this.parse();
            return new BinaryExpression(leftExpression, BinaryExpression.Operator.Concat, rightExpression);
        }
        return null;
    }

    private BinaryExpression binaryOperation(Expression leftExpression) throws Parser.ParseError {
        BinaryExpression.Operator op = null;
        if (parser.accept(TokenType.Star)) {
            op = BinaryExpression.Operator.Multiply;
        } else if (parser.accept(TokenType.Slash)) {
            op = BinaryExpression.Operator.Divide;
        } else if (parser.accept(TokenType.Modulo)) {
            op = BinaryExpression.Operator.Modulo;
        } else if (parser.accept(TokenType.Plus)) {
            op = BinaryExpression.Operator.Plus;
        } else if (parser.accept(TokenType.Minus)) {
            op = BinaryExpression.Operator.Minus;
        } else if (parser.accept(TokenType.LeftShift)) {
            op = BinaryExpression.Operator.LeftShift;
        } else if (parser.accept(TokenType.RightShift)) {
            op = BinaryExpression.Operator.RightShift;
        } else if (parser.accept(TokenType.BitwiseAnd)) {
            op = BinaryExpression.Operator.BitwiseAnd;
        } else if (parser.accept(TokenType.BitwiseOr)) {
            op = BinaryExpression.Operator.BitwiseOr;
        } else if (parser.accept(TokenType.And)) {
            op = BinaryExpression.Operator.And;
        } else if (parser.accept(TokenType.Or)) {
            op = BinaryExpression.Operator.Or;
        } else if (parser.accept(TokenType.LessThan)) {
            op = BinaryExpression.Operator.LessThan;
        } else if (parser.accept(TokenType.LessThanOrEquals)) {
            op = BinaryExpression.Operator.LessThanOrEquals;
        } else if (parser.accept(TokenType.GreaterThan)) {
            op = BinaryExpression.Operator.GreaterThan;
        } else if (parser.accept(TokenType.GreaterThanOrEquals)) {
            op = BinaryExpression.Operator.GreaterThanOrEquals;
        } else if (parser.accept(TokenType.Equals)) {
            op = BinaryExpression.Operator.Equals;
        } else if (parser.accept(TokenType.DoubleEquals)) {
            op = BinaryExpression.Operator.Equals;
        } else if (parser.accept(TokenType.NotEquals)) {
            op = BinaryExpression.Operator.NotEquals;
        }
        if (op != null) {
            Expression rightExpression = this.parse();
            return new BinaryExpression(leftExpression, op, rightExpression);
        }
        return null;
    }

    private Expression isNull(Expression expression) {
        if (parser.accept(TokenType.Is)) {
            if (parser.accept(TokenType.Null)) {
                return new IsNullExpression(expression);
            }
        }
        return null;
    }

    private Expression isNotNull(Expression expression) {
        if (parser.accept(TokenType.IsNot)) {
            if (parser.accept(TokenType.Null)) {
                return new NotExpression(new IsNullExpression(expression));
            }
        }
        return null;
    }

    private Expression between(Expression expression) throws Parser.ParseError {
        boolean not = false;
        if (parser.accept(TokenType.Not)) {
            not = true;
        }
        if (parser.accept(TokenType.Between)) {
            Expression subExp = parse();
            if (subExp == null) {
                throw parser.error("expression.between: Could not parse subexpression.");
            }
            if (subExp instanceof BinaryExpression) {
                BinaryExpression biExp = (BinaryExpression) subExp;
                if (biExp.operator() != BinaryExpression.Operator.And) {
                    throw parser.error("expression.between: Invalid subexpression.");
                }
                Expression lower = biExp.left();
                Expression upper = biExp.right();
                if (not) {
                    return new NotExpression(new BetweenExpression(expression, lower, upper));
                } else {
                    return new BetweenExpression(expression, lower, upper);
                }
            }
        }
        return null;
    }

    private Expression like(Expression expression) throws Parser.ParseError {
        boolean not = false;
        if (parser.accept(TokenType.Not)) {
            not = true;
        }
        if (parser.accept(TokenType.Like)) {
            Expression pattern = parse();
            Expression escape = null;
            if (parser.accept(TokenType.Escape)) {
                escape = parse();
            }
            if (not) {
                return new NotExpression(new LikeExpression(expression, pattern, escape));
            } else {
                return new LikeExpression(expression, pattern, escape);
            }
        }
        return null;
    }

    private Expression regexp(Expression expression) throws Parser.ParseError {
        boolean not = false;
        if (parser.accept(TokenType.Not)) {
            not = true;
        }
        if (parser.accept(TokenType.Regexp)) {
            Expression pattern = parse();
            if (not) {
                return new NotExpression(new RegexpExpression(expression, pattern));
            } else {
                return new RegexpExpression(expression, pattern);
            }
        }
        return null;
    }

    private Expression inValue(Expression expression) throws Parser.ParseError {
        boolean not = false;
        if (parser.accept(TokenType.Not)) {
            not = true;
        }
        if (parser.accept(TokenType.In)) {
            parser.expect(TokenType.LParen);
            if (parser.accept(TokenType.Select)) {
                return null;
            }
            List<Expression> values = new ArrayList<>();
            do {
                values.add(this.parse());
            } while (parser.accept(TokenType.Comma));
            parser.expect(TokenType.RParen);
            if (not) {
                return new NotExpression(new InValuesExpression(expression, values));
            } else {
                return new InValuesExpression(expression, values);
            }
        }
        return null;
    }

    private Expression inSelect(Expression expression) throws Parser.ParseError {
        boolean not = false;
        if (parser.accept(TokenType.Not)) {
            not = true;
        }
        if (parser.accept(TokenType.In)) {
            parser.expect(TokenType.LParen);
            if (!parser.accept(TokenType.Select)) {
                return null;
            }
            parser.setIndex(parser.getIndex() - 1);
            SelectStatement statement = selectParser.parse(TokenType.RParen);
            if (statement == null) {
                return null;
            }
            if (not) {
                return new NotExpression(new InSelectExpression(expression, statement));
            } else {
                return new InSelectExpression(expression, statement);
            }
        }
        return null;
    }

    private Expression select(Expression expression) throws Parser.ParseError {

        return null;
    }
}
