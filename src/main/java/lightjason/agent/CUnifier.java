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

package lightjason.agent;

import lightjason.language.ILiteral;
import lightjason.language.execution.IContext;
import lightjason.language.execution.IUnifier;
import lightjason.language.execution.expression.IExpression;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.util.Collection;
import java.util.stream.Collectors;


/**
 * unification algorithm
 */
public final class CUnifier implements IUnifier
{
    @Override
    public final IFuzzyValue<Boolean> parallelunify( final IContext<?> p_context, final ILiteral p_literal, final IExpression p_expression )
    {
        final Collection<ILiteral> l_result = this.search( p_context.getAgent(), p_literal );
        //System.out.println("----> " + l_result);
        return CBoolean.from( !l_result.isEmpty() );
    }

    @Override
    public final IFuzzyValue<Boolean> sequentialunify( final IContext<?> p_context, final ILiteral p_literal, final IExpression p_expression
    )
    {
        final Collection<ILiteral> l_result = this.search( p_context.getAgent(), p_literal );
        //System.out.println("----> " + l_result);
        return CBoolean.from( !l_result.isEmpty() );
    }

    /**
     * search all relevant literals within the agent beliefbase
     *
     * @param p_agent agent
     * @param p_literal literal search
     * @return collection of found literals or null if no literal is found
     *
     * @note try to search all literals which are exactly match
     * (values and annotations) the literal structure if no
     * literal is found, search runs again without annotation
     * definition
     */
    private Collection<ILiteral> search( final IAgent p_agent, final ILiteral p_literal )
    {
        // try to get a full match
        final Collection<ILiteral> l_fullmatch = p_agent.getBeliefBase()
                                                        .parallelStream( p_literal.isNegated(), p_literal.getFQNFunctor() )
                                                        .filter( i -> i.structurehash() == p_literal.structurehash() )
                                                        .collect( Collectors.toList() );

        if ( !l_fullmatch.isEmpty() )
            return l_fullmatch;

        // try to get a part match (without annotation)
        final Collection<ILiteral> l_partmatch = p_agent.getBeliefBase()
                                                        .parallelStream( p_literal.isNegated(), p_literal.getFQNFunctor() )
                                                        .filter( i -> i.valuestructurehash() == p_literal.valuestructurehash() )
                                                        .collect( Collectors.toList() );

        if ( !l_partmatch.isEmpty() )
            return l_partmatch;

        // try to match any possible literal by its functor
        final Collection<ILiteral> l_any = p_agent.getBeliefBase()
                                                  .parallelStream( p_literal.isNegated(), p_literal.getFQNFunctor() )
                                                  .collect( Collectors.toList() );

        return l_any;
    }
}
