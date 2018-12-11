package org.lightjason.agentspeak.language.newfuzzy.defuzzyfication;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.language.newfuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.newfuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.newfuzzy.membership.IFuzzyMembership;
import org.lightjason.agentspeak.language.newfuzzy.set.IFuzzySet;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.Stream;


public class CMOM<E extends Enum<?>> extends IBaseDefuzzification<E>
{
    /**
     * fuzzy membership function
     */
    private final IFuzzyMembership<E> m_membership;

    /**
     * ctor
     *
     * @param p_class fuzzy set class
     * @param p_default fuzzy enum type
     */
    public CMOM( @NonNull final Class<? extends IFuzzySet<E>> p_class, @NonNull final IFuzzyMembership<E> p_membership, @NonNull final E p_default )
    {
        super( p_class, p_default );
        m_membership = p_membership;
    }

    /**
     * ctor
     *
     * @param p_class fuzzy set class
     * @param p_default fuzzy enum type
     * @param p_success success function
     */
    public CMOM( @NonNull final Class<? extends IFuzzySet<E>> p_class,
                 @NonNull final BiFunction<E, Class<? extends IFuzzySet<E>>, Boolean> p_success, @NonNull final IFuzzyMembership<E> p_membership,
                 @NonNull final E p_default )
    {
        super( p_class, p_default, p_success );
        m_membership = p_membership;
    }

    @Nonnull
    @Override
    public E defuzzify( @Nonnull final Stream<IFuzzyValue<?>> p_value )
    {
        final IFuzzyValue<?>[] l_values = p_value.toArray( IFuzzyValue<?>[]::new );
        return this.index2enum(
            m_membership.apply(
                Arrays.stream( l_values ).mapToDouble( i -> i.fuzzy().doubleValue() ).sum()
                / Arrays.stream( l_values ).mapToDouble( i -> i.fuzzy().doubleValue() ).average().orElse( 1 )
            ).reduce(
                CFuzzyValue.of( m_default, 0 ),
                ( i, j ) -> i.fuzzy().doubleValue() < j.fuzzy().doubleValue() ? j : i
            ).get().ordinal()
        );
    }


}
