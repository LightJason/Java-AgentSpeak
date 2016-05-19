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

package lightjason.agentspeak.agent.unify;

import com.codepoetics.protonpack.StreamUtils;
import lightjason.agentspeak.language.ITerm;
import lightjason.agentspeak.language.variable.CRelocateMutexVariable;
import lightjason.agentspeak.language.variable.CRelocateVariable;
import lightjason.agentspeak.language.variable.IVariable;

import java.util.Set;
import java.util.stream.Stream;


/**
 * unify on at hash-based quality
 */
public final class CHash implements IAlgorithm
{

    @Override
    @SuppressWarnings( "unchecked" )
    public final <T extends ITerm> boolean unify( final Set<IVariable<?>> p_variables, final Stream<T> p_source, final Stream<T> p_target )
    {
        return StreamUtils.zip(
            p_source,
            p_target,
            ( s, t ) -> {
                // if s and t are variable create a realocated variable for backtracking
                if ( ( t instanceof IVariable<?> ) && ( s instanceof IVariable<?> ) )
                {
                    p_variables.add(
                        ( (IVariable<?>) t ).hasMutex()
                        ? new CRelocateMutexVariable<>( ( (IVariable<?>) t ).getFQNFunctor(), (IVariable<?>) s )
                        : new CRelocateVariable<>( ( (IVariable<?>) t ).getFQNFunctor(), (IVariable<?>) s )
                    );
                    return true;
                }

                // if target type is a variable set the value
                if ( t instanceof IVariable<?> )
                {
                    p_variables.add( ( (IVariable<Object>) t ).set( s ) );
                    return true;
                }

                // otherwise check equality
                return s.equals( t );
            }
        ).allMatch( i -> i );
    }
}
