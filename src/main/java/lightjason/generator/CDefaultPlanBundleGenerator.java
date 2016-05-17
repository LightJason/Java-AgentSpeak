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

package lightjason.generator;

import lightjason.action.IAction;
import lightjason.agent.CPlanBundle;
import lightjason.agent.IPlanBundle;
import lightjason.common.CCommon;
import lightjason.configuration.CDefaultPlanBundleConfiguration;
import lightjason.configuration.IPlanBundleConfiguration;
import lightjason.grammar.CParserPlanBundle;
import lightjason.grammar.IASTVisitorPlanBundle;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * plan bundle generator
 */
public class CDefaultPlanBundleGenerator implements IPlanBundleGenerator
{
    /**
     * logger
     */
    protected static final Logger LOGGER = CCommon.getLogger( CDefaultPlanBundleGenerator.class );
    /**
     * configuration
     */
    protected final IPlanBundleConfiguration m_configuration;


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

        m_configuration = new CDefaultPlanBundleConfiguration(
            l_visitor.getPlans(),
            l_visitor.getRules(),
            l_visitor.getInitialBeliefs()
        );
    }

    /**
     * ctor
     *
     * @param p_configuration configuration
     */
    protected CDefaultPlanBundleGenerator( final IPlanBundleConfiguration p_configuration )
    {
        m_configuration = p_configuration;
    }

    @Override
    public IPlanBundle generate( final Object... p_data ) throws Exception
    {
        LOGGER.info( MessageFormat.format( "generate planbundle: {0}", Arrays.toString( p_data ) ).trim() );
        return new CPlanBundle( m_configuration );
    }

    @Override
    public final Stream<IPlanBundle> generate( final int p_number, final Object... p_data ) throws Exception
    {
        return IntStream.range( 0, p_number )
                    .parallel()
                    .mapToObj( i -> {
                        try
                        {
                            return this.generate( p_data );
                        }
                        catch ( final Exception l_exception )
                        {
                            LOGGER.warning( MessageFormat.format( "error with message: {0}", l_exception ) );
                            return null;
                        }
                    } )
                    .filter( i -> i != null );
    }

}
