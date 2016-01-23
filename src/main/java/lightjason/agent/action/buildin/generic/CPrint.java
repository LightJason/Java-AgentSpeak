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

package lightjason.agent.action.buildin.generic;

import lightjason.agent.action.buildin.IBuildinAction;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;


/**
 * action for sum of elements
 */
public final class CPrint extends IBuildinAction
{

    /**
     * output stream
     */
    private final PrintStream m_stream;
    /**
     * argument seperator
     */
    private final String m_seperator;

    /**
     * ctor
     *
     * @note generates an output stream to system.out
     */
    public CPrint()
    {
        this( "   ", System.out );
    }

    /**
     * ctor
     *
     * @param p_seperator argument seperator
     * @param p_stream any byte output stream
     */
    public CPrint( final String p_seperator, final PrintStream p_stream )
    {
        super( 2 );
        m_stream = p_stream;
        m_seperator = p_seperator;
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
        m_stream.println(
                MessageFormat.format(
                        "{0}{1}",
                        StringUtils.join( p_argument.stream().map( i -> i.toString() ).collect( Collectors.toList() ), m_seperator ),
                        ( p_annotation.isEmpty()
                          ? ""
                          : MessageFormat.format(
                                  "{0}{1}",
                                  m_seperator,
                                  StringUtils.join( p_annotation.stream().map( i -> i.toString() ).collect( Collectors.toList() ), m_seperator )
                          )
                        )
                )
        );
        return CBoolean.from( true );
    }

}
