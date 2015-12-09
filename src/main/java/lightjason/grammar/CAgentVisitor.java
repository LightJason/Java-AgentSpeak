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

package lightjason.grammar;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import lightjason.common.CCommon;
import lightjason.error.CIllegalArgumentException;
import lightjason.language.CLiteral;
import lightjason.language.CMutexVariable;
import lightjason.language.CRawTerm;
import lightjason.language.CVariable;
import lightjason.language.ILiteral;
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.plan.CPlan;
import lightjason.language.plan.IBodyAction;
import lightjason.language.plan.IPlan;
import lightjason.language.plan.action.CAchievementGoal;
import lightjason.language.plan.action.CBeliefAction;
import lightjason.language.plan.action.CRawAction;
import lightjason.language.plan.action.CTestGoal;
import lightjason.language.plan.annotation.CAtomAnnotation;
import lightjason.language.plan.annotation.CNumberAnnotation;
import lightjason.language.plan.annotation.CSymbolicAnnotation;
import lightjason.language.plan.annotation.IAnnotation;
import lightjason.language.plan.trigger.CTrigger;
import lightjason.language.plan.trigger.ITrigger;
import lightjason.language.plan.unaryoperator.CDecrement;
import lightjason.language.plan.unaryoperator.CIncrement;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * class to visit each AST node of an agent
 */
public class CAgentVisitor extends lightjason.grammar.AgentBaseVisitor<Object> implements IAgentVisitor
{
    /**
     * initial goal
     */
    private ILiteral m_InitialGoal;
    /**
     * set with initial beliefs
     */
    private final Set<ILiteral> m_InitialBeliefs = new HashSet<>();
    /**
     * map with plans
     */
    private final SetMultimap<ITrigger<?>, IPlan> m_plans = HashMultimap.create();


    @Override
    public Object visitInitial_beliefs( final lightjason.grammar.AgentParser.Initial_beliefsContext p_context )
    {
        p_context.belief().parallelStream().map( i -> (ILiteral) this.visitBelief( i ) ).forEach( m_InitialBeliefs::add );
        return null;
    }

    @Override
    public Object visitInitial_goal( final lightjason.grammar.AgentParser.Initial_goalContext p_context )
    {
        m_InitialGoal = new CLiteral( p_context.atom().getText() );
        return null;
    }

    @Override
    public Object visitBelief( final lightjason.grammar.AgentParser.BeliefContext p_context )
    {
        return new CLiteral( (CLiteral) this.visitLiteral( p_context.literal() ), p_context.STRONGNEGATION() != null );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Object visitPlan( final lightjason.grammar.AgentParser.PlanContext p_context )
    {
        final ILiteral l_head = (ILiteral) this.visitLiteral( p_context.literal() );
        final ITrigger.EType l_trigger = (ITrigger.EType) this.visitPlan_trigger( p_context.plan_trigger() );
        final Set<IAnnotation<?>> l_annotation = (Set) this.visitAnnotations( p_context.annotations() );

        // parallel stream does not work with multi hashmap
        p_context.plandefinition().stream().forEach( i -> {

            final Pair<Object, List<IBodyAction>> l_content = (Pair<Object, List<IBodyAction>>) this.visitPlandefinition( i );
            final IPlan l_plan = new CPlan( new CTrigger( l_trigger, l_head.getFQNFunctor() ), l_head, l_content.getRight(), l_annotation );
            m_plans.put( l_plan.getTrigger(), l_plan );

        } );

        return null;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Object visitPlandefinition( final lightjason.grammar.AgentParser.PlandefinitionContext p_context )
    {
        return new ImmutablePair<Object, List<IBodyAction>>(
                this.visitPlan_context( p_context.plan_context() ), (List<IBodyAction>) this.visitBody( p_context.body() ) );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Object visitAnnotations( final lightjason.grammar.AgentParser.AnnotationsContext p_context )
    {
        if ( ( p_context == null ) || ( p_context.isEmpty() ) )
            return Collections.EMPTY_SET;

        final Set<IAnnotation<?>> l_annotation = new HashSet<>();
        if ( p_context.annotation_atom() != null )
            p_context.annotation_atom().stream().map( i -> (IAnnotation) this.visitAnnotation_atom( i ) ).forEach( l_annotation::add );
        if ( p_context.annotation_literal() != null )
            p_context.annotation_literal().stream().map( i -> (IAnnotation) this.visitAnnotation_literal( i ) ).forEach( l_annotation::add );

        return l_annotation.isEmpty() ? Collections.EMPTY_SET : l_annotation;
    }

    @Override
    public Object visitAnnotation_atom( final lightjason.grammar.AgentParser.Annotation_atomContext p_context )
    {
        if ( p_context.ATOMIC() != null )
            return new CAtomAnnotation( IAnnotation.EType.ATOMIC );

        if ( p_context.EXCLUSIVE() != null )
            return new CAtomAnnotation( IAnnotation.EType.EXCLUSIVE );

        if ( p_context.PARALLEL() != null )
            return new CAtomAnnotation( IAnnotation.EType.PARALLEL );

        throw new CIllegalArgumentException( CCommon.getLanguageString( this, "atomannotation", p_context.getText() ) );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Object visitAnnotation_numeric_literal( final lightjason.grammar.AgentParser.Annotation_numeric_literalContext p_context )
    {
        if ( p_context.FUZZY() != null )
            return new CNumberAnnotation<>( IAnnotation.EType.FUZZY, (Number) this.visitNumber( p_context.number() ) );

        if ( p_context.PRIORITY() != null )
            return new CNumberAnnotation<>( IAnnotation.EType.PRIORITY, ( (Number) this.visitNumber( p_context.number() ) ).longValue() );

        throw new CIllegalArgumentException( CCommon.getLanguageString( this, "numberannotation", p_context.getText() ) );
    }

    @Override
    public Object visitAnnotation_symbolic_literal( final lightjason.grammar.AgentParser.Annotation_symbolic_literalContext p_context )
    {
        if ( p_context.EXPIRES() != null )
            return new CSymbolicAnnotation( IAnnotation.EType.EXPIRES, (ILiteral) this.visitAtom( p_context.atom() ) );

        throw new CIllegalArgumentException( CCommon.getLanguageString( this, "symbolicliteralannotation", p_context.getText() ) );
    }

    @Override
    public Object visitPlan_goal_trigger( final lightjason.grammar.AgentParser.Plan_goal_triggerContext p_context )
    {
        switch ( p_context.getText() )
        {
            case "+!":
                return ITrigger.EType.ADDGOAL;
            case "-!":
                return ITrigger.EType.DELETEGOAL;

            default:
                throw new CIllegalArgumentException( CCommon.getLanguageString( this, "goaltrigger", p_context.getText() ) );
        }
    }

    @Override
    public Object visitPlan_belief_trigger( final lightjason.grammar.AgentParser.Plan_belief_triggerContext p_context )
    {
        switch ( p_context.getText() )
        {
            case "+":
                return ITrigger.EType.ADDBELIEF;
            case "-":
                return ITrigger.EType.DELETEBELIEF;
            case "-+":
                return ITrigger.EType.CHANGEBELIEF;

            default:
                throw new CIllegalArgumentException( CCommon.getLanguageString( this, "belieftrigger", p_context.getText() ) );
        }
    }

    @Override
    public Object visitPlan_context( final lightjason.grammar.AgentParser.Plan_contextContext p_context )
    {
        return p_context == null ? "" : p_context.getText();
    }

    @Override
    public Object visitBody( final lightjason.grammar.AgentParser.BodyContext p_context )
    {
        // filter null values of the body formular, because blank lines add a null value
        return p_context.body_formula().parallelStream().filter( i -> i != null ).map( i -> {

            final Object l_item = this.visitBody_formula( i );
            if ( l_item instanceof IBodyAction )
                return l_item;

            return new CRawAction<>( l_item );

        } ).collect( Collectors.toList() );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Object visitUnary_expression( final lightjason.grammar.AgentParser.Unary_expressionContext p_context )
    {
        switch ( p_context.unaryoperator().getText() )
        {
            case "++":
                return new CIncrement<>( (IVariable) this.visitVariable( p_context.variable() ) );

            case "--":
                return new CDecrement<>( (IVariable) this.visitVariable( p_context.variable() ) );

            default:
                throw new CIllegalArgumentException( CCommon.getLanguageString( this, "unaryoperator", p_context.getText() ) );
        }
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Object visitAchievement_goal_action( final lightjason.grammar.AgentParser.Achievement_goal_actionContext p_context )
    {
        return new CAchievementGoal( (ILiteral) this.visitLiteral( p_context.literal() ), p_context.DOUBLEEXCLAMATIONMARK() != null );
    }

    @Override
    public Object visitTest_goal_action( final lightjason.grammar.AgentParser.Test_goal_actionContext p_context )
    {
        return new CTestGoal( (ILiteral) this.visitLiteral( p_context.literal() ) );
    }

    @Override
    public Object visitBelief_action( final lightjason.grammar.AgentParser.Belief_actionContext p_context )
    {
        if ( p_context.PLUS() != null )
            return new CBeliefAction( (ILiteral) this.visitLiteral( p_context.literal() ), CBeliefAction.EAction.Add );

        if ( p_context.MINUS() != null )
            return new CBeliefAction( (ILiteral) this.visitLiteral( p_context.literal() ), CBeliefAction.EAction.Delete );

        if ( p_context.MINUSPLUS() != null )
            return new CBeliefAction( (ILiteral) this.visitLiteral( p_context.literal() ), CBeliefAction.EAction.Change );

        throw new CIllegalArgumentException( CCommon.getLanguageString( this, "beliefaction", p_context.getText() ) );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    @SuppressWarnings( "unchecked" )
    public Object visitLiteral( final lightjason.grammar.AgentParser.LiteralContext p_context )
    {
        return new CLiteral(
                this.visitAtom( p_context.atom() ).toString(),
                (Collection<ITerm>) this.visitTermlist( p_context.termlist() ),
                (Collection<ILiteral>) this.visitLiteralset( p_context.literalset() )
        );
    }

    @Override
    public Object visitTermlist( final lightjason.grammar.AgentParser.TermlistContext p_context )
    {
        if ( ( p_context == null ) || ( p_context.isEmpty() ) )
            return Collections.EMPTY_LIST;

        Object x = p_context.term().stream().map( i -> this.visitTerm( i ) ).filter( i -> i != null ).map(
                i -> i instanceof ITerm ? (ITerm) i : new CRawTerm<>( i )
        ).collect( Collectors.toList() );
        System.out.println( x );

        return x;
    }

    @Override
    public Object visitLiteralset( final lightjason.grammar.AgentParser.LiteralsetContext p_context )
    {
        if ( ( p_context == null ) || ( p_context.isEmpty() ) )
            return Collections.EMPTY_LIST;

        return p_context.literal().stream().map( i -> this.visitLiteral( i ) ).filter( i -> i != null ).collect( Collectors.toList() );
    }

    @Override
    public Object visitAtom( final lightjason.grammar.AgentParser.AtomContext p_context )
    {
        return p_context.getText();
    }

    @Override
    public Object visitVariable( final lightjason.grammar.AgentParser.VariableContext p_context )
    {
        return p_context.AT() == null ? new CVariable<>( p_context.getText() ) : new CMutexVariable<>( p_context.getText() );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public Object visitFloatnumber( final lightjason.grammar.AgentParser.FloatnumberContext p_context )
    {
        switch ( p_context.getText() )
        {
            case "pi":
                return ( p_context.MINUS() == null ? 1 : -1 ) * Math.PI;
            case "euler":
                return ( p_context.MINUS() == null ? 1 : -1 ) * Math.E;
            case "lightspeed":
                return (double) ( ( p_context.MINUS() == null ? 1 : -1 ) * 299792458 );
            case "avogadro":
                return ( p_context.MINUS() == null ? 1 : -1 ) * 6.0221412927e23;
            case "boltzmann":
                return ( p_context.MINUS() == null ? 1 : -1 ) * 8.617330350e-15;
            case "gravity":
                return ( p_context.MINUS() == null ? 1 : -1 ) * 6.67408e-11;
            case "electron":
                return ( p_context.MINUS() == null ? 1 : -1 ) * 9.10938356e-31;
            case "neutron":
                return ( p_context.MINUS() == null ? 1 : -1 ) * 1674927471214e-27;
            case "proton":
                return ( p_context.MINUS() == null ? 1 : -1 ) * 1.6726219e-27;

            default:
                return Double.valueOf( p_context.getText() );
        }
    }

    @Override
    public Object visitIntegernumber( final lightjason.grammar.AgentParser.IntegernumberContext p_context )
    {
        return Long.valueOf( p_context.getText() );
    }

    @Override
    public Object visitLogicalvalue( final lightjason.grammar.AgentParser.LogicalvalueContext p_context )
    {
        return p_context.TRUE() != null ? true : false;
    }

    @Override
    public Object visitString( final lightjason.grammar.AgentParser.StringContext p_context )
    {
        return p_context.getText();
    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final Set<ILiteral> getInitialBeliefs()
    {
        return m_InitialBeliefs;
    }

    @Override
    public final ILiteral getInitialGoal()
    {
        return m_InitialGoal;
    }

    @Override
    public SetMultimap<ITrigger<?>, IPlan> getPlans()
    {
        return m_plans;
    }

    @Override
    public Map<String, Object> getRules()
    {
        return null;
    }

}
