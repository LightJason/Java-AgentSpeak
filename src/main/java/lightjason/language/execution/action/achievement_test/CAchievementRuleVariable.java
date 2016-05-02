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

package lightjason.language.execution.action.achievement_test;

import lightjason.agent.IAgent;
import lightjason.language.CCommon;
import lightjason.language.ILiteral;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import lightjason.language.variable.IVariable;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Stream;


/**
 * achievement for rule-variable execution
 */
public final class CAchievementRuleVariable extends IAchievementRule<IVariable<?>>
{
    /**
     * ctor
     *
     * @param p_type value of the achievment-goal
     */
    public CAchievementRuleVariable( final IVariable<?> p_type )
    {
        super( p_type );
    }

    @Override
    public IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                         final List<ITerm> p_annotation
    )
    {
        return execute( p_context, CCommon.<ILiteral, ITerm>getRawValue( CCommon.replaceFromContext( p_context, m_value ) ), m_value.hasMutex() );
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "${0}", m_value );
    }

    @Override
    public final double score( final IAgent p_agent )
    {
        return 0;
    }

    @Override
    public Stream<IVariable<?>> getVariables()
    {
        return Stream.of( m_value );
    }
}
