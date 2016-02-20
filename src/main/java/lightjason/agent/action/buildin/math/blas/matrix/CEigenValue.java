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

import cern.colt.matrix.linalg.EigenvalueDecomposition;
import lightjason.agent.action.buildin.IBuildinAction;
import lightjason.language.CCommon;
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * creates the eigenvalues of a matrix
 */
public final class CEigenValue extends IBuildinAction
{
    /**
     * ctor
     */
    public CEigenValue()
    {
        super( 4 );
    }

    @Override
    public final int getMinimalArgumentNumber()
    {
        return 1;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        // argument must be a term with a matrix object
        p_return.add( CRawTerm.from(
                p_parallel
                ? Collections.synchronizedList(
                        Arrays.stream(
                                new EigenvalueDecomposition( CCommon.getRawValue( p_argument.get( 0 ) ) ).getRealEigenvalues().toArray()
                        ).boxed().sorted().collect( Collectors.toList() )
                )
                : Arrays.stream(
                        new EigenvalueDecomposition( CCommon.getRawValue( p_argument.get( 0 ) ) ).getRealEigenvalues().toArray()
                ).boxed().sorted().collect( Collectors.toList() )
        ) );

        return CBoolean.from( true );
    }
}
