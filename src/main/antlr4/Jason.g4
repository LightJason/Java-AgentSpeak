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

/**
 * initial grammar rule
 **/
agent :
    initial_beliefs?
    initial_goal?
    rules?
    plans
    ;

/**
 * rule to represent initial beliefs
 **/
initial_beliefs :
    belief*
    ;

/**
 * rule to represent the initial goal
 **/
initial_goal :
    EXCLAMATIONMARK
    atom
    DOT
    ;
// ---------------------------------------------------------------------------------------



// --- agent-behaviour structure ---------------------------------------------------------

/**
 * belief rule
 **/
belief :
    STRONGNEGATION clause DOT
    ;

/**
 * agent plans rule
 * @note one plan must exists
 **/
plans :
    plan+
    ;

/**
 * optional (prolog) rules
 **/
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
    ( AT clause )?
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
    ( AT clause )?
    clause
    RULEOPERATOR
    body
    DOT
    ;

/**
 * plan trigger which can match a goal or belief
 **/
plan_trigger :
    (plan_trigger_belief | plan_trigger_goal)
    atom
    ( LROUNDBRACKET list RROUNDBRACKET )?
    ;

/**
 * plan trigger for a goal
 **/
plan_trigger_goal :
    (PLUS | MINUS)
    EXCLAMATIONMARK
    ;

/**
 * plan trigger for a belief
 **/
plan_trigger_belief :
    PLUS
    | MINUS
    | MINUSPLUS
    ;

/**
 * logical context for plan matching
 **/
plan_context :
    logical_expression
    ;

/**
 * plan or block body
 **/
body :
    body_formula
    ( SEMICOLON body_formula )*
    ;
// ---------------------------------------------------------------------------------------



// --- agent-expression-context ----------------------------------------------------------
body_formula :
    (belief_actionoperator | plan_actionoperator)? clause
    | if_else
    | while_loop
    | for_loop
    | foreach_loop
    | assignment_expression
    | logical_expression
    ;

block_formula :
    LCURVEDBRACKET body RCURVEDBRACKET
    | body_formula
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

/**
 * logical expression
 **/
logical_expression :
    NEGATION logical_expression
    | LROUNDBRACKET logical_expression RROUNDBRACKET
    | logical_expression (XOR | AND | OR) logical_expression
    | comparison_expression
    | boolean
    | clause
    ;

/**
 * comparision expression
 **/
comparison_expression :
    arithmetic_expression comparator arithmetic_expression
    ;

/**
 * arithmetic expression
 **/
arithmetic_expression :
    LROUNDBRACKET arithmetic_expression RROUNDBRACKET
    | arithmetic_expression pointoperator arithmetic_expression
    | arithmetic_expression dashoperator arithmetic_expression
    | MINUS arithmetic_expression
    | number
    | variable
    | clause
    ;

/**
 * assignment expression (for assignin a variable)
 **/
assignment_expression :
    variable
    ( ASSIGN | DECONSTRUCT )
    term
    ;
// ---------------------------------------------------------------------------------------



// --- complex-data-types ----------------------------------------------------------------

/**
 * clause represent a structure existing
 * atom, optional argument, optional annotations
 **/
clause :
    atom
    ( LROUNDBRACKET list RROUNDBRACKET )?
    ( LANGULARBRACKET list RANGULARBRACKET )?
    ;

/**
 * terms are predictable structures
 **/
term :
    variable
    | string
    | number
    | clause
    | arithmetic_expression
    | logical_expression
    | LANGULARBRACKET list RANGULARBRACKET
    ;

/**
 * list equal to collcations
 **/
list :
    term ( COMMA term )*
    | variable LISTSEPARATOR variable
    ;

/**
 * atoms are defined like Prolog atoms
 * @note internal action in Jason can begin with a dot, but here it is removed
 **/
atom :
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

/**
 * plan action operator
 **/
plan_actionoperator :
    EXCLAMATIONMARK
    | DOUBLEEXCLAMATIONMARK
    | QUESTIONMARK
    ;

/**
 * belief-action operator
 **/
belief_actionoperator :
    PLUS
    | MINUS
    | MINUSPLUS
    ;

/**
 * comperator
 **/
comparator :
    LESS
    | LESSEQUAL
    | EQUAL
    | NOTEQUAL
    | GREATER
    | GREATEREQUAL
    ;

/**
 * arithmetic dash operator
 **/
dashoperator :
    PLUS
    | MINUS
    ;

/**
 * arithmetic point operator
 **/
pointoperator :
    POW
    | MULTIPLY
    | DIVIDE
    | MODULO
    ;

/**
 * unary operator
 **/
unaryoperator :
    INCREMENT
    | DECREMENT
    ;

/**
 * binary operator
 **/
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

/**
 * floating-point number
 **/
floatnumber :
    (PLUS | MINUS)? DIGIT DOT DIGIT+
    | constant
    ;

/**
 * integer number
 **/
integernumber :
    (PLUS | MINUS)? DIGIT+
    ;

/**
 * boolean values
 **/
boolean :
    TRUE
    | FALSE
    ;

/**
 * floating-point constants
 **/
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

/**
 * string define with single or double quotes
 **/
string :
    SINGLEQUOTESTRING
    | DOUBLEQUOTESTRING
    ;
// ---------------------------------------------------------------------------------------



// --- character structures --------------------------------------------------------------
EXCLAMATIONMARK            : '!';
STRONGNEGATION             : '~';
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

NEGATION                   : 'not';
TRUE                       : 'true' | 'success';
FALSE                      : 'false' | 'fail';

/**
 * prolog list seperator for head-tail definition
 **/
LISTSEPARATOR              : '|';

/**
 * allow on logical and-concationation
 * also c- and pascal-style-based & / and
 **/
AND                        : '&&';
/**
 * allow on logical or-concationation
 * also c- and pascal-style-based | / or
 **/
OR                         : '||';
/**
 * define an logical xor-definition
 **/
XOR                        : '^';

/**
 * variable operators
 **/
INCREMENT                  : '++';
DECREMENT                  : '--';
ASSIGN                     : '=';
DECONSTRUCT                : '=..';
ASSIGNINCREMENT            : '+=';
ASSIGNDECREMENT            : '-=';
ASSIGNMULTIPLY             : '*=';
ASSIGNDIVIDE               : '/=';

/**
 * comparator types
 **/
LESS                       : '<';
LESSEQUAL                  : '<=';
GREATER                    : '>';
GREATEREQUAL               : '>=';
EQUAL                      : '==';
NOTEQUAL                   : '\\==';

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

/**
 * string can be definied in single- and double-quotes
 **/
SINGLEQUOTESTRING          : '\'' ('\'\'' | ~('\''))* '\'';
DOUBLEQUOTESTRING          : '"' ('""' | ~('"'))* '"';

/**
 * char definitions
 **/
LOWERCASELETTER            : [a-z];
UPPERCASELETTER            : [A-Z];
DIGIT                      : [0-9];
// ---------------------------------------------------------------------------------------


// --- skip items ------------------------------------------------------------------------
/**
 * any whitespace
 **/
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
