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
    logical_expression
    | TRUE
    ;

body :
    body_formula
    ( SEMICOLON body_formula )*
    | TRUE
    ;

body_formula :
    actionoperator
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
    simple_logical_expression
    | ( ( NEGOTATION )? simple_logical_expression )
    | ( LROUNDBRACKET logical_expression RROUNDEBRACKET )
    | logical_or_expression
    ;

logical_or_expression :
    logical_and_expression ( OR logical_and_expression )*
    ;

logical_and_expression :
    simple_logical_expression ( AND logical_expression )*
    ;

simple_logical_expression :
    TRUE
    | FALSE
    | literal
    | relation_expression
    | variable
    ;

relation_expression :
    relation_term
    ( comparator relation_term )+
    ;

arithmetic_expression :
    arithmetic_term
    ( ( dashoperator | pointoperator ) arithmetic_term )*
    ;





term :
    literal
    | list
    | arithmetic_expression
    | variable
    | string
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
    literal
    | arithmetic_expression
    ;

arithmetic_term :
    number
    | variable
    | MINUS arithmetic_term
    | LROUNDBRACKET arithmetic_expression RROUNDEBRACKET
    ;




comparator :
    LESS
    | LESSEQUAL
    | EQUAL
    | NOTEQUAL
    | GREATER
    | GREATEREQUAL
    | UNIFY
    | DECONSTRUCT
    ;

dashoperator :
    PLUS
    | MINUS
    ;

pointoperator :
    POW
    | MULTIPLY
    | DIVIDE
    | DIVIDEINT
    | MODULO
    ;

actionoperator :
    EXCLAMATIONMARK
    | DOUBLEEXCLAMATIONMARK
    | QUESTIONMARK
    | PLUS
    | MINUS
    | MINUSPLUS
    ;





number :
    ;

variable :
    ;

atom :
    ;

string :
    ;





PLUS                   : '+';
MINUS                  : '-';
MINUSPLUS              : '-+';
EXCLAMATIONMARK        : '!';
DOUBLEEXCLAMATIONMARK  : '!!';
QUESTIONMARK           : '?';
STRONGNEGOTATION       : '~';
NEGOTATION             : 'not';
TRUE                   : 'true';
FALSE                  : 'false';
AND                    : '&';
OR                     : '|';
COMMA                  : ',';
DOT                    : '.';
ARROW                  : '<-';
AT                     : '@';
COLON                  : ':';
SEMICOLON              : ';';
LROUNDBRACKET          : '(';
RROUNDEBRACKET         : ')';
LANGULARBRACKET        : '[';
RANGULARBRACKET        : ']';
LESS                   : '<';
LESSEQUAL              : '<=';
GREATER                : '>';
GREATEREQUAL           : '>=';
EQUAL                  : '==';
NOTEQUAL               : '\\==';
UNIFY                  : '=';
DECONSTRUCT            : '=..';
POW                    : '**';
MULTIPLY               : '*';
DIVIDE                 : '/';
DIVIDEINT              : '//' | 'div';
MODULO                 : '%' | 'mod';

LOWERCASELETTER        : [a..z];
UPPERCASELETTER        : [A..Z];
SPACE                  : ' ';
UNDERSCORE             : '_';
DIGIT                  : [0..9];
