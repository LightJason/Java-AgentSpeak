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

import java.text.MessageFormat;


/**
 * number annotation
 */
public final class CNumberAnnotation<T extends Number> extends IBaseAnnotation<T>
{
    /**
     * ctor
     *
     * @param p_type type
     * @param p_data number
     */
    public CNumberAnnotation( final EType p_type, final T p_data )
    {
        super( p_type, p_data );
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0}({1})", m_type, m_data );
    }

}
