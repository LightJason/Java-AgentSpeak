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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
        final Collection<ILiteral> l_result = this.search( p_context.getAgent(), p_literal );
        //System.out.println("----> " + l_result);
        return CBoolean.from( !l_result.isEmpty() );
    }

    @Override
    public final IFuzzyValue<Boolean> sequentialunify( final IContext<?> p_context, final ILiteral p_literal, final IExpression p_expression
    )
    {
        // get possible literals
        final List<ILiteral> l_result = this.search( p_context.getAgent(), p_literal );
        if ( l_result.isEmpty() )
            return CBoolean.from( false );


        // on empty expression, first match will be used
        if ( p_expression == null )
        {
            this.updateContext( p_context, this.unify( l_result.get( 0 ), p_literal ) );
            return CBoolean.from( true );
        }

        // otherwise we try to evaluate the expression
        // @todo not working
        l_result.stream().map( i -> {
            final List<ITerm> l_return = new LinkedList<>();
            p_expression.execute(
                    this.updateContext( p_context, this.unify( i, p_literal ) ), false, Collections.<ITerm>emptyList(), l_return,
                    Collections.<ITerm>emptyList()
            );
            return ( l_return.size() == 1 ) && ( CCommon.<Boolean, ITerm>getRawValue( l_return.get( 0 ) ) );
        } ).findFirst();

        return CBoolean.from( !l_result.isEmpty() );
    }

    /**
     * updates within an instance context all variables with the unified values
     *
     * @param p_context context
     * @param p_unifiedvariables unified variables
     * @return context reference
     */
    private IContext<?> updateContext( final IContext<?> p_context, final List<IVariable<?>> p_unifiedvariables )
    {
        p_unifiedvariables.parallelStream().forEach( i -> p_context.getInstanceVariables().get( i.getFQNFunctor() ).set( i.getTyped() ) );
        return p_context;
    }


    /**
     * runs the unifiying process
     *
     * @param p_source source literal (literal which stores the data)
     * @param p_target target literal (literal which stores the variables)
     * @return list with unified variables
     */
    @SuppressWarnings( "unchecked" )
    private List<IVariable<?>> unify( final ILiteral p_source, final ILiteral p_target )
    {
        return StreamUtils.zip(
                p_source.orderedvalues(),
                ( (ILiteral) p_target.deepcopy() ).orderedvalues(),
                ( s, t ) -> t instanceof IVariable<?> ? ( (IVariable<Object>) t ).set( s ) : null
        ).filter( i -> i instanceof IVariable<?> ).collect( Collectors.toList() );
    }


    /**
     * searches the literals within the agent beliefbase
     *
     * @param p_agent agent
     * @param p_literal searched literal
     * @return list with possible literals
     */
    @Deprecated
    private List<ILiteral> search( final IAgent p_agent, final ILiteral p_literal )
    {
        final List<ILiteral> l_full = p_agent.getBeliefBase()
                                             .parallelStream( p_literal.isNegated(), p_literal.getFQNFunctor() )
                                             .filter( i -> ( i.valuehash() == p_literal.valuehash() ) &&
                                                           ( i.annotationhash() == p_literal.annotationhash() ) )
                                             .collect( Collectors.toList() );
        if ( !l_full.isEmpty() )
            return l_full;

        final List<ILiteral> l_part = p_agent.getBeliefBase()
                                             .parallelStream( p_literal.isNegated(), p_literal.getFQNFunctor() )
                                             .filter( i -> i.valuehash() == p_literal.valuehash() )
                                             .collect( Collectors.toList() );
        if ( !l_part.isEmpty() )
            return l_part;

        return p_agent.getBeliefBase()
                      .parallelStream( p_literal.isNegated(), p_literal.getFQNFunctor() )
                      .collect( Collectors.toList() );
    }


    /**
     * enum for unifying
     */
    private enum EUnify
    {
        EXACT,
        VALUES,
        ANY;


        /**
         * search all relevant literals within the agent beliefbase
         *
         * @param p_agent agent
         * @param p_literal literal search
         * @return stream of literals
         **/
        public Stream<ILiteral> getStream( final IAgent p_agent, final ILiteral p_literal )
        {
            switch ( this )
            {
                case EXACT:
                    return p_agent.getBeliefBase()
                                  .parallelStream( p_literal.isNegated(), p_literal.getFQNFunctor() )
                                  .filter( i -> ( i.valuehash() == p_literal.valuehash() ) &&
                                                ( i.annotationhash() == p_literal.annotationhash() ) );

                case VALUES:
                    return p_agent.getBeliefBase()
                                  .parallelStream( p_literal.isNegated(), p_literal.getFQNFunctor() )
                                  .filter( i -> i.valuehash() == p_literal.valuehash() );

                case ANY:
                    return p_agent.getBeliefBase()
                                  .parallelStream( p_literal.isNegated(), p_literal.getFQNFunctor() );

                default:
                    throw new CIllegalStateException();

            }
        }


    }

}
