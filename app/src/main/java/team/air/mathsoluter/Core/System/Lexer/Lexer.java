package team.air.mathsoluter.Core.System.Lexer;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import team.air.mathsoluter.Core.System.Token;

public class Lexer {

    static final HashMap<String, Token.TokenType> keywords = new HashMap<>();
    static {
        keywords.put("and", Token.TokenType.AND);
        keywords.put("if", Token.TokenType.IF);
        keywords.put("else", Token.TokenType.ELSE);
        keywords.put("for", Token.TokenType.FOR);
        keywords.put("while", Token.TokenType.WHILE);
        keywords.put("true", Token.TokenType.TRUE);
        keywords.put("false", Token.TokenType.FALSE);
        keywords.put("null", Token.TokenType.NULL);
        keywords.put("var", Token.TokenType.VAR);
        keywords.put("function", Token.TokenType.FUNCTION);
        keywords.put("return", Token.TokenType.RETURN);
        keywords.put("print", Token.TokenType.PRINT);
        keywords.put("or", Token.TokenType.OR);
        keywords.put("frac", Token.TokenType.FRAC);
        keywords.put("sin", Token.TokenType.SIN);
        keywords.put("cos", Token.TokenType.COS);
        keywords.put("tan", Token.TokenType.TAN);
        keywords.put("log", Token.TokenType.LOG);
        keywords.put("exp", Token.TokenType.EXP);
        keywords.put("bmod", Token.TokenType.BMOD);
        keywords.put("sqrt_", Token.TokenType.SQRT);
        keywords.put("sum", Token.TokenType.SUM);
        keywords.put("int_", Token.TokenType.INTEGRAL);
        keywords.put("infty", Token.TokenType.INFINITY);
        keywords.put("mathrm", Token.TokenType.MATHTHERM);
    }

    int current = 0;
    int start;
    int line;
    String code;
    ArrayList<Token> tokens;
    boolean isError = false;
    public ArrayList<Token> lex(String reg)
    {
        code = reg;
        tokens = new ArrayList<>();
        while(current < code.length())
        {
            start = current;
            scanToken();
            if(isError)
                return null;
        }
        return tokens;
    }

    void scanToken()
    {
        char c = next();
        switch (c) {
            case '(': addToken(Token.TokenType.OPERATOR_BRACKET_OPEN); break;
            case ')': addToken(Token.TokenType.OPERATOR_BRACKET_CLOSE); break;
            case '{': addToken(Token.TokenType.BRACE_BRACKET_OPEN); break;
            case '}': addToken(Token.TokenType.BRACE_BRACKET_CLOSE); break;
            case ',': addToken(Token.TokenType.COMMA); break;
            case '.': addToken(Token.TokenType.DOT); break;
            case '-': addToken(Token.TokenType.MINUS); break;
            case '+': addToken(Token.TokenType.PLUS); break;
            case '^': addToken(Token.TokenType.CAP); break;
            case '*': addToken(Token.TokenType.STAR); break;
            case '!': addToken(match('=') ? Token.TokenType.BANG_EQUAL : Token.TokenType.BANG); break;
            case '=': addToken(match('=') ? Token.TokenType.EQUAL_EQUAL : Token.TokenType.EQUAL); break;
            case '<': addToken(match('=') ? Token.TokenType.LESS_EQUAL : Token.TokenType.LESS); break;
            case '>': addToken(match('=') ? Token.TokenType.GREATER_EQUAL : Token.TokenType.GREATER); break;

            case '/':
                if (match('/'))
                    while (peek() != '\n' && !isAtEnd()) next();
                else
                    addToken(Token.TokenType.SLASH);
                break;

            case ';':
                    addToken(Token.TokenType.END_OF_LINE); break;
            case '@':
                addToken(Token.TokenType.DOG_SYMBOL); break;
            case '\\':
                addToken(Token.TokenType.BACK_SLASH); break;

            case ' ':
                case '\r':
                case '\t':
                // Ignore whitespace.
                break;

                case '\n':
                    line++;
                break;

            case '"': string(); break;
            case 'o':
                if (peek() == 'r')
                {
                    addToken(Token.TokenType.OR);
                    next();
                }
                break;

            default:

                if (isDigit(c))
                    number();
                else if (isAlpha(c))
                    identifier();
                else {
                    isError = true;
                }

               break;

        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) next();

        String text = code.substring(start, current);

        Token.TokenType type = keywords.get(text);
        if (type == null) type = Token.TokenType.IDENTIFIER;
        addToken(type, null);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    void number() {
        while (isDigit(peek())) next();

        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            next();

            while (isDigit(peek())) next();
        }

        addToken(Token.TokenType.NUMERICAL,
                Double.parseDouble(code.substring(start, current)));
    }

    private char peekNext() {
        if (current + 1 >= code.length()) return '\0';
        return code.charAt(current + 1);
    }

    boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            next();
        }

        if (isAtEnd()) {
            isError=true;
            return;
        }

        next();

        String value = code.substring(start + 1, current - 1);
        addToken(Token.TokenType.STRING, value);
    }

    private char peek() {
        if (current >= code.length()) return '\0';
        return code.charAt(current);
    }

    boolean match(char expected) {
        if (isAtEnd()) return false;
        if (code.charAt(current) != expected) return false;

        current++;
        return true;
    }

    boolean isAtEnd() {
        return current >= code.length();
    }

    void addToken(Token.TokenType type)
    {
        tokens.add(new Token(type));
    }

    void addToken(Token.TokenType type, Object literal) {
        String text = code.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    char next()
    {
        current+=1;
        return code.charAt(current-1);
    }

}
