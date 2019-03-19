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

package org.lightjason.agentspeak.action.storage;

import org.lightjason.agentspeak.action.IBaseAction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * storage default definitions
 */
public abstract class IBaseStorage extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -2187242564778364709L;
    /**
     * set with forbidden keys
     */
    protected final Function<String, Boolean> m_resolver;

    /**
     * ctor
     */
    protected IBaseStorage()
    {
        this( i -> false );
    }

    /**
     * ctor
     *
     * @param p_resolver resolver of forbidden keys
     * @warning resolver will be triggered in parallel
     */
    protected IBaseStorage( @Nonnull final Function<String, Boolean> p_resolver )
    {
        m_resolver = p_resolver;
    }

    /**
     * ctor
     *
     * @param p_forbidden forbidden keys
     */
    protected IBaseStorage( @Nullable final String... p_forbidden )
    {
        this(
            ( Objects.isNull( p_forbidden ) ) || ( p_forbidden.length == 0 )
            ? Stream.empty()
            : Arrays.stream( p_forbidden )
        );
    }

    /**
     * ctor
     *
     * @param p_fordbidden forbidden keys
     */
    protected IBaseStorage( @Nonnull final Stream<String> p_fordbidden )
    {
        final Set<String> l_names = p_fordbidden.collect( Collectors.toCollection( ConcurrentSkipListSet::new ) );
        m_resolver = l_names::contains;
    }

    /**
     * returns a stream which keys are forbidden
     *
     * @param p_keys key name stream
     * @return boolean stream with forbidden check
     */
    @Nonnull
    public final Stream<Boolean> forbiddenkeys( @Nonnull final Stream<String> p_keys )
    {
        return p_keys.map( m_resolver );
    }

    /**
     * returns a stream which keys are forbidden
     *
     * @param p_keys keys
     * @return boolean stream with forbidden check
     */
    @Nonnull
    public final Stream<Boolean> forbiddenkeys( @Nonnull final String... p_keys )
    {
        return this.forbiddenkeys( Arrays.stream( p_keys ) );
    }

}
