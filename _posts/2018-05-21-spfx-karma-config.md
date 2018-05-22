---
layout: post
title:  "SharePoint Framework(SPFx). Custom Karma configuration for different test result and coverage reports."
date:   2018-05-21 12:07:00 -0400
categories: spfx react karma
---
### Introduction
This post will describe how to customize [Karma](http://karma-runner.github.io/2.0/index.html) configuration for [SharePoint Framework (SPFx)](https://docs.microsoft.com/en-us/sharepoint/dev/spfx/sharepoint-framework-overview) web part projects with [Office UI Fabric React](https://github.com/OfficeDev/office-ui-fabric-react)  created/generated with [Yoman SharePoint Generator](https://docs.microsoft.com/en-us/sharepoint/dev/spfx/toolchain/scaffolding-projects-using-yeoman-sharepoint-generator) including:
 - Generate test result report in [JUnit](https://junit.org/) format (helpful for [VSTS CI dashboard](https://docs.microsoft.com/en-us/vsts/build-release/tasks/test/publish-test-results?view=vsts))
 - Generate test coverage report in text (console) format
 - Generate test coverage report in [Cobertura](http://cobertura.github.io/cobertura/) format (helpful for [VSTS CI dashboard](https://docs.microsoft.com/en-us/vsts/build-release/tasks/test/publish-code-coverage-results?view=vsts))
 
<br/>
### Steps  

<br/>
#### 1. Create SPFx React web part project (skip if you already have one)
Create your SharePoint Framework React web part project using the instructions/steps from ["Scaffold projects by using Yeoman SharePoint generator"](https://docs.microsoft.com/en-us/sharepoint/dev/spfx/toolchain/scaffolding-projects-using-yeoman-sharepoint-generator).

<br/>
#### 2. Create/run your unit test(s)
Create your unit tests and run them by executing

{% highlight bash %}

npm test

{% endhighlight %}

You should get list of successfully executed uni tests, ex:
{% highlight bash %}

Start:
  HelooWorld init test
    √ simple test

Finished in 0.016 secs / 0.001 secs

SUMMARY:
√ 1 tests completed
21 05 2018 20:54:07.569:DEBUG [karma]: Run complete, exiting.

{% endhighlight %}

<br/>
#### 3. Custom Karma config setup
The default configuration of Karma test runner comes with `sp-build-web` package and is available 
inside `node_modules` folder:
```
@microsoft/sp-build-web/lib/karma/karma.config
``` 

In order to customize the default/predefined configuration a custom config needs to be created.

Create custom Karma config file `karma.config.js` inside folder `<your_project_folder>/config`:

{% highlight js %}

"use strict";
const existingKarmaConfig = require('@microsoft/sp-build-web/lib/karma/karma.config');
module.exports = function (config) {
    existingKarmaConfig(config);
        config.set({
        });
};

{% endhighlight %}

We inherit our custom configuration from the default one. In order to enable new custom Karma config file we need to update `gulp.js` config. Add the following lines to `gulp.js`:

{% highlight js %}

// Extending Karma
const karmaTask = build.karma;
if (karmaTask) {
  karmaTask.taskConfig.configPath = './config/karma.config.js';
}

{% endhighlight %}

<br/>
#### 4.  Setup JUnit reporter
Install `karma-junit-reporter` package as Dev dependency:
{% highlight bash %}

npm i karma-junit-reporter --save-dev

{% endhighlight %}

The following steps needs to be done to enable JUnit reporter in Karma:
 - add JUnit reporter to the list of configured reporters
 - provide JUnit reporter configuration
 - add karma-junit-reporter plugin to the list of configured plugins

The below lines of karma.config.js combine all three steps:

{% highlight js %}

config.set({
        
        reporters: ['test-result', 'mocha-clean', 'coverage', 'junit'],
        
        junitReporter: {
            outputDir: 'temp/testResult' // results will be saved as $outputDir/$browserName.xml
          },

        plugins: config.plugins.concat([
            require('karma-junit-reporter')
        ])
    });

{% endhighlight %}

Run
{% highlight bash %}

npm test

{% endhighlight %}

Once executed check folder `<your_project/>temp/testResult` - it will contain file `TESTS-PhantomJS_*.xml` which contains test results in JUnit format.

<br/>
#### 5.  Setup console coverage reporter
In order to diaplsy test coverage report in text/console format we need to expand the list 
of preconfigured coverage reporters (`html` and `json`). Add the following configuration to karma.config.js:

{% highlight js %}
coverageReporter: {                        
            dir: 'temp\\coverage',
            reporters: [
                { type: 'html', subdir: 'js' },
                { type: 'json', subdir: './', file: 'js-coverage.json' },
                { type: 'text' },
                { type: 'text-summary' }]
        },

{% endhighlight %}

Run
{% highlight bash %}

npm test

{% endhighlight %}

and your console output will contain full coverage report, ex:

{% highlight bash %}

----------------------------|----------|----------|----------|----------|----------------|
File                        |  % Stmts | % Branch |  % Funcs |  % Lines |Uncovered Lines |
----------------------------|----------|----------|----------|----------|----------------|
 components\                |       84 |    71.43 |       75 |     91.3 |                |
  HelloWorld.js             |    80.95 |    71.43 |       75 |    89.47 |           5,22 |
  HelloWorld.module.scss.js |      100 |      100 |      100 |      100 |                |
----------------------------|----------|----------|----------|----------|----------------|
All files                   |       84 |    71.43 |       75 |     91.3 |                |
----------------------------|----------|----------|----------|----------|----------------|

=============================== Coverage summary ===============================
Statements   : 84% ( 21/25 )
Branches     : 71.43% ( 10/14 )
Functions    : 75% ( 6/8 )
Lines        : 91.3% ( 21/23 )
================================================================================
21 05 2018 22:22:12.787:DEBUG [karma]: Run complete, exiting.

{% endhighlight %}

<br/>
#### 6.  Setup Cobertura coverage reporter
Since Karma has built-in Cobertura coverage reporter the setup is similar to the previous step: add cobertura reporter to the list of configured coverage reporters in karma.config.js:

{% highlight js %}
coverageReporter: {                        
            dir: 'temp/coverage',
            reporters: [
                { type: 'html', subdir: 'js' },
                { type: 'json', subdir: './', file: 'js-coverage.json' },
                { type: 'text' },
                { type: 'text-summary' },
                { type: 'cobertura', subdir: './', file: 'cobertura.xml' }]
        },

{% endhighlight %}

Run
{% highlight bash %}

npm test

{% endhighlight %}

Once executed check folder `<your_project/>temp/coverage` - it will contain file `cobertura.xml` which contains test coverage results in Cobertura format.

<br/>
#### 7.  Enjoy your test and test coverage reports!
Full source of karma.config.js is below:
{% highlight bash %}

"use strict";
const existingKarmaConfig = require('@microsoft/sp-build-web/lib/karma/karma.config');

module.exports = function (config) {
    existingKarmaConfig(config);

    config.set({

        reporters: ['test-result', 'mocha-clean', 'coverage', 'junit'],

        coverageReporter: {                        
            dir: 'temp\\coverage',
            reporters: [
                { type: 'html', subdir: 'js' },
                { type: 'json', subdir: './', file: 'js-coverage.json' },
                { type: 'text' },
                { type: 'text-summary' },
                { type: 'cobertura', subdir: './', file: 'cobertura.xml' }]
        },

        junitReporter: {
            outputDir: 'temp\\testResult' // results will be saved as $outputDir/$browserName.xml            
        },

        plugins: config.plugins.concat([
            require('karma-junit-reporter')
        ])
    });

};

{% endhighlight %}
{% include custom_footer.html %}
