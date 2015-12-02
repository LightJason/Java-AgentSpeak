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

package lightjason.language.plan.action;

import lightjason.beliefbase.IBeliefBaseMask;
import lightjason.common.CCommon;
import lightjason.language.ILiteral;
import lightjason.language.plan.IPlan;

import java.text.MessageFormat;
import java.util.Set;


/**
 * belief action
 */
public final class CBeliefAction extends IAction
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

    /**
     * @todo change literal event is missing
     */
    @Override
    public boolean evaluate( final IBeliefBaseMask p_beliefbase, final Set<IPlan> p_runningplan )
    {
        switch ( m_action )
        {
            case Add:
                p_beliefbase.add( m_literal.getFunctorPath(), m_literal );
                break;

            case Delete:
                p_beliefbase.remove( m_literal.getFunctorPath(), m_literal );
                break;

            default:
                throw new IllegalArgumentException( CCommon.getLanguageString( this, "unknownaction", m_action ) );
        }

        return true;
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0}{1}", m_action, m_literal );
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
        private EAction( final String p_name )
        {
            m_name = p_name;
        }

        @Override
        public String toString()
        {
            return m_name;
        }
    }
}
