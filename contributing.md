# Contributing

We welcome any constructive feedback.

If you think there is anything missing or wish to contribute to our documentation feel free to [contact us](https://lightjason.github.io/contact/).
For some general discussion and fast answering just use our [discussion board](https://gitter.im/LightJason) so you can send us a message immediatly.
Within this board you can get some tips to use your framework in our own projects and we try to help you to get your working simulation.

On your request / proposal we will check your content and send a feedback to you or we include the content directly. All the content must use a
[LGPL license](https://en.wikipedia.org/wiki/GNU_Lesser_General_Public_License) and a [CC BY-SA 4.0 license](https://creativecommons.org/licenses/by-sa/4.0/)


## Example / Tutorial Contribution

Currently we are working on the basic implementation of the framework, so our examples are shown only some simple test-cases. If you have some
ideas, test-cases or tutorials we can publish your work on the website with your name. Do the following steps to send us your proposal:

1. fork our [example repository](https://github.com/LightJason/Examples)
2. create a new branch with your source code
3. build full working [Maven project](https://maven.apache.org/) on your code
4. analyse the code the code with our styleguide and bug metric
    1. use [Checkstyle](http://checkstyle.sourceforge.net/) to check our styleguide
    2. configuration file is found in the [AgentSpeak repository](https://github.com/LightJason/AgentSpeak/tree/master/src/analysis/checkstyle) and can be run manually
    3. or you can put [these lines](https://github.com/LightJason/AgentSpeak/blob/master/pom.xml#L602-L623) to your ```pom.xml``` to run it automatically
    4. configurate your compiler settings with [these lines](https://github.com/LightJason/AgentSpeak/blob/master/pom.xml#L535-L545), so that all warnings are reported
    5. use [FingBugs](http://findbugs.sourceforge.net/) with [these lines](https://github.com/LightJason/AgentSpeak/blob/master/pom.xml#L276-L286)
    6. and check your bugs with a report
5. fork your [website repository](https://github.com/LightJason/lightjason.github.io) 
6. put your description as draft under ```content/tutorials``` in a [Markdown](https://en.wikipedia.org/wiki/Markdown) file
7. send us both changes as a [pull-request](https://help.github.com/articles/creating-a-pull-request/) back


## Teaching Contribution

We are very interessed in teaching aspects of your framework. So we would like to optimize our explanation on the website for understanding
multi-agent systems (and the programming aspects). Our main goal is a very straight understandable way of muti-agent systems and their usage.
If you find anything understandable on your website or put some new (didactic) elements use the following steps:

1. fork your [website repository](https://github.com/LightJason/lightjason.github.io)
2. put your description as draft under ```content/tutorials``` or ```content/knowledgebase``` in a [Markdown](https://en.wikipedia.org/wiki/Markdown) file
3. check your changes, so it will work and fit the automatically builds
4. send us the changes as a [pull-request](https://help.github.com/articles/creating-a-pull-request/) back


## Issue Contribution

Our framework is still under developing and we are glad about any issue reporting, please take to heart how to report an issue

* Describe your issue precisely as possible
* Put some example code to the issue e.g. a failing unit-test or an example program, so we can reproduce the problem
* Test your example with the current master branch / developing version to check, if the issue exits on the current developing
* If you can found out, why the issue occures, just link / reference in your description the code lines which are not working correctly
* Before you commit your issue, check out if there is not another issue which is referencend to the same / similar problem (avoid duplication), put your description
to the exiting disscussion

## Pull-Request Contribution

Any new idea or helpful code extension or bug fixing is very helpful for us, please take heart to send the pull-request

* Keep on track, that all unit-tests should work with your current code, if a test is not working corretly after your changes, just fix it also
* Follwing our automatically styleguide checking and test your code with [FingBugs](http://findbugs.sourceforge.net/) to remove any errors
* Before you send the pull-request keep track with the current master branch and remove any conflict
* Put some description lines on your pull-request, so we can understand what the problem is and what you have done to solve it
