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

package lightjason.language.plan.annotation;

/**
 * annotation interface
 */
public interface IAnnotation<T>
{
    /**
     * returns the type of the annotation
     *
     * @return type
     */
    public EType getID();

    /**
     * returns the data of the annotation if exists
     *
     * @return data or null
     */
    public T getData();


    /**
     * annotation types
     */
    public enum EType
    {
        FUZZY( "@Fuzzy" ),
        PRIORITY( "@Priority" ),
        EXPIRES( "@Expires" ),
        ATOMIC( "@Atomic" ),
        EXCLUSIVE( "@Exclusive" ),
        PARALLEL( "@Parallel" );

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
