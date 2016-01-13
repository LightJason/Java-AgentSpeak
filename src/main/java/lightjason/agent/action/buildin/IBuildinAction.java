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

package lightjason.agent.action.buildin;

import lightjason.agent.action.IBaseAction;
import lightjason.common.CPath;
import lightjason.language.CCommon;
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.IContext;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;


/**
 * base class of build-in actions for setting name
 * by package/classname (without prefix character)
 */
public abstract class IBuildinAction extends IBaseAction
{
    /**
     * action name
     */
    private final CPath m_name;

    /**
     * ctor
     */
    protected IBuildinAction()
    {
        this( 2 );
    }

    /**
     * ctor
     *
     * @param p_length length of package parts
     */
    protected IBuildinAction( final int p_length )
    {
        final List<String> l_names = Arrays.asList( this.getClass().getCanonicalName().split( "\\." ) );
        l_names.set( l_names.size() - 1, l_names.get( l_names.size() - 1 ).substring( 1 ) );

        m_name = new CPath( l_names.subList( Math.max( 0, l_names.size() - p_length ), l_names.size() ) ).toLower();
    }


    @Override
    public final CPath getName()
    {
        return m_name;
    }

    /**
     * creates a filtered term stream with variables and raw terms
     *
     * @param p_context context variable
     * @param p_termlist term list
     * @return term stream
     */
    protected final static Stream<ITerm> getTermStream( final IContext<?> p_context, final Collection<ITerm> p_termlist )
    {
        return CCommon.replaceVariableFromContext( p_context, p_termlist ).stream().filter(
                i -> ( i instanceof IVariable<?> ) || ( i instanceof CRawTerm<?> ) );
    }
}
