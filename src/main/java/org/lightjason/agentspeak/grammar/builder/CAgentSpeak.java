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
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.error.CSyntaxErrorException;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.CBelief;
import org.lightjason.agentspeak.language.execution.CRepair;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.execution.achievement_test.CAchievementGoalLiteral;
import org.lightjason.agentspeak.language.execution.achievement_test.CAchievementGoalVariable;
import org.lightjason.agentspeak.language.execution.achievement_test.CTestGoal;
import org.lightjason.agentspeak.language.execution.achievement_test.CTestRule;
import org.lightjason.agentspeak.language.execution.assignment.CDeconstruct;
import org.lightjason.agentspeak.language.execution.assignment.CMultiAssignment;
import org.lightjason.agentspeak.language.execution.assignment.CSingleAssignment;
import org.lightjason.agentspeak.language.execution.assignment.CTernaryOperation;
import org.lightjason.agentspeak.language.execution.expression.IExpression;
import org.lightjason.agentspeak.language.execution.assignment.EAssignOperator;
import org.lightjason.agentspeak.language.execution.lambda.CLambdaInitializeRange;
import org.lightjason.agentspeak.language.execution.lambda.CLambdaInitializeStream;
import org.lightjason.agentspeak.language.execution.lambda.ILambdaStreaming;
import org.lightjason.agentspeak.language.execution.passing.CPassBoolean;
import org.lightjason.agentspeak.language.execution.passing.CPassRaw;
import org.lightjason.agentspeak.language.execution.passing.CPassVariableLiteral;
import org.lightjason.agentspeak.language.execution.passing.CPassVariable;
import org.lightjason.agentspeak.language.execution.passing.CPassExecution;
import org.lightjason.agentspeak.language.execution.unary.CDecrement;
import org.lightjason.agentspeak.language.execution.unary.CIncrement;
import org.lightjason.agentspeak.language.execution.unify.CDefaultUnify;
import org.lightjason.agentspeak.language.execution.unify.CExpressionUnify;
import org.lightjason.agentspeak.language.execution.unify.CVariableUnify;
import org.lightjason.agentspeak.language.execution.instantiable.plan.CPlan;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.execution.instantiable.plan.annotation.CAtomAnnotation;
import org.lightjason.agentspeak.language.execution.instantiable.plan.annotation.CValueAnnotation;
import org.lightjason.agentspeak.language.execution.instantiable.plan.annotation.IAnnotation;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.execution.instantiable.rule.CRulePlaceholder;
import org.lightjason.agentspeak.language.execution.instantiable.rule.IRule;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * builder of agentspeak components
 */
public final class CAgentSpeak
{
    /**
     * reg pattern for matching annotation constant values
     */
    private static final Pattern ANNOTATIONCONSTANT = Pattern.compile( ".+\\( .+, .+ \\)" );

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
     * @param p_trigger trigger terminal
     * @return plan
     */
    @Nonnull
    public static IPlan plan( @Nonnull final ParseTreeVisitor<?> p_visitor,
                              @Nullable final List<TerminalNode> p_annotation, @Nonnull final TerminalNode p_trigger, @Nonnull final RuleContext p_literal,
                              @Nonnull final List<? extends RuleContext> p_expression, @Nonnull final List<? extends RuleContext> p_body )
    {
        final ITrigger l_trigger = CTrigger.of(
            ITrigger.EType.of( p_trigger.getText() ),
            (ILiteral) p_visitor.visit( p_literal )
        );

        final List<IAnnotation<?>> l_annotation = Objects.isNull( p_annotation )
                                                  ? Collections.emptyList()
                                                  : p_annotation.stream()
                                                                .map( CAgentSpeak::annotation )
                                                                .filter( i -> !i.equals( IAnnotation.EMPTY ) )
                                                                .collect( Collectors.toList() );

        // @todo handle body & expression
        return new CPlan(
            l_annotation.stream(),
            l_trigger,

            IExpression.EMPTY,

            Stream.empty()
        );
    }

    /**
     * build a rule
     *
     * @param p_literal literal
     * @param p_annotation annotation stream
     * @return rule
     */
    @Nonnull
    public static IRule rule( @Nonnull final ILiteral p_literal, @Nonnull final Stream<TerminalNode> p_annotation )
    {
        // @todo set rule
        return IRule.EMPTY;
    }

    /**
     * create a rule placeholder
     *
     * @param p_literal literal
     * @return place holder rule
     */
    @Nonnull
    public static IRule ruleplaceholder( @Nonnull final ILiteral p_literal )
    {
        return new CRulePlaceholder(
            p_literal
        );
    }

    /**
     * build annotation object
     * @param p_annotation annotation terminal
     * @return null or annoation
     */
    @Nonnull
    public static IAnnotation<?> annotation( @Nullable final TerminalNode p_annotation )
    {
        if ( Objects.isNull( p_annotation ) )
            return IAnnotation.EMPTY;

        if ( p_annotation.getText().contains( "parallel" ) )
            return new CAtomAnnotation<>( IAnnotation.EType.PARALLEL );

        if ( p_annotation.getText().contains( "atomic" ) )
            return new CAtomAnnotation<>( IAnnotation.EType.ATOMIC );

        // on a constant annoation object, the data and name must be split at the comma,
        // the value can be a number value or a string
        if ( p_annotation.getText().contains( "constant" ) )
        {
            final Matcher l_match = ANNOTATIONCONSTANT.matcher( p_annotation.getText() );
            if ( !l_match.find() )
                return IAnnotation.EMPTY;

            final String l_data = l_match.group( 2 ).replace( "'", "" ).replace( "\"", "" );
            try
            {
                return new CValueAnnotation<>( IAnnotation.EType.CONSTANT, l_match.group( 1 ), CRaw.numbervalue( l_data ) );
            }
            catch ( final NumberFormatException l_exception )
            {
                return new CValueAnnotation<>( IAnnotation.EType.CONSTANT, l_match.group( 1 ), l_data );
            }
        }

        throw new CSyntaxErrorException( CCommon.languagestring( CAgentSpeak.class, "annotation" ) );
    }




    // --- execution content -----------------------------------------------------------------------------------------------------------------------------------

    /**
     * builds a repair chain
     *
     * @param p_chain input chain elements
     * @return null or repair
     */
    @Nonnull
    public static IExecution repair( @Nonnull final ParseTreeVisitor<?> p_visitor, @Nonnull final List<? extends RuleContext> p_chain )
    {
        return new CRepair( p_chain.stream().map( i -> (IExecution) p_visitor.visit( i ) ) );
    }

    /**
     * build repair formular
     *
     * @param p_visitor visitor
     * @param p_body body
     * @param p_next next formula
     * @return execution stream
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    public static Stream<IExecution> repairformula( @Nonnull final ParseTreeVisitor<?> p_visitor,
                                                    @Nonnull final RuleContext p_body, @Nullable final RuleContext p_next )
    {
        return Stream.concat(
            Stream.of( (IExecution) p_visitor.visit( p_body ) ),
            Objects.nonNull( p_next )
            ? (Stream<IExecution>) p_visitor.visit( p_next )
            : Stream.empty()
        );
    }

    /**
     * define block formula
     *
     * @param p_visitor visitor
     * @param p_body body
     * @param p_bodyformula other elements
     * @return stream of execution elements
     */
    @Nonnull
    public static Object blockformular( @Nonnull final ParseTreeVisitor<?> p_visitor,
                                        @Nonnull final RuleContext p_body, @Nullable final RuleContext p_bodyformula )
    {
        return Objects.nonNull( p_bodyformula )
               ? Stream.concat( Stream.of( p_visitor.visit( p_body ) ), (Stream<?>) p_visitor.visit( p_bodyformula ) )
               : Stream.of( p_visitor.visit( p_body ) );
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
    public static IExecution testgoal( @Nullable final TerminalNode p_dollar, @Nonnull final TerminalNode p_atom )
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
    public static IExecution achievementgoal( @Nonnull final ParseTreeVisitor<?> p_visitor,
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
                    Stream.empty()
                ),
                Objects.nonNull( p_doubleexclamationmark )
            );

        throw new CIllegalArgumentException( CCommon.languagestring( CAgentSpeak.class, "unknownachievmentgoal" ) );
    }

    /**
     * build an unary expression
     *
     * @param p_operator operator
     * @param p_variable variable
     * @return null or execution
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    public static IExecution unary( @Nonnull final ParseTreeVisitor<?> p_visitor, @Nonnull final TerminalNode p_operator, @Nonnull final RuleContext p_variable )
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
     * @param p_expression expression definition
     * @param p_true true execution
     * @param p_false false execution
     * @return ternary operator
     */
    @Nonnull
    public static IExecution ternary( @Nonnull final ParseTreeVisitor<?> p_visitor,
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
     * @param p_addbelief add-belief terminal
     * @param p_deletebelief delete-belief terminal
     * @param p_literal belief literal
     * @return null or execution
     */
    @Nonnull
    public static IExecution beliefaction( @Nullable final TerminalNode p_addbelief, @Nullable final TerminalNode p_deletebelief, @Nonnull final ILiteral p_literal )
    {
        if ( Objects.nonNull( p_addbelief ) )
            return new CBelief( p_literal, CBelief.EAction.ADD );

        if ( Objects.nonNull( p_deletebelief ) )
            return new CBelief( p_literal, CBelief.EAction.DELETE );

        throw new CSyntaxErrorException( CCommon.languagestring( CAgentSpeak.class, "unknownbeliefaction" ) );
    }

    /**
     * creates an action execution definition
     *
     * @param p_actionliteral action literal
     * @param p_actions map with actions
     * @return wrapped action
     */
    @Nonnull
    public static IExecution action( @Nonnull final ParseTreeVisitor<?> p_visitor,
                                     @Nonnull final RuleContext p_actionliteral, @Nonnull final Map<IPath, IAction> p_actions )
    {
        final ILiteral l_actionliteral = (ILiteral) p_visitor.visit( p_actionliteral );

        final IAction l_action = p_actions.get( l_actionliteral.fqnfunctor() );
        if ( Objects.isNull( l_action ) )
            throw new CIllegalArgumentException( CCommon.languagestring( CAgentSpeak.class, "actionunknown", p_actionliteral ) );

        if ( l_actionliteral.orderedvalues().count() < l_action.minimalArgumentNumber() )
            throw new CIllegalArgumentException( CCommon.languagestring( CAgentSpeak.class, "argumentnumber", p_actionliteral, l_action.minimalArgumentNumber() ) );

        return new CPassExecution( l_actionliteral.hasAt(), l_action );
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




    // --- assignment ------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * build deconstruct
     *
     * @return deconstruct execution
     */
    @Nonnull
    public static IExecution deconstruct( @Nonnull final ParseTreeVisitor<?> p_visitor, @Nonnull final Stream<IVariable<?>> p_variables,
                                          @Nullable final ITerm p_literal, @Nullable final ITerm p_variable )
    {
        return new CDeconstruct(
            p_variables,
            Objects.nonNull( p_literal ) ? p_literal : p_variable
        );
    }

    /**
     * build single assignment
     *
     * @param p_lhs left-hand-side
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
                (IVariable<?>) p_visitor.visit( p_lhs ),
                passvariable( (IVariable<?>) p_visitor.visit( p_ternary ) ),
                EAssignOperator.of( p_operator.getText() )
            );

        if ( Objects.nonNull( p_expression ) )
            return new CSingleAssignment(
                (IVariable<?>) p_visitor.visit( p_lhs ),
                (IExecution) p_visitor.visit( p_expression ),
                EAssignOperator.of( p_operator.getText() )
            );

        throw new CSyntaxErrorException( CCommon.languagestring( CAgentSpeak.class, "unknownassignment" ) );
    }

    /**
     * build multi assignment
     *
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
        final Object l_constraint = p_visitor.visit( p_constraint );

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
     * @param p_parallel parallel call
     * @param p_iterator base iterator
     * @return lambda expression
     */
    @Nonnull
    public static IExpression lambda( @Nonnull final ParseTreeVisitor<?> p_visitor, boolean p_parallel,
                                      @Nonnull final RuleContext p_iterator, @Nullable final RuleContext p_return, @Nonnull final RuleContext p_execution )
    {
        return null;
    }

    /**
     * build a lambda initialization
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
    public static IExecution lambdainitialization( @Nonnull final ParseTreeVisitor<?> p_visitor, @Nonnull final Set<ILambdaStreaming<?>> p_lambdastreaming,
                                                   @Nullable final TerminalNode p_hash, @Nullable final TerminalNode p_number,
                                                   @Nullable final RuleContext p_variable, @Nullable final List<? extends RuleContext> p_additional )
    {
        return Objects.nonNull( p_hash )

            ? new CLambdaInitializeRange(
                org.lightjason.agentspeak.language.CCommon.streamconcat(

                    Objects.nonNull( p_number )
                    ? Stream.of( passdata( CRaw.numbervalue( p_number ) ) )
                    : Stream.empty(),

                    Objects.nonNull( p_variable )
                    ? Stream.of( (IExecution) p_visitor.visit( p_variable ) )
                    : Stream.empty(),

                    Objects.nonNull( p_additional )
                    ? p_additional.stream().map( i -> (IExecution) p_visitor.visit( i ) )
                    : Stream.empty()

                ).toArray( IExecution[]::new )
            )

            : new CLambdaInitializeStream(
                org.lightjason.agentspeak.language.CCommon.streamconcat(

                    Objects.nonNull( p_number )
                    ? Stream.of( passdata( CRaw.numbervalue( p_number ) ) )
                    : Stream.empty(),

                    Objects.nonNull( p_variable )
                    ? Stream.of( (IExecution) p_visitor.visit( p_variable ) )
                    : Stream.empty(),

                    Objects.nonNull( p_additional )
                    ? p_additional.stream().map( i -> (IExecution) p_visitor.visit( i ) )
                    : Stream.empty()

                ).toArray( IExecution[]::new ),

                p_lambdastreaming
            );
    }

    /**
     * build a lambda element
     *
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
            return passvariable( (IVariable<?>) p_visitor.visit( p_variable ) );

        throw new CSyntaxErrorException( CCommon.languagestring( CAgentSpeak.class, "unknownlambdaelement" ) );
    }

}
