/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.buildin.generic.type;

import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.List;
import java.util.stream.Collectors;


/**
 * action to check if a type is a class.
 * The first argument defines a full-qualified Java
 * class name, and all other arguments are checked if
 * that are instances of this class, the action fails
 * if one of the arguments are not an instance of this
 * class or the class does not exist
 *
 * @code generic/type( "java.lang.String", X, Y, Z ); @endcode
 */
public final class CIs extends IBuildinAction
{

    /**
     * ctor
     */
    public CIs()
    {
        super( 3 );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 2;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return
    )
    {
        final List<ITerm> l_arguments = CCommon.flatcollection( p_argument ).collect( Collectors.toList() );
        final Class<?> l_class;
        try
        {
            l_class = Class.forName( l_arguments.get( 0 ).<String>raw() );
        }
        catch ( final ClassNotFoundException l_exception )
        {
            return CFuzzyValue.from( false );
        }

        return CFuzzyValue.from(
            l_arguments.stream()
                   .skip( 1 )
                   .map( ITerm::raw )
                   .allMatch( i -> l_class.isAssignableFrom( i.getClass() ) )
        );
    }

}
