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
    STRONGNEGATION? literal DOT
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
principles :
    principle+
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
    ( COLON plan_context )?
    ARROW body
    ;

/**
 * rules are similar to plans
 * but without context and trigger event
 **/
principle :
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
    ( FUZZY | PRIORITY )
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
 * logical context for plan matching
 **/
plan_context :
    logical_expression
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
    (belief_action | achievment_goal_action | test_goal_action )? literal
    | if_else
    | while_loop
    | for_loop
    | foreach_loop
    | assignment_expression
    | unary_expression
    | logical_expression
    | deconstruct_expression
    ;

block_formula :
    LCURVEDBRACKET body RCURVEDBRACKET
    | body_formula
    ;

if_else :
    IF LROUNDBRACKET logical_expression RROUNDBRACKET
    block_formula
    ( ELSE block_formula )?
    ;

while_loop :
    WHILE LROUNDBRACKET logical_expression RROUNDBRACKET
    block_formula
    ;

for_loop :
    FOR LROUNDBRACKET assignment_expression? SEMICOLON logical_expression SEMICOLON assignment_expression? RROUNDBRACKET
    block_formula
    ;

foreach_loop :
    FOR LROUNDBRACKET term RROUNDBRACKET
    block_formula
    ;

/**
 * logical expression
 **/
logical_expression :
    NEGATION logical_expression
    | LROUNDBRACKET logical_expression RROUNDBRACKET
    | logical_expression (XOR | AND | OR) logical_expression
    | comparison_expression
    | logicalvalue
    | literal
    ;

/**
 * comparision expression
 **/
comparison_expression :
    arithmetic_expression comparator arithmetic_expression
    ;

/**
 * arithmetic expression
 **/
arithmetic_expression :
    MINUS? LROUNDBRACKET arithmetic_expression RROUNDBRACKET
    | arithmetic_expression arithmeticoperator arithmetic_expression
    | number
    | variable
    | literal
    ;

/**
 * assignment expression (for assignin a variable)
 **/
assignment_expression :
    variable
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
 * deconstruct expression (splitting clauses)
 **/
deconstruct_expression :
    LANGULARBRACKET
    list_headtail
    RANGULARBRACKET
    DECONSTRUCT
    ( literal | variable )
    ;
// ---------------------------------------------------------------------------------------



// --- complex-data-types ----------------------------------------------------------------

/**
 * clause represent a literal structure existing
 * atom, optional argument, optional annotations
 **/
literal :
    atom
    ( LROUNDBRACKET list RROUNDBRACKET )?
    ( LANGULARBRACKET list RANGULARBRACKET )?
    ;

/**
 * terms are predictable structures
 **/
term :
    variable
    | string
    | number
    | literal
    | arithmetic_expression
    | logical_expression
    | LANGULARBRACKET list RANGULARBRACKET
    ;

/**
 * list equal to collcations
 **/
list :
    term ( COMMA term )*
    | list_headtail
    ;

/**
 * list with head-tail-annotation definition
 **/
list_headtail :
    variable
    LISTSEPARATOR
    variable
    (LISTSEPARATOR variable)?
    ;

/**
 * atoms are defined like Prolog atoms
 * @note internal action in Jason can begin with a dot, but here it is removed
 **/
atom :
    LOWERCASELETTER
    ( LOWERCASELETTER | UPPERCASELETTER | UNDERSCORE | DIGIT )*
    ;

/**
 * variables are defined like Prolog variables
 **/
variable :
    ( UPPERCASELETTER | UNDERSCORE )
    ( LOWERCASELETTER | UPPERCASELETTER | UNDERSCORE | DIGIT )*
    ;

/**
 * achivement-goal action
 **/
achievment_goal_action :
    EXCLAMATIONMARK | DOUBLEEXCLAMATIONMARK
    ;

/**
 * test-goal action
 **/
test_goal_action :
    QUESTIONMARK
    ;

/**
 * belief-action operator
 **/
belief_action :
    PLUS
    | MINUS
    | MINUSPLUS
    ;

/**
 * comperator
 **/
comparator :
    LESS
    | LESSEQUAL
    | EQUAL
    | NOTEQUAL
    | GREATER
    | GREATEREQUAL
    ;

/**
 * arithmetic operator
 **/
arithmeticoperator :
    LOWERCASELETTER+
    | PLUS
    | MINUS
    | MULTIPLY
    | DIVIDE
    | MODULO
    | POW
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
    MINUS? DIGIT+
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
    ;

/**
 * string define with single or double quotes
 **/
string :
    SINGLEQUOTESTRING
    | DOUBLEQUOTESTRING
    ;
// ---------------------------------------------------------------------------------------




