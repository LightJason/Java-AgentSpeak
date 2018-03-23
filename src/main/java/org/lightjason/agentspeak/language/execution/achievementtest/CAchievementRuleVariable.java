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
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


/**
 * achievement for rule-variable execution
 */
public final class CAchievementRuleVariable extends IAchievementRule<IExecution>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -3462378724593325487L;
    /**
     * parallel flag
     */
    private final boolean m_parallel;

    /**
     * ctor
     *
     * @param p_value value of the rule
     */
    public CAchievementRuleVariable( @Nonnull final IExecution p_value )
    {
        super( p_value );
        m_parallel = p_value.variables().anyMatch( IVariable::mutex );
    }

    @Nonnull
    @Override
    public IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                         @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_return = new ArrayList<>();
        if ( ( !m_value.execute( p_parallel, p_context, p_argument, l_return ).value() ) || ( l_return.size() == 1 ) )
            return CFuzzyValue.of( false );

        return execute(
            m_parallel,
            p_context,
            CCommon.replaceFromContext( p_context, l_return.get( 0 ) ).term()
        );
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "${0}", m_value );
    }

    @Nonnull
    @Override
    public Stream<IVariable<?>> variables()
    {
        return m_value.variables();
    }
}
