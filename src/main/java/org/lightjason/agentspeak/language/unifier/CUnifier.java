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

package org.lightjason.agentspeak.language.unifier;

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.IRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * unification algorithm
 */
public final class CUnifier implements IUnifier
{
    /**
     * hash-based unifier algorithm
     */
    private final IAlgorithm m_hashbased;
    /**
     * recursive unifier algorithm
     */
    private final IAlgorithm m_recursive;

    /**
     * ctor
     */
    public CUnifier()
    {
        this( new CHash(), new CRecursive() );
    }

    /**
     * ctor
     *
     * @param p_hashbased hash-based unification algorithm
     * @param p_recursive recursive-based unification algorithm
     */
    public CUnifier( @Nonnull final IUnifier.IAlgorithm p_hashbased, @Nonnull final IUnifier.IAlgorithm p_recursive )
    {
        m_hashbased = p_hashbased;
        m_recursive = p_recursive;
    }

    // --- inheritance & context modification ------------------------------------------------------------------------------------------------------------------

    @Nonnull
    @Override
    public Set<IVariable<?>> unify( @Nonnull final ILiteral p_source, @Nonnull final ILiteral p_target )
    {
        final Set<IVariable<?>> l_result = new HashSet<>();

        // try to unifier exact or if not possible by recursive on the value set
        if ( !(
            p_target.structurehash() == p_source.structurehash()
            ? m_hashbased.unify(
                l_result,
                CCommon.flattenrecursive( p_source.orderedvalues() ),
                CCommon.flattenrecursive( p_target.orderedvalues() )
            )
            : m_recursive.unify(
                l_result,
                p_source.orderedvalues(),
                p_target.orderedvalues()
            ) ) )
            return Collections.emptySet();

        return l_result;
    }

    @Nonnull
    @Override
    public IFuzzyValue<Boolean> unify( @Nonnull final IContext p_context, @Nonnull final ILiteral p_literal, final long p_variables,
                                       @Nonnull final IExecution p_expression, final boolean p_parallel
    )
    {
        // get all possible variables
        final List<Set<IVariable<?>>> l_variables = this.variables( p_context.agent(), p_literal, p_variables );
        if ( l_variables.isEmpty() )
            return CFuzzyValue.of( false );

        // otherwise the expression must be checked, first match will be used
        final Set<IVariable<?>> l_result = CCommon.parallelstream( l_variables.stream(), p_parallel )
                                                  .filter( i -> evaluateexpression( p_context, p_expression, i ) )
                                                  .findFirst()
                                                  .orElse( Collections.emptySet() );

        // if no match
        if ( l_result.isEmpty() )
            return CFuzzyValue.of( false );

        CCommon.updatecontext( p_context, l_result.stream() );
        return CFuzzyValue.of( true );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        return m_hashbased.hashCode() ^ m_recursive.hashCode();
    }


    @Override
    public boolean equals( final Object p_object )
    {
        return p_object instanceof IUnifier && this.hashCode() == p_object.hashCode();
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "hash-based unification: {0} / recursive unification: {1}", m_hashbased, m_recursive );
    }

    /**
     * evaluate expression
     *
     * @param p_context execution context (will be duplicated)
     * @param p_expression expression
     * @param p_variables current variables
     * @return boolean for correct evaluation
     */
    private static boolean evaluateexpression( final IContext p_context, final IExecution p_expression, final Set<IVariable<?>> p_variables )
    {
        // evalute expression result first, after that evaluate return arguments
        final List<ITerm> l_return = CCommon.argumentlist();
        final IFuzzyValue<Boolean> l_result =
            p_expression.execute(
                false,
                CCommon.updatecontext(
                    p_context.duplicate(),
                    p_variables.stream()
                ),
                Collections.emptyList(),
                l_return
            );

        return p_context.agent()
                        .fuzzy()
                        .getValue()
                        .defuzzify( l_result )
               && l_return.size() == 1
               && l_return.get( 0 ).<IRawTerm<?>>term().valueassignableto( Boolean.class )
               && l_return.get( 0 ).<Boolean>raw();
    }

    /**
     * search all relevant literals within the agent beliefbase and unifies the variables
     *
     * @param p_agent agent
     * @param p_literal literal search
     * @param p_variablenumber number of unified variables
     * @return list of literal sets
     **/
    private List<Set<IVariable<?>>> variables( final IAgent<?> p_agent, final ILiteral p_literal, final long p_variablenumber )
    {
        return p_agent.beliefbase()
                      .stream( p_literal.negated(), p_literal.fqnfunctor() )
                      .filter( i -> i.emptyValues() == p_literal.emptyValues() )
                      .map( i -> this.unify( i, Objects.requireNonNull( p_literal.deepcopy().<ILiteral>raw() ) ) )
                      .filter( i -> p_variablenumber == i.size() )
                      .collect( Collectors.toList() );
    }
}
