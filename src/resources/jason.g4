grammar jason;



agent :
    initial_beliefs
    initial_goals
    plans
    ;



initial_beliefs :
    beliefs
    rules
    ;

initial_goals :
    ( EXCLAMATIONMARK literal DOT )*
    ;



beliefs :
    ( literal DOT )*
    ;

rules :
    ( literal ARROW logical_expression DOT )*
    ;

plans :
    ( plan )*
    ;

plan :
    ( AT atomic_formula )?
    trigger_event
    COLON context
    ARROW body DOT
    ;







trigger_event :
    ( PLUS | MINUS )
    ( EXCLAMATIONMARK | QUESTIONMARK )?
    literal
    ;

literal :
    ( STRONGNEGOTATION )?
    atomic_formula
    ;

context :
    logical_expression | TRUE
    ;

body :
    body_formula
    ( SEMICOLON body_formula )* | TRUE
    ;

body_formula :
    ( EXCLAMATIONMARK | DOUBLEEXCLAMATIONMARK | QUESTIONMARK | PLUS | MINUS | MINUSPLUS )
    literal
    variable
    relation_expression
    ;

atomic_formula :
    ( atom | variable )
    ( LROUNDBRACKET termlist RROUNDEBRACKET )?
    ( LANGULARBRACKET termlist RANGULARBRACKET )?
    ;





logical_expression :
    simple_logical_expression |
    ( ( NEGOTATION )? simple_logical_expression ) |
    ( LROUNDBRACKET logical_expression RROUNDEBRACKET ) |
    (logical_expression ( AND | OR ) logical_expression)
    ;

simple_logical_expression :
    literal | relation_expression | variable
    ;

relation_expression :
    relation_term
    ( (LESS | LESSEQUAL | GREATER | GREATEREQUAL | EQUAL | NOTEQUAL | UNIFY | DECONSTRUCT ) relation_term )+
    ;

arithmetic_expression :
    arithmetic_term
    ( ( PLUS | MINUS | MULTIPLY | POW | DIVIDE | DIVIDEINT | MODULO ) arithmetic_term )*
    ;





term :
    literal | list | arithmetic_expression | variable | string
    ;

termlist :
    term ( COMMA term )*
    ;

list :
    LANGULARBRACKET
    ( term ( COMMA term )*  ( OR ( list | variable ) )  )
    RANGULARBRACKET
    ;

relation_term :
    literal | arithmetic_expression
    ;

arithmetic_term :
    number | variable |
    MINUS arithmetic_term |
    LROUNDBRACKET arithmetic_expression RROUNDEBRACKET
    ;




number :
    ;

variable :
    ;

atom :
    ;

string :
    ;




fragment PLUS                   : ('+');
fragment MINUS                  : ('-');
fragment MINUSPLUS              : ('-+');
fragment EXCLAMATIONMARK        : ('!');
fragment DOUBLEEXCLAMATIONMARK  : ('!!');
fragment QUESTIONMARK           : ('?');
fragment STRONGNEGOTATION       : ('~');
fragment NEGOTATION             : ('not');
fragment TRUE                   : ('true');
fragment AND                    : ('&');
fragment OR                     : ('|');
fragment COMMA                  : (',');
fragment DOT                    : ('.');
fragment ARROW                  : ('<-');
fragment AT                     : ('@');
fragment COLON                  : (':');
fragment SEMICOLON              : (';');
fragment LROUNDBRACKET          : ('(');
fragment RROUNDEBRACKET         : (')');
fragment LANGULARBRACKET        : ('[');
fragment RANGULARBRACKET        : (']');
fragment LESS                   : ('<');
fragment LESSEQUAL              : ('<=');
fragment GREATER                : ('>');
fragment GREATEREQUAL           : ('>=');
fragment EQUAL                  : ('==');
fragment NOTEQUAL               : ('\\==');
fragment UNIFY                  : ('=');
fragment DECONSTRUCT            : ('=..');
fragment POW                    : ('**');
fragment MULTIPLY               : ('*');
fragment DIVIDE                 : ('/');
fragment DIVIDEINT              : ('//' | 'div');
fragment MODULO                 : ('%' | 'mod');