package org.lightjason.agentspeak.language.execution.expression;

import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnull;
import java.util.function.Function;


public enum EUnaryOperator implements Function<ITerm, Object>
{
    NEGATION( "~" );

    /**
     * text name of the enum
     */
    private final String m_name;

    /**
     * ctor
     *
     * @param p_name text name
     */
    EUnaryOperator( final String p_name )
    {
        m_name = p_name;
    }

    @Override
    public final String toString()
    {
        return m_name;
    }

    @Override
    public final Object apply( @Nonnull final ITerm p_term )
    {
        return !p_term.<Boolean>raw();
    }
}
