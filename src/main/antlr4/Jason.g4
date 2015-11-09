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
    beliefs
    ;

initial_goal :
    Exclamationmark
    atom
    Dot
    ;
// ---------------------------------------------------------------------------------------



// --- agent-behaviour structure ---------------------------------------------------------
beliefs :
    ( StrongNegotation atom Dot )+
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
    ( At atomic_formula )?
    plan_trigger
    ( Colon plan_context )?
    ( Arrow body )?
    ( Arrow atom )?
    Dot
    ;

/**
 * rules are similar to plans
 * but without context and trigger event
 **/
rule :
    ( At atomic_formula )?
    atom
    RuleOperator
    body
    Dot
    ;

plan_trigger :
    ( Plus | Minus )
    Exclamationmark
    atom
    ( LRoundBracket list RRoundBracket )?
    ;

plan_context :
    logical_expression
    ;

body :
    body_formula
    ( Semicolon body_formula )*
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
    LCurvedBracket
    body
    RCurvedBracket
    ;

if_else :
    If LRoundBracket logical_expression RRoundBracket
    block_formula
    ( Else block_formula )?
    ;

while_loop :
    While LRoundBracket logical_expression RRoundBracket
    block_formula
    ;

for_loop :
    For LRoundBracket assignment_expression? Semicolon logical_expression Semicolon assignment_expression? RRoundBracket
    block_formula
    ;

foreach_loop :
    For LRoundBracket term RRoundBracket
    block_formula
    ;

logical_expression :
    logical_expression (And | Xor) logical_expression
    | logical_expression Or logical_expression
    | comparison_expression
    | LRoundBracket logical_expression RRoundBracket
    | boolean
    ;

comparison_expression :
    arithmetic_expression comparator arithmetic_expression
    | LRoundBracket comparison_expression RRoundBracket
    ;

arithmetic_expression :
    arithmetic_expression pointoperator arithmetic_expression
    | arithmetic_expression dashoperator arithmetic_expression
    | (Minus | Plus) arithmetic_expression
    | LRoundBracket arithmetic_expression RRoundBracket
    | number
    | variable
    ;

assignment_expression :
    variable
    Equal
    term
    ;
// ---------------------------------------------------------------------------------------



// --- complex-data-types ----------------------------------------------------------------
atomic_formula :
    atom
    ( LRoundBracket list RRoundBracket )?
    ( LAngularBracket list RAngularBracket )?
    ;

term :
    | LAngularBracket list RAngularBracket
    | atom
    | variable
    | String
    ;

list :
    term
    ( Comma term )*
    ;

/**
 * atoms are defined like Prolog atoms
 * @note compatibility with Jason syntax, an atom can begin
 * with a dot, it represents an internal Jason action, the dot
 * will ignored always
 **/
atom :
    Dot?
    LowerCaseLetter
    ( LowerCaseLetter | UpperCaseLetter | Underscore | Digit )*
    ;

/**
 * variables are defined like Prolog variables
 **/
variable :
    ( UpperCaseLetter | Underscore )
    ( LowerCaseLetter | UpperCaseLetter | Underscore | Digit )*
    ;



plan_actionoperator :
    Exclamationmark
    | DoubleExclamationmark
    | Questionmark
    ;

belief_actionoperator :
    Plus
    | Minus
    | MinusPlus
    ;

comparator :
    Less
    | LessEqual
    | Equal
    | NotEqual
    | Greater
    | GreaterEqual
    | Unify
    | Deconstruct
    ;

dashoperator :
    Plus
    | Minus
    ;

pointoperator :
    Pow
    | Multiply
    | Divide
    | Modulo
    ;

unaryoperator :
    Increment
    | Decrement
    ;

binaryoperator :
    AssignIncrement
    | AssignDecrement
    | AssignMultiply
    | AssignDivide
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
    (Plus | Minus)? Digit Dot Digit+
    | constant
    ;

integernumber :
    (Plus | Minus)? Digit+
    ;

boolean :
    True
    | False
    | literal
    ;

constant :
    Pi
    | Euler
    | Gravity
    | Avogadro
    | Boltzmann
    | Electron
    | Proton
    | Neutron
    | Lightspeed
    ;
// ---------------------------------------------------------------------------------------



// --- character structures --------------------------------------------------------------
Exclamationmark            : '!';
StrongNegotation           : '~';
Comma                      : ',';
Plus                       : '+';
Minus                      : '-';
MinusPlus                  : '-+';
DoubleExclamationmark      : '!!';
Questionmark               : '?';

Arrow                      : '<-';
RuleOperator               : ':-';
At                         : '@';
Colon                      : ':';
Semicolon                  : ';';
Dot                        : '.';
Underscore                 : '_';

If                         : 'if';
Else                       : 'else';
While                      : 'while';
For                        : 'for';

Pi                         : 'pi';
Euler                      : 'euler';
Gravity                    : ' gravity';
Avogadro                   : 'avogadro';
Boltzmann                  : 'boltzmann';
Electron                   : 'electron';
Proton                     : 'proton';
Neutron                    : 'neutron';
Lightspeed                 : 'lightspeed';

LRoundBracket              : '(';
RRoundBracket              : ')';
LAngularBracket            : '[';
RAngularBracket            : ']';
LCurvedBracket             : '{';
RCurvedBracket             : '}';

Negotation                 : 'not';
True                       : 'true' | 'success';
False                      : 'false' | 'fail';
/**
 * allow on logical and-concationation
 * also c- and pascal-style-based & / and
 **/
And                        : '&' | '&&';
/**
 * allow on logical or-concationation
 * also c- and pascal-style-based | / or
 **/
Or                         : '|' | '||';
/**
 * define an logical xor-definition
 **/
Xor                        : '^' | 'xor';

/**
 * variable operators
 **/
Increment                  : '++';
Decrement                  : '--';
Assign                     : '=';
AssignIncrement            : '+=';
AssignDecrement            : '-=';
AssignMultiply             : '*=';
AssignDivide               : '/=';

Less                       : '<';
LessEqual                  : '<=';
Greater                    : '>';
GreaterEqual               : '>=';
Equal                      : '==';
NotEqual                   : '\\==';
Unify                      : '=';
Deconstruct                : '=..';
/**
 * allow on pow also the key-word represenation
 **/
Pow                        : '**' | 'pow';
/**
 * allow on multiply the key-word representation
 **/
Multiply                   : '*' | 'mul';
/**
 * allow on divide the key-word definition
 **/
Divide                     : '/' | 'div';
Modulo                     : '%' | 'mod';

fragment LowerCaseLetter   : [a-z];
fragment UpperCaseLetter   : [A-Z];
fragment Digit             : [0-9];
/**
 * string can be definied in single- and double-quotes
 **/
String                     : ( '\'' ('\'\'' | ~('\''))* '\'' ) | ( '"' ('""' | ~('"'))* '"' );
// ---------------------------------------------------------------------------------------


// --- skip items ------------------------------------------------------------------------
Whitespace                 : (' ' | '\n' | '\t' | '\r')+ -> skip;
/**
 * add for line-comment also #
 **/
LineComment                : ('//' | '#') ~[\r\n]* -> skip;
/**
 * block comment allowed within the grammar
 * default behaviour does not allow block comments
 **/
Comment                    :   '/*' .*? '*/' -> skip;
// ---------------------------------------------------------------------------------------
