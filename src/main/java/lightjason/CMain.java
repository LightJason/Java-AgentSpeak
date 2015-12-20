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

import lightjason.agent.action.IAction;
import lightjason.agent.action.IBaseAction;
import lightjason.agent.generator.CDefaultAgentGenerator;
import lightjason.common.CPath;
import lightjason.language.CRawTerm;
import lightjason.language.ILiteral;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.util.Collection;
import java.util.HashSet;


@SuppressWarnings( "serial" )
public final class CMain
{

    /**
     * main
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args ) throws Exception
    {
        new CDefaultAgentGenerator( new FileInputStream( p_args[0] ), new HashSet<IAction>()
        {{
            add( new CPrint() );
            add( new CSetProperty() );
            add( new CMin() );
        }} ).generate().call();
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
        public final CBoolean execute( final IContext<?> p_context, final Collection<ILiteral> p_annotation, final Collection<ITerm> p_parameter,
                final Collection<ITerm> p_return
        )
        {
            System.out.println( StringUtils.join( p_parameter, ", " ) );
            return CBoolean.from( true );
        }

        @Override
        public final CPath getName()
        {
            return c_name;
        }

        @Override
        public final int getMinimalArgumentNumber()
        {
            return 0;
        }

    }

    /**
     * test setproperty action
     */
    private final static class CSetProperty extends IBaseAction
    {
        /**
         * static name of the action
         **/
        private static final CPath c_name = CPath.from( "setProperty" );

        @Override
        public final CPath getName()
        {
            return c_name;
        }

        @Override
        public final int getMinimalArgumentNumber()
        {
            return 3;
        }

        @Override
        public final CBoolean execute( final IContext<?> p_context, final Collection<ILiteral> p_annotation, final Collection<ITerm> p_parameter,
                final Collection<ITerm> p_return
        )
        {
            //System.out.println( "---> setProperty : " + p_parameter + "      " + p_annotation );
            return CBoolean.from( true );
        }

    }

    /**
     * test min action
     */
    private final static class CMin extends IBaseAction
    {
        /**
         * static name of the action
         **/
        private static final CPath c_name = CPath.from( "min" );

        @Override
        public CPath getName()
        {
            return c_name;
        }

        @Override
        public final int getMinimalArgumentNumber()
        {
            return 1;
        }

        @Override
        public CBoolean execute( final IContext<?> p_context, final Collection<ILiteral> p_annotation, final Collection<ITerm> p_parameter,
                final Collection<ITerm> p_return
        )
        {
            if ( p_parameter.size() < this.getMinimalArgumentNumber() )
                throw new IllegalArgumentException( "not enough arguments" );

            p_return.add( new CRawTerm<>( p_parameter.parallelStream().mapToDouble( i -> ( (Number) ( (CRawTerm) i ).getValue() ).doubleValue() ).min()
                                                     .getAsDouble() ) );

            return CBoolean.from( true );
        }
    }
}