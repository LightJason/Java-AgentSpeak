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

package org.lightjason.agentspeak.language.execution.expression;

import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.error.CEnumConstantNotPresentException;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.function.Function;


/**
 * expression unary operator
 */
public enum EUnaryOperator implements Function<ITerm, Object>
{
    NEGATION( "~", "not" );

    /**
     * text name of the enum
     */
    private final String[] m_operator;


    /**
     * ctor
     *
     * @param p_operator text name
     */
    EUnaryOperator( @Nonnull final String... p_operator )
    {
        m_operator = p_operator;
    }

    @Override
    public String toString()
    {
        return m_operator[0];
    }

    @Override
    public Object apply( @Nonnull final ITerm p_term )
    {
        switch ( this )
        {
            case NEGATION:
                return !p_term.<Boolean>raw();

            default:
                throw new CEnumConstantNotPresentException( this.getClass(), this.toString() );
        }
    }

    /**
     * returns operator of a string
     *
     * @param p_value string
     * @return operator
     */
    @Nonnull
    public static EUnaryOperator of( @Nonnull final String p_value )
    {
        return Arrays.stream( EUnaryOperator.values() )
                     .filter( i -> Arrays.stream( i.m_operator ).anyMatch( j -> j.equals( p_value ) ) )
                     .findFirst()
                     .orElseThrow( () -> new CIllegalArgumentException( CCommon.languagestring( EUnaryOperator.class, "unknownoperator", p_value ) ) );
    }
}
