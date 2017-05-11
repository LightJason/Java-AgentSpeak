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
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;


/**
 * dencrypting algorithm for decrypting data.
 * The actions decrypts data by the key, that is set on the first argument,
 * all other arguments are datasets for encrypting, the actions returns all
 * drcrypted datasets back and fails if a dataset cannot be decrypted
 *
 * @code [DecyptData1 | DecyptData2 | DecyptData3] = crypto/decrypt( Key, Dataset1, Dataset2, Dataset3 ); @endcode
 */
public final class CDecrypt extends IBuildinAction
{

    @Override
    public final int minimalArgumentNumber()
    {
        return 2;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
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
                          .map( ITerm::<String>raw )
                          .allMatch( i -> decrypt( l_algorithm, l_key, i, p_return ) )
        );
    }

    /**
     * decrypt
     *
     * @param p_algorithm algorithm
     * @param p_key key
     * @param p_dataset base64 encoded dataset
     * @param p_return return argument
     * @return successful execution
     */
    private static boolean decrypt( final EAlgorithm p_algorithm, final Key p_key, final String p_dataset, final List<ITerm> p_return )
    {
        try
        {
            p_return.add(
                CRawTerm.from(
                    SerializationUtils.deserialize(
                        p_algorithm.getDecryptCipher( p_key ).doFinal(
                            Base64.getDecoder().decode( p_dataset )
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
