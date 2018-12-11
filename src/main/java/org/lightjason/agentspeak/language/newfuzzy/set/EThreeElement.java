package org.lightjason.agentspeak.language.newfuzzy.set;

import edu.umd.cs.findbugs.annotations.NonNull;


/**
 * three element fuzzy with numerical representation
 */
public enum  EThreeElement implements IFuzzySet<EThreeElement>
{
    LOW,
    MEDIUM,
    HIGH;

    @NonNull
    @Override
    @SuppressWarnings( "unchecked" )
    public <V> V raw()
    {
        return (V) Integer.valueOf( this.ordinal() );
    }

    @Override
    public EThreeElement get()
    {
        return this;
    }
}
