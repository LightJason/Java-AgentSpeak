/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.buildin.crypto;

import org.apache.commons.lang3.SerializationUtils;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;


/**
 * encrypting algorithm.
 * Encrypts a set of datasets, which can be complex objects, the first argument of the action
 * is the encrypting key and all other arguments are datasets, the action returns all encypted
 * datasets and fails if one encryption fails or on a wrong algorithm
 *
 * @code [Encypt1 | Encrypt2 | Encypt3] = crypto/encrypt( Key, Dataset1, Dataset2, Dataset3 ); @endcode
 */
public final class CEncrypt extends IBuildinAction
{

    @Override
    public final int minimalArgumentNumber()
    {
        return 2;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        final Key l_key = p_argument.get( 0 ).raw();
        final EAlgorithm l_algorithm;
        try
        {
            l_algorithm = EAlgorithm.from( l_key.getAlgorithm() );
        }
        catch ( final IllegalArgumentException l_exception )
        {
            return CFuzzyValue.from( false );
        }

        return CFuzzyValue.from(
                   CCommon.flatstream( p_argument.stream().skip( 1 ) )
                          .map( ITerm::<Serializable>raw )
                          .allMatch( i -> encrypt( l_algorithm, l_key, i, p_return ) )
        );
    }


    /**
     * encrypts a datatset
     *
     * @param p_algorithm algorithm
     * @param p_key key
     * @param p_dataset dataset
     * @param p_return return argument
     * @return successful execution
     */
    private static boolean encrypt( @Nonnull final EAlgorithm p_algorithm, @Nonnull final Key p_key,
                                    @Nonnull final Serializable p_dataset, @Nonnull final List<ITerm> p_return )
    {
        try
        {
            p_return.add(
                CRawTerm.from(
                    Base64.getEncoder().encodeToString(
                        p_algorithm.getEncryptCipher( p_key ).doFinal(
                            SerializationUtils.serialize( p_dataset )
                        )
                    )
                )
            );

            return true;
        }
        catch ( final IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException l_exception )
        {
            return false;
        }
    }




}
