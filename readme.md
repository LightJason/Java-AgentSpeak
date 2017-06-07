# LightJason - AgentSpeak(L++)

![Circle CI](https://circleci.com/gh/LightJason/AgentSpeak.svg?style=shield)
![Coverage Status](https://coveralls.io/repos/github/LightJason/AgentSpeak/badge.svg?branch=master)

Based on the project [Jason](http://jason.sourceforge.net/) by Jomi F. Hübner and Rafael H. Bordini
a Java 8 implementation has been build-up with parallel execution calls. The version defines an additional [AgentSpeak(L)](https://en.wikipedia.org/wiki/AgentSpeak) grammar based on
[AntLR](http://www.antlr.org/) for simulating a [multi-agent system](https://en.wikipedia.org/wiki/Multi-agent_system)
with a fuzzy-based logical calculus and grammar features like lambda expressions. Agent execution based on a mathematical structure
to describe an optimizing process by a [finite-state-machine](https://en.wikipedia.org/wiki/Finite-state_machine)



## Requirements

* [JRE 1.8](http://www.java.com/)

### Development

* [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/)
* [Maven 3 or higher](http://maven.apache.org/)
* [Doxygen](http://www.doxygen.org/) with [GraphViz](http://www.graphviz.org/)
* [Source code documentation](http://lightjason.github.io/AgentSpeak/)
* [Open Hub Code Statistic](https://www.openhub.net/p/LightJason-AgentSpeak)
* [Coveralls.io Statistic](https://coveralls.io/github/LightJason/AgentSpeak)
* [Libraries.io Statistic](https://libraries.io/github/LightJason/AgentSpeak)



## Base Definitions

### <a name="belief">Beliefs</a>

* Beliefs implicitly describe the current state of the agent
* Beliefs will be updated before the cycle is run (beliefbase uses an update mechanism)
* Beliefs must be exists iif an expression is computed (beliefs can be exist on the fly)
* Belief addition triggers a plan with the definition ```+belief``` 
* Belief retraction triggers a plan with the definition ```-belief```
* Belief modification with ```-+``` does not exists anymore 
* [Variables](#variable) within a belief literal will be unified before the belief is added to the beliefbase


### <a name="action">Actions</a>

* Actions will be run immediately
* Actions can fail (false) or succeed (true)
* There is no difference between internal and external actions
* Actions can be also a logical or assignment expression (these are always true)
* Actions with ```@```-prefix wil be executed in parallel (each inner action will be run in parallel)


### <a name="plan">Plans</a>

* Plans are _sequences of [actions](#action), [rules](#rule) and/or achievement / test [goals](#goal)_
* Plans has got an optional context, that defines a constraint for execution (default is true and matches always)
* Plans fail iif an item of the plan fails
* Plans returns a boolean value which defines fail (false) and success (true)
* Plans run items in sequential order on default
* If the plan calls an _achievement [goal](#goal) addition_, the [goal](#goal) is added to the global [goal](#goal) list and the current plan will be continued, the _achievement [goal](#goal)_ will be run within the next cycle
* An _achievement [goal](#goal) deletion_ does not exists anymore
* All items results will be concatenated with a logical _and_ to calculate the plan result value
    
#### <a name="planinternal">Internals Constants</a>
 
* The plan has got additional [constant variables](#variable), that are added in the context condition (values are calculated before plan execution is started)
    * _PlanFail_ stores the number of fail runs and _PlanFailRatio_ normalized value in [0,1]
    * _PlanSuccessful_ stores the number of successful runs and _PlanSuccessfulRatio_ normalized value in [0,1] 
    * _PlanRuns_ number of runs of the plan (fail + successful runs)
    * _Cycle_ agent-cycle number
    
#### <a name="fuzzy">Fuzziness</a>

* Fuzzy value must be in [0,1]
* Each [action](#action) in a fuzzy-plan returns also a fuzzy value to define the fuzziness
* The [plan](#plan) or [rule](#rule) result returns true / false and the aggregated fuzzy value


### <a name="rule">Rules</a>

* Rules are similar to [plans](#plan) without the context condition
* Rules cannot be triggered by a goal, so they must be called from a plan
* Rules will be executed with the prefix ```$```
* Rules run immediatly
* Rules run sequentially on default
* Rules returns a boolean value which defines fail (false) and success (true)
* All items results will be concatinate with a logical _and_ to calculate the plan result value
* [Variables](#variable) will be passed, so if a rules succeed the value of the variable will be passed back to the calling plan


### <a name="annotation">Rule / Plan Annotation</a>

* Annotations can modify a plan / rule behaviour to change runtime semantic
* The following annotation can be used
    * ```@Constant( AnyValue, 5 )``` creates the given constant variable
    * ```@Atomic``` the plan / rule cannot be fail, it returns always true (only the [actions](#action) can fail)
    * ```@Parallel``` all items will be run in parallel
 
### <a name="goal">Goals</a>

* Semantically a goal marks a certain state of the world an agent _wishes to bring about_ [AgentSpeak, p.40]
* _Achievement goals_ triggers an _achievement goal addition_ which leads to the execution of a corresponding [plan](#plan)
* On agent start, there can exists one _initial goal_ only (like the ```main``` function in Java, C/C++)
* Each agent can track _more than one goal_ at the same time otherwise the agent idles (the suspending state is not used)
* Goals are triggered by external events which will match by the goal name
* Goals will be resolved into [plans](#plan) with equal name (and allowed context), the [plan](#plan) is the intantiiation of the goal
* Goals are run in parallel independed from other goals
* A goal is a sequence of [plans](#plan) which must all finished successfully
* A goal is part of exactly one [intention](#intention)
* If a goal can match a [desire](#desire) (the goal is near to the desire) it can add an event to match the desire [belief](#belief)
* If the agent is in sleeping / hibernate state and the ```wakeup``` method is called, it triggers the wakeup-goal

#### <a name="testgoal">Test Goals</a>

* A test goal is an atom with the definition ```?literal``` 
* The test return true iif a plan with an equal literal is within the current execution context (current running)


### <a name="intention">Intentions</a>

* An intention is the _convex hull_ of its [goals](#goal)
* The intention is set of of goals, which must exist simultaneously 
* Intentions cannot be in conflict with other intentions, so there dies not exists any overlaping


### <a name="desire">Desires</a>

* A Desire is a vertex of the edge of all intentions
* Desires are defined by a set of beliefs
* Desires can be in conflict with other desires, represented that the desires have got a large distance (much as possible) 
* The desire is successfully reached, iif all beliefs are existing anytime


### <a name="variable">Variables</a>

* Variables are written with an upper-case letter at begin
* Thread-safe variables for parallel runtime start with ```@``` (at-sign) followed by an upper-case letter
* Variables can store a literal or string to call a [rule](#rule) or [plan](#plan) e.g. ```!X(3,2)``` calls a plan or ```$X(2,1)``` calls a rule

### <a name="at-annotation">Action / Term Annotation</a>

* In LightJason one can specify HOW actions and terms will be executed / unified.
* Concept of ```action-term-annotation```s allows to annotate ```actions```, and ```terms``` to perform
    * unification (```>>```)
    * parallel execution (```@```), see [Variables](#variable) and _lambda expressions_.
    * ...
* If more than one ```action-term-annotation``` is needs to be added, they have to be ordered according to the rule: _First HOW, then WHAT_, e.g. ```@>>``` (parallel unification)
* To annotate multiple actions/terms brackets ```(```,```)``` can be used. See the following examples
* Examples
    * ```@>>( foo(X), X > 1 ) && Value > 0.5``` (unify ```foo(X)``` and ```X > 1``` in parallel and if this results in a true statement check whether ```Value > 0.5```)
    * ```>>foo(X) && X > 1 && Value > 0.5``` (unify ```foo(X)```, then test the following terms sequentially)


## <a name="graphic">Graphical Representation</a> 

![Structure](bdi.png)


## Coding - Agent

* Agent (ASL) can be defined as a logic program with [beliefs](#belief), [rules](#rule) and [plans](#plan)
* Agents must be run (triggered) by an external runtime e.g. from an outside system component
* [Plans](#plan) can be bundeled in a _plan-bundle_ which is semantic equal to a class, plan-bundles can be included in an agent


## <a name="cycle">Running Semantics - Agent-Cycle</a>

Semantik definition of Jason see chapter 10.1 [AgentSpeak, p.207]

1. execute perceiving on each beliefbase
    * run _perceive_ on each beliefbase perceiving object
    * add / delete beliefs in the beliefbase
    * beliefbase events will generate

2. if agent is in suspend state stop execution
    
3. run agent cycle

    1. run update of defuzzifcation
    2. collect plans, which match belief-events and goal-events
    3. create plan execution list of instantiated plans
    4. execute instantiated plans in parallel and apply the following rules for each body formula

        1. if an item is an _action_ or _rule_ execute it immediatly
        2. if an item is a _test goal_ check instantiated plans if a plan is found return true otherwise false
        3. if an item is an _achievment goal_ and
            * begins with ```!``` add a new trigger-event for the next cycle to instantiate a possible plan
            * begins with ```!!``` the plan which is matched by the goal is executated immediately within the current plan context          
        4. if an item is a belief-
            * addition, unify the literal and add it into the beliefbase and create a trigger-event
            * deletion, unify the literal and remove it from the beliefbase and create a trigger-event

    5. if a plan is finished check plan result, if it is false / fail create a ```-!goal``` event 

4. increment cycle value


## <a name="devaction">Developing Buildin Actions & Components</a>

* [ODE Solver](https://commons.apache.org/proper/commons-math/javadocs/api-3.6/org/apache/commons/math3/ode/package-summary.html) see [example](http://commons.apache.org/proper/commons-math/userguide/ode.html)
* [Curve Fitting](https://commons.apache.org/proper/commons-math/javadocs/api-3.6/org/apache/commons/math3/optim/package-summary.html)
* [Genetic Algorithm](https://commons.apache.org/proper/commons-math/javadocs/api-3.6/org/apache/commons/math3/genetics/package-summary.html)
* [Base Clustering](https://commons.apache.org/proper/commons-math/javadocs/api-3.6/org/apache/commons/math3/ml/clustering/package-summary.html)
* [FIPA communication interface](http://www.fipa.org/specs/fipa00061/index.html) (encapsulate message parsing) and [FIPA ontology](http://www.fipa.org/specs/fipa00086/index.html) definition
