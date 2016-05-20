package lightjason.agentspeak.beliefbase;

import lightjason.agentspeak.agent.IAgent;


/**
 * interface for generating non-existing beliefbases views
 */
public interface IViewGenerator<T extends IAgent<?>>
{

    /**
     * generates a  new view
     *
     * @param p_name name of the view
     * @return view object
     */
    IView<T> generate( final String p_name );
}
