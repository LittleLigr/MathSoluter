package team.air.mathsoluter.Core.System.Parser;

import java.util.ArrayList;

import team.air.mathsoluter.Core.System.Token;

public class Parser {


    int id = 0;
    ArrayList<Token> tokens;

    public Parser(ArrayList<Token> tokens)
    {
        this.tokens = tokens;
    }

    public ArrayList<Statement> parse() {
        ArrayList<Statement> statements = new ArrayList<>();
        while (isNoEnd()) {
            Statement decl = declaration();

                statements.add(decl);
        }
        
        return statements;
    }

    /*

    Нужно распарсить выражение, дя этого выделяем несколько уровней значимости команд
    Первый, самый важный, ищем Токены строк, цифр, true/false, скобок
    Дальше проверяем на ! -
    Затем на * /
    Затем + -
    Затем > >= < <=
    и в конце на != ==
    Действовать будем с помощью рекурсии, поэтому начнем с конца, в результате получим обратную польскую развертку

     */

    private Statement declaration()
    {
        try {
            if(match(Token.TokenType.VAR))return varDeclaration();
            return statement();
        }
        catch (ParserError e)
        {
            return null;
        }
    }

    private Statement varDeclaration() {
        Token name = consume(Token.TokenType.IDENTIFIER, "Expect variable name.");

        Expression initializer = null;
        if (match(Token.TokenType.EQUAL)) {
            initializer = expression();
        }

        consume(Token.TokenType.END_OF_LINE, "Expect ';' after variable declaration.");
        return new Statement.VarStatement(name, initializer);
    }

    private Statement statement() {
        if (match(Token.TokenType.PRINT)) return printStatement();
        if (match(Token.TokenType.BRACE_BRACKET_OPEN)) return new Statement.BlockStatement(block());

        return expressionStatement();
    }

    private Statement printStatement() {
        Expression value = expression();
        consume(Token.TokenType.END_OF_LINE, "Expect ';' after value.");
        return new Statement.PrintStatement(value);
    }

    private Statement expressionStatement() {
        Expression expr = expression();
        consume(Token.TokenType.END_OF_LINE, "Expect ';' after expression.");
        return new Statement.ExpressionStatement(expr);
    }

    private ArrayList<Statement> block() {
        ArrayList<Statement> statements = new ArrayList<>();

        while (!check(Token.TokenType.BRACE_BRACKET_CLOSE) && isNoEnd()) {
            statements.add(declaration());
        }

        consume(Token.TokenType.BRACE_BRACKET_CLOSE, "Expect '}' after block.");
        return statements;
    }

    Expression expression() {
        return assignment();

    }

    private Expression assignment() {
        Expression expr = equality();

        if (match(Token.TokenType.EQUAL)) {
            Token equals = previous();
            Expression value = assignment();

            if (expr instanceof Expression.Variable) {
                Token name = ((Expression.Variable)expr).value;
                return new Expression.Assign(name, value);
            }

        }

        return expr;
    }

    Expression equality()
    {
        Expression expr = comparsion();

        while(match(Token.TokenType.BANG_EQUAL, Token.TokenType.EQUAL_EQUAL))
        {
            Token operator = previous();
            Expression right = comparsion();
            expr = new Expression.Binary(expr, operator, right);
        }

        return  expr;
    }

    Expression comparsion()
    {
        Expression expr = addition();

        while(match(Token.TokenType.LESS, Token.TokenType.LESS_EQUAL, Token.TokenType.GREATER, Token.TokenType.GREATER_EQUAL))
        {
            Token operator = previous();
            Expression right = addition();
            expr = new Expression.Binary(expr, operator, right);
        }

        return  expr;
    }

    Expression addition()
    {
        Expression expr = multiplication();

        while(match(Token.TokenType.PLUS, Token.TokenType.MINUS))
        {
            Token operator = previous();
            Expression right = multiplication();
            expr = new Expression.Binary(expr, operator, right);
        }

        return  expr;
    }

    Expression multiplication()
    {
        Expression expr = unary();

        while(match(Token.TokenType.STAR, Token.TokenType.SLASH))
        {
            Token operator = previous();
            Expression right = unary();
            expr = new Expression.Binary(expr, operator, right);
        }

        return  expr;
    }

    Expression unary()
    {
        if(match(Token.TokenType.BANG, Token.TokenType.MINUS))
        {
            Token operator = previous();
            Expression right = unary();
            return new Expression.Unary(operator, right);
        }
        return primary();
    }

    Expression primary()
    {
        if (match(Token.TokenType.FALSE)) return new Expression.Literal(false);
        if (match(Token.TokenType.TRUE)) return new Expression.Literal(true);
        if (match(Token.TokenType.NULL)) return new Expression.Literal(null);

        if (match(Token.TokenType.NUMERICAL, Token.TokenType.STRING))
            return new Expression.Literal(previous().literal);

        if (match(Token.TokenType.IDENTIFIER)) {
            return new Expression.Variable(previous());
        }

        if (match(Token.TokenType.OPERATOR_BRACKET_OPEN)) {
            Expression expr = equality();
            consume(Token.TokenType.OPERATOR_BRACKET_CLOSE, "Expect ')' after expression.");
            return new Expression.Grouping(expr);
        }

        return null;
    }

    private Token consume(Token.TokenType type, String message) {
        if (check(type)) return advance();
        return null;
    }


    boolean match(Token.TokenType...types)
    {
        for (Token.TokenType type:types)
            if(check(type))
            {
                advance();
                return true;
            }
        return false;
    }

    boolean check(Token.TokenType type)
    {
        if(!isNoEnd())return false;
        return tokens.get(id).type==type;
    }

    boolean isNoEnd()
    {
        if(id<tokens.size())
            return true;
        return false;
    }

    Token previous()
    {
        return tokens.get(id-1);
    }

    Token advance()
    {
        if(isNoEnd())id+=1;
        return previous();
    }

    /*
    public Expression start()
    {
        while (id < tokens.size())
            result = nextToken();
       return result;
    }

    Expression nextToken()
    {
        Token token = tokens.get(id);
        id+=1;
        switch (token.type)
        {
            case NUMERICAL:
               return new Expression.Unary(token);
            case PLUS:
            case MINUS:
            case STAR:
            case SLASH:
                return new Expression.Binary(result, token, nextToken());

                default:
                    break;
        }

        return null;
    }*/


}
