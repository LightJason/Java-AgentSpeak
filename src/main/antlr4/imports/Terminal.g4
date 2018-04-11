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

lexer grammar Terminal;



// --- keyword rules must be first rules -----------------------------------------------------------------------------------------------------------------------

/**
 * boolean values
 */
LOGICALVALUE :
    TRUE
    | FALSE
    ;

/**
 * using floating-point number (double) and constants are used
 */
NUMBER :
    MINUS?
    ( CONSTANTNUMBER | DIGITSEQUENCE )
    ;

/**
 * floating-point constants
 */
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

/**
 * string define with single or double quotes
 */
STRING :
    SINGLEQUOTESTRING
    | DOUBLEQUOTESTRING
    ;



// --- operators must be second rules---------------------------------------------------------------------------------------------------------------------------

/**
 * strong negation must get a high priority
 */
STRONGNEGATION : '~' | 'not';

/**
 * assign operator
 */
ASSIGNOPERATOR :
    ASSIGN
    | ASSIGNINCREMENT
    | ASSIGNDECREMENT
    | ASSIGNMULTIPLY
    | ASSIGNDIVIDE
    | ASSIGNMODULO
    | ASSIGNPOW
    ;

/**
 * relational operator
 */
RELATIONALOPERATOR :
    EQUAL
    | NOTEQUAL
    | LESS
    | LESSEQUAL
    | GREATER
    | GREATEREQUAL
    ;

/**
 * logical operator with precendece 1
 */
LOGICALOPERATOR1 :
    XOR
    ;

/**
 * logical operator with precendece 2
 */
LOGICALOPERATOR2 :
    AND
    ;

/**
 * logical operator with precendece 3
 */
LOGICALOPERATOR3 :
    OR
    ;

/**
 * arithmetic operator with precendece 1
 */
ARITHMETICOPERATOR1 :
    POW
    ;

/**
 * arithmetic operator with precendece 2
 */
ARITHMETICOPERATOR2 :
    MULTIPLY
    | DIVIDE
    | MODULO
    ;

/**
 * arithmetic operator with precendece 3
 * @warning is also used for the belieftrigger
 */
ARITHMETICOPERATOR3 :
    PLUS
    | MINUS
    ;

/**
 * unary operator
 */
UNARYOPERATOR :
    INCREMENT
    | DECREMENT
    ;



// --- annotation and base structure ---------------------------------------------------------------------------------------------------------------------------

/**
 * annotation for rules and plans
 */
ANNOTATION :
    AT
    (
        ANNOTATION_CONSTANT
        | ANNOTATION_VARIABLEDESCRIPTION
        | ANNOTATION_STRING
        | PARALLEL
        | ATOMIC
    )
    ;

/**
 * annotation with string value
 */
ANNOTATION_STRING :
    ( DESCRIPTION | TAG )
    LEFTROUNDBRACKET
    STRING
    RIGHTROUNDBRACKET
    ;

/**
 * variable description annotation
 */
ANNOTATION_VARIABLEDESCRIPTION :
    VARIABLE
    LEFTROUNDBRACKET
    VARIABLEATOM
    COMMA
    STRING
    RIGHTROUNDBRACKET
    ;

/**
 * constant annotation
 */
ANNOTATION_CONSTANT :
    CONSTANT
    LEFTROUNDBRACKET
    VARIABLEATOM
    COMMA
    ( NUMBER | STRING )
    RIGHTROUNDBRACKET
    ;

/**
 * name structure of a variable
 */
VARIABLEATOM :
    ( UPPERCASELETTER | UNDERSCORE )
    ( LOWERCASELETTER | UPPERCASELETTER | DIGIT | SLASH )*
    ;

/**
 * atoms are defined like Prolog atoms
 */
ATOM :
    LOWERCASELETTER
    ( LOWERCASELETTER | UPPERCASELETTER | DIGIT | SLASH | MINUS | UNDERSCORE )*
    ;

/**
 * rule to represent the initial goal
 */
INITIALGOAL :
    EXCLAMATIONMARK
    ATOM
    DOT
    ;

/**
 * plan trigger
 */
PLANTRIGGER :
    (PLUS | MINUS) EXCLAMATIONMARK?
    ;




// --- character structures ------------------------------------------------------------------------------------------------------------------------------------

EXCLAMATIONMARK            : '!';
COMMA                      : ',';
DOUBLEEXCLAMATIONMARK      : '!!';
QUESTIONMARK               : '?';
DOLLAR                     : '$';
VLINE                      : '|';
HASH                       : '#';

LEFTARROW                  : '<-';
RIGHTARROW                 : '->';
RULEOPERATOR               : ':-';
AT                         : '@';
COLON                      : ':';
SEMICOLON                  : ';';
DOT                        : '.';
UNDERSCORE                 : '_';
SLASH                      : '/';
LEFTSHIFT                  : '<<';
RIGHTSHIFT                 : '>>';

LEFTROUNDBRACKET           : '(';
RIGHTROUNDBRACKET          : ')';
LEFTANGULARBRACKET         : '[';
RIGHTANGULARBRACKET        : ']';
LEFTCURVEDBRACKET          : '{';
RIGHTCURVEDBRACKET         : '}';

fragment PLUS              : '+';
fragment MINUS             : '-';
fragment DIVIDE            : '//';

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

fragment AND               : '&&' | 'and';
fragment OR                : '||' | 'or';
fragment XOR               : '^' | 'xor';

fragment TRUE              : 'true' | 'success';
fragment FALSE             : 'false' | 'fail';


fragment CONSTANT          : 'constant';
fragment PARALLEL          : 'parallel';
fragment ATOMIC            : 'atomic';
fragment DESCRIPTION       : 'description';
fragment TAG               : 'tag';
fragment VARIABLE          : 'variable';


DECONSTRUCT                : '=..';
fragment ASSIGN            : '=';
fragment ASSIGNINCREMENT   : '+=';
fragment ASSIGNDECREMENT   : '-=';
fragment ASSIGNMULTIPLY    : '*=';
fragment ASSIGNDIVIDE      : '//=';
fragment ASSIGNMODULO      : '%=';
fragment ASSIGNPOW         : '^=';
fragment INCREMENT         : '++';
fragment DECREMENT         : '--';


fragment LESS              : '<';
fragment LESSEQUAL         : '<=';
fragment GREATER           : '>';
fragment GREATEREQUAL      : '>=';
fragment EQUAL             : '==';
fragment NOTEQUAL          : '\\==' | '!=';


fragment POW               : '**';
fragment MULTIPLY          : '*';
fragment MODULO            : '%' | 'mod';

/**
 * string can be definied in single- and double-quotes
 */
fragment SINGLEQUOTESTRING : '\'' ~('\'')* '\'';
fragment DOUBLEQUOTESTRING : '"' ~('"')* '"';

/**
 * char definitions
 */
fragment LOWERCASELETTER   : [a-z];
fragment UPPERCASELETTER   : [A-Z];
fragment DIGIT             : [0-9];
fragment DIGITSEQUENCE     : DIGIT+ ('.' DIGIT+)?;



// --- skip items ----------------------------------------------------------------------------------------------------------------------------------------------

/**
 * any whitespace
 */
WHITESPACE                 : (' ' | '\n' | '\t' | '\r')+ -> skip;
/**
 * add for line-comment also
 */
LINECOMMENT                : ('//' | '#') .*? '\r'? '\n' -> skip;
/**
 * block comment allowed within the grammar
 * default behaviour does not allow block comments
 */
BLOCKCOMMENT                    :   '/*' .*? '*/' -> skip;
