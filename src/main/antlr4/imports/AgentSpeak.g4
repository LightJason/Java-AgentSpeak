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
 * base grammar rules of an additional version of AgentSpeak(L) without any terminal symbols,
 * the rules are restricted to the AgentSpeak elements e.g. beliefs, plan, ...
 **/
grammar AgentSpeak;
import Terminal;


// --- agent-behaviour structure ---------------------------------------------------------

/**
 * belief rule
 **/
belief :
    literal DOT
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
logicrules :
    logicrule+
    ;

/**
 * plan modified against the original Jason grammar,
 * so a context is optional (on default true) and the
 * plan body is also optional. The definition is
 * trigger name [ plancontent ]* .
 */
plan :
    annotations?
    plan_trigger
    literal
    plandefinition*
    DOT
    ;

/**
 * plan body & context definition
 * The definition is [ : condition ] [ <- body ]
 **/
plandefinition :
    ( COLON expression )?
    LEFTARROW body
    ;

/**
 * rules are similar to plans
 * but without context and trigger event
 **/
logicrule :
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
    ( FUZZY | SCORE )
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
    PLUS | MINUS
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
    | term

    | barrier

    | belief_action
    | test_goal_action
    | achievement_goal_action

    | deconstruct_expression
    | assignment_expression
    | unary_expression

    | lambda
    ;

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
    | LANGULARBRACKET termlist RANGULARBRACKET

    | expression
    | ternary_operation
    ;

/**
 * belief-action operator
 **/
belief_action :
    ( PLUS | MINUS | MINUSPLUS ) literal
    ;

/**
 * test-goal action
 **/
test_goal_action :
    QUESTIONMARK literal
    ;

/**
 * achivement-goal action
 **/
achievement_goal_action :
    ( EXCLAMATIONMARK | DOUBLEEXCLAMATIONMARK ) literal
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
 * deconstruct expression (splitting clauses)
 **/
deconstruct_expression :
    variablelist
    DECONSTRUCT
    ( literal | variable )
    ;

/**
 * barrier expression (synchronization)
 **/
barrier :
    LESS expression ( COMMA integernumber_positive )? GREATER
    ;

/**
 * unification expression
 **/
unification :
 AT? RSHIFT
 ( literal | LROUNDBRACKET literal COMMA expression RROUNDBRACKET )
 ;

/**
 * assignment expression (for assignin a variable)
 **/
assignment_expression :
    assignment_expression_singlevariable
    | assignment_expression_multivariable
    ;

/**
 * assignment of a single variable
 **/
assignment_expression_singlevariable :
    variable
    ASSIGN
    term
    ;

/**
 * assignment of a variable list
 **/
assignment_expression_multivariable :
    variablelist
    ASSIGN
    term
    ;

/**
 * unary expression
 **/
unary_expression :
    variable
    unaryoperator
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
    LROUNDBRACKET expression RROUNDBRACKET
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
    | LROUNDBRACKET expression_numeric RROUNDBRACKET
    ;

/**
 * block-formula of subsection
 **/
block_formula :
    LCURVEDBRACKET body RCURVEDBRACKET
    | body_formula
    ;

/**
 * lambda expression for iteration
 **/
lambda :
    AT? lambda_initialization
    RIGHTARROW variable
    lambda_return?
    COLON block_formula
    ;

/**
 * initialization of lambda expression
 **/
lambda_initialization :
    LROUNDBRACKET
    ( variable | literal )
    RROUNDBRACKET
    ;

/**
 * return argument lambda expression
 **/
lambda_return :
    VLINE variable
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
    ( LROUNDBRACKET termlist? RROUNDBRACKET )?
    ( LANGULARBRACKET literalset? RANGULARBRACKET )?
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
    LANGULARBRACKET
    variable ( VLINE variable )*
    RANGULARBRACKET
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
