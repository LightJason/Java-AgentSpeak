package org.lightjason.agentspeak.action.builtin.grid.routing.star;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tobject.ObjectMatrix2D;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.function.BiFunction;
import java.util.stream.Stream;


public final class CDStar extends IBaseAStar
{
    public CDStar()
    {
        super();
    }

    public CDStar( @NonNull final BiFunction<ObjectMatrix2D, DoubleMatrix1D, Boolean> p_walkable
    )
    {
        super( p_walkable );
    }

    @Override
    public Stream<DoubleMatrix1D> apply( final ObjectMatrix2D p_grid, final DoubleMatrix1D p_start, final DoubleMatrix1D p_end )
    {
        return Stream.of();
    }
}
