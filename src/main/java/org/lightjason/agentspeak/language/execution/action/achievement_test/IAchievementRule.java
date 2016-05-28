/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L)                                  #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp@lightjason.org)                      #
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

package org.lightjason.agentspeak.language.execution.action.achievement_test;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.execution.action.IBaseExecution;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.instantiable.rule.IRule;
import org.lightjason.agentspeak.language.variable.IRelocateVariable;
import org.lightjason.agentspeak.language.variable.IVariable;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;


/**
 * abstract class for execute a logical-rule
 */
abstract class IAchievementRule<T extends ITerm> extends IBaseExecution<T>
{
    /**
     * ctor
     *
     * @param p_type value of the achievment-goal
     */
    IAchievementRule( final T p_type )
    {
        super( p_type );
    }

    /**
     * execute rule from context
     *
     * @param p_context execution context
     * @param p_value execution literal
     * @param p_parallel parallel execution
     * @return boolean result
     */
    @SuppressWarnings( "unchecked" )
    protected static IFuzzyValue<Boolean> execute( final IContext p_context, final ILiteral p_value, final boolean p_parallel )
    {
        // read current rules, if not exists execution fails
        final Collection<IRule> l_rules = p_context.getAgent().getRules().get( p_value.getFQNFunctor() );
        if ( l_rules == null )
            return CFuzzyValue.from( false );

        // first step is the unification of the caller literal, so variables will be set from the current execution context
        final ILiteral l_unified = p_value.allocate( p_context );

        // second step execute backtracking rules sequential / parallel
        return (
            p_parallel
            ? l_rules.parallelStream()
            : l_rules.stream()
        ).map( i -> {

            // instantiate variables by unification of the rule literal
            final Set<IVariable<?>> l_variables = p_context.getAgent().getUnifier().literal( i.getIdentifier(), l_unified );

            // execute rule
            final IFuzzyValue<Boolean> l_return = i.execute(
                i.instantiate( p_context.getAgent(), l_variables.stream() ),
                false,
                Collections.<ITerm>emptyList(),
                Collections.<ITerm>emptyList(),
                Collections.<ITerm>emptyList()
            );

            // create rule result with fuzzy- and defuzzificated value and instantiate variable set
            return new ImmutableTriple<>( p_context.getAgent().getFuzzy().getDefuzzyfication().defuzzify( l_return ), l_return, l_variables );

        } )

         // find successfully ended rule
         .filter( i -> i.getLeft() )
         .findFirst()

         // realocate rule instantiated variables back to execution context
         .map( i -> {

             i.getRight().parallelStream()
              .filter( j -> j instanceof IRelocateVariable )
              .forEach( j -> ( (IRelocateVariable) j ).relocate() );

             return i.getMiddle();

         } )

         // otherwise rule fails (default behaviour)
         .orElse( CFuzzyValue.from( false ) );
    }

    @Override
    public final int hashCode()
    {
        return m_value.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return ( p_object != null ) && ( p_object instanceof IExecution ) && ( this.hashCode() == p_object.hashCode() );
    }

}
