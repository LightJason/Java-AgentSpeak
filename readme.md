# Light-Jason - Lightwire version of AgentSpeak(L)

Based on the project [Jason](http://jason.sourceforge.net/) by Jomi F. Hübner and Rafael H. Bordini
a Java 8 implementation has been created. The version defines an additional AgentSpeak grammar with
[AntLR](http://www.antlr.org/) and a fuzzy-based logical calculus and unification based on a mathematical
structure to describe an optimizing process.

## Base Definitions

### Beliefs

* Beliefs implicitly describe the current state of the agent
* Beliefs will be updated before the cycle is run (beliefbase uses an update mechanism)
* Messages and Percepts turn into beliefs
* Beliefs must be exists iif a expression is computed (beliefs can be exist on the fly)
* Belief addition triggers a plan with the definition ```+belief``` 
* Belief retraction triggers a plan with the definition ```-belief```


### Actions

* Actions will be run immediately
* Actions can fail (false) or succede (true)
* There is no difference between internal and external actions
* Actions can be also a logical or assignment expression (these are always true)


### Plans

* Plans are _sequences of actions, rules and/or achievement / test goals_
* Plans has got an optional context, that defines a constraint for execution (default is true and matches always)
* Plans fail iif an item of the plan fails
* Plans returns a boolean value which defines fail (false) and success (true)
* _Atomic Plans_ cannot fail, only the items within can fail
* Plans run items in sequential order
* _Parallel Plans_ run items in parallel
* If the plan calls an _achievement goal addition_, the goal is added to the global goal list and the current plan is paused until the goal is reached
* If the plan calls an _achievement goal deletion_, the goal is removed from the global goal list iif exists and returns true otherwise it returns false and the plan can fail
* All items results will be concatenated with a logical _and_ to calculate the plan result value
    
#### Internals
 
 * The plan has got additional beliefs, that are added in the context condition (values are calculated before plan execution is started)
    * _score_ returns the current score-value of the plan
    * _failrun_ stores the number of fail runs
    * _successrun_ stores the number of successful runs
    * _runs_ number of runs of the plan (fail + successful runs)


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

* Semantically a goal marks a certain state of the world an agent _wishes to bring about_ [AgentSpeak, p.40]
* New/Removed goals, i.e. _achievement goals_ trigger an _achievement goal addition/deletion_ which leads to the execution of a corresponding _plan_
* On agent start, there can exists one _initial goal_
* Each agent can track _more than one goal_ otherwise the agent suspends
* Goals are triggered by external events which will match by the goal name
* Goals will be resolved into plans with equal name (and allowed context)
* Goals are run in parallel independed from other goals
* A goal is a sequence of plans that which must all finished successfully
* A goal is part of exactly one intention
* A goal will be applied to a plan (a plan with equal name will be instantiiation)
* if a goal can match a desire (the goal is near to the desire) it can add an event to match the desire belief

### Intentions

* The intention is set of beliefs (of goals), which must exist simultaneously 
* An intention is the _convex hull_ of its goals
* Intentions cannot be in conflict with other intentions, so there dies not exists any overlaping

### Desires

* A Desire is a vertex of the edge of all intentions
* Desires are defined by a set of beliefs
* Desires can be in conflict with other desires, represented that the desires have got a large distance (much as possible) 
* The desire is successfully reached, iif all beliefs are existing anytime


![Structure](bdi.png)


## Coding

### Agent

* agent (ASL) can be defined as a logic program with beliefs, rules and plans
* plans can be bundeled in a _plan-bundle_ which is semantic equal to a class, plan-bundles can be included in an agent



## Running Semantics

### Agent-Cycle

Semantik definition of Jason see chapter 10.1 [AgentSpeak, p.207]

1. run update beliefbase
    * write waiting / marked beliefs into the beliefbase
    * generate beliefbase events

2. if agent is in suspend state
    * check wake-up goal iif match, wake-up agent otherwise stop execution
    
3. run agent cycle

    1. collect plans, which match the belief-events
    2. collect plans, which match the goal-events
    3. create plan execution list of _earmarked executing plans_ and _collected plans_
    4. execute collected plans in parallel and apply the following rules for each body formula

        1. if an item is an _action_ or _rule_ execute it immediatly
        2. if an item is a _test goal_ try to find within the current context a
            * rule, if found execute rule immediatly, result is passed for the test
            * plan, if found set current plan to _waiting state_ and add found plan to the _earmarked executing plans list_
                * if the plan has got an annotation _only_ all plans within the next cycle will be suspend until this plan is finished


        3. if an item is an _achievment goal_ and
            * begins with ```!``` add it to the _earmarked executing plans list_ and set the current plan to _waiting state_
            * begins with ```!!``` the plan which is mached by the goal is executated immediately
            
        4. if an item is a belief-
            * addition, mark the literal for adding into the beliefbase
            * deletion, mark the literal for removing from beliefbase
            * changing, mark the literal with changes for update within the beliefbase

    5. if a plan is finished check plan towards the _waiting state plan list_ and move waiting plans to the _earmarked executing plans list_
        * if the plan fails add if exists the _repair plan_ to the _earmarked executing plans list_

4. increment cycle value

### Static-Beliefs

default _unchangeable / unmodifiable_ beliefs which are exist always within the beliefbase

* _myname_ agent name, generates no event
* _mycycle_ current agent cylce which generates always ```-+``` event
* _mysuspend_ number of the suspending cycles, generate ```+``` and ```-+``` event
* _myawake_ exists when the agent is resumed from suspending, generate ```+``` event in cycle t and ```-``` event in cycle t+1
* _mybeliefbasesize_ number of beliefs within the current beliefbase, generates no event
* _myplanbasesize_ number of plans, generates no event
* _myexecutionplans_ string list with current execution plan names, generates no event
* _mywaitingplans_ string list with current waiting plan names, generates no event


## Todos

* Plans:
    * annotation of comparison (see p. 97f)
*  Beliefs:
    * (?) define annotations describing the origin/state/reason/... of a belief, including fixed ones like:
        * _source_
    * (?) also non-fixed for _Mental Notes_
    * (?) -> needs grammar update beliefs, similar to ```atomic_formula```, but has to be be more like ```[ list ]``` (this would also allow nested annotations (see p. 38)
    * (see p. 37ff for comparison w/ Jason)
* plan / beliefname did not be equal
* callback for actions to create concurrent calls after the action is finished
* communication definition / drop equal messages / each message triggers only one event
* parallel / SIMD unification with [type inhertitance](http://stackoverflow.com/questions/1396558/how-can-i-implement-the-unification-algorithm-in-a-language-like-java-or-c) or [Java Unification](https://code.google.com/p/kawala/source/browse/trunk/src/com/kaching/platform/common/types/Unification.java?r=265)
* expression grammar [AntLR](http://stackoverflow.com/questions/16469023/antlr-left-recursion-for-nesting-boolean-expressions), [C#](http://www.codeproject.com/Articles/18880/State-of-the-Art-Expression-Evaluation), [Expression Grammar](https://ivanyu.me/blog/2014/09/13/creating-a-simple-parser-with-antlr/)
* [ISO Prolog Specification](http://stackoverflow.com/questions/20119749/is-there-an-iso-prolog-reference-implementation)
* publish to [Maven central](http://stackoverflow.com/questions/14013644/hosting-a-maven-repository-on-github)

## Open Questions

* Currently in Jason a goal addition (to trigger an ```achievment_goal_action```) is denoted by either ```!foo``` or ```!!foo```, beliefs (which trigger ```belief_action```) are denoted by a trailing ```+``` or ```-``` (```+foo```/```-foo```). Conceptual this seems to be inconsistent. Also if we want to consider explicit goal removal (simmilar to belief removal) ```+!foo``` or ```+!!foo``` should be used in a plan's body. This would require reverting https://github.com/flashpixx/Light-Jason/commit/7c24bfd0252a15a2f1aa0646b5b376c154809ad8 and add ```(PLUS | MINUS)``` to ```DOUBLEEXCLAMATIONMARK```.
