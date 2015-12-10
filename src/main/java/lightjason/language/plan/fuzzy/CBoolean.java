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

package lightjason.language.plan.fuzzy;

import lightjason.common.CCommon;
import lightjason.error.CIllegalArgumentException;

import java.text.MessageFormat;
import java.util.function.Predicate;


/**
 * boolean fuzzy value
 */
public final class CBoolean implements IFuzzyValue<Boolean>
{
    /**
     * boolean value
     */
    private final Boolean m_value;
    /**
     * fuzzy value
     */
    private final double m_fuzzy;

    /**
     * ctor
     *
     * @param p_value value
     */
    public CBoolean( final Boolean p_value )
    {
        this( p_value, 1 );
    }

    /**
     * ctor
     *
     * @param p_value value
     * @param p_fuzzy fuzzy
     */
    public CBoolean( final Boolean p_value, final double p_fuzzy )
    {
        if ( !( ( p_fuzzy >= 0 ) && ( p_fuzzy <= 1 ) ) )
            throw new CIllegalArgumentException( CCommon.getLanguageString( this, "fuzzyvalue", p_fuzzy ) );

        m_fuzzy = p_fuzzy;
        m_value = p_value;
    }


    @Override
    public final Boolean getValue()
    {
        return m_value;
    }

    @Override
    public final Double getFuzzy()
    {
        return m_fuzzy;
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0}({1})", m_value, m_fuzzy );
    }

    /**
     * check fuzzy-trueness
     *
     * @return predicate
     */
    public static Predicate<CBoolean> isTrue()
    {
        return p -> p.getValue();
    }

    /**
     * factory
     *
     * @param p_value boolean value
     * @return boolean object
     */
    public static CBoolean from( final boolean p_value )
    {
        return new CBoolean( p_value );
    }

    /**
     * factory
     *
     * @param p_value boolean value
     * @param p_fuzzy fuzzy value
     * @return boolean object
     */
    public static CBoolean from( final boolean p_value, final double p_fuzzy )
    {
        return new CBoolean( p_value, p_fuzzy );
    }
}
