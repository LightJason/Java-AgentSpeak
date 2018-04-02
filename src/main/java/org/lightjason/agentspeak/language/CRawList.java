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

import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.error.CIllegalStateException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * list of raw values
 */
public final class CRawList implements IRawTerm<List<?>>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -3104914908249435963L;
    /**
     * values
     */
    private final IRawTerm<List<?>> m_value;


    /**
     * ctor
     *
     * @param p_value values
     */
    public CRawList( @Nonnull final Stream<ITerm> p_value )
    {
        m_value = CRawTerm.of( p_value.map( ITerm::raw ).collect( Collectors.toList() ) );
    }

    @Override
    public final boolean allocated()
    {
        return true;
    }

    @Nonnull
    @Override
    public final IRawTerm<List<?>> thrownotallocated() throws IllegalStateException
    {
        if ( !this.allocated() )
            throw new CIllegalStateException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "notallocated" ) );

        return this;
    }

    @Override
    public final boolean valueassignableto( @Nonnull final Class<?>... p_class )
    {
        return Arrays.stream( p_class ).anyMatch( i -> i.isAssignableFrom( m_value.raw().getClass() ) );
    }

    @Nullable
    @Override
    public final IRawTerm<List<?>> throwvaluenotassignableto( @Nonnull final Class<?>... p_class ) throws IllegalArgumentException
    {
        if ( !this.valueassignableto( p_class ) )
            throw new CIllegalArgumentException( CCommon.languagestring( this, "notassignable", Arrays.asList( p_class ) ) );

        return m_value;
    }

    @Nonnull
    @Override
    public final String functor()
    {
        return IPath.EMPTY.toString();
    }

    @Nonnull
    @Override
    public final IPath functorpath()
    {
        return IPath.EMPTY;
    }

    @Nonnull
    @Override
    public final IPath fqnfunctor()
    {
        return IPath.EMPTY;
    }

    @Override
    public final boolean hasVariable()
    {
        return false;
    }

    @Nullable
    @Override
    @SuppressWarnings( "unchecked" )
    public final <T> T raw()
    {
        return (T) m_value;
    }

    @Nonnull
    @Override
    public final ITerm deepcopy( @Nullable final IPath... p_prefix )
    {
        return this;
    }

    @Nonnull
    @Override
    public final ITerm deepcopysuffix()
    {
        return this;
    }

    @Override
    public final int structurehash()
    {
        return 0;
    }
}
