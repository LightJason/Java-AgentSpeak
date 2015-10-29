// --- default car agent ----------------------------------------------------------------
// drives depend on the Nagel-Schreckenberg model, the deceleration and acceleration
// depends on the distance to the current predecessor car
// --------------------------------------------------------------------------------------


// initial goal
!drive.

// acceleration
+!accelerate
   :    root_bind_speed(Speed) &
        root_bind_acceleration(Accelerate) &
        root_bind_maxspeed(MaxSpeed)

   <-   .min([MaxSpeed, Speed+Accelerate], NewSpeed);
        mecsim_propertyset(self, m_speed, NewSpeed);
        !drive.


// deceleration
+!decelerate
   :    root_bind_speed(Speed) &
        root_bind_deceleration(Decelerate) &
        Decelerate > 0

   <-
        .max([5, Speed-Decelerate], NewSpeed);
        mecsim_propertyset(self, m_speed, NewSpeed);
        !drive.


// driving call if a predecessor exists
// check distance and decelerate otherwise accelerate
+!drive
    :    root_bind_speed(Speed) &
         root_bind_deceleration(Deceleration) &
         root_mytraffic_predecessor([Predecessor]) &
         not (.empty([Predecessor]))

    <-
         // get distance to predecessing car
         Predecessor =.. [X|_];
         mecsim_literal2number(X,Distance);

         // add the speed range
         for ( .range(I, 1, math.ceil( Speed / 10 ) ) )
         {
             +speed_range(I * 10);
         }

         // calculate braking distance with gaussian sum
         // @see https://de.wikipedia.org/wiki/Gau%C3%9Fsche_Summenformel
         UpperSumIndex = math.floor( Speed / Deceleration );
         BrakingDistance = UpperSumIndex * ( Speed - 0.5 * Deceleration * ( UpperSumIndex + 1 ) );

        // check if predecessing car is too close
        if ( BrakingDistance > Scramble*Distance )
        {
             !decelerate;
        }
        else
        {
             !accelerate;
        }.


// default behaviour - accelerate
+!drive
   :    true
   <-
        !accelerate.
