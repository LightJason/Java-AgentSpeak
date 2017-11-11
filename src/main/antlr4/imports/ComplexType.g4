/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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
 * executable-terms are predictable structures
 **/
executable_term :
    STRING
    | number
    | LOGICALVALUE

    | executable_action
    | executable_rule

    | expression
    | ternary_operation
    ;

/**
 * terms are non-predictable structures
 **/
term :
    STRING
    | number
    | LOGICALVALUE

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
 (
   literal
   | LEFTROUNDBRACKET literal COMMA unification_constraint RIGHTROUNDBRACKET
 )
 ;

/*
 * unification constraint
 **/
unification_constraint :
    variable
    | expression
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
    executable_term
    ;

/**
 * ternary operation false-rule
 **/
ternary_operation_false :
    COLON
    executable_term
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
    ( expression_logical_negation | expression_logical_element | expression_numeric ) ( XOR expression )*
    ;

/**
 * logic element for expressions
 **/
expression_logical_element :
    LOGICALVALUE
    | variable
    | executable_action
    | executable_rule
    | unification
    ;

/**
 * negated expression
 **/
expression_logical_negation :
    STRONGNEGATION expression
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
    | executable_action
    | executable_rule
    | LEFTROUNDBRACKET expression_numeric RIGHTROUNDBRACKET
    ;

// ---------------------------------------------------------------------------------------



// --- complex-data-types ----------------------------------------------------------------

/**
 * rule for an action
 **/
executable_action :
    literal
    ;

/**
 * rule for execute a logical-rule
 **/
executable_rule :
    DOLLAR ( literal | variable_evaluate )
    ;

/**
 * variable-evaluation will be used for an executable call
 * like X(1,2,Y), it is possible for passing variables and parameters
 **/
variable_evaluate :
    variable
    ( LEFTROUNDBRACKET termlist RIGHTROUNDBRACKET )?
    ;

/**
 * clause represent a literal structure existing
 * atom, optional argument
 **/
literal :
    ( AT | STRONGNEGATION )?
    atom
    ( LEFTROUNDBRACKET termlist? RIGHTROUNDBRACKET )?
    ;

/**
 * generic list equal to collcations with empty clause
 **/
termlist :
    term ( COMMA term )*
    ;

/**
 * list with head-tail-notation definition
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
    ( LOWERCASELETTER | UPPERCASELETTER | UNDERSCORE | DIGIT | SLASH | MINUS )*
    ;

/**
 * variables are defined like Prolog variables,
 * @-prefix creates a thread-safe variable
 **/
variable :
    AT?
    variableatom
    ;

/**
 * name structure of a variable
 **/
variableatom:
    ( UPPERCASELETTER | UNDERSCORE )
    ( LOWERCASELETTER | UPPERCASELETTER | UNDERSCORE | DIGIT | SLASH )*
    ;

/**
 * default behaviour in Jason is only a floating-point number (double)
 * but here exists the difference between floating and integral number
 * types within the grammar, the div-operator (integer division) is removed,
 * also definied constants are used
 **/
number :
    MINUS?
    ( CONSTANTNUMBER | digitsequence )
    ;

/**
 * number definition
 **/
digitsequence :
    DIGIT+ ( DOT DIGIT+ )?
    ;

// ---------------------------------------------------------------------------------------
