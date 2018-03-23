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

package org.lightjason.agentspeak.language.execution.achievementtest;

import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;


/**
 * achievement for rule-literal execution
 */
public final class CAchievementRuleLiteral extends IAchievementRule<ILiteral>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -1575197582635138960L;

    /**
     * ctor
     *
     * @param p_literal literal of the call
     */
    public CAchievementRuleLiteral( @Nonnull final ILiteral p_literal )
    {
        super( p_literal );
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        return execute( m_value.hasAt(), p_context, m_value );
    }

    @Nonnull
    @Override
    @SuppressWarnings( "unchecked" )
    public final Stream<IVariable<?>> variables()
    {
        return Objects.isNull( m_value )
               ? Stream.empty()
               : CCommon.flattenrecursive( m_value.orderedvalues() )
                        .filter( i -> i instanceof IVariable<?> )
                        .map( ITerm::term );
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "$", m_value );
    }

}
