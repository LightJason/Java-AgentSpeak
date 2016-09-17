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

package org.lightjason.agentspeak.agent.unify;

import com.codepoetics.protonpack.StreamUtils;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.IRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.variable.CRelocateMutexVariable;
import org.lightjason.agentspeak.language.variable.CRelocateVariable;
import org.lightjason.agentspeak.language.variable.IVariable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * recursive unify
 */
public final class CRecursive implements IAlgorithm
{

    @Override
    @SuppressWarnings( "unchecked" )
    public final <T extends ITerm> boolean unify( final Set<IVariable<?>> p_variables, final Stream<T> p_source, final Stream<T> p_target )
    {
        final List<T> l_target = p_target.collect( Collectors.toList() );
        final List<T> l_source = p_source.collect( Collectors.toList() );

        if ( ( l_target.isEmpty() ) || ( l_source.isEmpty() ) )
            return true;

        if ( l_target.size() != l_source.size() )
            return false;

        return StreamUtils.zip(
            l_source.stream(),
            l_target.stream(),
            ( s, t ) -> {

                // if s and t are variable create a realocated variable for backtracking
                if ( ( t instanceof IVariable<?> ) && ( s instanceof IVariable<?> ) )
                {
                    p_variables.add(
                        ( (IVariable<?>) t ).mutex()
                        ? new CRelocateMutexVariable<>( ( (IVariable<?>) t ).fqnfunctor(), (IVariable<?>) s )
                        : new CRelocateVariable<>( ( (IVariable<?>) t ).fqnfunctor(), (IVariable<?>) s )
                    );
                    return true;
                }

                // if target type is a variable set the value
                if ( t instanceof IVariable<?> )
                {
                    p_variables.add( ( (IVariable<Object>) t ).set( s ) );
                    return true;
                }

                // if both raw values -> equality check
                if ( ( s instanceof IRawTerm<?> ) || ( t instanceof IRawTerm<?> ) )
                    return s.equals( t );

                // if a literal exists -> source and target literal must be equal with the functor -> recursive descent
                if ( ( s instanceof ILiteral ) && ( t instanceof ILiteral ) )
                {
                    final ILiteral l_sourceliteral = (ILiteral) s;
                    final ILiteral l_targetliteral = (ILiteral) t;

                    if ( !l_sourceliteral.fqnfunctor().equals( l_targetliteral.fqnfunctor() ) )
                        return false;

                    return this.unify( p_variables, l_sourceliteral.orderedvalues(), l_targetliteral.orderedvalues() );
                }

                // otherwise false
                return false;
            }
        ).allMatch( i -> i );
    }

}
