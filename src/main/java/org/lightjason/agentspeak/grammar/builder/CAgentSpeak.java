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

package org.lightjason.agentspeak.grammar.builder;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.error.CSyntaxErrorException;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.IRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.execution.achievementtest.CAchievementGoalLiteral;
import org.lightjason.agentspeak.language.execution.achievementtest.CAchievementGoalVariable;
import org.lightjason.agentspeak.language.execution.achievementtest.CAchievementRuleLiteral;
import org.lightjason.agentspeak.language.execution.achievementtest.CAchievementRuleVariable;
import org.lightjason.agentspeak.language.execution.achievementtest.CTestGoal;
import org.lightjason.agentspeak.language.execution.achievementtest.CTestRule;
import org.lightjason.agentspeak.language.execution.assignment.CDeconstruct;
import org.lightjason.agentspeak.language.execution.assignment.CMultiAssignment;
import org.lightjason.agentspeak.language.execution.assignment.CSingleAssignment;
import org.lightjason.agentspeak.language.execution.base.CTernaryOperation;
import org.lightjason.agentspeak.language.execution.assignment.EAssignOperator;
import org.lightjason.agentspeak.language.execution.base.CBelief;
import org.lightjason.agentspeak.language.execution.base.CRepair;
import org.lightjason.agentspeak.language.execution.expression.CBinaryExpression;
import org.lightjason.agentspeak.language.execution.expression.CUnaryExpression;
import org.lightjason.agentspeak.language.execution.expression.EBinaryOperator;
import org.lightjason.agentspeak.language.execution.expression.EUnaryOperator;
import org.lightjason.agentspeak.language.execution.expression.IExpression;
import org.lightjason.agentspeak.language.execution.instantiable.plan.CPlan;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.execution.instantiable.plan.annotation.CAtomAnnotation;
import org.lightjason.agentspeak.language.execution.instantiable.plan.annotation.CStringAnnotation;
import org.lightjason.agentspeak.language.execution.instantiable.plan.annotation.CConstantAnnotation;
import org.lightjason.agentspeak.language.execution.instantiable.plan.annotation.IAnnotation;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.execution.instantiable.rule.CRule;
import org.lightjason.agentspeak.language.execution.instantiable.rule.IRule;
import org.lightjason.agentspeak.language.execution.lambda.CLambda;
import org.lightjason.agentspeak.language.execution.lambda.CLambdaInitializeRange;
import org.lightjason.agentspeak.language.execution.lambda.CLambdaInitializeStream;
import org.lightjason.agentspeak.language.execution.lambda.ILambdaStreaming;
import org.lightjason.agentspeak.language.execution.passing.CPassBoolean;
import org.lightjason.agentspeak.language.execution.passing.CPassAction;
import org.lightjason.agentspeak.language.execution.passing.CPassRaw;
import org.lightjason.agentspeak.language.execution.passing.CPassVariable;
import org.lightjason.agentspeak.language.execution.passing.CPassVariableLiteral;
import org.lightjason.agentspeak.language.execution.unary.CDecrement;
import org.lightjason.agentspeak.language.execution.unary.CIncrement;
import org.lightjason.agentspeak.language.execution.unify.CDefaultUnify;
import org.lightjason.agentspeak.language.execution.unify.CExpressionUnify;
import org.lightjason.agentspeak.language.execution.unify.CVariableUnify;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


/**
 * builder of agentspeak components
 */
public final class CAgentSpeak
{
    /**
     * regular pattern for matching annotation constant values
     */
    private static final Pattern ANNOTATIONTWOPARAMETER = Pattern.compile( "\\(.+,.+\\)" );
    /**
     * regular pattern for matching annotation string values
     */
    private static final Pattern ANNOTATIONSTRING = Pattern.compile( "\\(.+\\)" );

    /**
     * ctor
     */
    private CAgentSpeak()
    {
    }



    // --- base structure --------------------------------------------------------------------------------------------------------------------------------------

    /**
     * build a plan object
     *
     * @param p_visitor visitor
     * @param p_annotation annotation
     * @param p_trigger trigger
     * @param p_literal plan literal
     * @param p_body plan definition
     * @return plan
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    public static Stream<IPlan> plan( @Nonnull final ParseTreeVisitor<?> p_visitor,
                                      @Nullable final List<TerminalNode> p_annotation, @Nonnull final RuleContext p_trigger,
                                      @Nonnull final RuleContext p_literal, @Nonnull final List<? extends RuleContext> p_body )
    {
        final ITrigger l_trigger = CTrigger.of(
            ITrigger.EType.of( p_visitor.visit( p_trigger ).toString() ),
            (ILiteral) p_visitor.visit( p_literal )
        );

        final IAnnotation<?>[] l_annotation = annotation( p_annotation );
        return p_body.stream()
                     .map( i -> (Pair<IExpression, IExecution[]>) p_visitor.visit( i ) )
                     .map( i -> new CPlan( l_annotation, l_trigger, i.getLeft(), i.getRight() ) );
    }

    /**
     * returns the plantrigger as string
     *
     * @param p_trigger plantrigger rule
     * @return string
     */
    public static String plantrigger( @Nonnull final RuleContext p_trigger )
    {
        return p_trigger.getText();
    }

    /**
     * build a plan body
     *
     * @param p_visitor visitor
     * @param p_expression expression
     * @param p_body body
     * @return pair of expression and body
     */
    @SuppressWarnings( "unchecked" )
    public static Pair<IExpression, IExecution[]> plandefinition( @Nonnull final ParseTreeVisitor<?> p_visitor,
                                                                  @Nullable final RuleContext p_expression, @Nonnull final RuleContext p_body )
    {
        return new ImmutablePair<>(
            Objects.nonNull( p_expression )
            ? (IExpression) p_visitor.visit( p_expression )
            : IExpression.EMPTY,
            ( (Stream<IExecution>) p_visitor.visit( p_body ) ).toArray( IExecution[]::new )
        );
    }

    /**
     * build a rule
     *
     * @param p_visitor visitor
     * @param p_literal literal
     * @param p_body execution body
     * @return rule stream
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    public static Stream<IRule> rule( @Nonnull final ParseTreeVisitor<?> p_visitor,
                                      @Nullable final RuleContext p_literal, @Nullable final List<? extends RuleContext> p_body )
    {
        if ( Objects.isNull( p_literal ) || Objects.isNull( p_body ) || p_body.isEmpty() )
            return Stream.empty();

        final ILiteral l_literal = (ILiteral) p_visitor.visit( p_literal );
        return p_body.stream()
                     .map( i -> ( (Stream<IExecution>) p_visitor.visit( i ) ).toArray( IExecution[]::new ) )
                     .map( i -> new CRule( l_literal, i ) );
    }



    // --- annotation ------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * build array of annotation
     *
     * @param p_annotation list of annoations
     * @return annotation array
     */
    @Nonnull
    private static IAnnotation<?>[] annotation( @Nullable final List<TerminalNode> p_annotation )
    {
        return Objects.isNull( p_annotation )
               ? new IAnnotation<?>[0]
               : p_annotation.stream()
                             .map( CAgentSpeak::annotation )
                             .filter( i -> !i.equals( IAnnotation.EMPTY ) )
                             .toArray( IAnnotation<?>[]::new );
    }

    /**
     * build annotation object
     * @param p_annotation annotation terminal
     * @return null or annoation
     */
    @Nonnull
    private static IAnnotation<?> annotation( @Nullable final TerminalNode p_annotation )
    {
        if ( Objects.isNull( p_annotation ) )
            return IAnnotation.EMPTY;

        final IAnnotation.EType l_type = IAnnotation.EType.of( p_annotation.getText() );
        switch ( l_type )
        {
            case PARALLEL:
            case ATOMIC:
                return new CAtomAnnotation<>( l_type );

            case VARIABLE:
                return annotationvariabledescription( l_type, p_annotation.getText() );

            case CONSTANT:
                return annotationconstant( l_type, p_annotation.getText() );

            case TAG :
            case DESCRIPTION :
                return annotationdescription( l_type, p_annotation.getText() );

            default:
                throw new CSyntaxErrorException( CCommon.languagestring( CAgentSpeak.class, "unknownannotation" ) );
        }
    }

    /**
     * build description annotation
     *
     * @param p_type annotation type
     * @param p_value annotation value string
     * @return annotation
     */
    private static IAnnotation<?> annotationdescription( @Nonnull final IAnnotation.EType p_type, @Nonnull final String p_value )
    {
        final Matcher l_match = ANNOTATIONSTRING.matcher( p_value );
        if ( !l_match.find() )
            return IAnnotation.EMPTY;

        return new CStringAnnotation( p_type, CRaw.cleanstring( l_match.group( 0 ).replaceAll( "\\(|\\)", "" ) ) );
    }

    /**
     * build constant annotation
     *
     * @param p_type annotation type
     * @param p_value annotation value string
     * @return annotation
     */
    private static IAnnotation<?> annotationconstant( @Nonnull final IAnnotation.EType p_type, @Nonnull final String p_value )
    {
        final Matcher l_match = ANNOTATIONTWOPARAMETER.matcher( p_value );
        if ( !l_match.find() )
            return IAnnotation.EMPTY;

        final String[] l_data = l_match.group().replaceAll( "\\(|\\)", "" ).split( "," );
        try
        {
            return new CConstantAnnotation<>( p_type, l_data[0], CRaw.numbervalue( l_data[1] ) );
        }
        catch ( final NumberFormatException l_exception )
        {
            return new CConstantAnnotation<>( p_type, l_data[0], CRaw.cleanstring( l_data[1] ) );
        }
    }

    /**
     * build variable description annotation
     *
     * @param p_type annotation type
     * @param p_value annotation value string
     * @return annotation
     */
    private static IAnnotation<?> annotationvariabledescription( @Nonnull final IAnnotation.EType p_type, @Nonnull final String p_value )
    {
        final Matcher l_match = ANNOTATIONTWOPARAMETER.matcher( p_value );
        if ( !l_match.find() )
            return IAnnotation.EMPTY;

        final String[] l_data = l_match.group().replaceAll( "\\(|\\)", "" ).split( "," );
        return new CConstantAnnotation<>( p_type, l_data[0], CRaw.cleanstring( l_data[1] ) );
    }



    // --- execution content -----------------------------------------------------------------------------------------------------------------------------------

    /**
     * builds a repair chain
     *
     * @param p_visitor visitor
     * @param p_chain input chain elements
     * @return null or repair
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    public static Stream<IExecution> repair( @Nonnull final ParseTreeVisitor<?> p_visitor, @Nonnull final List<? extends RuleContext> p_chain )
    {
        // @todo check to direct passing if argument is equal to 1 & pass value to plan execution
        return p_chain.stream().map( i -> (IExecution) p_visitor.visit( i ) );
    }

    /**
     * build repair formular
     *
     * @param p_visitor visitor
     * @param p_chain chain
     * @return execution stream
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    public static IExecution repairformula( @Nonnull final ParseTreeVisitor<?> p_visitor, @Nonnull final List<? extends RuleContext> p_chain )
    {
        return new CRepair( p_chain.stream().map( i -> (IExecution) p_visitor.visitChildren( i ) ) );
    }

    /**
     * define block formula
     *
     * @param p_visitor visitor
     * @param p_repairformula single repair formular
     * @param p_block block of elements
     * @return stream of execution elements
     */
    @Nonnull
    public static Object blockformular( @Nonnull final ParseTreeVisitor<?> p_visitor,
                                        @Nullable final RuleContext p_repairformula, @Nullable final RuleContext p_block )
    {
        return Objects.nonNull( p_repairformula )
            ? Stream.of( p_visitor.visit( p_repairformula ) )
            : (Stream<?>) p_visitor.visitChildren( p_block );
    }


    /**
     * create a single execution element
     *
     * @param p_visitor visitor
     * @param p_body list of parser element
     * @return first execution structur
     */
    @Nonnull
    public static Object bodyformular( @Nonnull final ParseTreeVisitor<?> p_visitor, @Nullable final RuleContext... p_body )
    {
        if ( Objects.isNull( p_body ) )
            throw new CSyntaxErrorException( CCommon.languagestring( CAgentSpeak.class, "unknownbody" ) );

        return Arrays.stream( p_body )
                     .filter( Objects::nonNull )
                     .findFirst()
                     .map( p_visitor::visitChildren )
                     .orElseThrow(  () -> new CSyntaxErrorException( CCommon.languagestring( CAgentSpeak.class, "unknownbody" ) ) );
    }



    // --- execution elements ----------------------------------------------------------------------------------------------------------------------------------

    /**
     * create a test-goal
     *
     * @param p_dollar dollar sign (rule / plan)
     * @param p_atom atom terminal
     * @return execution
     */
    @Nonnull
    public static IExecution executetestgoal( @Nullable final TerminalNode p_dollar, @Nonnull final TerminalNode p_atom )
    {
        return Objects.nonNull( p_dollar )
            ? new CTestRule( CPath.of( p_atom.getText() ) )
            : new CTestGoal( CPath.of( p_atom.getText() ) );
    }

    /**
     * build an achievment goal action
     *
     * @param p_visitor visitor
     * @param p_doubleexclamationmark immediatly
     * @param p_literal  literal
     * @param p_variable variable
     * @param p_arguments arguments
     * @return achievment goal
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    public static IExecution executeachievementgoal( @Nonnull final ParseTreeVisitor<?> p_visitor,
                                                     @Nullable final TerminalNode p_doubleexclamationmark,
                                                     @Nullable final RuleContext p_literal, @Nullable final RuleContext p_variable,
                                                     @Nullable final RuleContext p_arguments )
    {
        if ( Objects.nonNull( p_literal ) )
            return new CAchievementGoalLiteral( (ILiteral) p_visitor.visit( p_literal ), Objects.nonNull( p_doubleexclamationmark ) );

        if ( Objects.nonNull( p_variable ) )
            return new CAchievementGoalVariable(
                new CPassVariableLiteral(
                    (IVariable<?>) p_visitor.visit( p_variable ),
                    Objects.isNull( p_arguments )
                    ? Stream.empty()
                    : (Stream<ITerm>) p_visitor.visit( p_arguments )
                ),
                Objects.nonNull( p_doubleexclamationmark )
            );

        throw new CIllegalArgumentException( CCommon.languagestring( CAgentSpeak.class, "unknownachievmentgoal" ) );
    }

    /**
     * build an unary expression
     *
     * @param p_visitor visitor
     * @param p_operator operator
     * @param p_variable variable
     * @return null or execution
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    public static IExecution executeunary( @Nonnull final ParseTreeVisitor<?> p_visitor,
                                           @Nonnull final TerminalNode p_operator, @Nonnull final RuleContext p_variable )
    {
        switch ( p_operator.getText() )
        {
            case "++":
                return new CIncrement( (IVariable<Number>) p_visitor.visit( p_variable ) );

            case "--":
                return new CDecrement( (IVariable<Number>) p_visitor.visit( p_variable ) );

            default:
                throw new CSyntaxErrorException( CCommon.languagestring( CAgentSpeak.class, "unknownunary" ) );
        }
    }

    /**
     * build ternary operator
     *
     * @param p_visitor visitor
     * @param p_expression expression definition
     * @param p_true true execution
     * @param p_false false execution
     * @return ternary operator
     */
    @Nonnull
    public static IExecution executeternary( @Nonnull final ParseTreeVisitor<?> p_visitor,
                                             @Nonnull final RuleContext p_expression, @Nonnull final RuleContext p_true, @Nonnull final RuleContext p_false )
    {
        return new CTernaryOperation(
            (IExpression) p_visitor.visit( p_expression ),
            (IExecution) p_visitor.visit( p_true ),
            (IExecution) p_visitor.visit( p_false )
        );
    }

    /**
     * build belief action
     *
     * @param p_visitor visitor
     * @param p_literal belief literal
     * @param p_belieftrigger terminal
     * @return null or execution
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    public static IExecution executebelief( @Nonnull final ParseTreeVisitor<?> p_visitor, @Nonnull final RuleContext p_literal,
                                            @Nonnull final TerminalNode p_belieftrigger )
    {
        return new CBelief( CBelief.EAction.of( p_belieftrigger.getText() ), (ILiteral) p_visitor.visit( p_literal ) );
    }

    /**
     * creates an action execution definition
     *
     * @param p_visitor visitor
     * @param p_actionliteral action literal
     * @param p_actions map with actions
     * @return wrapped action
     */
    @Nonnull
    public static IExecution executeaction( @Nonnull final ParseTreeVisitor<?> p_visitor,
                                            @Nonnull final RuleContext p_actionliteral, @Nonnull final Map<IPath, IAction> p_actions )
    {
        final ILiteral l_actionliteral = (ILiteral) p_visitor.visit( p_actionliteral );

        final IAction l_action = p_actions.get( l_actionliteral.fqnfunctor() );
        if ( Objects.isNull( l_action ) )
            throw new CIllegalArgumentException( CCommon.languagestring( CAgentSpeak.class, "unknownaction", p_actionliteral.getText() ) );

        if ( l_actionliteral.orderedvalues().count() < l_action.minimalArgumentNumber() )
            throw new CIllegalArgumentException(
                CCommon.languagestring(
                    CAgentSpeak.class,
                    "wrongargumentnumber",
                    l_action.minimalArgumentNumber(),
                    p_actionliteral.getText()
                )
            );

        return new CPassAction( l_actionliteral.hasAt(), l_action, l_actionliteral.orderedvalues() );
    }

    /**
     * create a rule execution
     *
     * @param p_visitor visitor
     * @param p_literal literal
     * @param p_variableexecute variabel execution
     * @return rule execution
     */
    @Nonnull
    public static IExecution executerule( @Nonnull final ParseTreeVisitor<?> p_visitor,
                                          @Nullable final RuleContext p_literal, @Nullable final RuleContext p_variableexecute )
    {
        if ( Objects.nonNull( p_literal ) )
            return new CAchievementRuleLiteral( (ILiteral) p_visitor.visit( p_literal ) );

        if ( Objects.nonNull( p_variableexecute ) )
            return new CAchievementRuleVariable( (IExecution) p_visitor.visit( p_variableexecute ) );

        throw new CSyntaxErrorException( CCommon.languagestring( CAgentSpeak.class, "unknownruleexecution" ) );
    }

    /**
     * build a variable pass execution
     *
     * @param p_variable variable
     * @return variable pass execution
     */
    @Nonnull
    public static IExecution passvariable( @Nonnull final IVariable<?> p_variable )
    {
        return new CPassVariable( p_variable );
    }

    /**
     * build a data pass execution
     *
     * @param p_data native data
     * @tparam T data type
     * @return data pass execution
     */
    @Nonnull
    public static <T> IExecution passdata( @Nonnull final T p_data )
    {
        return new CPassRaw<>( p_data );
    }

    /**
     * build a boolean execution
     *
     * @param p_value value
     * @return execution
     */
    @Nonnull
    public static IExecution passboolean( final boolean p_value )
    {
        return new CPassBoolean( p_value );
    }

    /**
     * build a literal by variable
     *
     * @param p_visitor visitor
     * @param p_variable variable rule
     * @param p_termlist termlist rule
     * @return literal execution
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    public static IExecution passvaribaleliteral( @Nonnull final ParseTreeVisitor<?> p_visitor,
                                                  @Nonnull final RuleContext p_variable, @Nullable final RuleContext p_termlist )
    {
        return new CPassVariableLiteral(
            (IVariable<?>) p_visitor.visit( p_variable ),
            Objects.nonNull( p_termlist )
            ? (Stream<ITerm>) p_visitor.visit( p_termlist )
            : Stream.empty()
        );
    }

    /**
     * build variable list
     *
     * @param p_visitor visitor
     * @param p_list variable list
     * @return variabel stream
     */
    @Nonnull
    public static Stream<IVariable<?>> variablelist( @Nonnull final ParseTreeVisitor<?> p_visitor, @Nullable final List<? extends RuleContext> p_list )
    {
        return Objects.isNull( p_list )
            ? Stream.empty()
            : p_list.stream().map( i -> (IVariable<?>) p_visitor.visit( i ) );
    }



    // --- expression ------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * build an expression
     *
     * @param p_visitor visitor
     * @param p_unification unification term
     * @param p_term single term
     * @param p_unaryoperator unary operator
     * @param p_expression expression
     * @param p_binaryoperator binary operator
     * @param p_lhs left-hand-side argument
     * @param p_rhs right-hand-side argument
     * @return execution
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    public static IExecution expression( @Nonnull final ParseTreeVisitor<?> p_visitor, @Nullable final RuleContext p_term,
                                         @Nullable final RuleContext p_unification, @Nullable final TerminalNode p_unaryoperator,
                                         @Nullable final RuleContext p_expression, @Nullable final Token p_binaryoperator,
                                         @Nullable final RuleContext p_lhs, @Nullable final RuleContext p_rhs )
    {
        final IExecution l_term = termexecution( p_visitor, p_unification, p_term );
        if ( Objects.nonNull( l_term ) )
            return l_term;

        if ( Objects.nonNull( p_unaryoperator ) && Objects.nonNull( p_expression ) )
            return new CUnaryExpression( EUnaryOperator.of( p_unaryoperator.getText() ), (IExecution) p_visitor.visit( p_expression ) );

        if ( Objects.nonNull( p_expression ) )
            return (IExecution) p_visitor.visit( p_expression );

        if ( Objects.nonNull( p_binaryoperator ) && Objects.nonNull( p_lhs ) && Objects.nonNull( p_rhs ) )
            return new CBinaryExpression(
                EBinaryOperator.of( p_binaryoperator.getText() ),
                (IExecution) p_visitor.visit( p_lhs ),
                (IExecution) p_visitor.visit( p_rhs )
            );

        throw new CSyntaxErrorException( CCommon.languagestring( CAgentSpeak.class, "unknownexpression" ) );
    }

    /**
     * build an execution term
     *
     * @param p_visitor visitor
     * @param p_term term values
     * @return execution or null
     */
    @Nullable
    private static IExecution termexecution( @Nonnull final ParseTreeVisitor<?> p_visitor, @Nonnull final RuleContext... p_term )
    {
        final Object l_term = Arrays.stream( p_term )
                                    .filter( Objects::nonNull )
                                    .map( p_visitor::visit )
                                    .filter( Objects::nonNull )
                                    .findFirst()
                                    .orElse( null );

        if ( l_term instanceof IExecution )
            return (IExecution) l_term;

        if ( l_term instanceof IRawTerm<?> )
            return new CPassRaw<>( ( (IRawTerm<?>) l_term ).raw() );

        if ( l_term instanceof IVariable<?> )
            return new CPassVariable( (IVariable<?>) l_term );

        if ( Objects.nonNull( l_term ) )
            return new CPassRaw<>( l_term );

        return null;
    }


    // --- assignment ------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * build deconstruct
     *
     * @param p_visitor visitor
     * @param p_variable lhs variable list
     * @param p_literal rhs literal
     * @param p_variables rhs variable
     * @return deconstruct execution
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    public static IExecution deconstruct( @Nonnull final ParseTreeVisitor<?> p_visitor, @Nonnull final List<? extends RuleContext> p_variables,
                                          @Nullable final RuleContext p_literal, @Nullable final RuleContext p_variable )
    {
        if ( Objects.nonNull( p_literal ) && Objects.nonNull( p_variable ) )
            throw new CSyntaxErrorException( CCommon.languagestring( CAgentSpeak.class, "unknowndeconstruct" ) );

        return new CDeconstruct(
            p_variables.stream().map( i -> (IVariable<?>) p_visitor.visit( i ) ),
            Objects.nonNull( p_literal )
            ? (ITerm) p_visitor.visit( p_literal )
            : (ITerm) p_visitor.visit( p_variable )
        );
    }

    /**
     * build expression
     *
     * @param p_visitor visitor
     * @param p_expression expressions
     * @return expression execution
     */
    @Nonnull
    public static Object assignmentexpression( @Nonnull final ParseTreeVisitor<?> p_visitor, @Nonnull final RuleContext... p_expression )
    {
        return Arrays.stream( p_expression )
                     .filter( Objects::nonNull )
                     .findFirst()
                     .map( p_visitor::visit )
                     .orElseThrow( () -> new CSyntaxErrorException( CCommon.languagestring( CAgentSpeak.class, "unknownexpression" ) ) );
    }

    /**
     * build single assignment
     *
     * @param p_visitor visitor
     * @param p_lhs left-hand-side
     * @param p_operator operator
     * @param p_ternary ternary
     * @param p_expression expression
     * @return assignment execution
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    public static IExecution singleassignment( @Nonnull final ParseTreeVisitor<?> p_visitor, @Nonnull final RuleContext p_lhs,
                                               @Nonnull final TerminalNode p_operator, @Nullable final RuleContext p_ternary,
                                               @Nullable final RuleContext p_expression )
    {
        if ( Objects.nonNull( p_ternary ) )
            return new CSingleAssignment(
                EAssignOperator.of( p_operator.getText() ),
                (IVariable<?>) p_visitor.visit( p_lhs ),
                (IExecution) p_visitor.visit( p_ternary )
            );

        if ( Objects.nonNull( p_expression ) )
            return new CSingleAssignment(
                EAssignOperator.of( p_operator.getText() ),
                (IVariable<?>) p_visitor.visit( p_lhs ),
                (IExecution) p_visitor.visit( p_expression )
            );

        throw new CSyntaxErrorException( CCommon.languagestring( CAgentSpeak.class, "unknownassignment" ) );
    }

    /**
     * build multi assignment
     *
     * @param p_visitor visitor
     * @param p_variable variable
     * @param p_execution execution structure
     * @return assignment execution
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    public static IExecution multiassignment( @Nonnull final ParseTreeVisitor<?> p_visitor,
                                              @Nonnull final RuleContext p_variable, @Nonnull final RuleContext p_execution )
    {
        return new CMultiAssignment(
            (Stream<IVariable<?>>) p_visitor.visit( p_variable ),
            (IExecution) p_visitor.visit( p_execution )
        );
    }



    // --- unification -----------------------------------------------------------------------------------------------------------------------------------------

    /**
     * unification
     *
     * @param p_visitor visitor
     * @param p_parallel parallel call
     * @param p_literal literal call
     * @param p_constraint unficiation constraint
     * @return unification execution
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    public static IExecution unification( @Nonnull final ParseTreeVisitor<?> p_visitor, @Nullable final TerminalNode p_parallel,
                                          @Nonnull final RuleContext p_literal, @Nullable final RuleContext p_constraint )
    {
        final ILiteral l_literal = (ILiteral) p_visitor.visit( p_literal );
        final Object l_constraint = Objects.nonNull( p_constraint )
                                    ? p_visitor.visit( p_constraint )
                                    : null;

        if ( p_constraint instanceof IExpression )
            return new CExpressionUnify(
                Objects.nonNull( p_parallel ),
                l_literal,
                (IExpression) l_constraint
            );

        if ( p_constraint instanceof IVariable<?> )
            return new CVariableUnify(
                Objects.nonNull( p_parallel ),
                l_literal,
                (IVariable<?>) l_constraint
            );

        return new CDefaultUnify( Objects.nonNull( p_parallel ), l_literal );
    }

    /**
     * unification constraint
     *
     * @param p_visitor visitor
     * @param p_variable variable
     * @param p_expression expression
     * @return unification constraint as variable or expression
     */
    @Nonnull
    public static Object unificationconstraint( @Nonnull final ParseTreeVisitor<?> p_visitor,
                                                @Nullable final RuleContext p_variable, @Nullable final RuleContext p_expression )
    {
        if ( Objects.nonNull( p_expression ) )
            return p_visitor.visit( p_expression );

        if ( Objects.nonNull( p_variable ) )
            return p_visitor.visit( p_variable );

        throw new CSyntaxErrorException( CCommon.languagestring( CAgentSpeak.class, "unknownunificationconstraint" ) );
    }



    // --- lambda expression -----------------------------------------------------------------------------------------------------------------------------------

    /**
     * build a lambda expression
     *
     * @param p_visitor visitor
     * @param p_parallel parallel call
     * @param p_stream stream data
     * @param p_body execution body
     * @param p_return return element
     * @return lambda expression
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    public static IExecution lambda( @Nonnull final ParseTreeVisitor<?> p_visitor, boolean p_parallel,
                                     @Nonnull final RuleContext p_stream, @Nonnull final RuleContext p_iterationvariable,
                                     @Nonnull final RuleContext p_body,
                                     @Nullable final RuleContext p_return )
    {
        return new CLambda(
            p_parallel,
            (IExecution) p_visitor.visit( p_stream ),
            (IVariable<?>) p_visitor.visit( p_iterationvariable ),
            (Stream<IExecution>) p_visitor.visit( p_body ),
            Objects.nonNull( p_return )
            ? (IVariable<?>) p_visitor.visit( p_return )
            : IVariable.EMPTY
        );
    }

    /**
     * build a lambda stream
     *
     * @param p_visitor visitor
     * @param p_lambdastreaming lambda streaming
     * @param p_hash hash value (range or variable list)
     * @param p_number number values
     * @param p_variable variable
     * @param p_additional additional defintion
     * @return lambda initialization expression
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    public static IExecution lambdastream( @Nonnull final ParseTreeVisitor<?> p_visitor, @Nonnull final Set<ILambdaStreaming<?>> p_lambdastreaming,
                                           @Nullable final TerminalNode p_hash, @Nullable final TerminalNode p_number,
                                           @Nullable final RuleContext p_variable, @Nullable final List<? extends RuleContext> p_additional )
    {
        final Stream<IExecution> l_stream = org.lightjason.agentspeak.language.CCommon.streamconcatstrict(
            Objects.nonNull( p_number )
            ? Stream.of( passdata( CRaw.numbervalue( p_number ) ) )
            : Stream.empty(),

            Objects.nonNull( p_variable )
            ? Stream.of( passvariable( (IVariable<?>) p_visitor.visit( p_variable ) ) )
            : Stream.empty(),

            Objects.nonNull( p_additional )
            ? p_additional.stream().map( i -> (IExecution) p_visitor.visit( i ) )
            : Stream.empty()
        );

        return Objects.nonNull( p_hash )
            ? new CLambdaInitializeRange( l_stream )
            : new CLambdaInitializeStream( l_stream, p_lambdastreaming );
    }

    /**
     * build a lambda element
     *
     * @param p_visitor visitor
     * @param p_number number
     * @param p_variable variable
     * @return execution
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    public static IExecution lambdaelement( @Nonnull final ParseTreeVisitor<?> p_visitor,
                                            @Nullable final TerminalNode p_number, @Nullable final RuleContext p_variable )
    {
        if ( Objects.nonNull( p_number ) )
            return passdata( CRaw.numbervalue( p_number ) );

        if ( Objects.nonNull( p_variable ) )
            return passvariable( (IVariable<?>) p_visitor.visitChildren( p_variable ) );

        throw new CSyntaxErrorException( CCommon.languagestring( CAgentSpeak.class, "unknownlambdaelement" ) );
    }

}
