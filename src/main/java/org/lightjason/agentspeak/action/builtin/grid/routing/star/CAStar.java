package org.lightjason.agentspeak.action.builtin.grid.routing.star;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tobject.ObjectMatrix2D;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.apache.commons.collections.buffer.PriorityBuffer;
import org.apache.commons.lang3.tuple.Pair;
import org.lightjason.agentspeak.action.builtin.grid.routing.CNode;
import org.lightjason.agentspeak.action.builtin.grid.routing.EDirection;
import org.lightjason.agentspeak.action.builtin.grid.routing.EDistance;
import org.lightjason.agentspeak.action.builtin.grid.routing.IBaseRouting;
import org.lightjason.agentspeak.action.builtin.grid.routing.INode;

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



    public CAStar( @Nonnull final EDistance p_distance )
    {
        super( p_distance );
    }

    public CAStar( @Nonnull final EDistance p_distance, @NonNull final BiFunction<ObjectMatrix2D, DoubleMatrix1D, Boolean> p_walkable )
    {
        super( p_distance, p_walkable );
    }

    @Override
    public Stream<DoubleMatrix1D> apply( @Nonnull final ObjectMatrix2D p_grid, @Nonnull final DoubleMatrix1D p_start, @Nonnull final DoubleMatrix1D p_end )
    {
        // distance to start + estimate to end
        final Map<INode, Double> l_fscore = new ConcurrentHashMap<>();
        // distance to start (parent's g-score + distance from parent)
        final Map<INode, Double> l_gscore = new ConcurrentHashMap<>();

        // closed list
        final Set<INode> l_closedlist = Collections.synchronizedSet( new HashSet<>() );
        // we want the nodes with the lowest projected f value to be checked first
        final Queue<INode> l_openlist = new PriorityBlockingQueue<>(
            (int) p_grid.size() / 4,
            Comparator.comparingDouble( i -> l_fscore.getOrDefault( i, 0d ) )
        );

        l_openlist.add( new CNode( p_start ) );
        while ( !l_openlist.isEmpty() )
        {
            final INode l_current = l_openlist.remove();
            if ( l_current.position().equals( p_end ) )
                return this.constructpath( l_openlist, l_closedlist );

            l_closedlist.add( l_current );
            this.expandnode( p_grid, l_current, l_openlist, l_gscore, l_fscore );
        }


        return Stream.of();
    }


    private Stream<DoubleMatrix1D> constructpath( @Nonnull Queue<INode> p_openlist, @Nonnull Set<INode> p_closedlist )
    {
        return Stream.of();
    }

    private void expandnode( @Nonnull final ObjectMatrix2D p_grid, @Nonnull final INode p_current, @Nonnull Queue<DoubleMatrix1D> p_openlist,
                             @Nonnull Map<DoubleMatrix1D, Double> p_gscore, @Nonnull Map<DoubleMatrix1D, Double> p_fscore )
    {
        final double l_gscore = p_gscore.getOrDefault( p_current, 0D );

        this.walkable( p_grid, p_current )
            .parallel()
            .filter( i -> !p_openlist.contains( i ) || l_)
            .forEach( i -> p_gscore.put( i, l_gscore + m_distance.apply( i, p_current ).doubleValue() ) );


    }

    private Stream<INode> walkable( @Nonnull final ObjectMatrix2D p_grid, @Nonnull final INode p_current )
    {
        return Stream.of(
            walkable( p_grid, p_current.position(), EDirection.NORTH ),
            walkable( p_grid, p_current.position(), EDirection.EAST ),
            walkable( p_grid, p_current.position(), EDirection.SOUTH ),
            walkable( p_grid, p_current.position(), EDirection.WEST )
        ).filter( Pair::getKey ).map( Pair::getValue ).map( CNode::new );
    }


}
