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
    initial_beliefs
    initial_goals
    plans
    ;

initial_beliefs :
    beliefs
    rules
    ;

initial_goals :
    Exclamationmark
    literal
    Dot
    ;
// ---------------------------------------------------------------------------------------



// --- agent-behaviour structure ---------------------------------------------------------
beliefs :
    ( literal Dot )*
    ;

rules :
    rule*
    ;

plans :
    plan*
    ;

/**
 * rules can be used an annotation
 * and can use arithmetic expression
 **/
rule :
    ( At atomic_formula )?
    literal RuleOperator (logical_expression | arithmetic_expression) Dot
    ;

/**
 * plan modified against the original Jason grammar,
 * so a context is optional (on default true) and the
 * plan body is also optional
 */
plan :
    ( At atomic_formula )?
    trigger_event
    ( Colon context )?
    ( Arrow body )?
    Dot
    ;
// ---------------------------------------------------------------------------------------



// --- agent-expression-context ----------------------------------------------------------
trigger_event :
    ( Plus | Minus )
    ( Exclamationmark | Questionmark )?
    literal
    ;

literal :
    StrongNegotation?
    atomic_formula
    ;

context :
    logical_expression
    | True
    | False
    ;

body :
    body_formula
    ( Semicolon body_formula )*
    | True
    ;

body_formula :
    actionoperator literal
    | atomic_formula
    | variable
    | relation_expression
    | if_else
    | while_loop
    ;

block_formula :
    LAngularBracket
    body_formula*
    RAngularBracket
    ;

if_else :
    If LRoundBracket logical_expression LRoundBracket
    block_formula
    ( Else block_formula )?
    ;

while_loop :
    While LRoundBracket logical_expression RRoundBracket
    block_formula
    ;

for_loop :
    For LRoundBracket body_formula? Semicolon logical_expression Semicolon body_formula? RRoundBracket
    block_formula
    ;

foreach_loop :
    For LRoundBracket body_formula RRoundBracket
    block_formula
    ;

atomic_formula :
    ( atom | variable | variable unaryoperator | variable binaryoperator arithmetic_term )
    ( LRoundBracket term_list RRoundBracket )?
    ( LAngularBracket term_list RAngularBracket )?
    ;

logical_expression :
    Negotation? simple_logical_expression
    | Negotation? LRoundBracket logical_expression RRoundBracket
    | logical_or_expression
    ;

logical_or_expression :
    logical_and_expression
    ( Or logical_and_expression )*
    ;

logical_and_expression :
    simple_logical_expression
    ( (And | Xor) logical_expression )*
    ;

simple_logical_expression :
    True
    | False
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
    ( ( dashoperator | pointoperator | Assign ) arithmetic_term )*
    ;
// ---------------------------------------------------------------------------------------



// --- complex-data-types ----------------------------------------------------------------
term :
    literal
    | list
    | arithmetic_expression
    | variable
    | String
    ;

term_list :
    term
    ( Comma term )*
    ;

list :
    LAngularBracket
    ( term ( Comma term )*  ( Or ( list | variable ) )  )
    RAngularBracket
    ;

relation_term :
    literal
    | arithmetic_expression
    ;

arithmetic_term :
    number
    | variable
    | Minus arithmetic_term
    | LRoundBracket arithmetic_expression RRoundBracket
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

actionoperator :
    Exclamationmark
    | DoubleExclamationmark
    | Questionmark
    | Plus
    | Minus
    | MinusPlus
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
    | Pi
    | EulerNumber
    ;

integernumber :
    (Plus | Minus)? Digit+
    ;

variable :
    UpperCaseLetter ( LowerCaseLetter | UpperCaseLetter | Underscore | Digit )*
    | Underscore
    ;

/**
 * to create compatibility to Jason syntax, an atom can begin
 * with a dot, it represents an internal Jason action in the structure
 * jason.stdlib.<atom name>
 * @see http://jason.sourceforge.net/api/jason/stdlib/package-summary.html
 **/
atom :
    Dot?
    LowerCaseLetter
    ( LowerCaseLetter | UpperCaseLetter | Underscore | Digit )*
    ;
// ---------------------------------------------------------------------------------------



// --- character structures --------------------------------------------------------------
Exclamationmark        : '!';
StrongNegotation       : '~';
Comma                  : ',';
Plus                   : '+';
Minus                  : '-';
MinusPlus              : '-+';
DoubleExclamationmark  : '!!';
Questionmark           : '?';

Arrow                  : '<-';
RuleOperator           : ':-';
At                     : '@';
Colon                  : ':';
Semicolon              : ';';
Dot                    : '.';
Underscore             : '_';

If                     : 'if';
Else                   : 'else';
While                  : 'while';
For                    : 'for';
Pi                     : 'pi';
EulerNumber            : 'euler';

LRoundBracket          : '(';
RRoundBracket          : ')';
LAngularBracket        : '[';
RAngularBracket        : ']';

Negotation             : 'not';
True                   : 'true' | 'success';
False                  : 'false' | 'fail';
/**
 * allow on logical and-concationation
 * also c- and pascal-style-based & / and
 **/
And                    : '&' | '&&' | 'and';
/**
 * allow on logical or-concationation
 * also c- and pascal-style-based | / or
 **/
Or                     : '|' | '||' | 'or';
/**
 * define an logical xor-definition
 **/
Xor                    : '^' | 'xor';

/**
 * variable operators
 **/
Increment              : '++';
Decrement              : '--';
Assign                 : '=';
AssignIncrement        : '+=';
AssignDecrement        : '-=';
AssignMultiply         : '*=';
AssignDivide           : '/=';

Less                   : '<';
LessEqual              : '<=';
Greater                : '>';
GreaterEqual           : '>=';
Equal                  : '==';
NotEqual               : '\\==';
Unify                  : '=';
Deconstruct            : '=..';
/**
 * allow on pow also the key-word represenation
 **/
Pow                    : '**' | 'pow';
/**
 * allow on multiply the key-word representation
 **/
Multiply               : '*' | 'mul';
/**
 * allow on divide the key-word definition
 **/
Divide                 : '/' | 'div';
Modulo                 : '%' | 'mod';

LowerCaseLetter        : [a-z];
UpperCaseLetter        : [A-Z];
Digit                  : [0-9];
/**
 * string can be definied in single- and double-quotes
 **/
String                 : ( '\'' ('\'\'' | ~('\''))* '\'' ) | ( '"' ('""' | ~('"'))* '"' );
// ---------------------------------------------------------------------------------------


// --- skip items ------------------------------------------------------------------------
Whitespace             : (' ' | '\n' | '\t' | '\r')+ -> skip;
/**
 * add for line-comment also #
 **/
LineComment            : ('//' | '#') ~[\r\n]* -> skip;
/**
 * block comment allowed within the grammar
 * default behaviour does not allow block comments
 **/
Comment                :   '/*' .*? '*/' -> skip;
// ---------------------------------------------------------------------------------------
