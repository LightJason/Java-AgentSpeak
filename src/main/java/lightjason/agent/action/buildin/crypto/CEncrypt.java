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

package lightjason.agent.action.buildin.crypto;

import lightjason.agent.action.buildin.IBuildinAction;
import lightjason.language.CCommon;
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CFuzzyValue;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import org.apache.commons.lang3.SerializationUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;


/**
 * encrypting algorithm
 */
public final class CEncrypt extends IBuildinAction
{

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
        final Key l_key = CCommon.<Key, ITerm>getRawValue( p_argument.get( 0 ) );
        final EAlgorithm l_algorithm = EAlgorithm.valueOf( l_key.getAlgorithm() );

        p_return.addAll(
                p_argument.subList( 1, p_argument.size() ).stream()
                          .map( i -> SerializationUtils.serialize( CCommon.getRawValue( i ) ) )
                          .map( i -> {
                              try
                              {
                                  return l_algorithm.getEncryptCipher( l_key ).doFinal( i );
                              }
                              catch ( final NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException
                                      | BadPaddingException | IllegalBlockSizeException l_exception )
                              {
                                  return null;
                              }
                          } )
                          .filter( i -> i != null )
                          .map( i -> CRawTerm.from( Base64.getEncoder().encodeToString( i ) ) )
                          .collect( Collectors.toList() )
        );

        return CFuzzyValue.from( true );
    }

}
