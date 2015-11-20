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
@header { package lightjason; }


// --- agent-base structure -------------------------------------------------------------

/**
 * initial grammar rule
 **/
agent :
    initial_beliefs?
    initial_goal?
    principles?
    plans
    ;

/**
 * rule to represent initial beliefs
 **/
initial_beliefs :
    belief+
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
    STRONGNEGATION? literal DOT
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
principles :
    principle+
    ;

/**
 * plan modified against the original Jason grammar,
 * so a context is optional (on default true) and the
 * plan body is also optional and after the body
 * can be setup a repair plan
 */
plan :
    annotations?
    plan_trigger
    literal
    ( COLON plan_context )?
    ( ARROW body )?
    ( ARROW atom )?
    DOT
    ;

/**
 * rules are similar to plans
 * but without context and trigger event
 **/
principle :
    annotations?
    literal
    RULEOPERATOR
    body
    DOT
    ;

/**
 * annotation for rules and plans
 **/
annotations :
    ( annotation_atom | annotation_literal )+
    ;

/**
 * atomic annotations (without parameter)
 **/
annotation_atom :
    AT
    (ATOMIC | EXCLUSIVE | PARALLEL)
    ;

/**
 * annotation with parameter
 **/
annotation_literal :
    AT
    ( annotation_numeric_literal | annotation_symbolic_literal )
    ;

/**
 * annotations with numerical parameter
 **/
annotation_numeric_literal :
    ( FUZZY | PRIORITY )
    LROUNDBRACKET
    number
    RROUNDBRACKET
    ;

/**
 * annotation with symbolic value
 **/
annotation_symbolic_literal :
    EXPIRES
    LROUNDBRACKET
    atom
    RROUNDBRACKET
    ;

/**
 * plan trigger which can match a goal or belief
 **/
plan_trigger :
    (plan_belief_trigger | plan_goal_trigger)
    ;

/**
 * plan trigger for a goal
 **/
plan_goal_trigger :
    (PLUS | MINUS)
    EXCLAMATIONMARK
    ;

/**
 * plan trigger for a belief
 **/
plan_belief_trigger :
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
    (belief_action | achievment_goal_action | test_goal_action )? literal
    | if_else
    | while_loop
    | for_loop
    | foreach_loop
    | assignment_expression
    | logical_expression
    | deconstruct_expression
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
    | logicalvalue
    | literal
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
    MINUS? LROUNDBRACKET arithmetic_expression RROUNDBRACKET
    | arithmetic_expression arithmeticoperator arithmetic_expression
    | number
    | variable
    | literal
    ;

/**
 * assignment expression (for assignin a variable)
 **/
assignment_expression :
    (variable ASSIGN term)
    | ( variable unaryoperator )
    ;

/**
 * deconstruct expression (splitting clauses)
 **/
deconstruct_expression :
    LANGULARBRACKET
    list_headtail
    RANGULARBRACKET
    DECONSTRUCT
    ( literal | variable )
    ;
// ---------------------------------------------------------------------------------------



// --- complex-data-types ----------------------------------------------------------------

/**
 * clause represent a literal structure existing
 * atom, optional argument, optional annotations
 **/
literal :
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
    | literal
    | arithmetic_expression
    | logical_expression
    | LANGULARBRACKET list RANGULARBRACKET
    ;

/**
 * list equal to collcations
 **/
list :
    term ( COMMA term )*
    | list_headtail
    ;

/**
 * list with head-tail-annotation definition
 **/
list_headtail :
    variable
    LISTSEPARATOR
    variable
    (LISTSEPARATOR variable)?
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
 * achivement-goal action
 **/
achievment_goal_action :
    ( PLUS | MINUS ) EXCLAMATIONMARK
    | DOUBLEEXCLAMATIONMARK
    ;

/**
 * test-goal action
 **/
test_goal_action :
    ( PLUS | MINUS )
    QUESTIONMARK
    ;

/**
 * belief-action operator
 **/
belief_action :
    PLUS
    | MINUS
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
 * arithmetic operator
 **/
arithmeticoperator :
    LOWERCASELETTER+
    | PLUS
    | MINUS
    | MULTIPLY
    | DIVIDE
    | MODULO
    | POW
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
    MINUS?
    ( DIGIT+ DOT DIGIT+ | constant )
    ;

/**
 * integer number
 **/
integernumber :
    MINUS? DIGIT+
    ;

/**
 * boolean values
 **/
logicalvalue :
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
 * plan annotation
 **/
PARALLEL                   : 'parallel';
ATOMIC                     : 'atomic';
EXCLUSIVE                  : 'exclusive';
FUZZY                      : 'fuzzy';
PRIORITY                   : 'priority';
EXPIRES                    : 'expires';


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


POW                        : '**';
MULTIPLY                   : '*';
DIVIDE                     : '/';
MODULO                     : '%';

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
