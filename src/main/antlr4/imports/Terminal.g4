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

/**
 * grammar defines the terminal symbols of all grammar rules
 **/
lexer grammar Terminal;

/**
 * binary operator
 **/
BINARYOPERATOR :
    ASSIGNINCREMENT
    | ASSIGNDECREMENT
    | ASSIGNMULTIPLY
    | ASSIGNDIVIDE
    | ASSIGNMODULO
    | ASSIGNPOW
    ;

/**
 * unary operator
 **/
UNARYOPERATOR :
    INCREMENT
    | DECREMENT
    ;

/**
 * string define with single or double quotes
 **/
STRING :
    SINGLEQUOTESTRING
    | DOUBLEQUOTESTRING
    ;

/**
 * boolean values
 **/
LOGICALVALUE :
    TRUE
    | FALSE
    ;

/**
 * floating-point constants
 **/
CONSTANTNUMBER :
    PI
    | EULER
    | GRAVITY
    | AVOGADRO
    | BOLTZMANN
    | ELECTRON
    | PROTON
    | NEUTRON
    | LIGHTSPEED
    | POSITIVEINFINITY
    | NEGATIVEINFINITY
    | MAXIMUMVALUE
    | MINIMUMVALUE
    | NAN
    ;

// --- character structures --------------------------------------------------------------

EXCLAMATIONMARK            : '!';
STRONGNEGATION             : '~';
COMMA                      : ',';
PLUS                       : '+';
MINUS                      : '-';
DOUBLEEXCLAMATIONMARK      : '!!';
QUESTIONMARK               : '?';
DOLLAR                     : '$';

LEFTARROW                  : '<-';
RIGHTARROW                 : '->';
RULEOPERATOR               : ':-';
AT                         : '@';
COLON                      : ':';
SEMICOLON                  : ';';
DOT                        : '.';
UNDERSCORE                 : '_';
SLASH                      : '/';

fragment PI                : 'pi';
fragment EULER             : 'euler';
fragment GRAVITY           : 'gravity';
fragment AVOGADRO          : 'avogadro';
fragment BOLTZMANN         : 'boltzmann';
fragment ELECTRON          : 'electron';
fragment PROTON            : 'proton';
fragment NEUTRON           : 'neutron';
fragment LIGHTSPEED        : 'lightspeed';
fragment POSITIVEINFINITY  : 'positiveinfinity';
fragment NEGATIVEINFINITY  : 'negativeinfinity';
fragment MAXIMUMVALUE      : 'maximumvalue';
fragment MINIMUMVALUE      : 'minimumvalue';
fragment NAN               : 'notanumber';

LEFTROUNDBRACKET           : '(';
RIGHTROUNDBRACKET          : ')';
LEFTANGULARBRACKET         : '[';
RIGHTANGULARBRACKET        : ']';
LEFTCURVEDBRACKET          : '{';
RIGHTCURVEDBRACKET         : '}';

fragment TRUE              : 'true' | 'success';
fragment FALSE             : 'false' | 'fail';

LEFTSHIFT                  : '<<';
RIGHTSHIFT                 : '>>';

/**
 * plan annotation
 **/
PARALLEL                   : 'parallel';
ATOMIC                     : 'atomic';
CONSTANT                   : 'constant';


/**
 * prolog list seperator for head-tail definition
 **/
VLINE                      : '|';

/**
 * logical and-concationation (c-style)
 **/
AND                        : '&&';
/**
 * logical or-concationation (c-style)
 **/
OR                         : '||';
/**
 * logical xor-concationation (c-style)
 **/
XOR                        : '^';

/**
 * variable operators
 **/
fragment INCREMENT         : '++';
fragment DECREMENT         : '--';
ASSIGN                     : '=';
DECONSTRUCT                : '=..';
fragment ASSIGNINCREMENT   : '+=';
fragment ASSIGNDECREMENT   : '-=';
fragment ASSIGNMULTIPLY    : '*=';
fragment ASSIGNDIVIDE      : '/=';
fragment ASSIGNMODULO      : '%=';
fragment ASSIGNPOW         : '^=';

/**
 * comparator types
 **/
LESS                       : '<';
LESSEQUAL                  : '<=';
GREATER                    : '>';
GREATEREQUAL               : '>=';
EQUAL                      : '==';
NOTEQUAL                   : '\\==' | '!=';


POW                        : '**';
MULTIPLY                   : '*';
MODULO                     : '%';

/**
 * string can be definied in single- and double-quotes
 **/
fragment SINGLEQUOTESTRING : '\'' ~('\'')* '\'';
fragment DOUBLEQUOTESTRING : '"' ~('"')* '"';

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
LINECOMMENT                : ('//' | '#') .*? '\r'? '\n' -> skip;
/**
 * block comment allowed within the grammar
 * default behaviour does not allow block comments
 **/
BLOCKCOMMENT                    :   '/*' .*? '*/' -> skip;

// ---------------------------------------------------------------------------------------
