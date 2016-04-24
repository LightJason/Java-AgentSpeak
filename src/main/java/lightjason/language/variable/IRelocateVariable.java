package lightjason.language.variable;

/**
 * interface for relocated variables (linkage
 * between two variables for transfering the value)
 */
public interface IRelocateVariable
{

    /**
     * sets the value into the
     * relocated variable and returns
     * the modifed variable
     *
     * @return relocated variable
     */
    IVariable<?> relocate();

}
