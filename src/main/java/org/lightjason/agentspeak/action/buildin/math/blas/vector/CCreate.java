/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
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

import java.util.List;
import java.util.stream.Collectors;


/**
 * creates a dense- or sparse-vector.
 * The action creates a vector, the first \f$ n-1 \f$ arguments are
 * the size of the vector, the last argument defines as string a
 * dense or sparse vector (default is dense) and the action never fails
 *
 * @code [A|B|C] = math/blas/vector/create( 3, 2, 1, "dense | sparse"); @endcode
 */
public final class CCreate extends IBuildinAction
{
    /**
     * ctor
     */
    public CCreate()
    {
        super( 4 );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
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
                           .map( ITerm::<Number>raw )
                           .mapToInt( Number::intValue )
                           .boxed()
                           .map( DenseDoubleMatrix1D::new )
                           .map( CRawTerm::from )
                           .forEach( p_return::add );

                return CFuzzyValue.from( true );


            case SPARSE:
                l_arguments.stream()
                           .limit( l_limit )
                           .map( ITerm::<Number>raw )
                           .mapToInt( Number::intValue )
                           .boxed()
                           .map( SparseDoubleMatrix1D::new )
                           .map( CRawTerm::from )
                           .forEach( p_return::add );

                return CFuzzyValue.from( true );


            default:
                return CFuzzyValue.from( false );
        }
    }

}
