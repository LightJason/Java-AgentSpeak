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

package org.lightjason.agentspeak.language;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.hash.Hasher;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.grammar.CASTVisitorManual;
import org.lightjason.agentspeak.grammar.CErrorListener;
import org.lightjason.agentspeak.grammar.IASTVisitorManual;
import org.lightjason.agentspeak.grammar.IBaseParser;
import org.lightjason.agentspeak.grammar.ManualLexer;
import org.lightjason.agentspeak.grammar.ManualParser;
import org.lightjason.agentspeak.language.execution.IContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * default generic literal class for agent beliefs
 * a literal consists of a functor, an optional list of values
 * e.g. velocity(50)
 */
public final class CLiteral implements ILiteral
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -3253396471300120109L;
    /**
     * negation symbol
     */
    private static final String NEGATION = "~";
    /**
     * at symbol
     */
    private static final String AT = "@";
    /**
     * literal values
     */
    private final ImmutableMultimap<IPath, ITerm> m_values;
    /**
     * literal values as list
     */
    private final List<ITerm> m_orderedvalues;
    /**
     * literals functor
     */
    private final IPath m_functor;
    /**
     * negated option
     */
    private final boolean m_negated;
    /**
     * @ prefix is set
     */
    private final boolean m_at;
    /**
     * hash code
     */
    private final int m_hash;
    /**
     * hash of the structure
     */
    private final int m_structurehash;



    /**
     * ctor
     *
     * @param p_at @ prefix is set
     * @param p_negated negated flag
     * @param p_functor functor of the literal
     * @param p_values initial list of values
     */
    public CLiteral( final boolean p_at, final boolean p_negated, @Nonnull final IPath p_functor, @Nonnull final Collection<ITerm> p_values )
    {
        m_at = p_at;
        m_negated = p_negated;
        // create a full copy of the functor, because concurrency modification
        m_functor = new CPath( p_functor );

        // create immutable structures
        if ( !p_values.isEmpty() )
        {
            final Multimap<IPath, ITerm> l_values = LinkedListMultimap.create();
            p_values.forEach( i -> l_values.put( i.fqnfunctor(), i ) );
            m_values = ImmutableListMultimap.copyOf( l_values );
            m_orderedvalues = Collections.unmodifiableList( new ArrayList<>( p_values ) );
        }
        else
        {
            m_orderedvalues = Collections.emptyList();
            m_values = ImmutableListMultimap.of();
        }

        // calculates object hash value
        final Hasher l_hasher = CCommon.termhashing();
        l_hasher.putInt( m_functor.hashCode() );
        l_hasher.putBoolean( m_negated );
        l_hasher.putBoolean( m_at );
        m_orderedvalues.forEach( i -> l_hasher.putInt( i.hashCode() ) );
        m_hash = l_hasher.hash().asInt();

        // calculates the structure hash value of the value definition (need to start with value definition)
        final Hasher l_valuehasher = CCommon.termhashing();
        m_orderedvalues.forEach( i -> l_valuehasher.putInt( i.structurehash() ) );
        l_valuehasher.putBoolean( m_negated );
        l_valuehasher.putString( p_functor.path(), Charsets.UTF_8 );
        m_structurehash = l_valuehasher.hash().asInt();
    }

    /**
     * factory
     *
     * @param p_functor functor string
     * @param p_values value term
     * @return literal
     */
    public static ILiteral of( @Nonnull final String p_functor, @Nullable final ITerm... p_values )
    {
        return of(
            p_functor,
            ( Objects.isNull( p_values ) ) || ( p_values.length == 0 )
            ? Collections.emptySet()
            : Arrays.stream( p_values ).collect( Collectors.toList() )
        );
    }

    /**
     * factory
     *
     * @param p_functor functor string
     * @param p_values value term
     * @return literal
     */
    public static ILiteral of( @Nonnull final String p_functor, @Nonnull final Collection<ITerm> p_values )
    {
        return new CLiteral(
            p_functor.contains( AT ), p_functor.contains( NEGATION ), CPath.of( p_functor.replace( AT, "" ).replace( NEGATION, "" ) ),
            p_values
        );
    }

    /**
     * stream factory
     *
     * @param p_functor functor
     * @param p_values value stream
     * @return literal
     */
    public static ILiteral of( @Nonnull final String p_functor, @Nonnull final Stream<ITerm> p_values )
    {
        return of( p_functor, p_values.collect( Collectors.toList() ) );
    }

    /**
     * factory
     *
     * @param p_functor functor path
     * @param p_values values
     * @return literal
     */
    public static ILiteral of( @Nonnull final IPath p_functor, @Nullable final ITerm... p_values )
    {
        return of( false, false, p_functor, p_values );
    }

    /**
     * factory
     *
     * @param p_functor functor path
     * @param p_values values
     * @return literal
     */
    public static ILiteral of( @Nonnull final IPath p_functor, @Nonnull final Stream<ITerm> p_values )
    {
        return of( false, false, p_functor, p_values );
    }

    /**
     * factory
     *
     * @param p_at at
     * @param p_negated negation
     * @param p_functor functor path
     * @param p_values vales
     * @return literal
     */
    public static ILiteral of( final boolean p_at, final boolean p_negated, @Nonnull final IPath p_functor, @Nullable final ITerm... p_values )
    {
        return of( p_at, p_negated, p_functor, ( Objects.isNull( p_values ) ) || ( p_values.length == 0 ) ? Stream.empty() : Arrays.stream( p_values ) );
    }

    /**
     * factory
     *
     * @param p_at at
     * @param p_negated negation
     * @param p_functor functor path
     * @param p_values vales
     * @return literal
     */
    public static ILiteral of( final boolean p_at, final boolean p_negated, @Nonnull final IPath p_functor, @Nonnull final Stream<ITerm> p_values )
    {
        return new CLiteral( p_at, p_negated, p_functor, p_values.collect( Collectors.toList() ) );
    }

    /**
     * factory
     *
     * @param p_literal literal string
     * @return literal
     *
     * @throws Exception parsing and stream exception
     */
    @Nonnull
    public static ILiteral parse( @Nonnull final String p_literal ) throws Exception
    {
        return new CParser().parse( new ByteArrayInputStream( p_literal.getBytes( Charset.forName( "UTF-8" ) ) ) ).literal();
    }

    @Nonnull
    @Override
    public Stream<ITerm> values( @Nullable final IPath... p_path )
    {
        return ( Objects.isNull( p_path ) ) || ( p_path.length < 1 )
               ? m_values.values().stream()
               : p_path.length == 1
                 ? m_values.asMap().get( p_path[0] ).stream()
                 : m_values.asMap().get( p_path[0] ).stream()
                           .filter( i -> i instanceof ILiteral )
                           .flatMap( i -> ( (ILiteral) i ).values( Arrays.copyOfRange( p_path, 1, p_path.length ) ) );
    }

    @Nonnull
    @Override
    public Stream<ITerm> orderedvalues( @Nullable final IPath... p_path )
    {
        return ( Objects.isNull( p_path ) ) || ( p_path.length < 1 )
               ? m_orderedvalues.stream().sequential()
               : p_path.length == 1
                 ? m_orderedvalues.stream()
                                  .filter( i -> i.fqnfunctor().equals( p_path[0] ) ).sequential()
                 : m_orderedvalues.stream()
                                  .filter( i -> i.fqnfunctor().equals( p_path[0] ) )
                                  .filter( i -> i instanceof ILiteral )
                                  .map( ITerm::<ILiteral>raw )
                                  .filter( Objects::nonNull )
                                  .flatMap( i -> i.orderedvalues( Arrays.copyOfRange( p_path, 1, p_path.length ) ) );
    }

    @Override
    public boolean emptyValues()
    {
        return m_values.isEmpty();
    }

    @Override
    public int structurehash()
    {
        return m_structurehash;
    }

    @Override
    public boolean negated()
    {
        return m_negated;
    }

    @Override
    public boolean hasAt()
    {
        return m_at;
    }

    @Override
    public boolean hasVariable()
    {
        return m_orderedvalues.parallelStream().anyMatch( ITerm::hasVariable );
    }

    @Nonnull
    @Override
    public ILiteral bind( @Nonnull final IContext p_context )
    {
        return new CLiteral(
            m_at,
            m_negated,
            m_functor,
            m_orderedvalues.stream()
                           .map( i -> i instanceof ILiteral ? i.<ILiteral>term().bind( p_context ) : CCommon.bindbycontext( i, p_context ) )
                           .collect( Collectors.toList() )
        );
    }

    @Nonnull
    @Override
    public ILiteral allocate( @Nonnull final IContext p_context )
    {
        return new CLiteral(
            m_at,
            m_negated,
            m_functor,
            m_orderedvalues.stream()
                           .map( i -> i instanceof ILiteral ? i.<ILiteral>term().allocate( p_context ) : CRawTerm.of( CCommon.bindbycontext( i, p_context ) ) )
                           .collect( Collectors.toList() )
        );
    }

    @Nonnull
    @Override
    public String functor()
    {
        return m_functor.suffix();
    }

    @Nonnull
    @Override
    public IPath functorpath()
    {
        return m_functor.subpath( 0, m_functor.size() - 1 );
    }

    @Nonnull
    @Override
    public IPath fqnfunctor()
    {
        return m_functor;
    }

    @Nonnull
    @Override
    @SuppressWarnings( "unchecked" )
    public <V> V raw()
    {
        return (V) this;
    }

    @Override
    public int hashCode()
    {
        return m_hash;
    }

    @Override
    public boolean equals( final Object p_object )
    {
        return p_object instanceof ILiteral && this.hashCode() == p_object.hashCode();
    }

    @Nonnull
    @Override
    public ILiteral shallowcopy( @Nullable final IPath... p_prefix )
    {
        return ( Objects.isNull( p_prefix ) ) || ( p_prefix.length == 0 )

               ? new CLiteral(
            m_at, m_negated, m_functor,
            m_values.values()
        )

               : new CLiteral(
                   m_at, m_negated, p_prefix[0].append( m_functor ),
                   m_values.values()
               );
    }

    @Nonnull
    @Override
    public ILiteral shallowcopysuffix()
    {
        return new CLiteral(
            m_at, m_negated, CPath.of( m_functor.suffix() ),
            m_values.values()
        );
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0}{1}{2}{3}", m_negated ? NEGATION : "", m_at ? AT : "", m_functor, m_orderedvalues );
    }

    @Override
    public int compareTo( @Nonnull final ILiteral p_literal )
    {
        return Integer.compare( this.hashCode(), p_literal.hashCode() );
    }

    @Nonnull
    @Override
    public synchronized ITerm deepcopy( @Nullable final IPath... p_prefix )
    {
        return ( Objects.isNull( p_prefix ) ) || ( p_prefix.length == 0 )

               ?
               new CLiteral(
                   m_at, m_negated, m_functor,
                   m_values.values().stream().map( i -> i.deepcopy() ).collect( Collectors.toList() )
               )

               :
               new CLiteral(
                   m_at, m_negated, p_prefix[0].append( m_functor ),
                   m_values.values().stream().map( i -> i.deepcopy() ).collect( Collectors.toList() )
               );
    }

    @Nonnull
    @Override
    public synchronized ITerm deepcopysuffix()
    {
        return new CLiteral(
            m_at, m_negated, CPath.of( m_functor.suffix() ),
            m_values.values().stream().map( i -> i.deepcopy() ).collect( Collectors.toList() )
        );
    }


    /**
     * literal parser
     */
    private static final class CParser extends IBaseParser<IASTVisitorManual, ManualLexer, ManualParser>
    {

        /**
         * ctor
         *
         * @throws NoSuchMethodException on ctor-method call
         */
        CParser() throws NoSuchMethodException
        {
            super( new CErrorListener() );
        }

        @Nonnull
        @Override
        public IASTVisitorManual parse( @Nonnull final InputStream p_stream ) throws Exception
        {
            final IASTVisitorManual l_visitor = new CASTVisitorManual();
            l_visitor.visit( this.parser( p_stream ).root_literal() );
            return l_visitor;
        }

        @Override
        protected Class<ManualLexer> lexerclass()
        {
            return ManualLexer.class;
        }

        @Override
        protected Class<ManualParser> parserclass()
        {
            return ManualParser.class;
        }
    }
}
