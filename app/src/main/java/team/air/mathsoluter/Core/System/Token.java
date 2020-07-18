package team.air.mathsoluter.Core.System;

import java.util.HashMap;
import java.util.Map;

public class Token {

    public TokenType type;
    public String lexeme;
    public Object literal;
    public int line;

    public Token(TokenType type)
    {
        this.type = type;

    }

    public Token(TokenType type, String lexeme, Object literal, int line)
    {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }


    @Override
    public String toString() {
        return  type+" "+lexeme+" "+literal;
    }

    public static enum TokenType
    {
        OPERATOR_BRACKET_OPEN, // (
        OPERATOR_BRACKET_CLOSE,// )
        BRACE_BRACKET_OPEN,// {
        BRACE_BRACKET_CLOSE,// }
        SQUARE_BRACKET_OPEN,// [
        SQUARE_BRACKET_CLOSE,// ]
        PLUS, // +
        MINUS,// -
        STAR,// *
        SLASH,// /
        COMMA,// ,
        DOT,// .
        BANG,// !
        BANG_EQUAL, // !=
        EQUAL, // =
        EQUAL_EQUAL, // ==
        GREATER, // >
        GREATER_EQUAL, // >=
        LESS, // <
        LESS_EQUAL, // <=
        IDENTIFIER, // Идентификатор переменной
        STRING, // Строка
        NUMERICAL, // число
        IF,
        ELSE,
        FOR,
        WHILE,
        NULL,
        OR,
        AND,
        TRUE,
        FALSE,
        VAR,
        RETURN,
        FUNCTION,
        COMMENT,
        END_OF_LINE,
        PRINT,

        ALPHA,
        ALPHA_CAPITAL,
        BETA,
        BETA_CAPITAL,
        GAMMA,
        GAMMA_CAPITAL,
        PI,
        PI_CAPITAL,
        PHI,
        PHI_CAPITAL,
        THETA,
        THETA_CAPITAL,
        VARPHI,
        MU,
        COS,
        SIN,
        TAN,
        LOG,
        EXP,
        BMOD,
        FRAC,
        SQRT,
        SUM,
        INTEGRAL,
        INFINITY,
        MATHTHERM,
        BACK_SLASH,
        DOG_SYMBOL
    }
}
