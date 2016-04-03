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

package lightjason.agent.action.buildin.math.interpolate;

import lightjason.agent.action.buildin.IBuildinAction;
import lightjason.language.CCommon;
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CFuzzyValue;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import org.apache.commons.math3.analysis.UnivariateFunction;

import java.util.List;
import java.util.stream.Collectors;


/**
 * action to create interpolated values
 */
public final class CInterpolate extends IBuildinAction
{

    /**
     * ctor
     */
    public CInterpolate()
    {
        super( 3 );
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
        final UnivariateFunction l_function = CCommon.<UnivariateFunction, ITerm>getRawValue( p_argument.get( 0 ) );

        p_return.addAll(
                CCommon.flatList( p_argument.subList( 1, p_argument.size() ) ).stream()
                       .mapToDouble(
                               i -> CCommon.<Number, ITerm>getRawValue( i ).doubleValue()
                       )
                       .mapToObj( i -> CRawTerm.from( l_function.value( i ) ) )
                       .collect( Collectors.toList() )
        );

        return CFuzzyValue.from( true );
    }

}
