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

package lightjason.agent.generator;

import lightjason.agent.IPlanBundle;
import lightjason.agent.action.IAction;
import lightjason.grammar.CParserPlanBundle;
import lightjason.grammar.IASTVisitorPlanBundle;

import java.io.InputStream;
import java.util.Set;


/**
 * plan bundle generator
 */
public class CDefaultPlanBundleGenerator implements IPlanBundleGenerator
{

    /**
     * ctor
     *
     * @param p_stream input stream
     * @param p_actions set with actions
     * @throws Exception thrown on error
     */
    public CDefaultPlanBundleGenerator( final InputStream p_stream, final Set<IAction> p_actions ) throws Exception
    {
        final IASTVisitorPlanBundle l_visitor = new CParserPlanBundle( p_actions ).parse( p_stream );
    }


    @Override
    public <T> IPlanBundle generate( final T... p_data ) throws Exception
    {
        return null;
    }

}
