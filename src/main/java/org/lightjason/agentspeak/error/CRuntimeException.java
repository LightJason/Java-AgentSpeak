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

package org.lightjason.agentspeak.error;

import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.language.execution.IContext;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.logging.Logger;


/**
 * runtime exception
 */
public final class CRuntimeException extends RuntimeException implements IContextException
{
    /**
     * logger
     */
    private static final Logger LOGGER = CCommon.logger( CIllegalStateException.class );
    /**
     * serial id
     */
    private static final transient long serialVersionUID = -1053856178724776159L;
    /**
     * execution context
     */
    private final IContext m_context;



    /**
     * ctor
     *
     * @param p_context execution context
     */
    public CRuntimeException( final IContext p_context )
    {
        super();
        m_context = p_context;
        LOGGER.warning( MessageFormat.format( "exception is thrown: {0}", m_context ) );
    }

    /**
     * ctor
     *
     * @param p_message execution message
     * @param p_context execution context
     */
    public CRuntimeException( @Nonnull final String p_message, @Nonnull final IContext p_context )
    {
        super( p_message );
        m_context = p_context;
        LOGGER.warning( MessageFormat.format( "{0}: {1}", p_message, m_context ) );
    }

    /**
     * ctor
     *
     * @param p_message execution message
     * @param p_cause execption cause
     * @param p_context execution context
     */
    public CRuntimeException( @Nonnull final String p_message, @Nonnull final Throwable p_cause, @Nonnull final IContext p_context )
    {
        super( p_message, p_cause );
        m_context = p_context;
        LOGGER.warning( MessageFormat.format( "{0}: {1}", p_message, m_context ) );
    }

    /**
     * ctor
     *
     * @param p_cause execption cause
     * @param p_context execution context
     */
    public CRuntimeException( @Nonnull final Throwable p_cause, @Nonnull final IContext p_context )
    {
        super( p_cause );
        m_context = p_context;
        LOGGER.warning( MessageFormat.format( "{0}: {1}", p_cause.getMessage(), m_context ) );
    }

    /**
     * ctor
     *
     * @param p_message execution message
     * @param p_cause execption cause
     * @param p_enablesuppression suppression flag
     * @param p_writablestacktrace stacktrace flag
     * @param p_context execution context
     */
    protected CRuntimeException( final String p_message, final Throwable p_cause, final boolean p_enablesuppression,
                                 final boolean p_writablestacktrace, final IContext p_context
    )
    {
        super( p_message, p_cause, p_enablesuppression, p_writablestacktrace );
        m_context = p_context;
        LOGGER.warning( MessageFormat.format( "{0}: {1}", p_message, m_context ) );
    }

    @Nonnull
    @Override
    public final IContext context()
    {
        return m_context;
    }

}
