//------------------------------------------------------------------------------------------------------------------------------------------
//    initial beliefbase
//------------------------------------------------------------------------------------------------------------------------------------------

distribution("beta").                                           // distribution for generating random variables (e.g. beta, normal, uniform)
parameter1(2).                                                  // first parameter of the specified distribution
parameter2(5).                                                  // second parameter of the specified distribution
friction(0.5).                                                  // the coefficient of friction determines the texture of the street
!init.                                                          // intitializes the agent

//------------------------------------------------------------------------------------------------------------------------------------------
//    initialization
//------------------------------------------------------------------------------------------------------------------------------------------

/**
 * initializes the agent with a random scramble parameter
 **/
+!init
    :
        root_distribution(Distr) &&                             // specified distribution
        root_parameter1(Par1) &&                                // first distribution parameter
        root_parameter2(Par2)                                   // second distribution parameter
    <-
        R = mecsim_getSample(Distr, Par1, Par2);                // get scramble value of given distribution
        E = mecsim_getExpectation(Distr, Par1, Par2);           // get expectation of given distribution
        +scramble(R);                                           // set scramble value into beliefbase
        +expectation(E);                                        // set expectation into beliefbase
        +scramblingRatio( 1 / (1 - R + E) );                    // calculate scrambling ratio to determine driving behaviour
        ?scramblingRatio(SR);

        for( range( I, 0, mathceil( SR * 10 ) ) )               // perform a discretization of the srambling ratio
            +level( I )                                         // discretization of the scrambling ratio

        !drive.                                                 // start driving

//------------------------------------------------------------------------------------------------------------------------------------------
//    plans
//------------------------------------------------------------------------------------------------------------------------------------------

/**
 * checks if the agent reached its destination
 **/
+!checkGoal
    :
        root_mytraffic_routesample(index(Index), size(Size)) &&   // percentage of the way already driven
        root_bind_speed(Speed)                                    // current speed
    <-
        +percentage(Index/Size);
        if ( (Index + Speed) / Size >= 0.95 )                     // if the agent reaches its destination in the next simulation step
            println("arrived at destination").                    // TODO: perform a re-routing so that the agent will not be removed from the map


/**
 * update the current braking distance
 **/
+!checkBrakingDistance
    :
        root_bind_speed(V) &&                                   // current speed
        root_friction(F)                                        // current friction
    <-
        +brakingDistance(V * V / (2 * F * gravity)).            // calculate and set the current braking distance

/**
 * checks if there is a predecessor in front of the car
 **/
+!checkPredecessor
    <-
        ?root_mytraffic_predecessor([Predecessor]);             // get information about preceding cars (might be empty)
        if ( !(empty([Predecessor])) )                          // check if there is a preceding car
        {
            [X] =.. Predecessor;                                // get distance to predecessor
            Distance = mecsim_literal2number(X);                // convert distance literal to number term
            +distanceToPredecessor(Distance);                   // add belief with current distance
        }
        else
            +distanceToPredecessor(99999).                      // add a high distance value if no predecessor is in sight

/**
 * acceleration plan
 **/
+!accelerate
    : root_bind_speed(Speed) &&
      root_bind_acceleration(Accelerate) &&
      root_bind_maxspeed(MaxSpeed)
    <-
        NewSpeed = min(MaxSpeed, Speed+Accelerate);             // get new speed value
        -root_bind_speed(Speed);                                // remove old speed belief
        +root_bind_speed(NewSpeed);                             // add new speed belief
        !drive.                                                 // continue driving

/**
 * deceleration plan
 **/
+!decelerate
    : root_bind_speed(Speed) &&
      root_bind_deceleration(Decelerate)
    <-
        println("decelerate");
        NewSpeed = max(5, Speed-Decelerate);                    // get new speed value
        -root_bind_speed(Speed);                                // remove old speed belief
        +root_bind_speed(NewSpeed);                             // add new speed belief
        !drive.                                                 // continue driving

/**
 * the agent driver decelerates or accelerates the car
 * based on the distance to its preceding car and the
 * scramble parameter
**/
+!react
    : root_scramblingRatio(R) &&                                // scrambing ratio
      root_distanceToPredecessor(DP) &&                         // distance to current predecessor
      root_brakingDistance(DB)                                  // current braking distance
    <-
        if ( DB / DP > R )                                      // if relation between distances is critical
            !decelerate                                         // decelerate the car
        else
            !accelerate.                                        // accelerate the car

/**
 * default driving behaviour
 **/
+!drive <-
        !checkGoal;                                             // check if the destination was reached
        !checkBrakingDistance;                                  // check the current braking distance
        !checkPredecessor;                                      // check if the agent has a predecessor in front
        !react;                                                 // react on the environmental perceptions
        !drive.                                                 // continue driving

