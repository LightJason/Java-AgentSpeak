/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.crypto;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.apache.commons.lang3.SerializationUtils;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.context.CExecutionIllegalStateException;
import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
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
import java.util.stream.Stream;


/**
 * encrypting algorithm.
 * Encrypts a set of datasets, which can be complex objects, the first argument of the action
 * is the encrypting key and all other arguments are datasets, the action returns all encypted
 * datasets and fails if one encryption fails
 *
 * {@code [Encypt1 | Encrypt2 | Encypt3] = .crypto/encrypt( Key, Dataset1, Dataset2, Dataset3 );}
 */
public final class CEncrypt extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 5791747611724910332L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CEncrypt.class, "crypto" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Nonnegative
    @Override
    public int minimalArgumentNumber()
    {
        return 2;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        final Key l_key = p_argument.get( 0 ).raw();
        final ECryptAlgorithm l_algorithm;
        try
        {
            l_algorithm = ECryptAlgorithm.of( l_key.getAlgorithm() );
        }
        catch ( final IllegalArgumentException l_exception )
        {
            throw new CExecutionIllegealArgumentException( p_context, l_exception );
        }

        CCommon.flatten( p_argument.stream().skip( 1 ) )
               .map( ITerm::<Serializable>raw )
               .forEach( i -> encrypt( l_algorithm, l_key, i, p_return, p_context ) );

        return Stream.of();
    }


    /**
     * encrypts a datatset
     *
     * @param p_algorithm algorithm
     * @param p_key key
     * @param p_dataset dataset
     * @param p_return return argument
     * @param p_context execution context
     */
    private static void encrypt( @Nonnull final ECryptAlgorithm p_algorithm, @Nonnull final Key p_key,
                                 @Nonnull final Serializable p_dataset, @Nonnull final List<ITerm> p_return,
                                 @NonNull final IContext p_context )
    {
        try
        {
            p_return.add(
                CRawTerm.of(
                    Base64.getEncoder().encodeToString(
                        p_algorithm.getEncryptCipher( p_key ).doFinal(
                            SerializationUtils.serialize( p_dataset )
                        )
                    )
                )
            );
        }
        catch ( final IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException l_exception )
        {
            throw new CExecutionIllegalStateException( p_context, l_exception );
        }
    }

}
