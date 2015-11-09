/**
 * @cond LICENSE
 * ######################################################################################
 * # GPL License                                                                        #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015, Philipp Kraus (philipp.kraus@tu-clausthal.de)                  #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU General Public License as                            #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU General Public License for more details.                                       #
 * #                                                                                    #
 * # You should have received a copy of the GNU General Public License                  #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

grammar Jason;


// --- agent-base structure -------------------------------------------------------------
agent :
    initial_beliefs?
    initial_goal?
    rules?
    plans
    ;

initial_beliefs :
    belief*
    ;

initial_goal :
    EXCLAMATIONMARK
    atom
    DOT
    ;
// ---------------------------------------------------------------------------------------



// --- agent-behaviour structure ---------------------------------------------------------
belief :
    STRONGNEGOTATION atom DOT
    ;

plans :
    plan+
    ;

rules :
    rule+
    ;

/**
 * plan modified against the original Jason grammar,
 * so a context is optional (on default true) and the
 * plan body is also optional and after the body
 * can be setup a repair plan
 */
plan :
    ( AT atomic_formula )?
    plan_trigger
    ( COLON plan_context )?
    ( ARROW body )?
    ( ARROW atom )?
    DOT
    ;

/**
 * rules are similar to plans
 * but without context and trigger event
 **/
rule :
    ( AT atomic_formula )?
    atom
    RULEOPERATOR
    body
    DOT
    ;

plan_trigger :
    ( PLUS | MINUS )
    EXCLAMATIONMARK
    atom
    ( LROUNDBRACKET list RROUNDBRACKET )?
    ;

plan_context :
    logical_expression
    ;

body :
    body_formula
    ( SEMICOLON body_formula )*
    ;
// ---------------------------------------------------------------------------------------



// --- agent-expression-context ----------------------------------------------------------
body_formula :
    (belief_actionoperator | plan_actionoperator) atom
    | atomic_formula
    | if_else
    //| while_loop
    //| for_loop
    //| foreach_loop
    | logical_expression
    | arithmetic_expression
    | assignment_expression
    ;

block_formula :
    LCURVEDBRACKET
    body
    RCURVEDBRACKET
    ;

if_else :
    IF LROUNDBRACKET logical_expression RROUNDBRACKET
    block_formula
    ( ELSE block_formula )?
    ;

while_loop :
    WHILE LROUNDBRACKET logical_expression RROUNDBRACKET
    block_formula
    ;

for_loop :
    FOR LROUNDBRACKET assignment_expression? SEMICOLON logical_expression SEMICOLON assignment_expression? RROUNDBRACKET
    block_formula
    ;

foreach_loop :
    FOR LROUNDBRACKET term RROUNDBRACKET
    block_formula
    ;

logical_expression :
    logical_expression (XOR | AND | OR) logical_expression
    | comparison_expression
    | LROUNDBRACKET logical_expression RROUNDBRACKET
    | boolean
    ;

comparison_expression :
    arithmetic_expression comparator arithmetic_expression
    | LROUNDBRACKET comparison_expression RROUNDBRACKET
    ;

arithmetic_expression :
    arithmetic_expression pointoperator arithmetic_expression
    | arithmetic_expression dashoperator arithmetic_expression
    | MINUS arithmetic_expression
    | LROUNDBRACKET arithmetic_expression RROUNDBRACKET
    | number
    | variable
    ;

assignment_expression :
    variable
    EQUAL
    term
    ;
// ---------------------------------------------------------------------------------------



// --- complex-data-types ----------------------------------------------------------------
atomic_formula :
    atom
    ( LROUNDBRACKET list RROUNDBRACKET )?
    ( LANGULARBRACKET list RANGULARBRACKET )?
    ;

term :
    | LANGULARBRACKET list RANGULARBRACKET
    | atom
    | variable
    | string
    ;

list :
    term
    ( COMMA term )*
    ;

/**
 * atoms are defined like Prolog atoms
 * @note compatibility with Jason syntax, an atom can begin
 * with a dot, it represents an internal Jason action, the dot
 * will ignored always
 **/
atom :
    DOT?
    LOWERCASELETTER
    ( LOWERCASELETTER | UPPERCASELETTER | UNDERSCORE | DIGIT )*
    ;

/**
 * variables are defined like Prolog variables
 **/
variable :
    ( UPPERCASELETTER | UNDERSCORE )
    ( LOWERCASELETTER | UPPERCASELETTER | UNDERSCORE | DIGIT )*
    ;



plan_actionoperator :
    EXCLAMATIONMARK
    | DOUBLEEXCLAMATIONMARK
    | QUESTIONMARK
    ;

belief_actionoperator :
    PLUS
    | MINUS
    | MINUSPLUS
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
    | MODULO
    ;

unaryoperator :
    INCREMENT
    | DECREMENT
    ;

binaryoperator :
    ASSIGNINCREMENT
    | ASSIGNDECREMENT
    | ASSIGNMULTIPLY
    | ASSIGNDIVIDE
    ;

/**
 * default behaviour in Jason is only a floating-point number (double)
 * but here exists the difference between floating and integral number
 * types within the grammar, the div-operator (integer division) is removed,
 * also definied constants are used
 **/
number :
    floatnumber
    | integernumber
    ;

floatnumber :
    (PLUS | MINUS)? Digit DOT Digit+
    | constant
    ;

integernumber :
    (PLUS | MINUS)? Digit+
    ;

boolean :
    TRUE
    | FALSE
    ;

constant :
    PI
    | EULER
    | GRAVITY
    | AVOGADRO
    | BOLTZMANN
    | ELECTRON
    | PROTON
    | NEUTRON
    | LIGHTSPEED
    ;

string :
    SINGLEQUOTESTRING
    | DOUBLEQUOTESTRING
    ;
// ---------------------------------------------------------------------------------------



// --- character structures --------------------------------------------------------------
EXCLAMATIONMARK            : '!';
STRONGNEGOTATION           : '~';
COMMA                      : ',';
PLUS                       : '+';
MINUS                      : '-';
MINUSPLUS                  : '-+';
DOUBLEEXCLAMATIONMARK      : '!!';
QUESTIONMARK               : '?';

ARROW                      : '<-';
RULEOPERATOR               : ':-';
AT                         : '@';
COLON                      : ':';
SEMICOLON                  : ';';
DOT                        : '.';
UNDERSCORE                 : '_';

IF                         : 'if';
ELSE                       : 'else';
WHILE                      : 'while';
FOR                        : 'for';

PI                         : 'pi';
EULER                      : 'euler';
GRAVITY                    : 'gravity';
AVOGADRO                   : 'avogadro';
BOLTZMANN                  : 'boltzmann';
ELECTRON                   : 'electron';
PROTON                     : 'proton';
NEUTRON                    : 'neutron';
LIGHTSPEED                 : 'lightspeed';

LROUNDBRACKET              : '(';
RROUNDBRACKET              : ')';
LANGULARBRACKET            : '[';
RANGULARBRACKET            : ']';
LCURVEDBRACKET             : '{';
RCURVEDBRACKET             : '}';

NEGOTATION                 : 'not';
TRUE                       : 'true' | 'success';
FALSE                      : 'false' | 'fail';
/**
 * allow on logical and-concationation
 * also c- and pascal-style-based & / and
 **/
AND                        : '&' | '&&';
/**
 * allow on logical or-concationation
 * also c- and pascal-style-based | / or
 **/
OR                         : '|' | '||';
/**
 * define an logical xor-definition
 **/
XOR                        : '^' | 'xor';

/**
 * variable operators
 **/
INCREMENT                  : '++';
DECREMENT                  : '--';
ASSIGN                     : '=';
ASSIGNINCREMENT            : '+=';
ASSIGNDECREMENT            : '-=';
ASSIGNMULTIPLY             : '*=';
ASSIGNDIVIDE               : '/=';

LESS                       : '<';
LESSEQUAL                  : '<=';
GREATER                    : '>';
GREATEREQUAL               : '>=';
EQUAL                      : '==';
NOTEQUAL                   : '\\==';
UNIFY                      : '=';
DECONSTRUCT                : '=..';
/**
 * allow on pow also the key-word represenation
 **/
POW                        : '**' | 'pow';
/**
 * allow on multiply the key-word representation
 **/
MULTIPLY                   : '*' | 'mul';
/**
 * allow on divide the key-word definition
 **/
DIVIDE                     : '/' | 'div';
MODULO                     : '%' | 'mod';

LOWERCASELETTER   : [a-z];
UPPERCASELETTER   : [A-Z];
DIGIT             : [0-9];
/**
 * string can be definied in single- and double-quotes
 **/
SINGLEQUOTESTRING          : '\'' ('\'\'' | ~('\''))* '\'';
DOUBLEQUOTESTRING          : '"' ('""' | ~('"'))* '"';
// ---------------------------------------------------------------------------------------


// --- skip items ------------------------------------------------------------------------
WHITESPACE                 : (' ' | '\n' | '\t' | '\r')+ -> skip;
/**
 * add for line-comment also #
 **/
LINECOMMENT                : ('//' | '#') ~[\r\n]* -> skip;
/**
 * block comment allowed within the grammar
 * default behaviour does not allow block comments
 **/
BLOCKCOMMENT                    :   '/*' .*? '*/' -> skip;
// ---------------------------------------------------------------------------------------
