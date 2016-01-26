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

import cern.colt.matrix.DoubleMatrix1D;
import lightjason.agent.action.buildin.IBuildinAction;
import lightjason.language.CCommon;
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.util.List;
import java.util.stream.IntStream;


/**
 * assigns a value or matrix to all elements
 */
public final class CAssign extends IBuildinAction
{

    /**
     * ctor
     */
    public CAssign()
    {
        super( 4 );
    }

    @Override
    public final int getMinimalArgumentNumber()
    {
        return 2;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final Boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        // first argument must be a term with a matrix object, second assign value
        final DoubleMatrix1D l_vector = CCommon.<DoubleMatrix1D, ITerm>getRawValue( p_argument.get( 0 ) );
        final Object l_value = CCommon.getRawValue( p_argument.get( 1 ) );

        if ( l_value instanceof Double )
        {
            p_return.add( CRawTerm.from( l_vector.assign( (Double) l_value ) ) );
            return CBoolean.from( true );
        }

        if ( l_value instanceof DoubleMatrix1D )
        {
            p_return.add( CRawTerm.from( l_vector.assign( (DoubleMatrix1D) l_value ) ) );
            return CBoolean.from( true );
        }

        if ( l_value instanceof List<?> )
        {
            final List<Double> l_data = (List<Double>) l_value;
            IntStream.range( 0, Math.min( l_vector.size(), l_data.size() ) ).boxed().forEach( i -> l_vector.setQuick( i, l_data.get( i ) ) );
            return CBoolean.from( true );
        }

        return CBoolean.from( false );
    }
}
