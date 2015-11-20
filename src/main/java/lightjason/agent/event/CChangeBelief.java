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

package lightjason.agent.event;

/**
 * event to describe belief-change
 */
public class CChangeBelief implements IEvent<String>
{
    /**
     * event name
     */
    public static final String NAME = "change belief";
    /** event data **/
    private final String m_data;

    /**
     * ctor
     *
     * @param p_belieffunctor name of the functor
     */
    public CChangeBelief( final String p_belieffunctor )
    {
        m_data = p_belieffunctor;
    }

    @Override
    public int hashCode()
    {
        return m_data.hashCode() + this.getName().hashCode();
    }

    @Override
    public boolean equals( final Object p_object )
    {
        return p_object.hashCode() == this.hashCode();
    }

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public String getData()
    {
        return m_data;
    }
}
