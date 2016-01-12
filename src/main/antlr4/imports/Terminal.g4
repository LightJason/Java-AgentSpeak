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
 * grammar defines the terminal symbols of all grammar rules
 **/
lexer grammar Terminal;


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
SLASH                      : '/';

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

TRUE                       : 'true' | 'success';
FALSE                      : 'false' | 'fail';

/**
 * plan annotation
 **/
PARALLEL                   : 'parallel';
ATOMIC                     : 'atomic';
EXCLUSIVE                  : 'exclusive';
FUZZY                      : 'fuzzy';
SCORE                      : 'score';
EXPIRES                    : 'expires';
INFINITY                   : 'infinity';


/**
 * prolog list seperator for head-tail definition
 **/
LISTSEPARATOR              : '|';

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
SINGLEQUOTESTRING          : '\'' ~('\'')* '\'';
DOUBLEQUOTESTRING          : '"' ~('"')* '"';

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