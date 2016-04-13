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

package lightjason.grammar;

import lightjason.agent.action.IAction;
import lightjason.common.CPath;
import lightjason.common.IPath;
import lightjason.error.CIllegalArgumentException;
import lightjason.error.CSyntaxErrorException;
import lightjason.language.CLiteral;
import lightjason.language.CMutexVariable;
import lightjason.language.CRawTerm;
import lightjason.language.CVariable;
import lightjason.language.ILiteral;
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.IExecution;
import lightjason.language.execution.action.CProxyAction;
import lightjason.language.execution.action.CTernaryOperation;
import lightjason.language.execution.action.unify.CDefaultUnify;
import lightjason.language.execution.action.unify.CExpressionUnify;
import lightjason.language.execution.action.unify.CVariableUnify;
import lightjason.language.execution.expression.CAtom;
import lightjason.language.execution.expression.CProxyReturnExpression;
import lightjason.language.execution.expression.EOperator;
import lightjason.language.execution.expression.IExpression;
import lightjason.language.execution.expression.logical.CUnary;
import lightjason.language.execution.expression.numerical.CAdditive;
import lightjason.language.execution.expression.numerical.CComparable;
import lightjason.language.execution.expression.numerical.CMultiplicative;
import lightjason.language.execution.expression.numerical.CPower;
import lightjason.language.execution.expression.numerical.CRelational;
import lightjason.language.instantiable.rule.IRule;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * parser for complex-datatypes
 */
@SuppressWarnings( {"all", "warnings", "unchecked", "unused", "cast"} )
public class CASTVisitorType extends AbstractParseTreeVisitor<Object> implements IASTVisitorType
{
    /**
     * map with action definition
     */
    protected final Map<IPath, IAction> m_actions;
    /**
     * map with logical rules
     */
    protected final Map<IPath, IRule> m_rules;
    /**
     * parsed literal
     */
    protected ILiteral m_literal;
    /**
     * parsed expression
     */
    protected IExpression m_expression;
    /**
     * parsed term
     */
    protected ITerm m_term;


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
     */
    public CASTVisitorType( final Set<IAction> p_actions, final Set<IRule> p_rules )
    {
        m_actions = p_actions.stream().collect( Collectors.toMap( i -> i.getName(), i -> i ) );
        m_rules = p_rules.stream().collect( Collectors.toMap( i -> i.getIdentifier().getFQNFunctor(), i -> i ) );
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

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- term element rules ----------------------------------------------------------------------------------------------------------------------------------

    @Override
    public Object visitUnification( final TypeParser.UnificationContext p_context )
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
    public Object visitUnification_constraint( final TypeParser.Unification_constraintContext p_context )
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
    public Object visitTernary_operation( final TypeParser.Ternary_operationContext p_context )
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
        return lightjason.grammar.CCommon.getTermExecution( this.visitTerm( p_context.term() ), m_actions, m_rules );
    }



    @Override
    public final Object visitTernary_operation_false( final TypeParser.Ternary_operation_falseContext p_context )
    {
        return lightjason.grammar.CCommon.getTermExecution( this.visitTerm( p_context.term() ), m_actions, m_rules );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- simple datatypes ------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public Object visitLiteral( final TypeParser.LiteralContext p_context )
    {
        return new CLiteral(
                p_context.AT() != null,
                p_context.STRONGNEGATION() != null,
                CPath.from( this.visitAtom( p_context.atom() ).toString() ),
                (Collection<ITerm>) this.visitTermlist( p_context.termlist() ),
                (Collection<ILiteral>) this.visitLiteralset( p_context.literalset() )
        );
    }



    @Override
    public Object visitLiteralset( final TypeParser.LiteralsetContext p_context )
    {
        if ( ( p_context == null ) || ( p_context.isEmpty() ) )
            return Collections.EMPTY_LIST;

        return p_context.literal().stream().map( i -> this.visitLiteral( i ) ).filter( i -> i != null ).collect( Collectors.toList() );
    }



    @Override
    public Object visitTerm( final TypeParser.TermContext p_context )
    {
        if ( p_context.string() != null )
            return this.visitString( p_context.string() );
        if ( p_context.number() != null )
            return this.visitNumber( p_context.number() );
        if ( p_context.logicalvalue() != null )
            return this.visitLogicalvalue( p_context.logicalvalue() );

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

        throw new CIllegalArgumentException( lightjason.common.CCommon.getLanguageString( this, "termunknown", p_context.getText() ) );
    }



    @Override
    public Object visitTermlist( final TypeParser.TermlistContext p_context )
    {
        if ( ( p_context == null ) || ( p_context.isEmpty() ) )
            return Collections.EMPTY_LIST;

        return p_context.term().stream().map( i -> this.visitTerm( i ) ).filter( i -> i != null ).map(
                i -> i instanceof ITerm ? (ITerm) i : CRawTerm.from( i )
        ).collect( Collectors.toList() );
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
        return this.visitChildren( p_context );
    }



    @Override
    public final Object visitIntegernumber( final TypeParser.IntegernumberContext p_context )
    {
        return p_context.integernumber_negative() != null ? this.visitIntegernumber_negative( p_context.integernumber_negative() )
                                                          : this.visitIntegernumber_positive( p_context.integernumber_positive() );
    }



    @Override
    public Object visitIntegernumber_positive( final TypeParser.Integernumber_positiveContext p_context )
    {
        return Long.valueOf( p_context.getText() );
    }



    @Override
    public Object visitIntegernumber_negative( final TypeParser.Integernumber_negativeContext p_context )
    {
        return Long.valueOf( p_context.getText() );
    }



    @Override
    public Object visitFloatnumber( final TypeParser.FloatnumberContext p_context )
    {
        if ( p_context.getText().equals( "infinity" ) )
            return p_context.MINUS() == null ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;

        final Double l_constant = lightjason.grammar.CCommon.NUMERICCONSTANT.get( p_context.getText() );
        if ( l_constant != null )
            return l_constant;

        return Double.valueOf( p_context.getText() );
    }



    @Override
    public Object visitLogicalvalue( final TypeParser.LogicalvalueContext p_context )
    {
        return p_context.TRUE() != null;
    }



    @Override
    public final Object visitConstant( final TypeParser.ConstantContext p_context )
    {
        return this.visitChildren( p_context );
    }



    @Override
    public Object visitString( final TypeParser.StringContext p_context )
    {
        // remove quotes
        final String l_text = p_context.getText();
        return l_text.length() < 3 ? "" : l_text.substring( 1, l_text.length() - 1 );
    }



    @Override
    public Object visitAtom( final TypeParser.AtomContext p_context )
    {
        return p_context.getText();
    }



    @Override
    public Object visitVariable( final TypeParser.VariableContext p_context )
    {
        return p_context.AT() == null ? new CVariable<>( p_context.getText() ) : new CMutexVariable<>( p_context.getText() );
    }



    @Override
    public Object visitExpression( final TypeParser.ExpressionContext p_context )
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
    public Object visitExpression_logical_and( final TypeParser.Expression_logical_andContext p_context )
    {
        return lightjason.grammar.CCommon.createLogicalBinaryExpression(
                EOperator.AND,
                (IExpression) this.visitExpression_logical_xor( p_context.expression_logical_xor() ),
                p_context.expression() != null
                ? p_context.expression().stream().map( i -> (IExpression) this.visitExpression( i ) ).collect(
                        Collectors.toList() )
                : Collections.<IExpression>emptyList()
        );
    }



    @Override
    public Object visitExpression_logical_xor( final TypeParser.Expression_logical_xorContext p_context )
    {
        if ( p_context.expression_logical_element() != null )
            return lightjason.grammar.CCommon.createLogicalBinaryExpression(
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

        throw new CSyntaxErrorException( lightjason.common.CCommon.getLanguageString( this, "logicallefthandside", p_context.getText() ) );
    }



    @Override
    public Object visitExpression_logical_negation( final TypeParser.Expression_logical_negationContext p_context )
    {
        return new CUnary( EOperator.NEGATION, (IExpression) this.visitExpression( p_context.expression() ) );
    }



    @Override
    public Object visitExpression_logical_element( final TypeParser.Expression_logical_elementContext p_context )
    {
        if ( p_context.logicalvalue() != null )
            return new CAtom( this.visitLogicalvalue( p_context.logicalvalue() ) );

        if ( p_context.variable() != null )
            return new CAtom( this.visitVariable( p_context.variable() ) );

        if ( p_context.unification() != null )
            return new CProxyReturnExpression<>( (IExecution) this.visitUnification( p_context.unification() ) );

        if ( p_context.literal() != null )
            return new CProxyReturnExpression<>( new CProxyAction( m_actions, (ILiteral) this.visitLiteral( p_context.literal() ) ) );

        throw new CSyntaxErrorException( lightjason.common.CCommon.getLanguageString( this, "logicalelement", p_context.getText() ) );
    }



    @Override
    public Object visitExpression_numeric( final TypeParser.Expression_numericContext p_context )
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

        throw new CSyntaxErrorException( lightjason.common.CCommon.getLanguageString( this, "compareoperator", p_context.getText() ) );
    }



    @Override
    public Object visitExpression_numeric_relation( final TypeParser.Expression_numeric_relationContext p_context )
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

        throw new CSyntaxErrorException( lightjason.common.CCommon.getLanguageString( this, "relationaloperator", p_context.getText() ) );
    }



    @Override
    public Object visitExpression_numeric_additive( final TypeParser.Expression_numeric_additiveContext p_context )
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

        throw new CSyntaxErrorException( lightjason.common.CCommon.getLanguageString( this, "additiveoperator", p_context.getText() ) );
    }



    @Override
    public Object visitExpression_numeric_multiplicative( final TypeParser.Expression_numeric_multiplicativeContext p_context )
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

        throw new CSyntaxErrorException( lightjason.common.CCommon.getLanguageString( this, "multiplicativeoperator", p_context.getText() ) );
    }



    @Override
    public Object visitExpression_numeric_power( final TypeParser.Expression_numeric_powerContext p_context )
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
    public Object visitExpression_numeric_element( final TypeParser.Expression_numeric_elementContext p_context )
    {
        if ( p_context.number() != null )
            return new CAtom( this.visitNumber( p_context.number() ) );

        if ( p_context.variable() != null )
            return new CAtom( this.visitVariable( p_context.variable() ) );

        if ( p_context.literal() != null )
            return new CProxyReturnExpression<>( new CProxyAction( m_actions, (ILiteral) this.visitLiteral( p_context.literal() ) ) );

        throw new CSyntaxErrorException( lightjason.common.CCommon.getLanguageString( this, "numericelement", p_context.getText() ) );
    }



    @Override
    public final Object visitUnaryoperator( final TypeParser.UnaryoperatorContext p_context )
    {
        return this.visitChildren( p_context );
    }



    @Override
    public final Object visitBinaryoperator( final TypeParser.BinaryoperatorContext p_context )
    {
        return this.visitChildren( p_context );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- getter structure ------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final ILiteral getLiteral()
    {
        return m_literal;
    }

    @Override
    public final IExpression getExpression()
    {
        return m_expression;
    }

    @Override
    public final ITerm getTerm()
    {
        return m_term;
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

}
