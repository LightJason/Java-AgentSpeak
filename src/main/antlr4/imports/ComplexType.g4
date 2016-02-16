/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU Lesser General Public License as                     #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU Lesser General Public License for more details.                                #
 * #                                                                                    #
 * # You should have received a copy of the GNU Lesser General Public License           #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

/**
 * grammar for complex-datatypes
 **/
grammar ComplexType;
import Terminal;


// --- expression context ----------------------------------------------------------------

/**
 * terms are predictable structures
 **/
term :
    string
    | number
    | logicalvalue

    | literal
    | variable

    | variablelist
    | LEFTANGULARBRACKET termlist RIGHTANGULARBRACKET

    | expression
    | ternary_operation
    ;

/**
 * unification expression
 **/
unification :
 AT? RIGHTSHIFT
 ( literal | LEFTROUNDBRACKET literal COMMA expression RIGHTROUNDBRACKET )
 ;

/**
 * ternary operation
 **/
ternary_operation :
    expression
    ternary_operation_true
    ternary_operation_false
    ;

/**
 * ternary operation true-rule
 **/
ternary_operation_true :
    QUESTIONMARK
    term
    ;

/**
 * ternary operation false-rule
 **/
ternary_operation_false :
    COLON
    term
    ;

/**
 * logical & numeric entry rule for or-expression
 **/
expression :
    expression_bracket
    | expression_logical_and ( OR expression )*
    ;

/**
 * bracket expression
 **/
expression_bracket :
    LEFTROUNDBRACKET expression RIGHTROUNDBRACKET
    ;

/**
 * logical and-expression
 **/
expression_logical_and :
    expression_logical_xor ( AND expression )*
    ;

/**
 * logical xor-expression
 **/
expression_logical_xor :
    ( expression_logical_element | expression_logical_negation | expression_numeric ) ( XOR expression )*
    ;

/**
 * logic element for expressions
 **/
expression_logical_element :
    logicalvalue
    | variable
    | literal
    | unification
    ;

/**
 * negated expression
 **/
expression_logical_negation :
    EXCLAMATIONMARK expression
    ;

/**
 * numerical entry rule for equal expression
 **/
expression_numeric :
    expression_numeric_relation ( (EQUAL | NOTEQUAL) expression_numeric )?
    ;

/**
 * relation expression
 **/
expression_numeric_relation :
    expression_numeric_additive ( (LESS | LESSEQUAL | GREATER | GREATEREQUAL) expression_numeric )?
    ;

/**
 * numeric addition-expression
 **/
expression_numeric_additive :
    expression_numeric_multiplicative ( (PLUS | MINUS) expression_numeric )?
    ;

/**
 * numeric multiply-expression
 **/
expression_numeric_multiplicative :
    expression_numeric_power ( (SLASH | MODULO | MULTIPLY ) expression_numeric )?
    ;

/**
 * numeric pow-expression
 **/
expression_numeric_power :
    expression_numeric_element ( POW expression_numeric )?
    ;

/**
 * numeric element for expression
 **/
expression_numeric_element :
    number
    | variable
    | literal
    | LEFTROUNDBRACKET expression_numeric RIGHTROUNDBRACKET
    ;

// ---------------------------------------------------------------------------------------



// --- complex-data-types ----------------------------------------------------------------

/**
 * clause represent a literal structure existing
 * atom, optional argument, optional annotations
 **/
literal :
    ( AT | STRONGNEGATION )?
    atom
    ( LEFTROUNDBRACKET termlist? RIGHTROUNDBRACKET )?
    ( LEFTANGULARBRACKET literalset? RIGHTANGULARBRACKET )?
    ;

/**
 * generic list equal to collcations with empty clause
 **/
termlist :
    term ( COMMA term )*
    ;

/**
 * specified list only with literals and empty clause
 **/
literalset :
    literal ( COMMA literal )*
    ;

/**
 * list with head-tail-annotation definition
 **/
variablelist :
    LEFTANGULARBRACKET
    variable ( VLINE variable )*
    RIGHTANGULARBRACKET
    ;

/**
 * atoms are defined like Prolog atoms
 * @note internal action in Jason can begin with a dot, but here it is removed
 **/
atom :
    LOWERCASELETTER
    ( LOWERCASELETTER | UPPERCASELETTER | UNDERSCORE | DIGIT | SLASH )*
    ;

/**
 * variables are defined like Prolog variables,
 * @-prefix creates a thread-safe variable
 **/
variable :
    AT?
    ( UPPERCASELETTER | UNDERSCORE )
    ( LOWERCASELETTER | UPPERCASELETTER | UNDERSCORE | DIGIT | SLASH )*
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
    integernumber_positive
    | integernumber_negative
    ;

/**
 * positive integer number
 **/
integernumber_positive :
    PLUS? DIGIT+
    ;

/**
 * negative integer number
 **/
integernumber_negative :
    MINUS DIGIT+
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
    | INFINITY
    ;

/**
 * string define with single or double quotes
 **/
string :
    SINGLEQUOTESTRING
    | DOUBLEQUOTESTRING
    ;

// ---------------------------------------------------------------------------------------
