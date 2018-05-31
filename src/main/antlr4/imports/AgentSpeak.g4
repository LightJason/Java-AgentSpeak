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
 * base grammar rules of an additional version of AgentSpeak(L) without any terminal symbols,
 * the rules are restricted to the AgentSpeak elements e.g. beliefs, plan, ...
 */
grammar AgentSpeak;
import Terminal;


// --- agent-behaviour structure ---------------------------------------------------------

/**
 * belief rule
 */
belief :
    literal DOT
    ;

/**
 * plan modified against the original Jason grammar,
 * so a context is optional (on default true) and the
 * plan body is also optional. The definition is
 * trigger name [ plancontent ]* .
 */
plan :
    ANNOTATION*
    plantrigger
    literal
    plandefinition+
    DOT
    ;

/**
 * plan trigger
 */
plantrigger :
    ARITHMETICOPERATOR3 EXCLAMATIONMARK?
    ;


/**
 * plan definition
 */
plandefinition :
    ( COLON expression )? LEFTARROW body
    ;

/**
 * rules are similar to plans
 * but without context and trigger event
 */
logicrule :
    literal
    ( RULEOPERATOR body )+
    DOT
    ;

/**
 * block body
 */
body :
    repairformula
    ( SEMICOLON repairformula )*
    ;

/**
 * block-formula of subsection
 */
blockformula :
    repairformula
    | ( LEFTCURVEDBRACKET body RIGHTCURVEDBRACKET )
    ;

// ---------------------------------------------------------------------------------------



// --- agent-execution-context -----------------------------------------------------------

/**
 * repairable formula
 */
repairformula :
    bodyformula
    ( LEFTSHIFT bodyformula )*
    ;

/**
 * basic executable formula
 */
bodyformula :
    ternaryoperation
    | beliefaction

    | expression
    | deconstructexpression
    | assignmentexpression
    | unaryexpression
    | testaction
    | achievementgoal

    | unification
    | lambda
    ;

/**
 * expression rule
 */
expression :
    STRONGNEGATION single=expression
    | LEFTROUNDBRACKET single=expression RIGHTROUNDBRACKET
    | lhs=expression binaryoperator=ARITHMETICOPERATOR1 rhs=expression
    | lhs=expression binaryoperator=ARITHMETICOPERATOR2 rhs=expression
    | lhs=expression binaryoperator=ARITHMETICOPERATOR3 rhs=expression
    | lhs=expression binaryoperator=RELATIONALOPERATOR rhs=expression
    | lhs=expression binaryoperator=LOGICALOPERATOR1 rhs=expression
    | lhs=expression binaryoperator=LOGICALOPERATOR2 rhs=expression
    | lhs=expression binaryoperator=LOGICALOPERATOR3 rhs=expression
    | unification
    | term
    ;

/**
 * belief-action operator
 */
beliefaction :
    ARITHMETICOPERATOR3 literal
    ;

/**
 * test-goal / -rule action
 */
testaction :
    QUESTIONMARK DOLLAR? ATOM
    ;

/**
 * achivement-goal action
 */
achievementgoal :
    ( EXCLAMATIONMARK | DOUBLEEXCLAMATIONMARK )
    ( literal | ( variable termlist? ) )
    ;

// ---------------------------------------------------------------------------------------



// --- assignment structures -------------------------------------------------------------

/**
 * deconstruct expression (splitting clauses)
 */
deconstructexpression :
    variablelist
    DECONSTRUCT
    ( literal | variable )
    ;

/**
 * assignment expression (for assignin a variable)
 */
assignmentexpression :
    assignmentexpressionsinglevariable
    | assignmentexpressionmultivariable
    ;

/**
 * assignment of a single variable
 */
assignmentexpressionsinglevariable :
    variable
    ASSIGNOPERATOR
    ( ternaryoperation | expression )
    ;

/**
 * assignment of a variable list
 */
assignmentexpressionmultivariable :
    variablelist
    ASSIGNOPERATOR
    ( ternaryoperation | expression )
    ;

/**
 * unary expression
 */
unaryexpression :
    variable
    UNARYOPERATOR
    ;

// ---------------------------------------------------------------------------------------



// --- ternary operator -------------------------------------------------------------------

/**
 * ternary operation
 */
ternaryoperation :
    expression
    ternaryoperationtrue
    ternaryoperationfalse
    ;

/**
 * ternary operation true-rule
 */
ternaryoperationtrue :
    QUESTIONMARK
    expression
    ;

/**
 * ternary operation false-rule
 */
ternaryoperationfalse :
    COLON
    expression
    ;

// ---------------------------------------------------------------------------------------


// --- unification -----------------------------------------------------------------------

/**
 * unification expression
 */
unification :
    AT? RIGHTSHIFT
    (
        literal
        | LEFTROUNDBRACKET literal COMMA unificationconstraint RIGHTROUNDBRACKET
    )
    ;

/**
 * unification constraint
 */
unificationconstraint :
    variable
    | expression
    ;

// ---------------------------------------------------------------------------------------



// --- lambda expression -----------------------------------------------------------------

/**
 * lambda expression
 */
lambda :
    AT? lambdastream
    RIGHTARROW variable
    lambdareturn?
    COLON blockformula
    ;

/**
 * lambda stream operator
 */
lambdastream :
    LEFTROUNDBRACKET
    HASH?
    ( variable | NUMBER )
    lambdaelement*
    RIGHTROUNDBRACKET
    ;

/**
 * lambda elements
 */
lambdaelement :
    COMMA
    ( variable | NUMBER )
    ;

/**
 * return argument lambda expression
 */
lambdareturn :
    VLINE variable
    ;

// ---------------------------------------------------------------------------------------



// --- logic base elements ---------------------------------------------------------------

/**
 * terms are non-predictable structures
 */
term :
    termvalue
    | termvaluelist
    | variable
    | literal

    | executeaction
    | executerule
    | executevariable
    ;

/**
 * value
 */
termvalue :
    LOGICALVALUE
    | NUMBER
    | STRING
    ;

/**
 * value list
 */
termvaluelist :
    LEFTANGULARBRACKET
    termvalue ( COMMA termvalue )*
    RIGHTANGULARBRACKET
    ;

/**
 * rule for an action
 */
executeaction :
    DOT
    literal;

/**
 * rule for execute a logical-rule
 */
executerule :
    DOLLAR ( literal | executevariable )
    ;

/**
 * variable-evaluation will be used for an executable call
 * like X(1,2,Y), it is possible for passing variables and parameters
 */
executevariable :
    DOT
    variable
    termlist
    ;

/**
 * clause represent a literal structure existing
 * atom, optional argument
 */
literal :
    ( AT | STRONGNEGATION )?
    ATOM
    termlist?
    ;

/**
 * generic list equal to collcations with empty clause
 */
termlist :
    LEFTROUNDBRACKET term ( COMMA term )* RIGHTROUNDBRACKET
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

// ---------------------------------------------------------------------------------------
