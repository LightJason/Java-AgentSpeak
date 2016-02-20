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

import lightjason.common.CCommon;
import lightjason.language.ILiteral;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.text.MessageFormat;
import java.util.List;


/**
 * belief action
 *
 * @todo add unification for variables
 */
public final class CBeliefAction extends IBaseExecution<ILiteral>
{
    /**
     * running action
     */
    private final EAction m_action;

    /**
     * ctor
     *
     * @param p_literal literal
     * @param p_action action
     */
    public CBeliefAction( final ILiteral p_literal, final EAction p_action )
    {
        super( p_literal );
        m_action = p_action;
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0}{1}", m_action, m_value );
    }

    /**
     * @todo change literal event is missing
     * @todo disable actions
     */
    @Override
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        switch ( m_action )
        {
            case Add:
                p_context.getAgent().getBeliefBase().add( m_value );
                break;

            case Delete:
                p_context.getAgent().getBeliefBase().remove( m_value );
                break;

            default:
                throw new IllegalArgumentException( CCommon.getLanguageString( this, "unknownaction", m_action ) );
        }

        return CBoolean.from( true );
    }

    /**
     * belief action definition
     */
    public enum EAction
    {
        Add( "+" ),
        Delete( "-" ),
        Change( "-+" );

        /**
         * name
         */
        private final String m_name;

        /**
         * ctor
         *
         * @param p_name string represenation
         */
        EAction( final String p_name )
        {
            m_name = p_name;
        }

        @Override
        public final String toString()
        {
            return m_name;
        }
    }
}
