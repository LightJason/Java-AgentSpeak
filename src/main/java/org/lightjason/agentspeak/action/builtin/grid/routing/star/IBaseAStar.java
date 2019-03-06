package org.lightjason.agentspeak.action.builtin.grid.routing.star;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tobject.ObjectMatrix2D;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.apache.commons.collections.buffer.PriorityBuffer;
import org.apache.commons.lang3.tuple.Pair;
import org.lightjason.agentspeak.action.builtin.grid.routing.EDirection;
import org.lightjason.agentspeak.action.builtin.grid.routing.IBaseRouting;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;
import java.util.stream.Stream;


public abstract class IBaseAStar extends IBaseRouting
{
    protected final PriorityBuffer m_heap = new PriorityBuffer();

    protected IBaseAStar()
    {
        super();
    }

    protected IBaseAStar( @NonNull final BiFunction<ObjectMatrix2D, DoubleMatrix1D, Boolean> p_walkable )
    {
        super( p_walkable );
    }

    protected final Stream<DoubleMatrix1D> walkable( @Nonnull final ObjectMatrix2D p_grid, @Nonnull final DoubleMatrix1D p_current )
    {
        return Stream.of(
            walkable( p_grid, p_current, EDirection.NORTH ),
            walkable( p_grid, p_current, EDirection.EAST ),
            walkable( p_grid, p_current, EDirection.SOUTH ),
            walkable( p_grid, p_current, EDirection.WEST )
        ).filter( Pair::getKey ).map( Pair::getValue );
    }

}
