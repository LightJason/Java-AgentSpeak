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
import lightjason.error.CIllegalStateException;
import lightjason.language.CCommon;
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CFuzzyValue;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.AkimaSplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.DividedDifferenceInterpolator;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.interpolation.LoessInterpolator;
import org.apache.commons.math3.analysis.interpolation.NevilleInterpolator;

import java.util.List;


/**
 * action to create a spline interpolation
 */
public final class CCreate extends IBuildinAction
{

    /**
     * ctor
     */
    public CCreate()
    {
        super( 3 );
    }

    @Override
    public final int getMinimalArgumentNumber()
    {
        return 3;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        p_return.add( CRawTerm.from(
                EType.valueOf( CCommon.<String, ITerm>getRawValue( p_argument.get( 0 ) ).trim().toUpperCase() ).get(
                        CCommon.flatList( CCommon.getRawValue( p_argument.get( 1 ) ) ).stream()
                               .mapToDouble( i -> CCommon.<Number, ITerm>getRawValue( i ).doubleValue() ).toArray(),
                        CCommon.flatList( CCommon.getRawValue( p_argument.get( 2 ) ) ).stream()
                               .mapToDouble( i -> CCommon.<Number, ITerm>getRawValue( i ).doubleValue() ).toArray()
                )
        ) );

        return CFuzzyValue.from( true );
    }


    /**
     * enum type of interpolating definitions
     */
    private enum EType
    {
        AKIMA,
        DIVIDEDDIFFERENCE,
        LINEAR,
        LOESS,
        NEVILLE;


        /**
         * returns the interpolate objext
         *
         * @param p_x x-values
         * @param p_y y-values
         * @return interpolate object
         */
        public final UnivariateFunction get( final double[] p_x, final double[] p_y )
        {
            switch ( this )
            {
                case AKIMA:
                    return new AkimaSplineInterpolator().interpolate( p_x, p_y );

                case DIVIDEDDIFFERENCE:
                    return new DividedDifferenceInterpolator().interpolate( p_x, p_y );

                case LINEAR:
                    return new LinearInterpolator().interpolate( p_x, p_y );

                case LOESS:
                    return new LoessInterpolator().interpolate( p_x, p_y );

                case NEVILLE:
                    return new NevilleInterpolator().interpolate( p_x, p_y );

                default:
                    throw new CIllegalStateException( lightjason.common.CCommon.getLanguageString( this, "unknown", this ) );
            }
        }
    }

}
