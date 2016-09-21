/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
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

import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;
import org.apache.commons.lang3.SerializationUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * dencrypting algorithm
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
        final Key l_key = p_argument.get( 0 ).toAny();
        final EAlgorithm l_algorithm = EAlgorithm.valueOf( l_key.getAlgorithm() );


        p_return.addAll(
            p_argument.subList( 1, p_argument.size() ).stream()
                      .map( i -> Base64.getDecoder().decode( i.<String>toAny() ) )
                      .map( i -> {
                          try
                          {
                              return l_algorithm.getDecryptCipher( l_key ).doFinal( i );
                          }
                          catch ( final NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException
                              | BadPaddingException | IllegalBlockSizeException l_exception )
                          {
                              return null;
                          }
                      } )
                      .filter( Objects::nonNull )
                      .map( i -> CRawTerm.from( SerializationUtils.deserialize( i ) ) )
                      .collect( Collectors.toList() )
        );

        return CFuzzyValue.from( true );
    }

}
