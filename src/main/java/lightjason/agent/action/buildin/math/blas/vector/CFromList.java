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

package lightjason.agent.action.buildin.math.blas.vector;

import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.SparseDoubleMatrix1D;
import lightjason.agent.action.buildin.IBuildinAction;
import lightjason.agent.action.buildin.math.blas.EType;
import lightjason.language.CCommon;
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

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
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final Boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        // first argument is the list type
        // optional second argument is matrix type (default dense-matrix)
        final List<Double> l_data = CCommon.getRawValue( p_argument.get( 0 ) );
        switch ( p_argument.size() > 1 ? EType.valueOf( CCommon.getRawValue( p_argument.get( 1 ) ) ) : EType.DENSE )
        {
            case DENSE:
                final DenseDoubleMatrix1D l_densevector = new DenseDoubleMatrix1D( l_data.size() );
                IntStream.range( 0, l_data.size() ).boxed().forEach( i -> l_densevector.setQuick( i, l_data.get( i ) ) );
                p_return.add( new CRawTerm<>( l_densevector ) );
                break;

            case SPARSE:
                final SparseDoubleMatrix1D l_sparsevector = new SparseDoubleMatrix1D( l_data.size() );
                IntStream.range( 0, l_data.size() ).boxed().forEach( i -> l_sparsevector.setQuick( i, l_data.get( i ) ) );
                p_return.add( new CRawTerm<>( l_sparsevector ) );
                break;

            default:
        }
        return CBoolean.from( true );
    }

}
