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


import lightjason.generic.ITerm;
import lightjason.generic.implementation.CVariable;

import java.util.HashSet;
import java.util.Set;


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
        //m_initialbeliefs.add(  );
        return super.visitBelief( p_context );
    }

    @Override
    public Object visitClause( final lightjason.JasonParser.ClauseContext p_context )
    {
        //new CClause( p_context.atom().getText(), p_context. != null )
        return super.visitClause( p_context );
    }

    @Override
    public Object visitTerm( final lightjason.JasonParser.TermContext p_context )
    {
        //if ((p_context.number() != null) && (p_context.number().floatnumber()) != null);

        Object x = super.visitTerm( p_context );
        if ( x != null )
            System.out.println( x + "    " + x.getClass() );

        return x;
    }

    @Override
    public Object visitVariable( final lightjason.JasonParser.VariableContext p_context )
    {
        return new CVariable<>( p_context.getText() );
    }

    @Override
    public Object visitFloatnumber( final lightjason.JasonParser.FloatnumberContext p_context )
    {
        switch ( p_context.getText() )
        {
            case "pi":
                return Math.PI;
            case "euler":
                return Math.E;
            case "lightspeed":
                return (double) ( 299792458 );
            case "avogadro":
                return 6.0221412927e23;
            case "boltzmann":
                return 8.617330350e-15;
            case "gravity":
                return 6.67408e-11;
            case "electron":
                return 9.10938356e-31;
            case "neutron":
                return 1674927471214e-27;
            case "proton":
                return 1.6726219e-27;

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

    public final Set<ITerm> getInitialBeliefs()
    {
        return m_initialbeliefs;
    }
}
