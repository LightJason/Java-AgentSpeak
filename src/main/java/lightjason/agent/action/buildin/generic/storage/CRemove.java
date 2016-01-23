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

package lightjason.agent.action.buildin.generic.storage;

import lightjason.language.CCommon;
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * removes an element by name from the agent-storage
 */
public final class CRemove extends IStorage
{

    /**
     * ctor
     */
    public CRemove()
    {
        super();
    }

    /**
     * ctor
     *
     * @param p_forbidden forbidden keys
     */
    public CRemove( final String... p_forbidden )
    {
        super( Arrays.asList( p_forbidden ) );
    }

    /**
     * ctor
     *
     * @param p_fordbidden forbidden keys
     */
    public CRemove( final Collection<String> p_fordbidden )
    {
        super( p_fordbidden );
    }

    @Override
    public final int getMinimalArgumentNumber()
    {
        return 1;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final Boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        final String l_key = CCommon.getRawValue( p_argument.get( 0 ) );
        if ( m_forbidden.contains( l_key ) )
            return CBoolean.from( false );

        p_return.add( CRawTerm.from( p_context.getAgent().getStorage().remove( CCommon.getRawValue( p_argument.get( 0 ) ) ) ) );
        return CBoolean.from( true );
    }

}
