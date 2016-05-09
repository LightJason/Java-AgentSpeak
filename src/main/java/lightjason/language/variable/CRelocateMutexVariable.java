package lightjason.language.variable;

import com.rits.cloning.Cloner;
import lightjason.common.CPath;
import lightjason.common.IPath;
import lightjason.language.ITerm;


/**
 * class for a mutex relocated variable
 *
 * @tparam T variable type
 */
public final class CRelocateMutexVariable<T> extends CMutexVariable<T> implements IRelocateVariable
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
    public CRelocateMutexVariable( final IVariable<?> p_variable, final IVariable<?> p_relocate )
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
    private CRelocateMutexVariable( final IPath p_functor, final T p_value, final IVariable<?> p_variable )
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
               ? new CRelocateMutexVariable<T>( m_functor, m_value, m_relocate )
               : new CRelocateMutexVariable<T>( p_prefix[0].append( m_functor ), m_value, m_relocate );
    }

    @Override
    public final ITerm deepcopySuffix()
    {
        return new CRelocateMutexVariable<>( CPath.from( m_functor.getSuffix() ), new Cloner().deepClone( m_value ), m_relocate );
    }

    @Override
    public final ITerm deepcopy( final IPath... p_prefix )
    {
        return new CRelocateMutexVariable<>(
                ( p_prefix == null ) || ( p_prefix.length == 0 )
                ? m_functor
                : m_functor.append( p_prefix[0] ),
                new Cloner().deepClone( m_value ),
                m_relocate
        );
    }

    @Override
    public final IVariable<T> shallowcopySuffix()
    {
        return new CRelocateMutexVariable<>( this, m_relocate );
    }

}
