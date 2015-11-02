# Light-Jason - a lightwire implementation of the AgentSpeak language

Based on the project [Jason](http://jason.sourceforge.net/) by Jomi F. Hübner and Rafael H. Bordini
a Java 8 implementation has been created. The version defines an additional AgentSpeak grammer with
[AntLR](http://www.antlr.org/) and a fuzzy-based logical calculus and unification.

## Base Definitions

### Beliefs

* Beliefs describe implicit the current state of the agent
* Beliefs will be updated before the cycle is run (beliefbase uses an update mechanism)
* Messages and Percepts are beliefs
* Beliefs must be exists iif a expression is computed (beliefs can be exist on-fly)
* Belief addition triggers an plan with the structure ```+belief```
* Belief deletion triggers an plan with the structure ```-belief```


### Actions

* Actions will be run immediatly
* Actions can fail (false) or success (true)
* There is no difference between internal and external actions
* Actions can be also an logical expression (assignments are logical expression that are always true)


### Plans

* Everything is a plan
* Plans are _sequences of actions and/or plans_
* Plans has got an optional context, that defines a constraint for execution (default is true and matchs always)
* Plans fail iif an item of the plan fail
* Plans returns a boolean value which defines fail (false) and success (true)
* _Atomic Plans_ cannot be fail, only the items within can fail
* Plans are run items sequentially
* _Parallel Plans_ run actions in parallel
* If the plan calls an _achievment goal addition_, the goal is added to the global goal list and the current plan is
paused until the goal is reached
* If the plan calls an _achievment goal deletion_, the goal is removed from the global goal list iif exists and returns
true otherwise it returns false
* If the plan calls an _test goal_ than the plan calls the test goal immediatly
* All items results will be concatinate with a logical _and_ to calculate the plan result value

### Rules

* Rules are plans with a context, that uses the default value true

 
### Goals

* Goals are a _sequences of plans_
* On agent start, there can exists one _initial goal_
* Each agent can track _more than one goal_ otherwise the agent suspends
* Goals are triggering by external events which will match by the goal name
* Goals will be resolved into plans with equal name (and allowed context)
* Goals are run in parallel independed from other goals

## Running Semantics

1. run update beliefbase with creating belief addition / deletion events
2. run agent cycle
    2.1 trigger plan, which match the belief-events
    2.2 trigger plan, which match the goal-events
3. increment cycle value


## Todos

* define _fixed_ annotions like:
    * _expires_ to remove a plan / belief automatically 
    * _fuzzy_ to define a fuzziness value of a plan / belief
    * _parallel_ to run a plan / parallel
    * _atomic_ to run a plan always with return value true
