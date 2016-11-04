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

package org.lightjason.agentspeak.error;

import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.language.execution.IContext;

import java.text.MessageFormat;
import java.util.logging.Logger;


/**
 * runtime exception
 */
@SuppressWarnings( "serial" )
public final class CRuntimeException extends RuntimeException implements IContextException
{
    /**
     * logger
     */
    private static final Logger LOGGER = CCommon.logger( CIllegalStateException.class );
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
    public CRuntimeException( final String p_message, final IContext p_context )
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
    public CRuntimeException( final String p_message, final Throwable p_cause, final IContext p_context )
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
    public CRuntimeException( final Throwable p_cause, final IContext p_context )
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
     * @param p_enableSuppression suppression flag
     * @param p_writableStackTrace stacktrace flag
     * @param p_context execution context
     */
    protected CRuntimeException( final String p_message, final Throwable p_cause, final boolean p_enableSuppression, final boolean p_writableStackTrace,
                                 final IContext p_context
    )
    {
        super( p_message, p_cause, p_enableSuppression, p_writableStackTrace );
        m_context = p_context;
        LOGGER.warning( MessageFormat.format( "{0}: {1}", p_message, m_context ) );
    }

    @Override
    public final IContext context()
    {
        return m_context;
    }

}
