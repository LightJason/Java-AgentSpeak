# Light-Jason - a lightwire implementation of the AgentSpeak language

Based on the project [Jason](http://jason.sourceforge.net/) by Jomi F. Hübner and Rafael H. Bordini
a Java 8 implementation has been created. The version defines an additional AgentSpeak grammer with
[AntLR](http://www.antlr.org/) and a fuzzy-based logical calculus and unification.

## Base Definitions

### Beliefs

* Beliefs describe implicit the current state of the agent
* Beliefs will be updated before the cycle is run (beliefbase uses an update mechanism)
* Messages and Percepts are beliefs
* Beliefs must be exists iif a plan condition is computed (beliefs can be exist on-fly)

### Actions

* Actions will be run immediatly
* Actions can fail
* Actions return a boolean value which defines fail (false) and success (true)

### Plans

* Plans are _sequences of actions_
* Plan can be used a condition to define a constraint for execution
* Plans fail iif an action fails
* _Atomic Plans_ cannot be fail, only the actions of the plans can fail
* Plans are run actions sequentially
* _Parallel Plans_ run actions in parallel
* All action results will be concatinate with a logical and to calculate the plan result value
* Plans returns a boolean value which defines fail (false) and success (true)
* On each cycle a plan will be full executed
 
### Goals
 
* Goals are a _sequences of planes_
* Each agent can track _more than one goal_
* On each cycle all _current plans_ within the _current goals_ are run in parallel
* If a plan fails a _repair plan_ is searched and will be the current plan in the next cycle
* After a plan is finished the goal will be checked if it can be achieved

