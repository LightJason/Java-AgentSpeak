package org.lightjason.agentspeak.language.newfuzzy.norm;

import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;


/**
 * fuzzy prodcut norm
 *
 * @tparam E fuzzy enum type
 */
public final class CProduct<E extends Enum<?>> implements IFuzzyNorm<E>
{
    @Override
    public IFuzzyValue<E> apply( final IFuzzyValue<E> p_value, final IFuzzyValue<E> p_value2 )
    {
        return new CFuzzyValue<>( p_value.fuzzy() + p_value2.fuzzy() - p_value.fuzzy() * p_value2.fuzzy() );
    }
}
