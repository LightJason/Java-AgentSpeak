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

package org.lightjason.agentspeak.generator;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.CNoSuchElementException;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * abstract base action
 */
public abstract class IBaseActionGenerator implements IActionGenerator
{

    /**
     * loaded actions
     */
    private final Map<IPath, IAction> m_actions;
    /**
     * other action generator
     */
    private final Set<IActionGenerator> m_others = Collections.synchronizedSet( new HashSet<>() );

    /**
     * ctor
     */
    protected IBaseActionGenerator()
    {
        m_actions = Collections.emptyMap();
    }

    /**
     * ctor
     * @param p_actions action stream
     */
    protected IBaseActionGenerator( @NonNull final Stream<IAction> p_actions )
    {
        m_actions = p_actions.collect( Collectors.toConcurrentMap( IAction::name, i -> i, ( i, j ) -> i ) );
    }

    @Override
    public final IActionGenerator add( @Nonnull final IActionGenerator... p_generator )
    {
        return this.add( Arrays.stream( p_generator ) );
    }

    @Override
    public final IActionGenerator remove( @Nonnull final IActionGenerator... p_generator )
    {
        return this.remove( Arrays.stream( p_generator ) );
    }

    @Override
    public final IActionGenerator add( @Nonnull final Stream<IActionGenerator> p_generator )
    {
        p_generator.forEach( m_others::add );
        return this;
    }

    @Override
    public final IActionGenerator remove( @Nonnull final Stream<IActionGenerator> p_generator )
    {
        p_generator.forEach( m_others::remove );
        return this;
    }

    @Override
    public final boolean contains( @Nonnull final IPath p_path )
    {
        return m_actions.containsKey( p_path ) || m_others.parallelStream().anyMatch( i -> i.contains( p_path ) ) || this.stream( p_path ).isPresent();
    }

    @Override
    public IAction apply( final IPath p_path )
    {
        // get action from cache
        if ( m_actions.containsKey( p_path ) )
            return m_actions.get( p_path );

        // check others
        final Optional<IAction> l_other = m_others.parallelStream().filter( i -> i.contains( p_path ) ).findFirst().map( i -> i.apply( p_path ) );
        if ( l_other.isPresent() )
            return l_other.get();

        // try to find action
        final Optional<IAction> l_action = this.stream( p_path );
        if ( l_action.isEmpty() )
            throw new CNoSuchElementException( CCommon.languagestring( this, "notfound", p_path ) );

        m_actions.putIfAbsent( l_action.get().name(), l_action.get() );
        return l_action.get();
    }

    protected abstract Optional<IAction>  stream( @Nonnull IPath p_path );
}
