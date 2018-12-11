package org.lightjason.agentspeak.language.newfuzzy.defuzzyfication;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.language.newfuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.newfuzzy.membership.IFuzzyMembership;
import org.lightjason.agentspeak.language.newfuzzy.set.IFuzzySet;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.stream.Stream;


/**
 * defuzzification with mean-of-maxima
 *
 * @tparam E enum fuzzy type
 */
public class CMOM<E extends Enum<?>> extends IBaseDefuzzification<E>
{
    /**
     * ctor
     *
     * @param p_class fuzzy set class
     * @param p_membership membership function
     * @param p_default fuzzy enum type
     */
    public CMOM( @NonNull final Class<? extends IFuzzySet<E>> p_class, @NonNull final IFuzzyMembership<E> p_membership, @NonNull final Number p_default )
    {
        super( p_class, p_default, p_membership );
    }

    @Nonnull
    @Override
    public Number defuzzify( @Nonnull final Stream<IFuzzyValue<?>> p_value )
    {
        final IFuzzyValue<?>[] l_values = p_value.toArray( IFuzzyValue<?>[]::new );
        return m_membership.apply(
                Arrays.stream( l_values ).mapToDouble( i -> i.fuzzy().doubleValue() ).sum()
                / Arrays.stream( l_values ).mapToDouble( i -> i.fuzzy().doubleValue() ).average().orElse( 1 )
            ).reduce(
                m_default,
                ( i, j ) -> i.fuzzy().doubleValue() < j.fuzzy().doubleValue() ? j : i
            ).fuzzy();
    }

    @Override
    public boolean success( @NonNull final Number p_value )
    {
        // scale the gravity on the maximum to the enum result
        return p_value.doubleValue() / this.maximum().orElse( 1 ) >= 0.5;
    }

}
