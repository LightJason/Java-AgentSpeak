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
import lightjason.language.ITerm;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 *
 */
public class CAgentParseVisitor extends lightjason.JasonBaseVisitor<Object>
{
    /**
     * set with initial beliefs
     */
    private Set<ITerm> m_initialbeliefs = new HashSet<>();

    @Override
    public Object visitBelief( final lightjason.JasonParser.BeliefContext p_context )
    {
        Object x = new CLiteral( (CLiteral) this.visitLiteral( p_context.literal() ), p_context.STRONGNEGATION() != null );
        System.out.println( x );
        return x;
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
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
        return p_context.term().parallelStream().map( i -> super.visitTerm( i ) ).collect( Collectors.toCollection( () -> new LinkedList<>() ) );
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

    public final Set<ITerm> getInitialBeliefs()
    {
        return m_initialbeliefs;
    }
}
