/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.buildin.math.blas.vector;

import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.SparseDoubleMatrix1D;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.action.buildin.math.blas.EType;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * creates a dense- or sparse-vector from as string.
 * The action creates for each input argument a vector
 * by parsing the string, the last string can be "dense | sparse"
 * to defining a sparse / dense vector, the action never fails
 *
 * @code [V1|V2] = math/blas/vector/parse( "1,2,3", "7,8,9,10,12", "dense|dense" );
 * @note seperator is comma, semicolon or space
 */
public final class CParse extends IBuildinAction
{
    /**
     * ctor
     */
    public CParse()
    {
        super( 4 );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return
    )
    {
        final List<ITerm> l_arguments = CCommon.flatcollection( p_argument ).collect( Collectors.toList() );
        final int l_limit;
        final EType l_type;
        if ( ( CCommon.rawvalueAssignableTo( l_arguments.get( l_arguments.size() - 1 ), String.class ) )
             && ( EType.exists( l_arguments.get( l_arguments.size() - 1 ).<String>raw() ) ) )
        {
            l_type = EType.from( l_arguments.get( l_arguments.size() - 1 ).<String>raw() );
            l_limit = l_arguments.size() - 1;
        }
        else
        {
            l_type = EType.DENSE;
            l_limit = l_arguments.size();
        }


        // create vectors
        switch ( l_type )
        {
            case DENSE:
                l_arguments.stream()
                           .limit( l_limit )
                           .map( ITerm::<String>raw )
                           .map( CParse::parse )
                           .map( DenseDoubleMatrix1D::new )
                           .map( CRawTerm::from )
                           .forEach( p_return::add );

                return CFuzzyValue.from( true );

            case SPARSE:
                l_arguments.stream()
                           .limit( l_limit )
                           .map( ITerm::<String>raw )
                           .map( CParse::parse )
                           .map( SparseDoubleMatrix1D::new )
                           .map( CRawTerm::from )
                           .forEach( p_return::add );

                return CFuzzyValue.from( true );

            default:
        }

        return CFuzzyValue.from( false );
    }

    /**
     * parses the string
     *
     * @param p_string string
     * @return double array
     */
    private static double[] parse( final String p_string )
    {
        return Arrays.stream( p_string.split( ";|,|\\s" ) )
                     .map( String::trim )
                     .filter( i -> !i.isEmpty() )
                     .mapToDouble( Double::parseDouble )
                     .toArray();
    }

}
