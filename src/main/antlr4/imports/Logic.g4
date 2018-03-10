/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
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

grammar Logic;
import Terminal;


/**
 * terms are non-predictable structures
 */
term :
    STRING
    | NUMBER
    | LOGICALVALUE

    | execute_action
    | execute_rule
    | execute_variable

    | literal
    | variable
    ;

/**
 * rule for an action
 */
execute_action :
    DOT
    literal;

/**
 * rule for execute a logical-rule
 */
execute_rule :
    DOLLAR ( literal | execute_variable )
    ;

/**
 * variable-evaluation will be used for an executable call
 * like X(1,2,Y), it is possible for passing variables and parameters
 */
execute_variable :
    DOT
    variable
    ( LEFTROUNDBRACKET termlist RIGHTROUNDBRACKET )?
    ;

/**
 * clause represent a literal structure existing
 * atom, optional argument
 */
literal :
    ( AT | STRONGNEGATION )?
    ATOM
    ( LEFTROUNDBRACKET termlist? RIGHTROUNDBRACKET )?
    ;

/**
 * generic list equal to collcations with empty clause
 */
termlist :
    term ( COMMA term )*
    ;

/**
 * list with head-tail-notation definition
 */
variablelist :
    LEFTANGULARBRACKET
    variable ( VLINE variable )*
    RIGHTANGULARBRACKET
    ;

/**
 * variables are defined like Prolog variables,
 * @-prefix creates a thread-safe variable
 */
variable :
    AT?
    VARIABLEATOM
    ;
