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
 * # along with this program. If not, see <http://www.gnu.org/licenses/>.               #
 * ######################################################################################
 * @endcond
 */

package lightjason.language.plan.trigger;

/**
 * event definition
 */
public interface ITrigger<T>
{

    /**
     * returns the type of the event
     *
     * @return type
     */
    public EType getID();

    /**
     * returns the data or null
     *
     * @return data
     */
    public T getData();


    /**
     * event types
     */
    public enum EType
    {
        ADDBELIEF( "+" ),
        DELETEBELIEF( "-" ),
        CHANGEBELIEF( "-+" ),
        ADDGOAL( "+!" ),
        DELETEGOAL( "-!" );

        /**
         * text name of the enum
         */
        private final String m_name;

        /**
         * ctor
         *
         * @param p_name text name
         */
        private EType( final String p_name )
        {
            m_name = p_name;
        }

        @Override
        public String toString()
        {
            return m_name;
        }
    }

}
