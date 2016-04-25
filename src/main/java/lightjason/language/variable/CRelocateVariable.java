package lightjason.language.variable;

import lightjason.common.IPath;

import java.text.MessageFormat;


/**
 * class for a relocated variable
 *
 * @tparam T variable type
 */
public final class CRelocateVariable<T> extends CVariable<T> implements IRelocateVariable
{
    /**
     * reference to relocated variable
     */
    private final IVariable<?> m_relocate;

    /**
     * ctor
     *
     * @param p_variable original variable, which should be used
     * @param p_relocate variable which should be relocated
     */
    public CRelocateVariable( final IVariable<?> p_variable, final IVariable<?> p_relocate )
    {
        super( p_variable.getFunctor(), p_variable.getTyped() );
        m_relocate = p_relocate;
    }

    /**
     * private ctor for creating object-copy
     *
     * @param p_functor functor
     * @param p_value value
     * @param p_variable referenced variable
     */
    private CRelocateVariable( final IPath p_functor, final T p_value, final IVariable<?> p_variable )
    {
        super( p_functor, p_value );
        m_relocate = p_variable;
    }

    @Override
    public final IVariable<?> relocate()
    {
        return m_relocate.set( this.getTyped() );
    }

    @Override
    public final IVariable<T> shallowcopy( final IPath... p_prefix )
    {
        return ( p_prefix == null ) || ( p_prefix.length == 0 )
               ? new CRelocateVariable<T>( m_functor, m_value, m_relocate )
               : new CRelocateVariable<T>( p_prefix[0].append( m_functor ), m_value, m_relocate );
    }

    @Override
    public final IVariable<T> shallowcopySuffix()
    {
        return new CRelocateVariable<>( this, m_relocate );
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0}({1})>{2}", m_functor, m_value == null ? "" : m_value, m_relocate );
    }
}
