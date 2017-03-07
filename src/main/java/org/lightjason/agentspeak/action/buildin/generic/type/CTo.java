/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
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
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.List;
import java.util.stream.Collectors;


/**
 * action to cast any java object type.
 * The action casts any value to the given full-qualified
 * class name in the first argument, the action fails on
 * casting errors
 *
 * @code [A|B] = generic/type/to( "java.lang.String", X, Y ); @endcode
 */
public final class CTo extends IBuildinAction
{

    /**
     * ctor
     */
    public CTo()
    {
        super( 3 );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 2;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        final List<ITerm> l_arguments = CCommon.flatcollection( p_argument ).collect( Collectors.toList() );

        return CFuzzyValue.from(
            l_arguments.stream()
               .skip( 1 )
               .map( ITerm::raw )
               .allMatch( i -> CTo.cast( l_arguments.get( 0 ).<String>raw(), i, p_return ) )
        );

    }


    /**
     * cast any object into
     * another type
     *
     * @param p_class full-qualified class name
     * @param p_object object
     * @param p_return list with return terms
     * @return successfull flag
     */
    private static boolean cast( final String p_class, final Object p_object, final List<ITerm> p_return )
    {
        try
        {
            p_return.add(
                CRawTerm.from(
                    Class.forName( p_class ).cast( p_object )
                )
            );
            return true;
        }
        catch ( final ClassNotFoundException l_exception )
        {
            return false;
        }
    }

}
