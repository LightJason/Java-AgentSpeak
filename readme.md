# LightJason - AgentSpeak(L++)

![Circle CI](https://img.shields.io/circleci/project/github/LightJason/Java-AgentSpeak.svg) ![Coverage Status](https://img.shields.io/coveralls/github/LightJason/AgentSpeak-Java.svg) ![Maven Central](https://img.shields.io/maven-central/v/org.lightjason/agentspeak.svg)

Based on the project [Jason](http://jason.sourceforge.net/) by Jomi F. Hübner and Rafael H. Bordini an implementation has been build-up with parallel execution calls. The version defines an additional [AgentSpeak(L)](https://en.wikipedia.org/wiki/AgentSpeak) grammar based on [AntLR](http://www.antlr.org/) for simulating a [multi-agent system](https://en.wikipedia.org/wiki/Multi-agent_system) with a fuzzy-based logical calculus and grammar features like lambda expressions. Agent execution based on a mathematical structure to describe an optimizing process by a [finite-state-machine](https://en.wikipedia.org/wiki/Finite-state_machine)


## Base Definitions

### Belief

* Beliefs implicitly describe the current state of the agent
* Beliefs will be updated before the cycle is run (beliefbase uses an update mechanism)
* Beliefs must be exists iif an expression is computed (beliefs can be exist on the fly)
* Belief addition triggers a plan with the definition ```+belief``` 
* Belief retraction triggers a plan with the definition ```-belief```
* Belief modification with ```-+``` does not exists anymore 
* [Variables](#variable) within a belief literal will be unified before the belief is added to the beliefbase


### Action

* Actions will be run immediately
* Actions can fail (fuzzy-logic false) or succeed (fuzzy-logic true)
* There is no difference between internal and external actions
* Actions with ```@```-prefix wil be executed in parallel (each inner action will be run in parallel)


### Plan

* Plans are _sequences of [actions](#action), [rules](#rule) and/or achievement / test [goals](#goal)_
* Plans has got an optional context, that defines a constraint for execution (default is fuzzy-logic true and matches always)
* Plans fail iif the defuzzyfication returns fuzzy-logic false
* Plans returns a boolean value which defines fail (fuzzy-logic  false) and success (fuzzy-logic  true)
* Plans run items in sequential order on default
* If the plan calls an _achievement [goal](#goal) addition_, the [goal](#goal) will be added for the next cycle
* An _achievement [goal](#goal) deletion_ does not exists anymore
    
#### Internals Constants
 
* The plan has got additional [constant variables](#variable), that are added in the context condition (values are calculated before plan execution is started)
    * _PlanFail_ stores the number of fail runs and _PlanFailRatio_ normalized value in [0,1]
    * _PlanSuccessful_ stores the number of successful runs and _PlanSuccessfulRatio_ normalized value in [0,1] 
    * _PlanRuns_ number of runs of the plan (fail + successful runs)
    
#### Fuzziness

* Fuzzy value must be in [0,1]
* Each [action](#action) in a fuzzy-plan returns also a fuzzy value to define the fuzziness
* The [plan](#plan) or [rule](#rule) result returns fuzzy-logic true / false and the aggregated fuzzy value


### Rule

* Rules are similar to [plans](#plan) without the context condition
* Rules cannot be triggered by a goal, so they must be called from a plan
* Rules will be executed with the prefix ```$```
* Rules run immediatly
* Rules run sequentially on default
* Rules returns a fuzzy-logic result for success or fail
* [Variables](#variable) will be passed, so if a rules succeed the value of the variable will be passed back to the calling plan


### Rule / Plan Annotation

* Annotations can modify a plan / rule behaviour to change runtime semantic
* The following annotation can be used
    * ```@Constant( AnyValue, 5 )``` creates the given constant variable
    * ```@Atomic``` the plan / rule cannot be fail, it returns always true (only the [actions](#action) can fail)
    * ```@Parallel``` all items will be run in parallel
 
### Goal

* Semantically a goal marks a certain state of the world an agent _wishes to bring about_ [AgentSpeak, p.40]
* _Achievement goals_ triggers an _achievement goal addition_ which leads to the execution of a corresponding [plan](#plan)
* On agent start, there can exists one _initial goal_ only (like the ```main``` function in Java or C/C++)
* Each agent can track _more than one goal_ at the same time otherwise the agent idles (the suspending state is not used)
* Goals are triggered by external events which will match by the goal name
* Goals will be resolved into [plans](#plan) with equal name (and allowed context), the [plan](#plan) is the intantiiation of the goal
* Goals are run in parallel independed from other goals
* A goal is a sequence of [plans](#plan) which must all finished completly
* A goal is part of exactly one [intention](#intention)
* If a goal can match a [desire](#desire) (the goal is near to the desire) it can add an event to match the desire [belief](#belief)
* If the agent is in sleeping / hibernate state and the ```wakeup``` method is called, it triggers the wakeup-goal

#### Test Goals

* A test goal is an atom with the definition ```?literal``` 
* The test return true iif a plan with an equal literal is within the current execution context (current running)


### Intention

* An intention is the _convex hull_ of its [goals](#goal)
* The intention is set of of goals, which must exist simultaneously 
* Intentions cannot be in conflict with other intentions, so there dies not exists any overlaping


### Desire

* A Desire is a vertex of the edge of all intentions
* Desires are defined by a set of beliefs
* Desires can be in conflict with other desires, represented that the desires have got a large distance (much as possible) 
* The desire is successfully reached, iif all beliefs are existing anytime


### Variable

* Variables are written with an upper-case letter at begin
* Thread-safe variables for parallel runtime start with ```@``` (at-sign) followed by an upper-case letter
* Variables can store a literal or string to call a [rule](#rule) or [plan](#plan) e.g. ```!X(3,2)``` calls a plan or ```$X(2,1)``` calls a rule

### Action / Term Annotation

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


## Graphical Representation

![Structure](https://raw.githubusercontent.com/LightJason/AgentSpeak/master/bdi.png)
