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

package lightjason.generic.implementation;


/**
 * immutable belief storage
 */
public class CImmutableBeliefStorage<N, M> extends CBeliefStorage<N, M>
{

    @Override
    public final void addMultiElement( final String p_key, final N p_element )
    {
    }

    @Override
    public final void addSingleElement( final String p_key, final M p_element )
    {
    }

    @Override
    public final void clear()
    {
    }

    @Override
    public final boolean remove( final String p_key )
    {
        return false;
    }

    @Override
    public final boolean removeMultiElement( final String p_key, final N p_element )
    {
        return false;
    }

    @Override
    public final boolean removeSingleElement( final String p_key )
    {
        return false;
    }
}
