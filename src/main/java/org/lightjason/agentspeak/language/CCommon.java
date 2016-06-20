/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L)                                  #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp@lightjason.org)                      #
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

package org.lightjason.agentspeak.language;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.language.execution.CContext;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.instantiable.IInstantiable;
import org.lightjason.agentspeak.language.variable.CConstant;
import org.lightjason.agentspeak.language.variable.IVariable;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * common structure for execution definition
 */
public final class CCommon
{
    /**
     * private ctor - avoid instantiation
     */
    private CCommon()
    {
    }

    /**
     * updates within an instance context all variables with the variable
     *
     * @param p_context context
     * @param p_unifiedvariables unified variables as stream
     * @return context reference
     */
    public static IContext updatecontext( final IContext p_context, final Stream<IVariable<?>> p_unifiedvariables )
    {
        p_unifiedvariables.forEach( i -> p_context.instancevariables().get( i.fqnfunctor() ).set( i.typed() ) );
        return p_context;
    }

    /**
     * creates the instantiate execution context
     *
     * @param p_instance instance object
     * @param p_agent agent
     * @param p_variable variable stream
     * @return context object
     */
    public static IContext instantiate( final IInstantiable p_instance, final IAgent<?> p_agent, final Stream<IVariable<?>> p_variable )
    {
        final Set<IVariable<?>> l_variables = p_instance.variables().parallel().map( i -> i.shallowcopy() ).collect( Collectors.toSet() );
        Stream.of(
            p_variable,
            p_agent.variablebuilder().generate( p_agent, p_instance ),
            Stream.of( new CConstant<>( "Score", p_instance.score( p_agent ) ) ),
            Stream.of( new CConstant<>( "Cycle", p_agent.cycle() ) )
        ).reduce( Stream::concat )
              .orElseGet( Stream::<IVariable<?>>empty )
              .forEach( i -> {
                  l_variables.remove( i );
                  l_variables.add( i );
              } );

        return new CContext( p_agent, p_instance, Collections.unmodifiableSet( l_variables ) );
    }

    /**
     * consts the variables within a literal
     *
     * @param p_literal literal
     * @return map with frequency
     */
    public static Map<IVariable<?>, Integer> getVariableFrequency( final ILiteral p_literal )
    {
        return Collections.unmodifiableMap(
            Stream.concat(
                recursiveterm( p_literal.orderedvalues() ),
                recursiveliteral( p_literal.annotations() )
            )
                  .filter( i -> i instanceof IVariable<?> )
                  .map( i -> (IVariable<?>) i )
                  .collect( Collectors.toMap( i -> i, i -> 1, Integer::sum ) )
        );
    }

    /**
     * returns a native / raw value of a term
     *
     * @param p_value any value type
     * @return term value or raw value
     */
    @SuppressWarnings( "unchecked" )
    public static <T, N> T getRawValue( final N p_value )
    {
        if ( p_value instanceof IVariable<?> )
            return ( (IVariable<?>) p_value ).typed();
        if ( p_value instanceof IRawTerm<?> )
            return ( (IRawTerm<?>) p_value ).typed();

        return (T) p_value;
    }

    /**
     * checks a term value for assignable class
     *
     * @param p_value any value type
     * @param p_class assignable class
     * @return term value or raw value
     */
    @SuppressWarnings( "unchecked" )
    public static <T> boolean isRawValueAssignableTo( final T p_value, final Class<?>... p_class )
    {
        if ( p_value instanceof IVariable<?> )
            return ( (IVariable<?>) p_value ).valueAssignableTo( p_class );
        if ( p_value instanceof IRawTerm<?> )
            return ( (IRawTerm<?>) p_value ).valueAssignableTo( p_class );

        return Arrays.asList( p_class ).stream().map( i -> i.isAssignableFrom( p_value.getClass() ) ).anyMatch( i -> i );
    }


    /**
     * replace variables with context variables
     *
     * @param p_context execution context
     * @param p_terms replacing term list
     * @return result term list
     */
    public static List<ITerm> replaceFromContext( final IContext p_context, final Collection<? extends ITerm> p_terms )
    {
        return p_terms.stream().map( i -> replaceFromContext( p_context, i ) ).collect( Collectors.toList() );
    }

    /**
     * replace variable with context variable
     * other values will be passed without context access
     *
     * @param p_context execution context
     * @param p_term term
     * @return replaces variable object
     */
    public static ITerm replaceFromContext( final IContext p_context, final ITerm p_term )
    {
        if ( !( p_term instanceof IVariable<?> ) )
            return p_term;

        final IVariable<?> l_variable = p_context.instancevariables().get( p_term.fqnfunctor() );
        if ( l_variable != null )
            return l_variable;

        throw new CIllegalArgumentException(
            org.lightjason.agentspeak.common.CCommon.getLanguageString( CCommon.class, "variablenotfoundincontext", p_term.fqnfunctor() )
        );
    }


    /**
     * flat term map into flat term list
     *
     * @param p_terms term collection
     * @return flat term map
     */
    public static List<ITerm> flatList( final List<? extends ITerm> p_terms )
    {
        return flattenToStream( p_terms ).collect( Collectors.toList() );
    }

    /**
     * flats and concat the term list
     *
     * @param p_input input term list
     * @return byte sequence with UTF-8 encoding
     *
     * @throws UnsupportedEncodingException is thrown on wrong encoding type
     */
    public static byte[] getBytes( final List<ITerm> p_input ) throws UnsupportedEncodingException
    {
        final StringBuilder l_result = new StringBuilder();
        ( flatList( p_input ) ).stream().forEach( i -> l_result.append( getRawValue( i ).toString() ) );
        return l_result.toString().getBytes( "UTF-8" );
    }

    /**
     * recursive stream of term values
     *
     * @param p_input term stream
     * @return term stream
     */
    @SuppressWarnings( "unchecked" )
    public static Stream<ITerm> recursiveterm( final Stream<ITerm> p_input )
    {
        return p_input.flatMap( i -> i instanceof ILiteral ? recursiveterm( ( (ILiteral) i ).orderedvalues() ) : Stream.of( i ) );
    }

    /**
     * recursive stream of literal values
     *
     * @param p_input term stream
     * @return term stream
     *
     * @note annotations cannot use annotation within
     */
    public static Stream<ITerm> recursiveliteral( final Stream<ILiteral> p_input )
    {
        return p_input.flatMap( i -> recursiveterm( i.orderedvalues() ) );
    }

    /**
     * returns the hasing function for term data
     *
     * @return hasher
     */
    public static Hasher getTermHashing()
    {
        return Hashing.murmur3_32().newHasher();
    }

    /*
     * recursive flattering of a list structure
     *
     * @param p_list any collection type
     * @return term stream
     */
    @SuppressWarnings( "unchecked" )
    private static Stream<ITerm> flattenToStream( final Collection<?> p_list )
    {
        return p_list.stream().flatMap( i -> {
            final Object l_value = getRawValue( i );
            return l_value instanceof Collection<?>
                   ? flattenToStream( (List<?>) l_value )
                   : Stream.of( CRawTerm.from( l_value ) );
        } );
    }
}
