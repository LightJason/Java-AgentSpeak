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
* All items results will be concatinate with a logical __and__ to calculate the plan result value
* On each cycle a plan will be full executed with all subplans

### Rules

* Rules are plans with a context, that uses the default value true

### Goals

* Goals are Rules or plans which are executed by matching the context constraint on the unification process

 
### Intentions
 
* Intentions are a _sequences of plans_
* Each agent can track _more than one intention_
* On each cycle all _current plans_ within the _current intention list_ are run in parallel
* If a plan fails a _repair plan_ is searched and will be the _current plan_ in the next cycle for the intention
* After a plan is finished the intention will be checked if it can be achieved otherwise it fails


## Todos

* define _fixed_ annotions like:
    * _expires_ to remove a plan / belief automatically 
    * _fuzzy_ to define a fuzziness value of a plan / belief
    * _parallel_ to run a plan / parallel
    * _atomic_ to run a plan always with return value true
* define agent cycle    

```java

@Override
public IAgent call() throws Exception
{
    // run beliefbase update, because
    // environment can be changed
    m_beliefbase.update();
    if (m_suspend)
        return this;
        
    // run agent-cycle
    m_intentions.parallelStream().map( i -> i.execute( m_cycle ) );
    m_cycle++;

    // return changed agent
    return this;
}

```