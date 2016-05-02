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

package lightjason.language.execution.action.goaltest;

import lightjason.agent.IAgent;
import lightjason.language.ILiteral;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import lightjason.language.instantiable.rule.IRule;
import lightjason.language.variable.IVariable;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;


/**
 * proxy rule to encapsulate all rules
 */
public final class CAchievementRuleLiteral extends IAchievementRule<ILiteral>
{

    /**
     * ctor
     *
     * @param p_literal literal of the call
     */
    public CAchievementRuleLiteral( final ILiteral p_literal )
    {
        super( p_literal );
    }

    /**
     * @bug some indeterministic execution
     */
    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        return this.execute( p_context, m_value, m_value.hasAt() );
    }

    @Override
    public final double score( final IAgent p_agent )
    {
        // rules can create a cyclic reference so on calculate the score value
        // a cyclic reference must be ignored
        final Collection<IRule> l_rules = p_agent.getRules().get( m_value.getFQNFunctor() );
        return l_rules == null
               ? p_agent.getAggregation().error()
               : l_rules.parallelStream()
                        .filter( i -> this.equals( i ) )
                        .mapToDouble( i -> i.score( p_agent ) )
                        .sum();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final Stream<IVariable<?>> getVariables()
    {
        return Stream.concat(
                lightjason.language.CCommon.recursiveterm( m_value.orderedvalues() ),
                lightjason.language.CCommon.recursiveliteral( m_value.annotations() )
        )
                     .parallel()
                     .filter( i -> i instanceof IVariable<?> )
                     .map( i -> ( (IVariable<?>) i ) );
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return this.hashCode() == p_object.hashCode();
    }

    @Override
    public final int hashCode()
    {
        return m_value.hashCode();
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "$", m_value );
    }

}
