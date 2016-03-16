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

import com.codepoetics.protonpack.StreamUtils;
import lightjason.error.CIllegalStateException;
import lightjason.language.CCommon;
import lightjason.language.ILiteral;
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.IContext;
import lightjason.language.execution.IUnifier;
import lightjason.language.execution.expression.IExpression;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * unification algorithm
 */
public final class CUnifier implements IUnifier
{
    @Override
    public final IFuzzyValue<Boolean> parallelunify( final IContext<?> p_context, final ILiteral p_literal, final IExpression p_expression )
    {
        return CBoolean.from( false );
    }

    @Override
    public final IFuzzyValue<Boolean> sequentialunify( final IContext<?> p_context, final ILiteral p_literal, final IExpression p_expression
    )
    {
        // get all possible variables
        final List<Set<IVariable<?>>> l_variables = EUnify.EXACT.unify( p_context.getAgent(), p_literal );
        if ( l_variables.isEmpty() )
            l_variables.addAll( EUnify.VALUEEXACT.unify( p_context.getAgent(), p_literal ) );
        if ( l_variables.isEmpty() )
            l_variables.addAll( EUnify.ANY.unify( p_context.getAgent(), p_literal ) );
        if ( l_variables.isEmpty() )
            return CBoolean.from( false );

        // if no expression exists, returns the first unified structure
        if ( p_expression == null )
        {
            this.updateContext( p_context, l_variables.get( 0 ).parallelStream() );
            return CBoolean.from( true );
        }

        // otherwise the expression must be checked, first match will be used
        final Set<IVariable<?>> l_result = l_variables.stream()
                                                      .filter( i -> {
                                                          final List<ITerm> l_return = new LinkedList<>();
                                                          final IFuzzyValue<Boolean> x = p_expression.execute(
                                                                  this.updateContext(
                                                                          p_context.duplicate(),
                                                                          i.parallelStream()
                                                                  ),
                                                                  false,
                                                                  Collections.<ITerm>emptyList(),
                                                                  l_return,
                                                                  Collections.<ITerm>emptyList()
                                                          );
                                                          return ( l_return.size() == 1 ) && ( CCommon.<Boolean, ITerm>getRawValue( l_return.get( 0 ) ) );
                                                      } )
                                                      .findFirst()
                                                      .orElse( Collections.<IVariable<?>>emptySet() );

        // if no match
        if ( l_result.isEmpty() )
            return CBoolean.from( false );

        this.updateContext( p_context, l_result.parallelStream() );
        return CBoolean.from( true );
    }

    /**
     * updates within an instance context all variables with the unified values
     *
     * @param p_context context
     * @param p_unifiedvariables unified variables as stream
     * @return context reference
     */
    private IContext<?> updateContext( final IContext<?> p_context, final Stream<IVariable<?>> p_unifiedvariables )
    {
        p_unifiedvariables.forEach( i -> p_context.getInstanceVariables().get( i.getFQNFunctor() ).set( i.getTyped() ) );
        return p_context;
    }

    /**
     * enum for unifying
     */
    private enum EUnify
    {
        EXACT,
        VALUEEXACT,
        ANY;


        /**
         * search all relevant literals within the agent beliefbase and unifies the variables
         *
         * @param p_agent agent
         * @param p_literal literal search
         * @return list of literal sets
         * @todo recursive descend of ordered / annotation values
         **/
        public List<Set<IVariable<?>>> unify( final IAgent p_agent, final ILiteral p_literal )
        {
            switch ( this )
            {
                case EXACT:
                    return p_agent.getBeliefBase()
                                  .parallelStream( p_literal.isNegated(), p_literal.getFQNFunctor() )
                                  .filter( i -> ( i.valuehash() == p_literal.valuehash() ) &&
                                                ( i.annotationhash() == p_literal.annotationhash() )
                                  )
                                  .map( i -> {
                                      final ILiteral l_literal = (ILiteral) p_literal.deepcopy();
                                      return Stream.concat(
                                              this.unifyexact( l_literal.orderedvalues(), i.orderedvalues() ),
                                              this.unifyexact( l_literal.annotations(), i.annotations() )
                                      ).collect( Collectors.toSet() );
                                  } )
                                  .filter( i -> !i.isEmpty() )
                                  .collect( Collectors.toList() );

                case VALUEEXACT:
                    return p_agent.getBeliefBase()
                                  .parallelStream( p_literal.isNegated(), p_literal.getFQNFunctor() )
                                  .filter( i -> i.valuehash() == p_literal.valuehash() )
                                  .map( i -> {
                                      final ILiteral l_literal = (ILiteral) p_literal.deepcopy();
                                      return Stream.concat(
                                              this.unifyexact( l_literal.orderedvalues(), i.orderedvalues() ),
                                              this.unifyfuzzy( l_literal.annotations(), i.annotations() )
                                      ).collect( Collectors.toSet() );
                                  } )
                                  .filter( i -> !i.isEmpty() )
                                  .collect( Collectors.toList() );


                case ANY:
                    return p_agent.getBeliefBase()
                                  .parallelStream( p_literal.isNegated(), p_literal.getFQNFunctor() )
                                  .map( i -> {
                                      final ILiteral l_literal = (ILiteral) p_literal.deepcopy();
                                      return Stream.concat(
                                              this.unifyexact( l_literal.orderedvalues(), i.orderedvalues() ),
                                              this.unifyfuzzy( l_literal.annotations(), i.annotations() )
                                      ).collect( Collectors.toSet() );
                                  } )
                                  .filter( i -> !i.isEmpty() )
                                  .collect( Collectors.toList() );

                default:
                    throw new CIllegalStateException( lightjason.common.CCommon.getLanguageString( this, "unificationnotexists" ) );

            }
        }

        /**
         * runs the exact (hash equal) unifiying process
         *
         * @param p_target term stream of targets (literal which stores the variables as instance)
         * @param p_source term stream of sources
         * @return list with unified variables
         *
         * @tparam T term type
         */
        @SuppressWarnings( "unchecked" )
        private <T> Stream<IVariable<?>> unifyexact( final Stream<T> p_target, final Stream<T> p_source )
        {
            return StreamUtils.zip(
                    p_source,
                    p_target,
                    ( s, t ) -> t instanceof IVariable<?> ? ( (IVariable<Object>) t ).set( s ) : null
            )
                              .filter( i -> i instanceof IVariable<?> )
                              .map( i -> (IVariable<?>) i );
        }

        /**
         * runs the fuzzy (hash unequal) unifiying process
         *
         * @param p_target term stream of targets (literal which stores the variables as instance)
         * @param p_source term stream of sources
         * @return list with unified variables
         *
         * @tparam T term type
         * @bug incomplete
         */
        private <T> Stream<IVariable<?>> unifyfuzzy( final Stream<T> p_target, final Stream<T> p_source )
        {
            return Stream.of();
        }
    }

}
