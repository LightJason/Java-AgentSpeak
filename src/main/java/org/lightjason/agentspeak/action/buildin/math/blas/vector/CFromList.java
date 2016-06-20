/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L)                                  #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp@lightjason.org)                      #
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

import cern.colt.matrix.DoubleMatrix1D;
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
import java.util.stream.IntStream;


/**
 * creates a dense- or sparse-vector from a list
 */
public final class CFromList extends IBuildinAction
{
    /**
     * ctor
     */
    public CFromList()
    {
        super( 4 );
    }

    @Override
    public final int getMinimalArgumentNumber()
    {
        return 1;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        // first argument is the list type
        // optional second argument is matrix type (default dense-matrix)
        final List<Double> l_data = CCommon.raw( p_argument.get( 0 ) );
        switch ( p_argument.size() > 1 ? EType.valueOf( CCommon.raw( p_argument.get( 1 ) ) ) : EType.DENSE )
        {
            case DENSE:
                p_return.add( assign( l_data, new DenseDoubleMatrix1D( l_data.size() ) ) );
                break;

            case SPARSE:
                p_return.add( assign( l_data, new SparseDoubleMatrix1D( l_data.size() ) ) );
                break;

            default:
        }
        return CFuzzyValue.from( true );
    }

    /**
     * assigns the list values to the vector
     *
     * @param p_data list
     * @param p_vector vector
     * @return term
     */
    private static ITerm assign( final List<Double> p_data, final DoubleMatrix1D p_vector )
    {
        IntStream.range( 0, p_data.size() ).boxed().forEach( i -> p_vector.setQuick( i, p_data.get( i ) ) );
        return CRawTerm.from( p_vector );
    }

}
