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
import org.antlr.v4.runtime.tree.TerminalNode;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.error.CSyntaxErrorException;
import org.lightjason.agentspeak.grammar.builder.CRaw;
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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * parser for complex-datatypes
 */
@SuppressWarnings( {"all", "warnings", "unchecked", "unused", "cast"} )
public final class CASTVisitorManual extends AbstractParseTreeVisitor<Object> implements IASTVisitorManual
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
    public CASTVisitorManual()
    {
        this( Collections.<IAction>emptySet(), Collections.<IRule>emptySet() );
    }

    /**
     * ctor
     *
     * @param p_actions set with actions
     * @param p_rules set with rules
     */
    public CASTVisitorManual( @Nonnull final Set<IAction> p_actions, @Nonnull final Set<IRule> p_rules )
    {
        m_actions = p_actions.stream().collect( Collectors.toMap( i -> i.name(), i -> i ) );
        p_rules.stream().forEach( i -> m_rules.put( i.identifier().fqnfunctor(), i ) );
    }

    // --- start rules -----------------------------------------------------------------------------------------------------------------------------------------



    @Override
    public Object visitRoot_literal( final ManualParser.Root_literalContext p_context )
    {
        m_literal = (ILiteral) this.visitChildren( p_context );
        return null;
    }

    @Override
    public final Object visitExpression_type( final ManualParser.Expression_typeContext p_context )
    {
        m_expression = (IExpression) this.visitChildren( p_context );
        return null;
    }

    @Override
    public final Object visitExpression_term( final ManualParser.Expression_termContext p_context )
    {
        m_term = (ITerm) this.visitChildren( p_context );
        return null;
    }

    @Override
    public final Object visitExecutable_term( final ManualParser.Executable_termContext p_context )
    {
        if ( Objects.nonNull( p_context.STRING() ) )
            return new CRawAction<>( p_context.STRING() );
        if ( Objects.nonNull( p_context.number() ) )
            return new CRawAction<>( this.visitNumber( p_context.number() ) );
        if ( Objects.nonNull( p_context.LOGICALVALUE() ) )
            return new CRawAction<>( logicalvalue( p_context.LOGICALVALUE().getText() ) );

        if ( Objects.nonNull( p_context.executable_action() ) )
            return this.visitExecutable_action( p_context.executable_action() );
        if ( Objects.nonNull( p_context.executable_rule() ) )
            return this.visitExecutable_rule( p_context.executable_rule() );

        if ( Objects.nonNull( p_context.expression() ) )
            return this.visitExpression( p_context.expression() );
        if ( Objects.nonNull( p_context.ternary_operation() ) )
            return this.visitTernary_operation( p_context.ternary_operation() );

        throw new CIllegalArgumentException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "termunknown", p_context.getText() ) );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- term element rules ----------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final Object visitUnification( final ManualParser.UnificationContext p_context )
    {
        final Object l_constraint = this.visitUnification_constraint( p_context.unification_constraint() );

        if ( l_constraint instanceof IExpression )
            return new CExpressionUnify(
                Objects.nonNull( p_context.AT() ),
                (ILiteral) this.visitLiteral( p_context.literal() ),
                (IExpression) l_constraint
            );

        if ( l_constraint instanceof IVariable<?> )
            return new CVariableUnify(
                Objects.nonNull( p_context.AT() ),
                (ILiteral) this.visitLiteral( p_context.literal() ),
                (IVariable<?>) l_constraint
            );

        return new CDefaultUnify( Objects.nonNull( p_context.AT() ), (ILiteral) this.visitLiteral( p_context.literal() ) );
    }

    @Override
    public final Object visitUnification_constraint( final ManualParser.Unification_constraintContext p_context )
    {
        if ( Objects.isNull( p_context ) )
            return null;

        if ( Objects.nonNull( p_context.expression() ) )
            return this.visitExpression( p_context.expression() );

        if ( Objects.nonNull( p_context.variable() ) )
            return this.visitVariable( p_context.variable() );

        return null;
    }

    @Override
    public final Object visitTernary_operation( final ManualParser.Ternary_operationContext p_context )
    {
        return new CTernaryOperation(
            (IExpression) this.visitExpression( p_context.expression() ),
            (IExecution) this.visitTernary_operation_true( p_context.ternary_operation_true() ),
            (IExecution) this.visitTernary_operation_false( p_context.ternary_operation_false() )
        );
    }

    @Override
    public final Object visitTernary_operation_true( final ManualParser.Ternary_operation_trueContext p_context )
    {
        return this.visitExecutable_term( p_context.executable_term() );
    }

    @Override
    public final Object visitTernary_operation_false( final ManualParser.Ternary_operation_falseContext p_context )
    {
        return this.visitExecutable_term( p_context.executable_term() );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- simple datatypes ------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final Object visitLiteral( final ManualParser.LiteralContext p_context )
    {
        return CRaw.literal(
            p_context.AT(),
            p_context.STRONGNEGATION(),
            p_context.ATOM(),
            (Collection<ITerm>) this.visitTermlist( p_context.termlist() )
        );
    }

    @Override
    public final Object visitTerm( final ManualParser.TermContext p_context )
    {
        if ( Objects.nonNull( p_context.STRING() ) )
            return stringvalue( p_context.STRING().getText() );
        if ( Objects.nonNull( p_context.number() ) )
            return this.visitNumber( p_context.number() );
        if ( Objects.nonNull( p_context.LOGICALVALUE() ) )
            return logicalvalue( p_context.LOGICALVALUE().getText() );

        if ( Objects.nonNull( p_context.literal() ) )
            return this.visitLiteral( p_context.literal() );
        if ( Objects.nonNull( p_context.variable() ) )
            return this.visitVariable( p_context.variable() );

        if ( Objects.nonNull( p_context.termlist() ) )
            return this.visitTermlist( p_context.termlist() );
        if ( Objects.nonNull( p_context.expression() ) )
            return this.visitExpression( p_context.expression() );
        if ( Objects.nonNull( p_context.ternary_operation() ) )
            return this.visitTernary_operation( p_context.ternary_operation() );

        throw new CIllegalArgumentException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "termunknown", p_context.getText() ) );
    }

    @Override
    public final Object visitTermlist( final ManualParser.TermlistContext p_context )
    {
        if ( ( Objects.isNull( p_context ) ) || ( p_context.isEmpty() ) )
            return Collections.<ITerm>emptyList();

        return p_context.term().stream()
                        .map( i -> this.visitTerm( i ) )
                        .filter( i -> Objects.nonNull( i ) )
                        .map( i -> i instanceof ITerm ? (ITerm) i : CRawTerm.from( i ) )
                        .collect( Collectors.toList() );
    }

    @Override
    public final Object visitVariablelist( final ManualParser.VariablelistContext p_context )
    {
        return this.visitChildren( p_context );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- raw rules -------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final Object visitNumber( final ManualParser.NumberContext p_context )
    {
        if ( Objects.nonNull( p_context.CONSTANTNUMBER() ) )
            return numericonstant( p_context.CONSTANTNUMBER().getText() );

        final Number l_value = (Number) this.visitChildren( p_context );
        return p_context.MINUS() != null
                ? -1 * l_value.doubleValue()
                : l_value.doubleValue();
    }

    @Override
    public final Object visitDigitsequence( final ManualParser.DigitsequenceContext p_context )
    {
        return Double.valueOf( p_context.getText() );
    }

    @Override
    public final Object visitAtom( final ManualParser.AtomContext p_context )
    {
        return p_context.getText();
    }

    @Override
    public final Object visitVariable( final ManualParser.VariableContext p_context )
    {
        return Objects.isNull( p_context.AT() )
               ? new CVariable<>( (String) this.visitVariableatom( p_context.variableatom() ) )
               : new CMutexVariable<>( (String) this.visitVariableatom( p_context.variableatom() ) );
    }

    @Override
    public final Object visitVariableatom( final ManualParser.VariableatomContext p_context )
    {
        return p_context.getText();
    }

    @Override
    public final Object visitExpression( final ManualParser.ExpressionContext p_context )
    {
        // bracket expression
        if ( Objects.nonNull( p_context.expression_bracket() ) )
            return this.visitExpression_bracket( p_context.expression_bracket() );

        // or-expression
        return CCommon.createLogicalBinaryExpression(
            EOperator.OR,
            (IExpression) this.visitExpression_logical_and( p_context.expression_logical_and() ),
            Objects.nonNull( p_context.expression() )
            ? p_context.expression().stream().map( i -> (IExpression) this.visitExpression( i ) ).collect(
                Collectors.toList() )
            : Collections.<IExpression>emptyList()
        );
    }

    @Override
    public final Object visitExpression_bracket( final ManualParser.Expression_bracketContext p_context )
    {
        return this.visitExpression( p_context.expression() );
    }

    @Override
    public final Object visitExpression_logical_and( final ManualParser.Expression_logical_andContext p_context )
    {
        return CCommon.createLogicalBinaryExpression(
            EOperator.AND,
            (IExpression) this.visitExpression_logical_xor( p_context.expression_logical_xor() ),
            Objects.nonNull( p_context.expression() )
            ? p_context.expression().stream().map( i -> (IExpression) this.visitExpression( i ) ).collect(
                Collectors.toList() )
            : Collections.<IExpression>emptyList()
        );
    }

    @Override
    public final Object visitExpression_logical_xor( final ManualParser.Expression_logical_xorContext p_context )
    {
        if ( Objects.nonNull( p_context.expression_logical_element() ) )
            return CCommon.createLogicalBinaryExpression(
                EOperator.XOR,
                (IExpression) this.visitExpression_logical_element( p_context.expression_logical_element() ),
                Objects.nonNull( p_context.expression() )
                ? p_context.expression().stream().map( i -> (IExpression) this.visitExpression( i ) ).collect(
                    Collectors.toList() )
                : Collections.<IExpression>emptyList()
            );

        if ( Objects.nonNull( p_context.expression_logical_negation() ) )
            return this.visitExpression_logical_negation( p_context.expression_logical_negation() );

        if ( Objects.nonNull( p_context.expression_numeric() ) )
            return this.visitExpression_numeric( p_context.expression_numeric() );

        throw new CSyntaxErrorException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "logicallefthandside", p_context.getText() ) );
    }

    @Override
    public final Object visitExpression_logical_negation( final ManualParser.Expression_logical_negationContext p_context )
    {
        return new CUnary( EOperator.NEGATION, (IExpression) this.visitExpression( p_context.expression() ) );
    }

    @Override
    public final Object visitExpression_logical_element( final ManualParser.Expression_logical_elementContext p_context )
    {
        if ( Objects.nonNull( p_context.LOGICALVALUE() ) )
            return new CAtom( logicalvalue( p_context.LOGICALVALUE().getText() ) );

        if ( Objects.nonNull( p_context.variable() ) )
            return new CAtom( this.visitVariable( p_context.variable() ) );

        if ( Objects.nonNull( p_context.unification() ) )
            return new CProxyReturnExpression<>( (IExecution) this.visitUnification( p_context.unification() ) );

        if ( Objects.nonNull( p_context.executable_action() ) )
            return new CProxyReturnExpression<>( (IExecution) this.visitExecutable_action( p_context.executable_action() ) );

        if ( Objects.nonNull( p_context.executable_rule() ) )
            return new CProxyReturnExpression<>( (IExecution) this.visitExecutable_rule( p_context.executable_rule() ) );

        throw new CSyntaxErrorException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "logicalelement", p_context.getText() ) );
    }

    @Override
    public final Object visitExpression_numeric( final ManualParser.Expression_numericContext p_context )
    {
        if ( Objects.isNull( p_context.expression_numeric() ) )
            return this.visitExpression_numeric_relation( p_context.expression_numeric_relation() );

        if ( Objects.nonNull( p_context.EQUAL() ) )
            return new CComparable(
                EOperator.EQUAL,
                (IExpression) this.visitExpression_numeric_relation( p_context.expression_numeric_relation() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( Objects.nonNull( p_context.NOTEQUAL() ) )
            return new CComparable(
                EOperator.NOTEQUAL,
                (IExpression) this.visitExpression_numeric_relation( p_context.expression_numeric_relation() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        throw new CSyntaxErrorException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "compareoperator", p_context.getText() ) );
    }

    @Override
    public final Object visitExpression_numeric_relation( final ManualParser.Expression_numeric_relationContext p_context )
    {
        if ( Objects.isNull( p_context.expression_numeric() ) )
            return this.visitExpression_numeric_additive( p_context.expression_numeric_additive() );

        if ( Objects.nonNull( p_context.GREATER() ) )
            return new CRelational(
                EOperator.GREATER,
                (IExpression) this.visitExpression_numeric_additive( p_context.expression_numeric_additive() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( Objects.nonNull( p_context.GREATEREQUAL() ) )
            return new CRelational(
                EOperator.GREATEREQUAL,
                (IExpression) this.visitExpression_numeric_additive( p_context.expression_numeric_additive() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( Objects.nonNull( p_context.LESS() ) )
            return new CRelational(
                EOperator.LESS,
                (IExpression) this.visitExpression_numeric_additive( p_context.expression_numeric_additive() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( Objects.nonNull( p_context.LESSEQUAL() ) )
            return new CRelational(
                EOperator.LESSEQUAL,
                (IExpression) this.visitExpression_numeric_additive( p_context.expression_numeric_additive() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        throw new CSyntaxErrorException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "relationaloperator", p_context.getText() ) );
    }

    @Override
    public final Object visitExpression_numeric_additive( final ManualParser.Expression_numeric_additiveContext p_context )
    {
        if ( Objects.isNull( p_context.expression_numeric() ) )
            return this.visitExpression_numeric_multiplicative( p_context.expression_numeric_multiplicative() );

        if ( Objects.nonNull( p_context.PLUS() ) )
            return new CAdditive(
                EOperator.PLUS,
                (IExpression) this.visitExpression_numeric_multiplicative( p_context.expression_numeric_multiplicative() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( Objects.nonNull( p_context.MINUS() ) )
            return new CAdditive(
                EOperator.MINUS,
                (IExpression) this.visitExpression_numeric_multiplicative( p_context.expression_numeric_multiplicative() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        throw new CSyntaxErrorException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "additiveoperator", p_context.getText() ) );
    }

    @Override
    public final Object visitExpression_numeric_multiplicative( final ManualParser.Expression_numeric_multiplicativeContext p_context )
    {
        if ( Objects.isNull( p_context.expression_numeric() ) )
            return this.visitExpression_numeric_power( p_context.expression_numeric_power() );

        if ( Objects.nonNull( p_context.MULTIPLY() ) )
            return new CMultiplicative(
                EOperator.MULTIPLY,
                (IExpression) this.visitExpression_numeric_power( p_context.expression_numeric_power() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( Objects.nonNull( p_context.SLASH() ) )
            return new CMultiplicative(
                EOperator.DIVIDE,
                (IExpression) this.visitExpression_numeric_power( p_context.expression_numeric_power() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( Objects.nonNull( p_context.MODULO() ) )
            return new CMultiplicative(
                EOperator.MODULO,
                (IExpression) this.visitExpression_numeric_power( p_context.expression_numeric_power() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        throw new CSyntaxErrorException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "multiplicativeoperator", p_context.getText() ) );
    }

    @Override
    public final Object visitExpression_numeric_power( final ManualParser.Expression_numeric_powerContext p_context )
    {
        if ( Objects.isNull( p_context.expression_numeric() ) )
            return this.visitExpression_numeric_element( p_context.expression_numeric_element() );

        return new CPower(
            EOperator.POWER,
            (IExpression) this.visitExpression_numeric_element( p_context.expression_numeric_element() ),
            (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
        );
    }

    @Override
    public final Object visitExpression_numeric_element( final ManualParser.Expression_numeric_elementContext p_context )
    {
        if ( Objects.nonNull( p_context.number() ) )
            return new CAtom( this.visitNumber( p_context.number() ) );

        if ( Objects.nonNull( p_context.variable() ) )
            return new CAtom( this.visitVariable( p_context.variable() ) );

        if ( Objects.nonNull( p_context.executable_action() ) )
            return new CProxyReturnExpression<>( (IExecution) this.visitExecutable_action( p_context.executable_action() ) );

        if ( Objects.nonNull( p_context.executable_rule() ) )
            return new CProxyReturnExpression<>( (IExecution) this.visitExecutable_rule( p_context.executable_rule() ) );

        throw new CSyntaxErrorException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "numericelement", p_context.getText() ) );
    }

    @Override
    public final Object visitExecutable_action( final ManualParser.Executable_actionContext p_context )
    {
        return new CProxyAction( m_actions, (ILiteral) this.visitLiteral( p_context.literal() ) );
    }

    @Override
    public final Object visitExecutable_rule( final ManualParser.Executable_ruleContext p_context )
    {
        if ( Objects.nonNull( p_context.literal() ) )
            return new CAchievementRuleLiteral( (ILiteral) this.visitLiteral( p_context.literal() ) );

        return null;
    }

    @Override
    public final Object visitVariable_evaluate( final ManualParser.Variable_evaluateContext p_context )
    {
        return null;
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- helper ----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * parsing number
     *
     * @param p_number terminal number node
     * @return number value
     */
    private static Number numbervalue( final TerminalNode p_number )
    {
        final Double l_constant = org.lightjason.agentspeak.grammar.CCommon.NUMERICCONSTANT.get( p_value );
        if ( Objects.nonNull( l_constant ) )
            return l_constant;

        return Double.valueOf( p_number.getText() );
    }

    /**
     * converts a string token to the type
     *
     * @param p_value string value
     * @return boolean value
     */
    private static boolean logicalvalue( @Nonnull final TerminalNode p_value )
    {
        return ( !p_value.getText().isEmpty() ) && ( ( "true".equals( p_value.getText() ) ) || ( "success".equals( p_value.getText() ) ) );
    }

    /**
     * create a string value without quotes
     *
     * @param p_value string
     * @return string without quotes
     */
    private static String stringvalue( @Nonnull final TerminalNode p_value )
    {
        return p_value.getText().length() < 3 ? "" : p_value.getText().substring( 1, p_value.getText().length() - 1 );
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
