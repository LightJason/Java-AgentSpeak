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

package lightjason;


import lightjason.language.CLiteral;
import lightjason.language.CVariable;
import lightjason.language.ILiteral;
import lightjason.language.ITerm;
import lightjason.runtime.IPlan;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * class to visit each AST node
 */
public class CAgentSpeakVisitor extends lightjason.JasonBaseVisitor<Object> implements IAgentSpeakVisitor
{
    /**
     * set with initial beliefs
     */
    private final Set<ILiteral> m_initialbeliefs = new HashSet<>();
    /**
     * initial goal
     */
    private ILiteral m_initialgoal;
    /**
     *
     */
    private final Map<String, Set<IPlan>> m_plans = new HashMap<>();

    @Override
    public Object visitInitial_beliefs( final lightjason.JasonParser.Initial_beliefsContext p_context )
    {
        p_context.belief().parallelStream().map( i -> (ILiteral) this.visitBelief( i ) ).forEach( m_initialbeliefs::add );
        return null;
    }

    @Override
    public Object visitInitial_goal( final lightjason.JasonParser.Initial_goalContext p_context )
    {
        m_initialgoal = new CLiteral( p_context.atom().getText() );
        return null;
    }

    @Override
    public Object visitBelief( final lightjason.JasonParser.BeliefContext p_context )
    {
        return new CLiteral( (CLiteral) this.visitLiteral( p_context.literal() ), p_context.STRONGNEGATION() != null );
    }

    @Override
    public Object visitPlan( final lightjason.JasonParser.PlanContext p_context )
    {
        //final IPlan l_plan = new CPlan( p_context.atom().getText() );
        //System.out.println(p_context.atom().getText());
        /*
        final Set<IPlan> l_plans = m_plans.getOrDefault( l_plan.getName(), new HashSet<>() );

        l_plans.add( l_plan );
        m_plans.putIfAbsent(  l_plan.getName(), l_plans );
        */
        return super.visitPlan( p_context );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    @SuppressWarnings( "unchecked" )
    public Object visitLiteral( final lightjason.JasonParser.LiteralContext p_context )
    {
        switch ( p_context.list().size() )
        {
            case 1:
                return new CLiteral( this.visitAtom( p_context.atom() ).toString(), (List<ITerm>) this.visitList( p_context.list( 0 ) ) );

            case 2:
                return new CLiteral(
                        this.visitAtom( p_context.atom() ).toString(), (List<ITerm>) this.visitList( p_context.list( 0 ) ),
                        new HashSet<>( (List<ITerm>) this.visitList( p_context.list( 1 ) ) )
                );

            default:
                return new CLiteral( this.visitAtom( p_context.atom() ).toString() );
        }
    }

    @Override
    public Object visitTerm( final lightjason.JasonParser.TermContext p_context )
    {
        return super.visitTerm( p_context );
    }

    @Override
    public Object visitList( final lightjason.JasonParser.ListContext p_context )
    {
        return p_context.term().parallelStream().map( i -> super.visitTerm( i ) ).collect( Collectors.toList() );
    }

    @Override
    public Object visitAtom( final lightjason.JasonParser.AtomContext p_context )
    {
        return p_context.getText();
    }

    @Override
    public Object visitVariable( final lightjason.JasonParser.VariableContext p_context )
    {
        return new CVariable<>( p_context.getText() );
    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public Object visitFloatnumber( final lightjason.JasonParser.FloatnumberContext p_context )
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
    public Object visitIntegernumber( final lightjason.JasonParser.IntegernumberContext p_context )
    {
        return Long.valueOf( p_context.getText() );
    }

    @Override
    public Object visitLogicalvalue( final lightjason.JasonParser.LogicalvalueContext p_context )
    {
        return p_context.TRUE() != null ? true : false;
    }

    @Override
    public Object visitString( final lightjason.JasonParser.StringContext p_context )
    {
        return p_context.getText();
    }
    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public Set<ILiteral> getInitialBeliefs()
    {
        return m_initialbeliefs;
    }

    @Override
    public ILiteral getInitialGoal()
    {
        return m_initialgoal;
    }

    @Override
    public Map<String, Set<IPlan>> getPlans()
    {
        return m_plans;
    }
}
