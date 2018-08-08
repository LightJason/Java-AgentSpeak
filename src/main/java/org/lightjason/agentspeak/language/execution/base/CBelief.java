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

package org.lightjason.agentspeak.language.execution.base;

import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.error.CEnumConstantNotPresentException;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IBaseExecution;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;


/**
 * belief action
 */
public final class CBelief extends IBaseExecution<ILiteral>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -2856258502338708361L;
    /**
     * running action
     */
    private final EAction m_action;

    /**
     * ctor
     *
     * @param p_action action
     * @param p_literal literal
     */
    public CBelief( @Nonnull final EAction p_action, @Nonnull final ILiteral p_literal )
    {
        super( p_literal );
        m_action = p_action;
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0}{1}", m_action, m_value );
    }

    @Nonnull
    @Override
    public IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                         @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        m_action.accept( p_context, m_value );
        return CFuzzyValue.of( true );
    }

    /**
     * belief action definition
     */
    public enum EAction implements BiConsumer<IContext, ILiteral>
    {
        ADD( "+" ),
        DELETE( "-" );
        /**
         * name
         */
        private final String m_operator;

        /**
         * ctor
         *
         * @param p_operator string represenation
         */
        EAction( @Nonnull final String p_operator )
        {
            m_operator = p_operator;
        }

        @Override
        public String toString()
        {
            return m_operator;
        }

        @Override
        public void accept( @Nonnull final IContext p_context, @Nonnull final ILiteral p_literal )
        {
            switch ( this )
            {
                case ADD:
                    p_context.agent().beliefbase().add( p_literal.allocate( p_context ) );
                    break;

                case DELETE:
                    p_context.agent().beliefbase().remove( p_literal.allocate( p_context ) );
                    break;

                default:
                    throw new CEnumConstantNotPresentException( this.getClass(), this.toString() );
            }
        }

        /**
         * builds a action by a string
         *
         * @param p_value string value
         * @return action
         */
        @Nonnull
        public static EAction of( @Nonnull final String p_value )
        {
            return Arrays.stream( EAction.values() )
                         .filter( i -> i.m_operator.equals( p_value ) )
                         .findFirst()
                         .orElseThrow( () -> new NoSuchElementException( CCommon.languagestring( EAction.class, "unknownoperator", p_value ) ) );
        }
    }
}
