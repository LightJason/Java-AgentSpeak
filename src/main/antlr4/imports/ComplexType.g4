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


// https://stackoverflow.com/questions/30976962/nested-boolean-expression-parser-using-antlr
// http://www.gregbugaj.com/?p=251
// http://meri-stuff.blogspot.de/2011/09/antlr-tutorial-expression-language.html


equation :
    expression
    RELATIONALOPERATOR
    expression
    ;

expression :
    termx ( (PLUS | MINUS ) termx )*
    ;

termx :
    factor ( ( MULTIPLY | SLASH | MODULO ) factor)*
    ;

factor :
    value ( POW value)*
    ;

value :
       number
       | variable



// ---------------------------------------------------------------------------------------

// --- executable elements ---------------------------------------------------------------

/**
 * rule for an action
 **/
execute_action :
    DOT
    literal;

/**
 * rule for execute a logical-rule
 **/
execute_rule :
    DOLLAR ( literal | execute_variable )
    ;

/**
 * variable-evaluation will be used for an executable call
 * like X(1,2,Y), it is possible for passing variables and parameters
 **/
execute_variable :
    variable
    ( LEFTROUNDBRACKET termlist RIGHTROUNDBRACKET )?
    ;











// --- complex-data-types ----------------------------------------------------------------

/**
 * terms are non-predictable structures
 **/
term :
    STRING
    | NUMBER
    | LOGICALVALUE

    | literal
    | variable

    | variablelist
    | LEFTANGULARBRACKET termlist RIGHTANGULARBRACKET
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
    ( LOWERCASELETTER | UPPERCASELETTER | DIGIT | SLASH | MINUS )*
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
    ( LOWERCASELETTER | UPPERCASELETTER | DIGIT | SLASH )*
    ;

// ---------------------------------------------------------------------------------------
