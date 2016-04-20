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

import com.google.common.collect.Multimap;
import lightjason.agent.IAgent;
import lightjason.common.CCommon;
import lightjason.common.IPath;
import lightjason.error.CIllegalArgumentException;
import lightjason.language.ILiteral;
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.IContext;
import lightjason.language.execution.IExecution;
import lightjason.language.execution.fuzzy.CFuzzyValue;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import lightjason.language.instantiable.rule.IRule;
import lightjason.language.score.IAggregation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


/**
 * proxy action to encapsulate all rules
 *
 * @note inner annotations cannot be used on the
 * grammer definition, so the inner annotations are ignored
 * @bug not working
 * @todo check cyclic rule reference on score calculation A -> B -> A
 */
public final class CProxyRule implements IExecution
{
    /**
     * literal definition
     */
    private final ILiteral m_literal;
    /**
     * collection with possible rules
     */
    private final Collection<IRule> m_rules;
    /**
     * object hash
     */
    private final int m_hash;

    /**
     * ctor
     *
     * @param p_rules map with rules
     * @param p_literal literal of the call
     */
    public CProxyRule( final Multimap<IPath, IRule> p_rules, final ILiteral p_literal )
    {
        if ( !p_rules.asMap().containsKey( p_literal.getFQNFunctor() ) )
            throw new CIllegalArgumentException( CCommon.getLanguageString( this, "ruleunknown", p_literal ) );

        m_literal = p_literal;
        m_rules = Collections.unmodifiableCollection( p_rules.asMap().get( p_literal.getFQNFunctor() ) );
        m_hash = m_rules.parallelStream().mapToInt( i -> i.hashCode() ).sum();
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        System.out.println( "####>>> " + m_literal.unify( p_context ) );
        return CFuzzyValue.from( true );
    }

    @Override
    public final double score( final IAggregation p_aggregate, final IAgent p_agent )
    {
        return m_rules.parallelStream().filter( i -> !m_rules.contains( i ) ).mapToDouble( i -> i.score( p_aggregate, p_agent ) ).sum();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final Stream<IVariable<?>> getVariables()
    {
        return Stream.concat(
                lightjason.language.CCommon.recursiveterm( m_literal.orderedvalues() ),
                lightjason.language.CCommon.recursiveliteral( m_literal.annotations() )
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
        return m_hash;
    }

    @Override
    public final String toString()
    {
        return m_literal.toString();
    }
}
