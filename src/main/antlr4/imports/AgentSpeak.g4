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
 * base grammar rules of an additional version of AgentSpeak(L) without any terminal symbols,
 * the rules are restricted to the AgentSpeak elements e.g. beliefs, plan, ...
 **/
grammar AgentSpeak;
import ComplexType;


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
    logicalruledefinition+
    DOT
    ;

/**
 * rule definition similar to plan
 **/
logicalruledefinition :
    RULEOPERATOR
    body
    ;

/**
 * annotation for rules and plans
 **/
annotations :
    AT ( ANNOTATIONATOM | annotation_literal )+
    ;

/**
 * annotation (with parameter)
 **/
annotation_literal :
    CONSTANT
    LEFTROUNDBRACKET
    variableatom
    COMMA
    ( NUMBER | STRING )
    RIGHTROUNDBRACKET
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



// --- agent-execution-context -----------------------------------------------------------

/**
 * basic executable formula
 **/
body_formula :
    ternary_operation
    | belief_action

    | deconstruct_expression
    | assignment_expression
    | unary_expression
    | binary_expression

    | repair_formula
    | expression
    | unification
    | lambda
    ;


/**
 * repairable formula
 **/
repair_formula :
    ( term | test_action | achievement_goal_action )
    ( LEFTSHIFT repair_formula )?
    ;

/**
 * belief-action operator
 **/
belief_action :
    ( PLUS | MINUS ) literal
    ;

/**
 * test-goal / -rule action
 **/
test_action :
    QUESTIONMARK DOLLAR? atom
    ;

/**
 * achivement-goal action
 **/
achievement_goal_action :
    ( EXCLAMATIONMARK | DOUBLEEXCLAMATIONMARK ) ( literal | execute_variable )
    ;

// ---------------------------------------------------------------------------------------



// --- assignment structures -------------------------------------------------------------

/**
 * deconstruct expression (splitting clauses)
 **/
deconstruct_expression :
    variablelist
    DECONSTRUCT
    ( literal | variable )
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
    UNARYOPERATOR
    ;

/**
 * binary expression
 **/
binary_expression :
    variable
    BINARYOPERATOR
    expression
    ;

// https://stackoverflow.com/questions/30976962/nested-boolean-expression-parser-using-antlr

/**
 * expression rule
 **/
expression :
    LEFTROUNDBRACKET expression RIGHTROUNDBRACKET
    | STRONGNEGATION expression
    | lhs=expression  operator=ARITHMETICOPERATOR1 rhs=expression
    | lhs=expression  operator=ARITHMETICOPERATOR2 rhs=expression
    | lhs=expression  operator=ARITHMETICOPERATOR3 rhs=expression
    | lhs=expression operator=RELATIONALOPERATOR rhs=expression
    | lhs=expression operator=LOGICALOPERATOR1 rhs=expression
    | lhs=expression operator=LOGICALOPERATOR2 rhs=expression
    | term
    ;

// ---------------------------------------------------------------------------------------



// --- ternary operator -------------------------------------------------------------------

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

// ---------------------------------------------------------------------------------------


// --- unification -----------------------------------------------------------------------

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
// ---------------------------------------------------------------------------------------



// --- lambda expression -----------------------------------------------------------------

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
 * block-formula of subsection
 **/
block_formula :
    LEFTCURVEDBRACKET body RIGHTCURVEDBRACKET
    | body_formula
    ;

/**
 * initialization of lambda expression
 **/
lambda_initialization :
    LEFTROUNDBRACKET
    ( variable | lambda_range )
    RIGHTROUNDBRACKET
    ;

/**
 * lambda range
 **/
lambda_range :
    NUMBER
    ( COMMA NUMBER )?
    ;

/**
 * return argument lambda expression
 **/
lambda_return :
    VLINE variable
    ;

// ---------------------------------------------------------------------------------------
