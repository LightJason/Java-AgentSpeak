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

package org.lightjason.agentspeak.grammar;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.error.CSyntaxErrorException;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.execution.action.CProxyAction;
import org.lightjason.agentspeak.language.execution.action.CRawAction;
import org.lightjason.agentspeak.language.execution.action.CTernaryOperation;
import org.lightjason.agentspeak.language.execution.action.achievement_test.CAchievementRuleLiteral;
import org.lightjason.agentspeak.language.execution.action.unify.CDefaultUnify;
import org.lightjason.agentspeak.language.execution.action.unify.CExpressionUnify;
import org.lightjason.agentspeak.language.execution.action.unify.CVariableUnify;
import org.lightjason.agentspeak.language.execution.expression.CAtom;
import org.lightjason.agentspeak.language.execution.expression.CProxyReturnExpression;
import org.lightjason.agentspeak.language.execution.expression.EOperator;
import org.lightjason.agentspeak.language.execution.expression.IExpression;
import org.lightjason.agentspeak.language.execution.expression.logical.CUnary;
import org.lightjason.agentspeak.language.execution.expression.numerical.CAdditive;
import org.lightjason.agentspeak.language.execution.expression.numerical.CComparable;
import org.lightjason.agentspeak.language.execution.expression.numerical.CMultiplicative;
import org.lightjason.agentspeak.language.execution.expression.numerical.CPower;
import org.lightjason.agentspeak.language.execution.expression.numerical.CRelational;
import org.lightjason.agentspeak.language.instantiable.rule.IRule;
import org.lightjason.agentspeak.language.variable.CMutexVariable;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * parser for complex-datatypes
 */
@SuppressWarnings( {"all", "warnings", "unchecked", "unused", "cast"} )
public final class CASTVisitorType extends AbstractParseTreeVisitor<Object> implements IASTVisitorType
{
    /**
     * map with action definition
     */
    private final Map<IPath, IAction> m_actions;
    /**
     * map with logical rules
     */
    private final Multimap<IPath, IRule> m_rules = LinkedHashMultimap.create();
    /**
     * parsed literal
     */
    private ILiteral m_literal = ILiteral.EMPTY;
    /**
     * parsed expression
     */
    private IExpression m_expression = IExpression.EMPTY;
    /**
     * parsed term
     */
    private ITerm m_term = ITerm.EMPTY;


    /**
     * ctor
     */
    public CASTVisitorType()
    {
        this( Collections.<IAction>emptySet(), Collections.<IRule>emptySet() );
    }

    /**
     * ctor
     *
     * @param p_actions set with actions
     * @param p_rules set with rules
     */
    public CASTVisitorType( @Nonnull final Set<IAction> p_actions, @Nonnull final Set<IRule> p_rules )
    {
        m_actions = p_actions.stream().collect( Collectors.toMap( i -> i.name(), i -> i ) );
        p_rules.stream().forEach( i -> m_rules.put( i.identifier().fqnfunctor(), i ) );
    }

    // --- start rules -----------------------------------------------------------------------------------------------------------------------------------------


    @Override
    public final Object visitLiteral_type( final TypeParser.Literal_typeContext p_context )
    {
        m_literal = (ILiteral) this.visitChildren( p_context );
        return null;
    }

    @Override
    public final Object visitExpression_type( final TypeParser.Expression_typeContext p_context )
    {
        m_expression = (IExpression) this.visitChildren( p_context );
        return null;
    }

    @Override
    public final Object visitExpression_term( final TypeParser.Expression_termContext p_context )
    {
        m_term = (ITerm) this.visitChildren( p_context );
        return null;
    }

    @Override
    public final Object visitExecutable_term( final TypeParser.Executable_termContext p_context )
    {
        if ( p_context.STRING() != null )
            return new CRawAction<>( p_context.STRING() );
        if ( p_context.number() != null )
            return new CRawAction<>( this.visitNumber( p_context.number() ) );
        if ( p_context.LOGICALVALUE() != null )
            return new CRawAction<>( logicalvalue( p_context.LOGICALVALUE().getText() ) );

        if ( p_context.executable_action() != null )
            return this.visitExecutable_action( p_context.executable_action() );
        if ( p_context.executable_rule() != null )
            return this.visitExecutable_rule( p_context.executable_rule() );

        if ( p_context.expression() != null )
            return this.visitExpression( p_context.expression() );
        if ( p_context.ternary_operation() != null )
            return this.visitTernary_operation( p_context.ternary_operation() );

        throw new CIllegalArgumentException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "termunknown", p_context.getText() ) );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- term element rules ----------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final Object visitUnification( final TypeParser.UnificationContext p_context )
    {
        final Object l_constraint = this.visitUnification_constraint( p_context.unification_constraint() );

        if ( l_constraint instanceof IExpression )
            return new CExpressionUnify(
                p_context.AT() != null,
                (ILiteral) this.visitLiteral( p_context.literal() ),
                (IExpression) l_constraint
            );

        if ( l_constraint instanceof IVariable<?> )
            return new CVariableUnify(
                p_context.AT() != null,
                (ILiteral) this.visitLiteral( p_context.literal() ),
                (IVariable<?>) l_constraint
            );

        return new CDefaultUnify( p_context.AT() != null, (ILiteral) this.visitLiteral( p_context.literal() ) );
    }

    @Override
    public final Object visitUnification_constraint( final TypeParser.Unification_constraintContext p_context )
    {
        if ( p_context == null )
            return null;

        if ( p_context.expression() != null )
            return this.visitExpression( p_context.expression() );

        if ( p_context.variable() != null )
            return this.visitVariable( p_context.variable() );

        return null;
    }

    @Override
    public final Object visitTernary_operation( final TypeParser.Ternary_operationContext p_context )
    {
        return new CTernaryOperation(
            (IExpression) this.visitExpression( p_context.expression() ),
            (IExecution) this.visitTernary_operation_true( p_context.ternary_operation_true() ),
            (IExecution) this.visitTernary_operation_false( p_context.ternary_operation_false() )
        );
    }

    @Override
    public final Object visitTernary_operation_true( final TypeParser.Ternary_operation_trueContext p_context )
    {
        return this.visitExecutable_term( p_context.executable_term() );
    }

    @Override
    public final Object visitTernary_operation_false( final TypeParser.Ternary_operation_falseContext p_context )
    {
        return this.visitExecutable_term( p_context.executable_term() );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- simple datatypes ------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final Object visitLiteral( final TypeParser.LiteralContext p_context )
    {
        return new CLiteral(
            p_context.AT() != null,
            p_context.STRONGNEGATION() != null,
            CPath.from( this.visitAtom( p_context.atom() ).toString() ),
            (Collection<ITerm>) this.visitTermlist( p_context.termlist() )
        );
    }

    @Override
    public final Object visitTerm( final TypeParser.TermContext p_context )
    {
        if ( p_context.STRING() != null )
            return stringvalue( p_context.STRING().getText() );
        if ( p_context.number() != null )
            return this.visitNumber( p_context.number() );
        if ( p_context.LOGICALVALUE() != null )
            return logicalvalue( p_context.LOGICALVALUE().getText() );

        if ( p_context.literal() != null )
            return this.visitLiteral( p_context.literal() );
        if ( p_context.variable() != null )
            return this.visitVariable( p_context.variable() );

        if ( p_context.termlist() != null )
            return this.visitTermlist( p_context.termlist() );
        if ( p_context.expression() != null )
            return this.visitExpression( p_context.expression() );
        if ( p_context.ternary_operation() != null )
            return this.visitTernary_operation( p_context.ternary_operation() );

        throw new CIllegalArgumentException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "termunknown", p_context.getText() ) );
    }

    @Override
    public final Object visitTermlist( final TypeParser.TermlistContext p_context )
    {
        if ( ( p_context == null ) || ( p_context.isEmpty() ) )
            return Collections.<ITerm>emptyList();

        return p_context.term().stream()
                        .map( i -> this.visitTerm( i ) )
                        .filter( i -> i != null )
                        .map( i -> i instanceof ITerm ? (ITerm) i : CRawTerm.from( i ) )
                        .collect( Collectors.toList() );
    }

    @Override
    public final Object visitVariablelist( final TypeParser.VariablelistContext p_context )
    {
        return this.visitChildren( p_context );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- raw rules -------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final Object visitNumber( final TypeParser.NumberContext p_context )
    {
        if ( p_context.CONSTANTNUMBER() != null )
            return numericonstant( p_context.CONSTANTNUMBER().getText() );

        final Number l_value = (Number) this.visitChildren( p_context );
        return p_context.MINUS() != null
               ? -1 * l_value.doubleValue()
               : l_value.doubleValue();
    }

    @Override
    public final Object visitDigitsequence( final TypeParser.DigitsequenceContext p_context )
    {
        return Double.valueOf( p_context.getText() );
    }

    @Override
    public final Object visitAtom( final TypeParser.AtomContext p_context )
    {
        return p_context.getText();
    }

    @Override
    public final Object visitVariable( final TypeParser.VariableContext p_context )
    {
        return p_context.AT() == null
               ? new CVariable<>( (String) this.visitVariableatom( p_context.variableatom() ) )
               : new CMutexVariable<>( (String) this.visitVariableatom( p_context.variableatom() ) );
    }

    @Override
    public final Object visitVariableatom( final TypeParser.VariableatomContext p_context )
    {
        return p_context.getText();
    }

    @Override
    public final Object visitExpression( final TypeParser.ExpressionContext p_context )
    {
        // bracket expression
        if ( p_context.expression_bracket() != null )
            return this.visitExpression_bracket( p_context.expression_bracket() );

        // or-expression
        return CCommon.createLogicalBinaryExpression(
            EOperator.OR,
            (IExpression) this.visitExpression_logical_and( p_context.expression_logical_and() ),
            p_context.expression() != null
            ? p_context.expression().stream().map( i -> (IExpression) this.visitExpression( i ) ).collect(
                Collectors.toList() )
            : Collections.<IExpression>emptyList()
        );
    }

    @Override
    public final Object visitExpression_bracket( final TypeParser.Expression_bracketContext p_context )
    {
        return this.visitExpression( p_context.expression() );
    }

    @Override
    public final Object visitExpression_logical_and( final TypeParser.Expression_logical_andContext p_context )
    {
        return CCommon.createLogicalBinaryExpression(
            EOperator.AND,
            (IExpression) this.visitExpression_logical_xor( p_context.expression_logical_xor() ),
            p_context.expression() != null
            ? p_context.expression().stream().map( i -> (IExpression) this.visitExpression( i ) ).collect(
                Collectors.toList() )
            : Collections.<IExpression>emptyList()
        );
    }

    @Override
    public final Object visitExpression_logical_xor( final TypeParser.Expression_logical_xorContext p_context )
    {
        if ( p_context.expression_logical_element() != null )
            return CCommon.createLogicalBinaryExpression(
                EOperator.XOR,
                (IExpression) this.visitExpression_logical_element( p_context.expression_logical_element() ),
                p_context.expression() != null
                ? p_context.expression().stream().map( i -> (IExpression) this.visitExpression( i ) ).collect(
                    Collectors.toList() )
                : Collections.<IExpression>emptyList()
            );

        if ( p_context.expression_logical_negation() != null )
            return this.visitExpression_logical_negation( p_context.expression_logical_negation() );

        if ( p_context.expression_numeric() != null )
            return this.visitExpression_numeric( p_context.expression_numeric() );

        throw new CSyntaxErrorException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "logicallefthandside", p_context.getText() ) );
    }

    @Override
    public final Object visitExpression_logical_negation( final TypeParser.Expression_logical_negationContext p_context )
    {
        return new CUnary( EOperator.NEGATION, (IExpression) this.visitExpression( p_context.expression() ) );
    }

    @Override
    public final Object visitExpression_logical_element( final TypeParser.Expression_logical_elementContext p_context )
    {
        if ( p_context.LOGICALVALUE() != null )
            return new CAtom( logicalvalue( p_context.LOGICALVALUE().getText() ) );

        if ( p_context.variable() != null )
            return new CAtom( this.visitVariable( p_context.variable() ) );

        if ( p_context.unification() != null )
            return new CProxyReturnExpression<>( (IExecution) this.visitUnification( p_context.unification() ) );

        if ( p_context.executable_action() != null )
            return new CProxyReturnExpression<>( (IExecution) this.visitExecutable_action( p_context.executable_action() ) );

        if ( p_context.executable_rule() != null )
            return new CProxyReturnExpression<>( (IExecution) this.visitExecutable_rule( p_context.executable_rule() ) );

        throw new CSyntaxErrorException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "logicalelement", p_context.getText() ) );
    }

    @Override
    public final Object visitExpression_numeric( final TypeParser.Expression_numericContext p_context )
    {
        if ( p_context.expression_numeric() == null )
            return this.visitExpression_numeric_relation( p_context.expression_numeric_relation() );

        if ( p_context.EQUAL() != null )
            return new CComparable(
                EOperator.EQUAL,
                (IExpression) this.visitExpression_numeric_relation( p_context.expression_numeric_relation() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( p_context.NOTEQUAL() != null )
            return new CComparable(
                EOperator.NOTEQUAL,
                (IExpression) this.visitExpression_numeric_relation( p_context.expression_numeric_relation() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        throw new CSyntaxErrorException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "compareoperator", p_context.getText() ) );
    }

    @Override
    public final Object visitExpression_numeric_relation( final TypeParser.Expression_numeric_relationContext p_context )
    {
        if ( p_context.expression_numeric() == null )
            return this.visitExpression_numeric_additive( p_context.expression_numeric_additive() );

        if ( p_context.GREATER() != null )
            return new CRelational(
                EOperator.GREATER,
                (IExpression) this.visitExpression_numeric_additive( p_context.expression_numeric_additive() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( p_context.GREATEREQUAL() != null )
            return new CRelational(
                EOperator.GREATEREQUAL,
                (IExpression) this.visitExpression_numeric_additive( p_context.expression_numeric_additive() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( p_context.LESS() != null )
            return new CRelational(
                EOperator.LESS,
                (IExpression) this.visitExpression_numeric_additive( p_context.expression_numeric_additive() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( p_context.LESSEQUAL() != null )
            return new CRelational(
                EOperator.LESSEQUAL,
                (IExpression) this.visitExpression_numeric_additive( p_context.expression_numeric_additive() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        throw new CSyntaxErrorException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "relationaloperator", p_context.getText() ) );
    }

    @Override
    public final Object visitExpression_numeric_additive( final TypeParser.Expression_numeric_additiveContext p_context )
    {
        if ( p_context.expression_numeric() == null )
            return this.visitExpression_numeric_multiplicative( p_context.expression_numeric_multiplicative() );

        if ( p_context.PLUS() != null )
            return new CAdditive(
                EOperator.PLUS,
                (IExpression) this.visitExpression_numeric_multiplicative( p_context.expression_numeric_multiplicative() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( p_context.MINUS() != null )
            return new CAdditive(
                EOperator.MINUS,
                (IExpression) this.visitExpression_numeric_multiplicative( p_context.expression_numeric_multiplicative() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        throw new CSyntaxErrorException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "additiveoperator", p_context.getText() ) );
    }

    @Override
    public final Object visitExpression_numeric_multiplicative( final TypeParser.Expression_numeric_multiplicativeContext p_context )
    {
        if ( p_context.expression_numeric() == null )
            return this.visitExpression_numeric_power( p_context.expression_numeric_power() );

        if ( p_context.MULTIPLY() != null )
            return new CMultiplicative(
                EOperator.MULTIPLY,
                (IExpression) this.visitExpression_numeric_power( p_context.expression_numeric_power() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( p_context.SLASH() != null )
            return new CMultiplicative(
                EOperator.DIVIDE,
                (IExpression) this.visitExpression_numeric_power( p_context.expression_numeric_power() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( p_context.MODULO() != null )
            return new CMultiplicative(
                EOperator.MODULO,
                (IExpression) this.visitExpression_numeric_power( p_context.expression_numeric_power() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        throw new CSyntaxErrorException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "multiplicativeoperator", p_context.getText() ) );
    }

    @Override
    public final Object visitExpression_numeric_power( final TypeParser.Expression_numeric_powerContext p_context )
    {
        if ( p_context.expression_numeric() == null )
            return this.visitExpression_numeric_element( p_context.expression_numeric_element() );

        return new CPower(
            EOperator.POWER,
            (IExpression) this.visitExpression_numeric_element( p_context.expression_numeric_element() ),
            (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
        );
    }

    @Override
    public final Object visitExpression_numeric_element( final TypeParser.Expression_numeric_elementContext p_context )
    {
        if ( p_context.number() != null )
            return new CAtom( this.visitNumber( p_context.number() ) );

        if ( p_context.variable() != null )
            return new CAtom( this.visitVariable( p_context.variable() ) );

        if ( p_context.executable_action() != null )
            return new CProxyReturnExpression<>( (IExecution) this.visitExecutable_action( p_context.executable_action() ) );

        if ( p_context.executable_rule() != null )
            return new CProxyReturnExpression<>( (IExecution) this.visitExecutable_rule( p_context.executable_rule() ) );

        throw new CSyntaxErrorException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "numericelement", p_context.getText() ) );
    }

    @Override
    public final Object visitExecutable_action( final TypeParser.Executable_actionContext p_context )
    {
        return new CProxyAction( m_actions, (ILiteral) this.visitLiteral( p_context.literal() ) );
    }

    @Override
    public final Object visitExecutable_rule( final TypeParser.Executable_ruleContext p_context )
    {
        if ( p_context.literal() != null )
            return new CAchievementRuleLiteral( (ILiteral) this.visitLiteral( p_context.literal() ) );

        return null;
    }

    @Override
    public final Object visitVariable_evaluate( final TypeParser.Variable_evaluateContext p_context )
    {
        return null;
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- helper ----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * returns the value of a numeric constant
     *
     * @param p_value constant name
     * @return number value
     */
    private static Number numericonstant( @Nonnull final String p_value )
    {
        final Double l_constant = org.lightjason.agentspeak.grammar.CCommon.NUMERICCONSTANT.get( p_value );
        if ( l_constant != null )
            return l_constant;

        throw new CSyntaxErrorException( org.lightjason.agentspeak.common.CCommon.languagestring( CASTVisitorType.class, "constantunknown", p_value ) );
    }

    /**
     * converts a string token to the type
     *
     * @param p_value string value
     * @return boolean value
     */
    private static boolean logicalvalue( @Nonnull final String p_value )
    {
        return ( !p_value.isEmpty() ) && ( ( "true".equals( p_value ) ) || ( "success".equals( p_value ) ) );
    }

    /**
     * create a string value without quotes
     *
     * @param p_value string
     * @return string without quotes
     */
    private static String stringvalue( @Nonnull final String p_value )
    {
        return p_value.length() < 3 ? "" : p_value.substring( 1, p_value.length() - 1 );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- getter structure ------------------------------------------------------------------------------------------------------------------------------------

    @Nonnull
    @Override
    public final ILiteral literal()
    {
        return m_literal;
    }

    @Nonnull
    @Override
    public final IExpression expression()
    {
        return m_expression;
    }

    @Nonnull
    @Override
    public final ITerm term()
    {
        return m_term;
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

}
