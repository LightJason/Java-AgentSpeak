/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.error;

import lightjason.language.execution.IContext;


/**
 * runtime exception
 */
@SuppressWarnings( "serial" )
public final class CRuntimeException extends RuntimeException
{
    /**
     * execution context
     */
    private final IContext<?> m_context;


    /**
     * ctor
     *
     * @param p_context execution context
     */
    public CRuntimeException( final IContext<?> p_context )
    {
        super();
        m_context = p_context;
    }

    /**
     * ctor
     *
     * @param p_message execution message
     * @param p_context execution context
     */
    public CRuntimeException( final String p_message, final IContext<?> p_context )
    {
        super( p_message );
        m_context = p_context;
    }

    /**
     * ctor
     *
     * @param p_message execution message
     * @param p_cause execption cause
     * @param p_context execution context
     */
    public CRuntimeException( final String p_message, final Throwable p_cause, final IContext<?> p_context )
    {
        super( p_message, p_cause );
        m_context = p_context;
    }

    /**
     * ctor
     *
     * @param p_cause execption cause
     * @param p_context execution context
     */
    public CRuntimeException( final Throwable p_cause, final IContext<?> p_context )
    {
        super( p_cause );
        m_context = p_context;
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
                                 final IContext<?> p_context
    )
    {
        super( p_message, p_cause, p_enableSuppression, p_writableStackTrace );
        m_context = p_context;
    }

    /**
     * returns the execution context
     *
     * @return context
     */
    public final IContext<?> getContext()
    {
        return m_context;
    }

}
