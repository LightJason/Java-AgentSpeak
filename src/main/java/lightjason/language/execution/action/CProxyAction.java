/**
 * @cond LICENSE
 * ######################################################################################
 * # GPL License                                                                        #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015, Philipp Kraus (philipp.kraus@tu-clausthal.de)                  #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU General Public License as                            #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU General Public License for more details.                                       #
 * #                                                                                    #
 * # You should have received a copy of the GNU General Public License                  #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package lightjason.language.execution.action;

import lightjason.agent.action.IAction;
import lightjason.common.CCommon;
import lightjason.common.CPath;
import lightjason.error.CIllegalArgumentException;
import lightjason.language.ILiteral;
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.IContext;
import lightjason.language.execution.IExecution;
import lightjason.language.execution.fuzzy.CBoolean;
import org.antlr.v4.runtime.misc.Triple;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * proxy action to encapsulate all actions
 */
public final class CProxyAction implements IExecution
{
    /**
     * inner execution structure in reverse order with number of argmuments
     */
    private final List<Triple<IExecution, Integer, Integer>> m_execution = new LinkedList<>();
    /**
     * initial / inner arguments
     */
    private final List<ITerm> m_initialarguments = new LinkedList<>();


    /**
     * ctor
     *
     * @param p_literal literal
     * @param p_actions actions definition
     */
    public CProxyAction( final ILiteral p_literal, final Map<CPath, IAction> p_actions )
    {
        this.createCaller( p_literal, p_actions );
    }

    @Override
    public final String toString()
    {
        return m_execution.toString() + "\t" + m_initialarguments;
    }

    @Override
    public CBoolean execute( final IContext<?> p_context, final Collection<ILiteral> p_annotation, final Collection<ITerm> p_parameter,
            final Collection<ITerm> p_return
    )
    {
        // foo( bar(3,4), blub( xxx(3,4) ) ) -> xxx, blub, bar, foo

        // build initial argument stack for execution (replace variables with local stack definition)
        final List<ITerm> l_argumentstack = m_initialarguments.stream().map( i -> {

            final IVariable<?> l_variable = p_context.getInstanceVariables().get( i.getFQNFunctor() );
            if ( l_variable != null )
                return l_variable;

            return i;

        } ).collect( Collectors.toCollection( LinkedList::new ) );

        // execute action and build sublists of argument stack
        m_execution.stream().forEach( i -> {


            i.a.execute( p_context, p_annotation, l_argumentstack, l_argumentstack );
        } );


        return CBoolean.from( true );
    }

    /**
     * create execution stack of function and arguments
     *
     * @param p_literal literal
     * @param p_actions map with action definition
     */
    @SuppressWarnings( "unchecked" )
    private void createCaller( final ILiteral p_literal, final Map<CPath, IAction> p_actions )
    {
        final IAction l_action = p_actions.get( p_literal.getFQNFunctor() );
        if ( l_action == null )
            throw new CIllegalArgumentException( CCommon.getLanguageString( this, "actionunknown", p_literal ) );

        // check argument numbers
        if ( l_action.getMinimalArgumentNumber() > l_action.getMaximalArgumentNumber() )
            throw new CIllegalArgumentException(
                    CCommon.getLanguageString( this, "argumentminmax", p_literal, l_action.getMinimalArgumentNumber(), l_action.getMaximalArgumentNumber() ) );

        // check number of arguments
        if ( !( ( l_action.getMinimalArgumentNumber() <= p_literal.getValues().size() ) &&
                ( p_literal.getValues().size() <= l_action.getMaximalArgumentNumber() ) ) )
            throw new CIllegalArgumentException(
                    CCommon.getLanguageString( this, "argumentnumber", p_literal, l_action.getMinimalArgumentNumber(), l_action.getMaximalArgumentNumber() ) );

        // create execution definition as stack definition
        m_execution.add( 0, new Triple<IExecution, Integer, Integer>( l_action, l_action.getMinimalArgumentNumber(), l_action.getMaximalArgumentNumber() ) );
        p_literal.getValues().entries().stream().forEach( i -> {

            if ( i.getValue() instanceof ILiteral )
                this.createCaller( (ILiteral) i.getValue(), p_actions );
            else
                m_initialarguments.add( i.getValue() );

        } );
    }
}
