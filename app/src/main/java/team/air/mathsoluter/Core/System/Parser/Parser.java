package team.air.mathsoluter.Core.System.Parser;

import android.widget.TextView;

import java.util.ArrayList;

import team.air.mathsoluter.Core.System.Token;

public class Parser {

    int id = 0;
    ArrayList<Token> tokens;

    TextView consoleOutput;

    public Parser(ArrayList<Token> tokens)
    {
        this.tokens = tokens;
    }

    public ArrayList<Statement> parse(TextView consoleOutput) {
        this.consoleOutput=consoleOutput;
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
            if (match(Token.TokenType.CLASS)) return classDeclaration();
            if(match(Token.TokenType.FUNCTION)) return functionStatement("function");
            if(match(Token.TokenType.DOG_SYMBOL)) return undefinedExpression();
            if(match(Token.TokenType.VAR))return varDeclaration();
            return statement();
        }
        catch (ParserError e)
        {
            return null;
        }
    }

    private Statement undefinedExpression()
    {
        return  null;
    }

    private Statement.ClassStatement classDeclaration()
    {
        Token name = consume(Token.TokenType.IDENTIFIER, "expect class name");
        consume(Token.TokenType.BRACE_BRACKET_OPEN, "expect {");
        ArrayList<Statement.FunctionStatement> functions = new ArrayList<>();

        while(!check(Token.TokenType.BRACE_BRACKET_CLOSE)&& isNoEnd())
        {
            functions.add(functionStatement("function"));
        }

        consume(Token.TokenType.BRACE_BRACKET_CLOSE, "expect }");
        return new Statement.ClassStatement(name, functions);
    }

    private Statement varDeclaration() {
        Token name = consume(Token.TokenType.IDENTIFIER, "Expect variable name.");

        Expression initializer = null;
        if (match(Token.TokenType.EQUAL)) {
            initializer = expression();
        }

        Statement.BlockStatement setter=null, getter=null;
        if(match(Token.TokenType.COLON))
        {
            if(match(Token.TokenType.SET)&&match(Token.TokenType.BRACE_BRACKET_OPEN))
                setter = new Statement.BlockStatement(block());
            if(match(Token.TokenType.GET)&&match(Token.TokenType.BRACE_BRACKET_OPEN))
                getter = new Statement.BlockStatement(block());
        }

        consume(Token.TokenType.END_OF_LINE, "Expect ';' after variable declaration.");
        return new Statement.VarStatement(name, initializer, setter, getter);
    }

    private Statement statement() {
        if (match(Token.TokenType.FOR)) return forStatement();
        if (match(Token.TokenType.IF)) return ifStatement();
        if (match(Token.TokenType.PRINT)) return printStatement();
        if(match(Token.TokenType.RETURN)) return returnStatement();
        if (match(Token.TokenType.WHILE)) return whileStatement();
        if (match(Token.TokenType.BRACE_BRACKET_OPEN)) return new Statement.BlockStatement(block());

        return expressionStatement();
    }

    private Statement forStatement() {
        consume(Token.TokenType.OPERATOR_BRACKET_OPEN, "Expect '(' after 'while'.");
        Statement initializer;
        if (match(Token.TokenType.END_OF_LINE)) {
            initializer = null;
        } else if (match(Token.TokenType.VAR)) {
            initializer = varDeclaration();
        } else {
            initializer = expressionStatement();
        }

        Expression condition = null;
        if(!check(Token.TokenType.END_OF_LINE))
            condition=expression();
        consume(Token.TokenType.END_OF_LINE, "expect ;");

        Expression increment = null;
        if(!check(Token.TokenType.OPERATOR_BRACKET_CLOSE))
            increment = expression();
        consume(Token.TokenType.OPERATOR_BRACKET_CLOSE, "expect )");
        Statement body = statement();

        if (increment != null)
        {
            ArrayList<Statement> list = new ArrayList<>();
            list.add(body);
            list.add(new Statement.ExpressionStatement(increment));
            body = new Statement.BlockStatement(list);
        }

        if (condition == null) condition = new Expression.Literal(true);
        body = new Statement.WhileStatement(condition, body);

        if (initializer != null)
        {
            ArrayList<Statement> list = new ArrayList<>();
            list.add(initializer);
            list.add(body);
            body = new Statement.BlockStatement(list);
        }

        return body;
    }

    private Statement whileStatement() {
        consume(Token.TokenType.OPERATOR_BRACKET_OPEN, "Expect '(' after 'while'.");
        Expression condition = expression();
        consume(Token.TokenType.OPERATOR_BRACKET_CLOSE, "Expect ')' after condition.");
        Statement body = statement();

        return new Statement.WhileStatement(condition, body);
    }

    private Statement ifStatement() {
        consume(Token.TokenType.OPERATOR_BRACKET_OPEN, "Expect '(' after 'if'.");
        Expression condition = expression();
        consume(Token.TokenType.OPERATOR_BRACKET_CLOSE, "Expect ')' after if condition.");

        Statement thenBranch = statement();
        Statement elseBranch = null;
        if (match(Token.TokenType.ELSE)) {
            elseBranch = statement();
        }

        return new Statement.IfStatement(condition, thenBranch, elseBranch);
    }

    private Statement returnStatement() {
        Token keyword = previous();
        Expression value = null;
        if (!check(Token.TokenType.END_OF_LINE)) {
            value = expression();
        }

        consume(Token.TokenType.END_OF_LINE, "Expect ';' after return value.");
        return new Statement.ReturnStatement(value);
    }

    private Statement printStatement() {
        Expression value = expression();
        consume(Token.TokenType.END_OF_LINE, "Expect ';' after value.");
        return new Statement.PrintStatement(value, consoleOutput);
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

    private Statement.FunctionStatement functionStatement(String name)
    {
        boolean isStatic = false;

        if(match(Token.TokenType.STATIC))
            isStatic=true;

        Token funcName = consume(Token.TokenType.IDENTIFIER, "Expect "+name+" function");
        consume(Token.TokenType.OPERATOR_BRACKET_OPEN, "expect (");

        ArrayList<Token> parameters = new ArrayList<>();

        if(!check(Token.TokenType.OPERATOR_BRACKET_CLOSE))
            do {
                parameters.add(consume(Token.TokenType.IDENTIFIER, "expect param"));
            }
            while(match(Token.TokenType.COMMA));

        consume(Token.TokenType.OPERATOR_BRACKET_CLOSE, "expect )");
        consume(Token.TokenType.BRACE_BRACKET_OPEN, "Expect '{' before " + name + " body.");
        ArrayList<Statement> body = block();
        return new Statement.FunctionStatement(funcName, parameters, body, isStatic);
    }

    Expression expression() {
        return assignment();
    }

    private Expression assignment() {
        Expression expr = or();

        if (match(Token.TokenType.EQUAL)) {
            Token equals = previous();
            Expression value = assignment();

            if (expr instanceof Expression.Variable) {
                Token name = ((Expression.Variable)expr).value;
                return new Expression.Assign(name, value);
            }
            else if(expr instanceof Expression.Get)
            {
                Expression.Get _get = (Expression.Get)expr;
                return new Expression.Set(_get, value);
            }
        }
        return expr;
    }

    private Expression or() {
        Expression expr = and();

        while (match(Token.TokenType.OR)) {
            Token operator = previous();
            Expression right = and();
            expr = new Expression.Logical(expr, operator, right);
        }

        return expr;
    }

    private Expression and() {
        Expression expr = equality();

        while (match(Token.TokenType.AND)) {
            Token operator = previous();
            Expression right = equality();
            expr = new Expression.Logical(expr, operator, right);
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
        Expression expr = pow();

        while(match(Token.TokenType.STAR, Token.TokenType.SLASH))
        {
            Token operator = previous();
            Expression right = pow();
            expr = new Expression.Binary(expr, operator, right);
        }

        return  expr;
    }

    Expression pow()
    {
        Expression expr = unary();

        while(match(Token.TokenType.CAP))
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
        return call();
    }

    Expression call(){
        Expression expr = primary();
        while (true)
        {
            if(match(Token.TokenType.OPERATOR_BRACKET_OPEN))
                expr = finishCall(expr);
            else if(match(Token.TokenType.DOT))
            {
                Token name = consume(Token.TokenType.IDENTIFIER, "expect var name");
                expr = new Expression.Get(expr,name);
            }
            else break;
        }

        return expr;
    }

    Expression finishCall(Expression callee)
    {
        ArrayList<Expression> arguments = new ArrayList<>();
        if(!check(Token.TokenType.OPERATOR_BRACKET_CLOSE))
        {
            do {
                arguments.add(expression());
            }
            while (match(Token.TokenType.COMMA));
        }

        Token parent = consume(Token.TokenType.OPERATOR_BRACKET_CLOSE, "");
        return new Expression.Call(callee, arguments);
    }

    Expression primary()
    {
        if(match(Token.TokenType.BACK_SLASH))
            return texExpression();
        if(match(Token.TokenType.BRACE_BRACKET_OPEN))
        {
            Expression expression = expression();
            consume(Token.TokenType.BRACE_BRACKET_CLOSE, "expect }");
            return expression;
        }

        if (match(Token.TokenType.FALSE)) return new Expression.Literal(false);
        if (match(Token.TokenType.TRUE)) return new Expression.Literal(true);
        if (match(Token.TokenType.NULL)) return new Expression.Literal(null);

        if (match(Token.TokenType.NUMERICAL, Token.TokenType.STRING))
            return new Expression.Literal(previous().literal);

        if (match(Token.TokenType.THIS)) return new Expression.This(previous());

        if (match(Token.TokenType.IDENTIFIER))
            return new Expression.Variable(previous());

        if (match(Token.TokenType.OPERATOR_BRACKET_OPEN)) {
            Expression expr = equality();
            consume(Token.TokenType.OPERATOR_BRACKET_CLOSE, "Expect ')' after expression.");
            return new Expression.Grouping(expr);
        }

        return null;
    }

    private Expression texExpression()
    {
        if(match(Token.TokenType.INTEGRAL))
            return mathExpression3Arg(previous().type);
        if(match(Token.TokenType.FRAC))
            return mathExpression2Arg(previous().type);
        if(match(Token.TokenType.SIN, Token.TokenType.COS, Token.TokenType.TAN))
            return mathExpression1Arg(previous().type);
        return null;
    }

    private Expression mathExpression3Arg(Token.TokenType type)
    {
        consume(Token.TokenType.BRACE_BRACKET_OPEN, "Expect '{' after expression.");
        Expression expr1 = expression();
        consume(Token.TokenType.BRACE_BRACKET_CLOSE, "Expect '}' after expression.");
        consume(Token.TokenType.CAP, "Expect '^' after expression.");
        consume(Token.TokenType.BRACE_BRACKET_OPEN, "Expect '{' after expression.");
        Expression expr2 = expression();
        consume(Token.TokenType.BRACE_BRACKET_CLOSE, "Expect '}' after expression.");
        consume(Token.TokenType.BRACE_BRACKET_OPEN, "Expect '{' after expression.");
        Expression expr3 = expression();
        consume(Token.TokenType.BRACE_BRACKET_CLOSE, "Expect '}' after expression.");
        return new Expression.MathExpression3Arg(type, expr1, expr2, expr3);
    }

    private Expression mathExpression2Arg(Token.TokenType type)
    {
        consume(Token.TokenType.BRACE_BRACKET_OPEN, "Expect '{' after expression.");
        Expression expr1 = expression();
        consume(Token.TokenType.BRACE_BRACKET_CLOSE, "Expect '}' after expression.");
        consume(Token.TokenType.BRACE_BRACKET_OPEN, "Expect '{' after expression.");
        Expression expr2 = expression();
        consume(Token.TokenType.BRACE_BRACKET_CLOSE, "Expect '}' after expression.");
        return new Expression.MathExpression2Arg(type, expr1, expr2);
    }

    private Expression mathExpression1Arg(Token.TokenType type)
    {
        consume(Token.TokenType.BRACE_BRACKET_OPEN, "Expect '{' after expression.");
        Expression expr1 = expression();
        consume(Token.TokenType.BRACE_BRACKET_CLOSE, "Expect '}' after expression.");
        return new Expression.MathExpression1Arg(type, expr1);
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
