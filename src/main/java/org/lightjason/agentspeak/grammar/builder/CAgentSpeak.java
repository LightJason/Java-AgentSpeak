/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.grammar.builder;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.lightjason.agentspeak.language.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.instantiable.plan.annotation.CAtomAnnotation;
import org.lightjason.agentspeak.language.instantiable.plan.annotation.CValueAnnotation;
import org.lightjason.agentspeak.language.instantiable.plan.annotation.IAnnotation;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


/**
 * builder of agentspeak components
 */
public final class CAgentSpeak
{
    /**
     * reg pattern for matching annotation constant values
     */
    private static Pattern ANNOTATIONCONSTANT = Pattern.compile( ".+\\( .+, .+ \\)" );

    /**
     * ctor
     */
    private CAgentSpeak()
    {

    }

    /**
     * build a plan object
     *
     * @param p_trigger trigger terminal
     * @return plan
     */
    @Nullable
    public static IPlan plan( @Nonnull final TerminalNode p_trigger, @Nonnull final Stream<TerminalNode> p_annotation )
    {


        switch ( ITrigger.EType.from( p_trigger.getText() ) )
        {

            default:
                return null;
        }
    }

    @Nullable
    public static IAnnotation<?> annotation( @Nullable final TerminalNode p_annotation )
    {
        if ( Objects.isNull( p_annotation ) )
            return null;

        if ( p_annotation.getText().contains( "parallel" ) )
            return new CAtomAnnotation<>( IAnnotation.EType.PARALLEL );

        if ( p_annotation.getText().contains( "atomic" ) )
            return new CAtomAnnotation<>( IAnnotation.EType.ATOMIC );

        if ( p_annotation.getText().contains( "constant" ) )
        {
            final Matcher l_match = ANNOTATIONCONSTANT.matcher( p_annotation.getText() );
            if ( l_match.find() )
            {
                final String l_data = l_match.group( 2 ).replace( "'", "" ).replace( "\"", "" );
                try
                {
                    return new CValueAnnotation<>( IAnnotation.EType.CONSTANT, l_match.group( 1 ), CRaw.numbervalue( l_data ) );
                }
                catch ( final NumberFormatException l_exception )
                {
                    return new CValueAnnotation<>( IAnnotation.EType.CONSTANT, l_match.group( 1 ), l_data );
                }
            }
        }

        return null;
    }
}
