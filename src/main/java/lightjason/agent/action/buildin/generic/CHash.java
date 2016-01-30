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

package lightjason.agent.action.buildin.generic;

import lightjason.agent.action.buildin.IBuildinAction;
import lightjason.language.CCommon;
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;


/**
 * hash algorithm
 */
public final class CHash extends IBuildinAction
{

    @Override
    public final int getMinimalArgumentNumber()
    {
        return 2;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final Boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        try
        {
            p_return.add( CRawTerm.from(
                    String.format( "%032x", new BigInteger(
                            1,
                            MessageDigest.getInstance( CCommon.getRawValue( p_argument.get( 0 ) ) )
                                         .digest( this.concat( p_argument.subList( 1, p_argument.size() ) ) )
                    ) )
            ) );

            return CBoolean.from( true );
        }
        catch ( final NoSuchAlgorithmException p_exception )
        {
            return CBoolean.from( false );
        }
    }

    /**
     * flats and concat the argument data
     *
     * @param p_input input arguments
     * @return byte sequence
     */
    private byte[] concat( final List<ITerm> p_input )
    {
        final StringBuilder l_result = new StringBuilder();
        ( CCommon.flatList( p_input ) ).stream().forEach( i -> l_result.append( CCommon.getRawValue( i ).toString() ) );
        return l_result.toString().getBytes();
    }
}
