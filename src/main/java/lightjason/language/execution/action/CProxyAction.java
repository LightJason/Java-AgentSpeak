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
import lightjason.language.execution.IContext;
import lightjason.language.execution.IExecution;
import lightjason.language.execution.fuzzy.CBoolean;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * proxy action to encapsulate all actions
 */
public final class CProxyAction implements IExecution
{
    /**
     * inner execution structure in reverse order
     */
    private final List<IExecution> m_execution = new LinkedList<>();


    /**
     * ctor
     *
     * @param p_literal literal
     * @param p_actions actions definition
     */
    public CProxyAction( final ILiteral p_literal, final Map<CPath, IAction> p_actions )
    {
        this.createCallerStack( p_literal, p_actions );
    }

    @Override
    public final String toString()
    {
        return m_execution.toString();
    }

    @Override
    public CBoolean execute( final IContext<?> p_context, final Collection<ILiteral> p_annotation, final Collection<ITerm> p_parameter,
            final Collection<ITerm> p_return
    )
    {
        // foo( bar(3,4), blub( xxx(3,4) ) ) -> xxx, blub, bar, foo

        final List<ITerm> l_argumentstack = new LinkedList<>();
        m_execution.stream().forEach( i -> i.execute( p_context, p_annotation, l_argumentstack, l_argumentstack ) );

        return CBoolean.from( true );
    }

    @SuppressWarnings( "unchecked" )
    private void createCallerStack( final ILiteral p_literal, final Map<CPath, IAction> p_actions )
    {
        final IExecution l_action = p_actions.get( p_literal.getFQNFunctor() );
        if ( l_action == null )
            throw new CIllegalArgumentException( CCommon.getLanguageString( this, "actionunknown", p_literal ) );
        m_execution.add( l_action );

        p_literal.getValues().entries().stream().forEach( i -> {

            if ( i.getValue() instanceof ILiteral )
                this.createCallerStack( (ILiteral) i.getValue(), p_actions );

        } );
    }
}
