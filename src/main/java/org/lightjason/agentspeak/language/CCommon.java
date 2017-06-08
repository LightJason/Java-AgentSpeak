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

package org.lightjason.agentspeak.language;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.error.CIllegalStateException;
import org.lightjason.agentspeak.language.execution.CContext;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.action.unify.IUnifier;
import org.lightjason.agentspeak.language.instantiable.IInstantiable;
import org.lightjason.agentspeak.language.instantiable.plan.statistic.IPlanStatistic;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.variable.CConstant;
import org.lightjason.agentspeak.language.variable.IVariable;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

    //--- plan / rule instantiation ----------------------------------------------------------------------------------------------------------------------------

    /**
     * updates within an instance context all variables of the stream
     *
     * @param p_context context
     * @param p_unifiedvariables unified variables as stream
     * @return context reference
     */
    public static IContext updatecontext( final IContext p_context, final Stream<IVariable<?>> p_unifiedvariables )
    {
        p_unifiedvariables.parallel().forEach( i -> p_context.instancevariables().get( i.fqnfunctor() ).set( i.raw() ) );
        return p_context;
    }

    /**
     * creates the instantiate execution context with default variables
     *
     * @param p_instance instance object
     * @param p_agent agent
     * @param p_variable variable stream
     * @return context object
     */
    public static IContext instantiate( final IInstantiable p_instance, final IAgent<?> p_agent, final Stream<IVariable<?>> p_variable )
    {
        final Set<IVariable<?>> l_variables = p_instance.variables().parallel().map( i -> i.shallowcopy() ).collect( Collectors.toSet() );
        CCommon.streamconcat(
            p_variable,
            p_agent.variablebuilder().apply( p_agent, p_instance ),
            Stream.of( new CConstant<>( "Cycle", p_agent.cycle() ) )
        )
               .peek( l_variables::remove )
               .forEach( l_variables::add );

        return new CContext( p_agent, p_instance, Collections.unmodifiableSet( l_variables ) );
    }


    /**
     * unifies trigger and creates the set of variables
     *
     * @note target trigger literal must be cloned to avoid variable overwriting
     * @param p_unifier unifier
     * @param p_source input trigger (with values)
     * @param p_target trigger (of a plan / rule)
     * @return pair of valid unification and unified variables
     */
    public static Pair<Boolean, Set<IVariable<?>>> unifytrigger( final IUnifier p_unifier, final ITrigger p_source, final ITrigger p_target )
    {
        // filter for avoid duplicated instantiation on non-existing values
        if ( !( p_source.literal().emptyValues() == p_target.literal().emptyValues() ) )
            return new ImmutablePair<>( false, Collections.emptySet() );

        // unify variables, source trigger literal must be copied
        final Set<IVariable<?>> l_variables = p_unifier.unify( p_source.literal(), p_target.literal().deepcopy().<ILiteral>raw() );

        // check for completely unification (of all variables)
        return l_variables.size() == CCommon.variablefrequency( p_target.literal() ).size()
               ? new ImmutablePair<>( true, l_variables )
               : new ImmutablePair<>( false, Collections.emptySet() );
    }

    /**
     * instantiate a plan with context and plan-specific variables
     *
     * @param p_planstatistic plan statistic for instatiation
     * @param p_agent agent
     * @param p_variables instantiated variables
     * @return pair of planstatistic and context
     */
    public static Pair<IPlanStatistic, IContext> instantiateplan( final IPlanStatistic p_planstatistic, final IAgent<?> p_agent, final Set<IVariable<?>> p_variables )
    {
        return new ImmutablePair<>(
            p_planstatistic,
            p_planstatistic.plan().instantiate(
                p_agent,
                Stream.concat( p_variables.stream(), p_planstatistic.variables() )
            )
        );
    }

    // --- variable / term helpers -----------------------------------------------------------------------------------------------------------------------------

    /**
     * concat multiple streams
     *
     * @param p_streams streams
     * @tparam T any value type
     * @return concated stream
     */
    @SafeVarargs
    @SuppressWarnings( "varargs" )
    public static <T> Stream<T> streamconcat( final Stream<T>... p_streams )
    {
        return Arrays.stream( p_streams ).reduce( Stream::concat ).orElseGet( Stream::empty );
    }

    /**
     * consts the variables within a literal
     *
     * @param p_literal literal
     * @return map with frequency
     */
    public static Map<IVariable<?>, Integer> variablefrequency( final ILiteral p_literal )
    {
        return Collections.unmodifiableMap(
            recursiveterm( p_literal.orderedvalues() )
                  .filter( i -> i instanceof IVariable<?> )
                  .map( i -> (IVariable<?>) i )
                  .collect( Collectors.toMap( i -> i, i -> 1, Integer::sum ) )
        );
    }

    /**
     * checks a term value for assignable class
     *
     * @param p_value any value type
     * @param p_class assignable class
     * @return term value or raw value
     */
    @SuppressWarnings( "unchecked" )
    public static <T> boolean rawvalueAssignableTo( final T p_value, final Class<?>... p_class )
    {
        if ( p_value instanceof IVariable<?> )
            return ( (IVariable<?>) p_value ).valueAssignableTo( p_class );
        if ( p_value instanceof IRawTerm<?> )
            return ( (IRawTerm<?>) p_value ).valueAssignableTo( p_class );

        return Arrays.stream( p_class ).anyMatch( i -> i.isAssignableFrom( p_value.getClass() ) );
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
            org.lightjason.agentspeak.common.CCommon.languagestring( CCommon.class, "variablenotfoundincontext", p_term.fqnfunctor() )
        );
    }


    /**
     * flat term-in-term collection into
     * a straight term list
     *
     * @param p_terms term collection
     * @return flat term stream
     */
    public static Stream<ITerm> flatcollection( final Collection<? extends ITerm> p_terms )
    {
        return flattenToStream( p_terms.stream() );
    }

    /**
     * flat term-in-term stream into
     * a straight term list
     *
     * @param p_terms term stream
     * @return flat term stream
     */
    public static Stream<ITerm> flatstream( final Stream<? extends ITerm> p_terms )
    {
        return flattenToStream( p_terms );
    }

    /**
     * recursive stream of term values
     *
     * @param p_input term stream
     * @return term stream
     */
    public static Stream<ITerm> recursiveterm( final Stream<ITerm> p_input )
    {
        return p_input.flatMap( i -> i instanceof ILiteral ? recursiveterm( ( i.<ILiteral>raw() ).orderedvalues() ) : Stream.of( i ) );
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

    /*
     * recursive flattering of a stream structure
     *
     * @param p_list any stream
     * @return term stream
     */
    @SuppressWarnings( "unchecked" )
    private static Stream<ITerm> flattenToStream( final Stream<?> p_stream )
    {
        return p_stream.flatMap( i -> {
            final Object l_value = i instanceof ITerm ? ( (ITerm) i ).raw() : i;
            return l_value instanceof Collection<?>
                   ? flattenToStream( ( (Collection<?>) l_value ).stream() )
                   : Stream.of( CRawTerm.from( l_value ) );
        } );
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

    // --- compression algorithm -------------------------------------------------------------------------------------------------------------------------------

    /**
     * calculates the levenshtein distance
     *
     * @param p_first first string
     * @param p_second second string
     * @param p_insertweight inserting weight
     * @param p_replaceweight replace weight
     * @param p_deleteweight delete weight
     * @return distance
     * @see https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
     */
    public static double levenshtein( final String p_first, final String p_second, final double p_insertweight, final double p_replaceweight,
                                      final double p_deleteweight )
    {
        // the array of distances
        double[] l_cost = IntStream.range( 0, p_first.length() + 1 ).mapToDouble( i -> i ).toArray();
        double[] l_newcost = new double[l_cost.length];

        for ( int j = 1; j < p_second.length() + 1; j++ )
        {
            l_newcost[0] = j;

            // calculate cost of operation for all characters
            for ( int i = 1; i < l_cost.length; i++ )
                l_newcost[i] = min(
                    l_cost[i - 1] + ( p_first.charAt( i - 1 ) == p_second.charAt( j - 1 ) ? 0 : p_replaceweight ),
                    l_newcost[i - 1] + p_deleteweight,
                    l_cost[i] + p_insertweight
                );

            final double[] l_swap = l_cost;
            l_cost = l_newcost;
            l_newcost = l_swap;
        }

        return l_cost[p_first.length()];
    }


    /**
     * returns the minimum of three elemens
     *
     * @param p_first first value
     * @param p_second second value
     * @param p_third third value
     * @return minimum
     */
    public static double min( final double p_first, final double p_second, final double p_third )
    {
        return Math.min( Math.min( p_first, p_second ), p_third );
    }


    /**
     * normalized-compression-distance
     *
     * @param p_compression compression algorithm
     * @param p_first first string
     * @param p_second second string
     * @return distance in [0,1]
     */
    public static double ncd( final ECompression p_compression, final String p_first, final String p_second )
    {
        if ( p_first.equals( p_second ) )
            return 0;

        final double l_first = compress( p_compression, p_first );
        final double l_second = compress( p_compression, p_second );
        return ( compress( p_compression, p_first + p_second ) - Math.min( l_first, l_second ) ) / Math.max( l_first, l_second );
    }


    /**
     * compression algorithm
     *
     * @param p_compression compression algorithm
     * @param p_input input string
     * @return number of compression bytes
     * @warning counting stream returns the correct number of bytes after flushing
     */
    private static double compress( final ECompression p_compression, final String p_input )
    {
        final DataOutputStream l_counting = new DataOutputStream( new NullOutputStream() );

        try (
            final InputStream l_input = new ByteArrayInputStream( p_input.getBytes( StandardCharsets.UTF_8 ) );
            final OutputStream l_compress = p_compression.get( l_counting )
        )
        {
            IOUtils.copy( l_input, l_compress );
        }
        catch ( final IOException l_exception )
        {
            return 0;
        }

        return l_counting.size();
    }


    /**
     * compression algorithm
     */
    public enum ECompression
    {
        BZIP,
        GZIP,
        DEFLATE,
        PACK200,
        XZ;

        /**
         * enum names
         */
        private static final Set<String> ALGORITHMS = Collections.unmodifiableSet(
                                                          Arrays.stream( ECompression.values() )
                                                                .map( i -> i.name().toUpperCase( Locale.ROOT ) )
                                                                .collect( Collectors.toSet() )
                                                      );

        /**
         * creates a compression stream
         *
         * @param p_datastream data-counting stream
         * @return compression output stream
         * @throws IOException throws on any io error
         */
        public final OutputStream get( final DataOutputStream p_datastream ) throws IOException
        {
            switch ( this )
            {
                case BZIP : return new BZip2CompressorOutputStream( p_datastream );

                case GZIP : return new GzipCompressorOutputStream( p_datastream );

                case DEFLATE : return new DeflateCompressorOutputStream( p_datastream );

                case PACK200 : return new Pack200CompressorOutputStream( p_datastream );

                case XZ : return new XZCompressorOutputStream( p_datastream );

                default :
                    throw new CIllegalStateException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "unknown", this ) );
            }
        }

        /**
         * returns a compression value
         *
         * @param p_value string name
         * @return compression value
         */
        public static ECompression from( final String p_value )
        {
            return ECompression.valueOf( p_value.toUpperCase( Locale.ROOT ) );
        }


        /**
         * checks if a compression exists
         *
         * @param p_value compression name
         * @return existance flag
         */
        public static boolean exist( final String p_value )
        {
            return ALGORITHMS.contains( p_value.toUpperCase( Locale.ROOT ) );
        }

    }


}
