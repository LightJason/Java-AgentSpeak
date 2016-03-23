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
import lightjason.common.IPath;
import lightjason.error.CSyntaxErrorException;
import lightjason.language.ILiteral;
import lightjason.language.execution.IExecution;
import lightjason.language.execution.action.CProxyAction;
import lightjason.language.execution.action.CRawAction;
import lightjason.language.execution.expression.EOperator;
import lightjason.language.execution.expression.IExpression;
import lightjason.language.execution.expression.logical.CBinary;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * common parser components
 */
public final class CCommon
{

    /**
     * numeric constant values - infinity is defined manually
     */
    @SuppressWarnings( "serial" )
    public static final Map<String, Double> NUMERICCONSTANT = new HashMap<String, Double>()
    {{

        put( "pi", Math.PI );
        put( "euler", Math.E );
        put( "lightspeed", 299792458.0 );
        put( "avogadro", 6.0221412927e23 );
        put( "boltzmann", 8.617330350e-15 );
        put( "gravity", 6.67408e-11 );
        put( "electron", 9.10938356e-31 );
        put( "neutron", 1674927471214e-27 );
        put( "proton", 1.6726219e-27 );

    }};

    /**
     * ctor
     */
    private CCommon()
    {
    }


    /**
     * creates an executable structure of a parsed term item
     *
     * @param p_item any parsed item (term rule)
     * @param p_actions map with agent actions
     * @return execution structure or null
     */
    public static IExecution getTermExecution( final Object p_item, final Map<IPath, IAction> p_actions )
    {
        // null value will be passed
        if ( p_item == null )
            return null;

        // executable structures will be passed
        if ( p_item instanceof IExecution )
            return (IExecution) p_item;

        // literals are actions
        if ( p_item instanceof ILiteral )
            return new CProxyAction( p_actions, (ILiteral) p_item );

        // otherwise only simple types encapsulate
        return new CRawAction<>( p_item );
    }

    /**
     * creates a logical expression concationation with single operator
     *
     * @param p_operator operator
     * @param p_lefthandside left-hand-side expression
     * @param p_righthandside right-hand-side expressions
     * @return concat expression
     */
    public static IExpression createLogicalBinaryExpression( final EOperator p_operator, final IExpression p_lefthandside,
                                                             final Collection<IExpression> p_righthandside
    )
    {
        if ( ( !p_operator.isBinary() ) || ( !p_operator.isLogical() ) )
            throw new CSyntaxErrorException( lightjason.common.CCommon.getLanguageString( CCommon.class, "notbinarylogicoperator", p_operator ) );

        final List<IExpression> l_expression = new LinkedList<>();
        l_expression.add( p_lefthandside );
        l_expression.addAll( p_righthandside );

        // only left-hand-side is existing
        if ( l_expression.size() == 1 )
            return l_expression.get( 0 );

        // otherwise creare concat expression
        while ( l_expression.size() > 1 )
            l_expression.add( 0, new CBinary( p_operator, l_expression.remove( 0 ), l_expression.remove( 0 ) ) );

        return l_expression.get( 0 );
    }
}
