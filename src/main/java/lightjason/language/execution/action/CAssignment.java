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
 * # along with this program. If not, see <http://www.gnu.org/licenses/>.               #
 * ######################################################################################
 * @endcond
 */

package lightjason.language.execution.action;

import lightjason.language.CCommon;
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.IContext;
import lightjason.language.execution.IExecution;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * assignment action
 */
public class CAssignment<N, M extends IExecution> extends IBaseExecution<IVariable<N>>
{
    /**
     * right-hand argument
     */
    private final M m_righthand;

    /**
     * ctor
     *
     * @param p_lefthand left-hand argument (variable)
     * @param p_righthand right-hand argument
     */
    public CAssignment( final IVariable<N> p_lefthand, final M p_righthand )
    {
        super( p_lefthand );
        m_righthand = p_righthand;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public IFuzzyValue<Boolean> execute( final IContext<?> p_context, final Collection<ITerm> p_annotation, final Collection<ITerm> p_parameter,
                                         final Collection<ITerm> p_return
    )
    {
        final List<ITerm> l_return = new LinkedList<>();
        final IFuzzyValue<Boolean> l_rightreturn = m_righthand.execute( p_context, Collections.<ITerm>emptyList(), Collections.<ITerm>emptyList(), l_return );
        if ( ( !l_rightreturn.getValue() ) || ( l_return.isEmpty() ) )
            return CBoolean.from( false );

        ( (IVariable<N>) CCommon.replaceVariableFromContext( p_context, m_value ) ).set( (N) l_return.get( 0 ) );

        return CBoolean.from( true );
    }

    @Override
    public final int hashCode()
    {
        return m_value.hashCode() + m_righthand.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return this.hashCode() == p_object.hashCode();
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0} = {1}", m_value, m_righthand );
    }

    @Override
    @SuppressWarnings( "serial" )
    public final Set<IVariable<?>> getVariables()
    {
        return new HashSet<IVariable<?>>()
        {{
            add( m_value.clone() );
            addAll( m_righthand.getVariables() );
        }};
    }
}
