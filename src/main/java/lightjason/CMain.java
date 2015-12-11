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

package lightjason;

import lightjason.agent.IAgent;
import lightjason.agent.action.IAction;
import lightjason.agent.action.IBaseAction;
import lightjason.agent.generator.CDefaultGenerator;
import lightjason.common.CPath;
import lightjason.language.ILiteral;
import lightjason.language.ITerm;
import lightjason.language.plan.fuzzy.CBoolean;

import java.io.FileInputStream;
import java.util.Collection;
import java.util.HashSet;


public final class CMain
{

    /**
     * main
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args ) throws Exception
    {
        new CDefaultGenerator( new FileInputStream( p_args[0] ), new HashSet<IAction>()
        {{
            add( new CPrint() );
            add( new CSetProperty() );
        }} ).generate();
    }

    /**
     * test print action
     */
    private final static class CPrint extends IBaseAction
    {
        /**
         * static name of the action
         **/
        private static final CPath c_name = CPath.from( "print" );


        @Override
        public final CBoolean execute( final IAgent p_agent, final Collection<ITerm> p_parameter, final Collection<ILiteral> p_annotation
        )
        {
            System.out.println( "---> print : " + p_parameter + "      " + p_annotation + "      " + p_agent );
            return CBoolean.from( true );
        }

        @Override
        public final CPath getName()
        {
            return c_name;
        }
    }

    /**
     * test setproperty action
     */
    private final static class CSetProperty extends IBaseAction
    {
        /** static name of the action **/
        private static final CPath c_name = CPath.from( "setProperty" );

        @Override
        public final CPath getName()
        {
            return c_name;
        }

        @Override
        public final CBoolean execute( final IAgent p_agent, final Collection<ITerm> p_parameter, final Collection<ILiteral> p_annotation
        )
        {
            System.out.println( "---> setProperty : " + p_parameter + "      " + p_annotation + "      " + p_agent );
            return CBoolean.from( true );
        }

    }
}