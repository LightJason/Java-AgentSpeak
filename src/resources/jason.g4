grammar jason;



program :
    initial_beliefs
    initial_goals
    plans
    ;



initial_beliefs :
    beliefs
    rules
    ;

initial_goals :
    ( EXCLAMATIONMARK literal END )*
    ;



beliefs :
    ( literal END )*
    ;

rules :
    ( literal BEGIN logical_expression END )*
    ;

plans :
    ( plan )*
    ;

plan :
    ( AT atomic_formula )?
    trigger_event
    COLON context
    BEGIN body END
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






logical_expression :
    ( ( NEGOTATION )? simple_logical_expression ) | logical_condition
    ;

logical_condition :
    logical_expression | ( logical_expression ( AND | OR ) logical_expression )
    ;

simple_logical_expression :
    literal | relation_expression | variable
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
    ( OPENROUNDBRACKET termlist CLOSROUNDEBRACKET )?
    ( OPENANGULARBRACKET termlist CLOSANGULARBRACKET )?
    ;

termlist :
    term ( COMMA term )*
    ;

term :
    literal | list | arithmetic_expression | variable | string
    ;

list :
    OPENANGULARBRACKET
    ( term ( COMMA term )*  ( OR ( list | variable ) )  )
    CLOSANGULARBRACKET
    ;

relation_expression :
    relation_term
    ( (LESS | LESSEQUAL | GREATER | GREATEREQUAL | EQUAL | NOTEQUAL | UNIFY | DECONSTRUCT ) relation_term )+
    ;





fragment PLUS                   : ('+');
fragment MINUX                  : ('-');
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
fragment END                    : ('.');
fragment BEGIN                  : ('<-');
fragment AT                     : ('@');
fragment COLON                  : (':');
fragment SEMICOLON              : (';');
fragment OPENROUNDBRACKET       : ('(');
fragment CLOSROUNDEBRACKET      : (')');
fragment OPENANGULARBRACKET     : ('[');
fragment CLOSANGULARBRACKET     : (']');
fragment LESS                   : ('<');
fragment LESSEQUAL              : ('<=');
fragment GREATER                : ('>');
fragment GREATEREQUAL           : ('>=');
fragment EQUAL                  : ('==');
fragment NOTEQUAL               : ('\\==');
fragment UNIFY                  : ('=');
fragment DECONSTRUCT            : ('=..');