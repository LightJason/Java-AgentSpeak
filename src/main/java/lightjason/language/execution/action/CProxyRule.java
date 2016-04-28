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

package lightjason.language.execution.action;

import lightjason.agent.IAgent;
import lightjason.language.ILiteral;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.IExecution;
import lightjason.language.execution.fuzzy.CFuzzyValue;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import lightjason.language.instantiable.rule.IRule;
import lightjason.language.variable.IRelocateVariable;
import lightjason.language.variable.IVariable;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;


/**
 * proxy rule to encapsulate all rules
 *
 */
public final class CProxyRule implements IExecution
{
    /**
     * literal of the execution call
     */
    private final ILiteral m_callerliteral;

    /**
     * ctor
     *
     * @param p_callerliteral literal of the call
     */
    public CProxyRule( final ILiteral p_callerliteral )
    {
        m_callerliteral = p_callerliteral;
    }

    /**
     * @bug some indeterministic execution
     */
    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        // read current rules, if not exists execution fails
        final Collection<IRule> l_rules = p_context.getAgent().getRules().get( m_callerliteral.getFQNFunctor() );
        if ( l_rules == null )
            return CFuzzyValue.from( false );

        // first step is the unification of the caller literal, so variables will be set from the current execution context
        final ILiteral l_unified = m_callerliteral.unify( p_context );

        // second step execute backtracking rules sequential / parallel
        return (
                m_callerliteral.hasAt()
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
    public final double score( final IAgent p_agent )
    {
        // rules can create a cyclic reference so on calculate the score value
        // a cyclic reference must be ignored
        final Collection<IRule> l_rules = p_agent.getRules().get( m_callerliteral.getFQNFunctor() );
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
                lightjason.language.CCommon.recursiveterm( m_callerliteral.orderedvalues() ),
                lightjason.language.CCommon.recursiveliteral( m_callerliteral.annotations() )
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
        return m_callerliteral.hashCode();
    }

    @Override
    public final String toString()
    {
        return m_callerliteral.toString();
    }

}
