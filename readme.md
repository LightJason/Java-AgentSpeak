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

* Plans are _sequences of actions and/or rules_
* Plans has got an optional context, that defines a constraint for execution (default is true and matchs always)
* Plans fail iif an item of the plan fail
* Plans returns a boolean value which defines fail (false) and success (true)
* _Atomic Plans_ cannot be fail, only the items within can fail
* Plans are run items sequentially
* _Parallel Plans_ run items in parallel
* If the plan calls an _achievment goal addition_, the goal is added to the global goal list and the current plan is
paused until the goal is reached
* If the plan calls an _achievment goal deletion_, the goal is removed from the global goal list iif exists and returns
true otherwise it returns false and the plan can fail
* If the plan calls an _test goal_ than the plan calls the test goal immediatly
* All items results will be concatinate with a logical _and_ to calculate the plan result value

### Rules

* Rules are similar to plans without the context condition
* Rules cannot be triggered by a goal, so they must be called from a plan
* Rules run immediatly
* Rules run sequentially
* _Parallel Rules_ run items in parallel
* _Atomic Rules_ cannot be fail, only the items within can fail
* Rules returns a boolean value which defines fail (false) and success (true)
* All items results will be concatinate with a logical _and_ to calculate the plan result value

 
### Goals

* Goals are a _sequences of plans_
* On agent start, there can exists one _initial goal_
* Each agent can track _more than one goal_ otherwise the agent suspends
* Goals are triggering by external events which will match by the goal name
* Goals will be resolved into plans with equal name (and allowed context)
* Goals are run in parallel independed from other goals

## Running Semantics

### Agent-Cycle

1. run update beliefbase with creating belief addition / deletion events
2. if agent is in suspend, stop execution
3. run agent cycle

    1. collect plans, which match the belief-events
    2. collect plans, which match the goal-events
    3. create plan execution list of _earmarked executing plans_ and _collected plans_
    4. execute collected plans in parallel

        1. if an item is a _action_ or _rule_ execute it immediatly
        2. if an item is a _test goal_ try to find within the current context a

            1. rule, if found execute rule immediatly, result is passed for the test
            2. plan, if found set current plan to _waiting state_ and execute found plan within the next cycle

        3. if an item is an _achievment goal_ add it to the _earmarked executing plans list_ and set the current plan to _waiting state_

    5. if a plan is finished set the plan to the _earmarked executing plans list_

4. increment cycle value

### Static-Beliefs

default _unchangeable / unmodifiable_ beliefs which are exist always within the beliefbase

* _myname_ agent name, generates no event
* _mycycle_ current agent cylce which generates always -+ event
* _mysuspend_ number of the suspending cycles, generate + and -+ event
* _myawake_ exists when the agent is resumed from suspending, generate + event in cycle t and - event in cycle t+1
* _mybeliefbasesize_ number of beliefs within the current beliefbase, generates no event
* _myplanbasesize_ number of plans, generates no event
* _myexecutionplans_ string list with current execution plan names, generates no event
* _mywaitingplans_ string list with current waiting plan names, generates no event



## Todos

* define _fixed_ annotions like:
    * _expires_ to remove a plan / belief automatically 
    * _fuzzy_ to define a fuzziness value of a plan / belief
    * _parallel_ to run a plan / parallel
    * _atomic_ to run a plan always with return value true
    * _priority_ value to define the matching priority
* semantic definition of ``!!`` must be defined
* atomic_formula arithmetic term fix
* body_formula check literal / atomic_forumla rule
* plan / beliefname did not be equal
* arithmetic expression operator priority
* Prolog list ```[H|T]``` structure
* fix repair plan definition only for goals