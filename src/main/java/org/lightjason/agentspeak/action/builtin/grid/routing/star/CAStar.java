package org.lightjason.agentspeak.action.builtin.grid.routing.star;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tobject.ObjectMatrix2D;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.apache.commons.collections.buffer.PriorityBuffer;
import org.apache.commons.lang3.tuple.Pair;
import org.lightjason.agentspeak.action.builtin.grid.routing.EDirection;
import org.lightjason.agentspeak.action.builtin.grid.routing.IBaseRouting;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.function.BiFunction;
import java.util.stream.Stream;


public final class CAStar extends IBaseRouting
{

    protected final PriorityBuffer m_heap = new PriorityBuffer();

    // https://github.com/jonasnick/A-star/blob/master/astar/AStar.java
    // https://github.com/pshafer/dstar
    // https://github.com/shu8i/AStar_DStarLite/blob/master/src/cs440/assignment1/control/AStar.java
    // https://github.com/shu8i/AStar_DStarLite/blob/master/src/cs440/assignment1/control/AdaptiveAStar.java



    public CAStar()
    {
        super();
    }

    public CAStar( @NonNull final BiFunction<ObjectMatrix2D, DoubleMatrix1D, Boolean> p_walkable )
    {
        super( p_walkable );
    }

    @Override
    public Stream<DoubleMatrix1D> apply( final ObjectMatrix2D p_grid, final DoubleMatrix1D p_start, final DoubleMatrix1D p_end )
    {
        // distance to start + estimate to end
        final Map<DoubleMatrix1D, Double> l_fscore = new ConcurrentHashMap<>();
        // distance to start (parent's g-score + distance from parent)
        final Map<DoubleMatrix1D, Double> l_gscore = new ConcurrentHashMap<>();

        // closed list
        final Set<DoubleMatrix1D> l_closedlist = Collections.synchronizedSet( new HashSet<>() );
        // we want the nodes with the lowest projected f value to be checked first
        final Queue<DoubleMatrix1D> l_openlist = new PriorityBlockingQueue<>(
            (int) p_grid.size() / 4,
            Comparator.comparingDouble( i -> l_fscore.getOrDefault( i, 0d ) )
        );

        l_openlist.add( p_start );
        while ( !l_openlist.isEmpty() )
        {
            final DoubleMatrix1D l_current = l_openlist.remove();
            if ( l_current.equals( p_end ) )
                return Stream.of();

            l_closedlist.add( l_current );
            this.expandnode( l_current );
        }


        return Stream.of();
    }


    private final void expandnode( final DoubleMatrix1D p_current )
    {

    }

    private final Stream<DoubleMatrix1D> walkable( @Nonnull final ObjectMatrix2D p_grid, @Nonnull final DoubleMatrix1D p_current )
    {
        return Stream.of(
            walkable( p_grid, p_current, EDirection.NORTH ),
            walkable( p_grid, p_current, EDirection.EAST ),
            walkable( p_grid, p_current, EDirection.SOUTH ),
            walkable( p_grid, p_current, EDirection.WEST )
        ).filter( Pair::getKey ).map( Pair::getValue );
    }


}
