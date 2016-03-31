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

package lightjason.agent.action.buildin.math.blas.matrix;

import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import lightjason.agent.action.buildin.IBuildinAction;
import lightjason.agent.action.buildin.math.blas.EType;
import lightjason.language.CCommon;
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.util.List;


/**
 * creates a dense- or sparse-matrix
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
    public final int getMinimalArgumentNumber()
    {
        return 2;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        // first argument is row-size, second colum-size
        // optional third argument is matrix type (default dense-matrix)
        switch ( p_argument.size() > 2 ? EType.valueOf( CCommon.getRawValue( p_argument.get( 3 ) ) ) : EType.DENSE )
        {
            case DENSE:
                p_return.add(
                        CRawTerm.from( new DenseDoubleMatrix2D(
                                CCommon.<Number, ITerm>getRawValue( p_argument.get( 0 ) ).intValue(),
                                CCommon.<Number, ITerm>getRawValue( p_argument.get( 1 ) ).intValue()
                        ) )
                );
                break;

            case SPARSE:
                p_return.add(
                        CRawTerm.from( new SparseDoubleMatrix2D(
                                CCommon.<Number, ITerm>getRawValue( p_argument.get( 0 ) ).intValue(),
                                CCommon.<Number, ITerm>getRawValue( p_argument.get( 1 ) ).intValue()
                        ) )
                );
                break;

            default:
        }
        return CBoolean.from( true );
    }

}
