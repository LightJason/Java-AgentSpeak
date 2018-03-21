# Contributing

We welcome any constructive feedback.

If you think there is anything missing or wish to contribute to our documentation feel free to [contact us](https://lightjason.github.io/contact/).
For some general discussion and fast answering just use our [discussion board](https://gitter.im/LightJason) so you can send us a message immediately.
Within this board you can get some tips to use your framework in our own projects and we try to help you to get your working simulation.

On your request / proposal we will check your content and send a feedback to you or we include the content directly. All the content must use a
[LGPL license](https://en.wikipedia.org/wiki/GNU_Lesser_General_Public_License) and a [CC BY-SA 4.0 license](https://creativecommons.org/licenses/by-sa/4.0/)


## Example / Tutorial Contribution

Currently we are working on the basic implementation of the framework, so our examples are shown only some simple test-cases. If you have some
ideas, test-cases or tutorials we can publish your work on the website with your name. Do the following steps to send us your proposal:

1. Fork our [example repository](https://github.com/LightJason/Examples).
2. Create a new branch with your source code.
3. Build full working [Maven project](https://maven.apache.org/) for your code.
4. Analyse the code with our styleguide and bug metric:
    1. Use [Checkstyle](http://checkstyle.sourceforge.net/) to check our styleguide.
    2. A configuration file can be found in the [AgentSpeak repository](https://github.com/LightJason/AgentSpeak/tree/master/src/analysis/checkstyle) and can be run manually,
       or you can put [these lines](https://github.com/LightJason/AgentSpeak/blob/master/pom.xml#L602-L623) to your ```pom.xml``` to run it automatically.
    4. Configure your compiler settings with [these lines](https://github.com/LightJason/AgentSpeak/blob/master/pom.xml#L535-L545), so that all warnings are reported.
    5. Use [FingBugs](http://findbugs.sourceforge.net/) with [these lines](https://github.com/LightJason/AgentSpeak/blob/master/pom.xml#L276-L286).
    6. Check your bugs with a report.
5. Fork our [website repository](https://github.com/LightJason/lightjason.github.io) .
6. Put your description as draft under ```content/tutorials``` in a [Markdown](https://en.wikipedia.org/wiki/Markdown) file.
7. Send us both changes as a [pull-request](https://help.github.com/articles/creating-a-pull-request/) back.


## Teaching Contribution

We are very interested in teaching aspects of your framework. So we would like to optimize our explanation on the website for understanding
multi-agent systems (and the programming aspects). Our main goal is a very straight understandable way of muti-agent systems and their usage.
If you find anything understandable on your website or put some new (didactic) elements use the following steps:

1. Fork our [website repository](https://github.com/LightJason/lightjason.github.io).
2. Put your description as draft under ```content/tutorials``` or ```content/knowledgebase``` in a [Markdown](https://en.wikipedia.org/wiki/Markdown) file.
3. Check your changes, so it will work and won't break the automatic builds.
4. Send us the changes as a [pull-request](https://help.github.com/articles/creating-a-pull-request/).


## Issue Contribution

Our framework is still under development and we are glad about any reported issue.
Please take to heart how to report an issue:

* Describe the issue as precisely as possible.
* Put some example code to the issue e.g. a failing unit-test or an example program, so we can reproduce the problem.
* Test your example with the current master (release) branch whether the issue exists only in the current developing branch.
* If you found out, why the issue occurs, just link / reference in your description the code lines which are not working correctly.
* Before you commit your issue, check if there is not another issue which covers the same problem (avoid duplication) and put your description into the discussion of the exiting issue. If your issue covers a similar problem please reference it in the related issue.

You can use the following template for submitting issues:

```
### Expected behaviour and actual behaviour.

### Steps to reproduce the problem.

### Specifications like the version of the project, operating system, or hardware.
```

## Pull-Request Contribution

Any new ideas, helpful code extensions or bug fixes are very helpful for us, so please take heart to sending pull-requests.

* Make sure, that all unit-tests work with your current code. If a test is not working correctly after your changes, make sure that your contribution is indeed correct before changing existing tests.
* Follow our automatic styleguide checks and test your code with [FingBugs](http://findbugs.sourceforge.net/) to remove any errors.
* Before you send the pull-request, keep track with the current master branch and remove any conflict.
* Put a meaningful description in your pull-request, so we can understand what the problem is and what you have done to solve it.
