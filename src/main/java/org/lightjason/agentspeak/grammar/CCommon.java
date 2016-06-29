/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L)                                  #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
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

import com.codepoetics.protonpack.StreamUtils;
import org.lightjason.agentspeak.error.CSyntaxErrorException;
import org.lightjason.agentspeak.language.execution.expression.EOperator;
import org.lightjason.agentspeak.language.execution.expression.IExpression;
import org.lightjason.agentspeak.language.execution.expression.logical.CBinary;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * common parser components
 */
public final class CCommon
{

    /**
     * numeric constant values - infinity is defined manually
     */
    static final Map<String, Double> NUMERICCONSTANT = Collections.unmodifiableMap(
        StreamUtils.zip(
            Stream.of(
                "pi",
                "euler",
                "lightspeed",
                "avogadro",
                "boltzmann",
                "gravity",
                "electron",
                "neutron",
                "proton"
            ),

            Stream.of(
                Math.PI,
                Math.E,
                299792458.0,
                6.0221412927e23,
                8.617330350e-15,
                6.67408e-11,
                9.10938356e-31,
                1674927471214e-27,
                1.6726219e-27
            ),

            AbstractMap.SimpleImmutableEntry::new
        ).collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue ) ) );


    /**
     * ctor
     */
    private CCommon()
    {
    }

    /**
     * creates a logical expression concationation with single operator
     *
     * @param p_operator operator
     * @param p_lefthandside left-hand-side expression
     * @param p_righthandside right-hand-side expressions
     * @return concat expression
     */
    static IExpression createLogicalBinaryExpression( final EOperator p_operator, final IExpression p_lefthandside,
                                                      final Collection<IExpression> p_righthandside
    )
    {
        if ( ( !p_operator.isBinary() ) || ( !p_operator.isLogical() ) )
            throw new CSyntaxErrorException( org.lightjason.agentspeak.common.CCommon.languagestring( CCommon.class, "notbinarylogicoperator", p_operator ) );

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
