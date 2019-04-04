/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
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

package org.lightjason.agentspeak.language.unifier;

import com.codepoetics.protonpack.StreamUtils;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.stream.Stream;


/**
 * unifier on at hash-based quality
 */
public final class CHashUnifyAlgorithm extends IBaseUnifyAlgorithm
{

    @Override
    public <T extends ITerm> boolean unify( @Nonnull final Set<IVariable<?>> p_variables, @Nonnull final Stream<T> p_source, @Nonnull final Stream<T> p_target )
    {
        return StreamUtils.zip(
            p_source,
            p_target,
            ( s, t ) -> bothvariables( p_variables, s, t ) || ( variables( p_variables, s, t ) || s.equals( t ) )
        ).allMatch( i -> i );
    }
}
