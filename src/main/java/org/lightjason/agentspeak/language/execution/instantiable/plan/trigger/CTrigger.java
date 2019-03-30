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

package org.lightjason.agentspeak.language.execution.instantiable.plan.trigger;

import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ILiteral;

import javax.annotation.Nonnull;
import java.text.MessageFormat;


/**
 * event with literal data
 */
public final class CTrigger implements ITrigger
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -4216265954626567558L;
    /**
     * variable number
     */
    private final int m_variables;
    /**
     * literal with unified variables
     */
    private final ILiteral m_literal;
    /**
     * event type
     */
    private final EType m_event;
    /**
     * hashcode
     */
    private final int m_hashcode;
    /**
     * content hashcode
     */
    private final int m_structurehash;

    /**
     * ctor
     *
     * @param p_event type
     * @param p_literal literal with unified variables
     */
    @SuppressWarnings( "unchecked" )
    public CTrigger( @Nonnull final EType p_event, @Nonnull final ILiteral p_literal )
    {
        m_event = p_event;
        m_literal = p_literal;
        m_variables = CCommon.variablefrequency( p_literal ).size();
        m_hashcode = m_event.hashCode() ^ m_literal.hashCode();
        m_structurehash = m_event.hashCode() ^ m_literal.structurehash();
    }

    /**
     * creates a trigger event^
     *
     * @param p_event event
     * @param p_literal trigger literal
     * @return trigger object
     */
    public static ITrigger of( @Nonnull final EType p_event, @Nonnull final ILiteral p_literal )
    {
        return new CTrigger( p_event, p_literal );
    }

    @Override
    public int hashCode()
    {
        return m_hashcode;
    }

    @Override
    public boolean equals( final Object p_object )
    {
        return p_object instanceof ITrigger && this.hashCode() == p_object.hashCode();
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0}{1}", m_event, m_literal );
    }

    @Override
    public EType type()
    {
        return m_event;
    }

    @Override
    public ILiteral literal()
    {
        return m_literal;
    }

    @Override
    public int variablesize()
    {
        return m_variables;
    }

    @Override
    public int structurehash()
    {
        return m_structurehash;
    }

    @Nonnull
    @Override
    public ITrigger shallowcopy( final IPath... p_prefix )
    {
        return new CTrigger( m_event, m_literal.shallowcopy( p_prefix ) );
    }

    @Nonnull
    @Override
    public ITrigger shallowcopysuffix()
    {
        return new CTrigger( m_event, m_literal.shallowcopysuffix() );
    }

    @Override
    public int compareTo( @Nonnull final ITrigger p_other )
    {
        return p_other.toString().compareTo( this.toString() );
    }
}
