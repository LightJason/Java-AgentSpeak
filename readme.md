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
* Actions can fail (false) or succede (true)
* There is no difference between internal and external actions
* Actions can be also an logical expression (assignments are logical expression that are always true)


### Plans

* Plans are _sequences of actions, rules and/or achievement goals_
* Plans has got an optional context, that defines a constraint for execution (default is true and matchs always)
* Plans fail iif an item of the plan fails
* Plans returns a boolean value which defines fail (false) and success (true)
* _Atomic Plans_ cannot fail, only the items within can fail
* Plans run items in sequential order
* _Parallel Plans_ run items in parallel
* If the plan calls an _achievment goal addition_, the goal is added to the global goal list and the current plan is paused until the goal is reached
* If the plan calls an _achievment goal deletion_, the goal is removed from the global goal list iif exists and returns true otherwise it returns false and the plan can fail
* If the plan calls an _test goal_ then the plan calls the test goal immediatly
* All items results will be concatinated with a logical _and_ to calculate the plan result value
* Each plan denotes its success/failure of execution in form of a ```(succeeds,fails)``` score, attached to its name in the static belief list _myplanscore_ (initialized at agent's birth with ```(0,0)```).
  * After successful or failed execution of the whole plan, the corresponding value gets incremented

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

* Semantically a goal marks a certain state of the world an agent _whishes to bring about_ [AgentSpeak, p.40]
* New/Removed goals, i.e. _achievement goals_ trigger an _achievement goal addition/deletion_ which leads to the execution of a corresponding _plan_
* On agent start, there can exists one _initial goal_
* Each agent can track _more than one goal_ otherwise the agent suspends
* Goals are triggering by external events which will match by the goal name
* Goals will be resolved into plans with equal name (and allowed context)
* Goals are run in parallel independed from other goals

## Running Semantics

### Definitions

| BDI-Convention    | Jason                    | Light-Jason       |
| ----------------- | ------------------------ | ----------------- |
| Action            | Internal-Action          | Action            |
| Action            | External-Action          | Action            |
| Goal              | Goal                     | Plan              |
| ?                 | Achievement Goal add/del | Event (Planname)  | 
| Intention         | Intention                |                   |
| Plan              | Plan                     | Plan              |
|                   | Rule                     | Rule              |
|                   |                          | Repair-Plan       |
| Desire            | Goal                     | Plan              |
| Intention         | Goal                     | Plan              |


### Agent-Cycle

Semantik definition of Jason see chapter 10.1 [AgentSpeak, p.207]

1. run update beliefbase
    * write waiting / marked beliefs into the beliefbase
    * generate beliefbase events

2. if agent is in suspend state
    * check wakup goal iif match, wake-up agent otherwise stop execution
    
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
            * begins with ```!!``` the plan which is mached by the goal is executated immediatly
            
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
* _mycycle_ current agent cylce which generates always -+ event
* _mysuspend_ number of the suspending cycles, generate + and -+ event
* _myawake_ exists when the agent is resumed from suspending, generate + event in cycle t and - event in cycle t+1
* _mybeliefbasesize_ number of beliefs within the current beliefbase, generates no event
* _myplanbasesize_ number of plans, generates no event
* _myexecutionplans_ string list with current execution plan names, generates no event
* _mywaitingplans_ string list with current waiting plan names, generates no event
* _myplanscore_ string list with plan list and score value for defining successful finished


## Convert Jason to LightJason

1. remove all dots (```.```) on actions / literals
2. change all dots (```.```) within the plan body to semicolon (```;```) except the termination dot at the end of the plan / rule
3. fix all belief- and plan calls - a belief goal starts with ```+```, ```-``` or ```-+```, a plan goal with ```+!```, ```-!```, ```+?``` or ```-?```
4. fix all boolean structure: logical-or is defined with ```||``` and logical-and with ```&&```
5. fix all bracket structure e.g. on if-else with unique meaning




## Todos

* Plans:
    * define _fixed_ annotations in plan labels, e.g. ```@label +planname```, like:
        * _expires_ to remove a plan / belief automatically 
        * _fuzzy_ to define a fuzziness value of a plan / belief
        * _parallel_ to run a plan / parallel
        * _atomic_ to run a plan always with return value true
        * _priority_ value to define the matching priority (renamed to _significance_)
        * _only_ value that set all current running plans to the waiting state, execute this plan only and reactivate the other plans after the execution is finished
    *  (see p. 97f for comparison w/ Jason)
 *  Beliefs:
    * (?) define annotations describing the origin/state/reason/... of a belief, including fixed ones like:
        * _source_
        * _expires_
        * ... (?)
    * (?) also non-fixed for _Mental Notes_
    * (?) -> needs grammar update @beliefs, similar to ```atomic_formula```, but has to be be more like ```[ list ]``` (this would also allow nested annotations (see p. 38)
    * (see p. 37ff for comparison w/ Jason)
* atomic_formula arithmetic term fix
* body_formula check literal / atomic_forumla rule
* plan / beliefname did not be equal
* arithmetic expression operator priority
* Prolog list ```[H|T]``` structure
* fix repair plan definition only for goals / syntax format and event handling must be discussed
* callback for actions to create concurrent calls after the action is finished
* communication definition / drop equal messages / each message triggers only one event
* parallel / SIMD unification with [type inhertitance](http://stackoverflow.com/questions/1396558/how-can-i-implement-the-unification-algorithm-in-a-language-like-java-or-c) or [Java Unification](https://code.google.com/p/kawala/source/browse/trunk/src/com/kaching/platform/common/types/Unification.java?r=265)
* expression grammar [AntLR](http://stackoverflow.com/questions/16469023/antlr-left-recursion-for-nesting-boolean-expressions), [C#](http://www.codeproject.com/Articles/18880/State-of-the-Art-Expression-Evaluation), [Expression Grammar](https://ivanyu.me/blog/2014/09/13/creating-a-simple-parser-with-antlr/)
* [ISO Prolog Specification](http://stackoverflow.com/questions/20119749/is-there-an-iso-prolog-reference-implementation)
