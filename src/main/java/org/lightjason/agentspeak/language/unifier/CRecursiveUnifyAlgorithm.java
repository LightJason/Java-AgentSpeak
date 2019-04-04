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
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.IRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * recursive unifier
 */
public final class CRecursiveUnifyAlgorithm extends IBaseUnifyAlgorithm
{

    @Override
    public <T extends ITerm> boolean unify( @Nonnull final Set<IVariable<?>> p_variables, final Stream<T> p_source, final Stream<T> p_target )
    {
        final List<T> l_target = p_target.collect( Collectors.toList() );
        final List<T> l_source = p_source.collect( Collectors.toList() );

        if ( l_target.isEmpty() || l_source.isEmpty() )
            return true;

        if ( l_target.size() != l_source.size() )
            return false;

        return StreamUtils.zip(
            l_source.stream(),
            l_target.stream(),
            ( s, t ) ->
            {
                if ( Stream.of( bothvariables( p_variables, s, t ), variables( p_variables, s, t ) ).filter( i -> i ).findFirst().orElse( false ) )
                    return true;

                // if both raw values -> equality check
                if ( s instanceof IRawTerm<?> || t instanceof IRawTerm<?> )
                    return s.equals( t );

                return this.recursion( p_variables, s, t );
            }
        ).allMatch( i -> i );
    }

    /**
     * recursive descent on literal
     *
     * @param p_variables variables
     * @param p_source source term
     * @param p_target target term
     * @tparam T term type
     * @return unification literal
     */
    private <T extends ITerm> boolean recursion( @Nonnull final Set<IVariable<?>> p_variables, @Nonnull final T p_source, @Nonnull final T p_target )
    {
        if ( !( p_source instanceof ILiteral && p_target instanceof ILiteral ) )
            return false;

        final ILiteral l_sourceliteral = (ILiteral) p_source;
        final ILiteral l_targetliteral = (ILiteral) p_target;

        if ( !l_sourceliteral.fqnfunctor().equals( l_targetliteral.fqnfunctor() ) )
            return false;

        return this.unify( p_variables, l_sourceliteral.orderedvalues(), l_targetliteral.orderedvalues() );
    }

}
