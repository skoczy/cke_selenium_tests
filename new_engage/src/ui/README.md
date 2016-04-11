Sitemaster Angular UI
=====================

A UI for Sitemaster based on AngularJS and Zurb Foundation for Apps.

This setup is based on the boilerplate at https://github.com/jakemmarsh/angularjs-gulp-browserify-boilerplate , with some adjustments, especially for making the setup work together with Foundation for Apps.

## Getting up & running

1. Make sure nodejs and Sass is installed on your machine.
2. Run `npm install` from the root directory
3. Run `gulp dev` (may require installing Gulp globally `npm install gulp -g`)
4. Your browser will automatically be opened and directed to the browser-sync proxy address
5. To prepare assets for production, run the `gulp prod` task (Note: the production task does not fire up the express server, and won't provide you with browser-sync's live reloading. Simply use `gulp dev` during development. More information below)

Now that `gulp dev` is running, the server is up as well and serving files from the `/build` directory. Any changes in the `/app` directory will be automatically processed by Gulp and the changes will be injected to any open browsers pointed at the proxy address.

## Common tasks

- `gulp test` Run the entire test suit
  - `gulp protractor` only runs the protractor tests
  - `gulp unit` only runs the unit tests (this will not exit on completion)
- `gulp dev` Start the local development environment with live reload
- `gulp prod` Builds the entire project into `build/` for packaging

# Testing

Test coverage is divided into unit- and end-to-end testing. [Karma](http://karma-runner.github.io/) and [Jasmine](http://jasmine.github.io/) is used for unit testing, while [Protractor](https://github.com/angular/protractor) and [Jasmine](http://jasmine.github.io/) is used for E2E.

A Gulp tasks exists for running the test framework. Running `gulp test` will run any and all tests inside the `/test` directory and show the results (and any errors) in the terminal.

## Running local Selenium

Prerequisite:
- Java 7
- Chrome
  - With [ChromeDriver](https://sites.google.com/a/chromium.org/chromedriver/downloads)
- Firefox
- [Selenium standalone server](http://www.seleniumhq.org/download/)

## Environment variables

You can adjust the endpoint to run selenium tests against by setting a local system environment value.

`URL='string'` exported to your local environment. Eg. `export URL=http://imgur.com/r/kittens`, will run Selenium against `http://imgur.com/r/kittens`.

Default environmental values are:  
seleniumAddress: http://localhost:4444/wd/hub   
baseUrl: http://localhost:3002

## Running Selenium

#### First option, standalone:

To start Selenium Standalone server with the ChromeDriver open a terminal and type in `java -jar /path/to/selenium-server-standalone-2.48.2.jar -Dwebdriver.chrome.driver=/path/to/chromedriver`.

#### Second option, selenium hub:

First start the hub: `java -jar selenium-server-standalone-2.48.2.jar -role hub` then connect any number of webdrivers as you need. Adjust port and driver as needed.

Firefox:
`java -jar selenium-server-standalone-2.48.2.jar -role webdriver -port 5555 -browser browserName=firefox`

Chrome:
`java -jar selenium-server-standalone-2.48.2.jar -role webdriver -Dwebdriver.chrome.driver=/path/to/chromedriver -port 5556 -browser browserName=chrome`

#### Third option, using docker images
Due to limitations of docker it's recommended to use the local Java-version. But if you don't want to clutter your system, and only need to test one browser at the time you can run a docker container on your host network.

If you're using OSX you also need to update the `seleniumAddress` in `test/protractor.conf.js` to the correct docker-machine. You can obtain the current IP-address from your docker-machine using the `docker-machine` utility. Run `docker-machine ip env`, where `env` is your docker-machine. Typically `dev` or `default`.

To start the container: `docker run -d -p 4444:4444 -v /dev/shm:/dev/shm selenium/standalone-chrome:2.48.2`

# Writing tests
All tests are located in `src/ui/test/{unit|e2e}` and are automatically loaded on each run. To run tests in a certain order either make them as one big test or name them numerically. Eg. `{1..200}-sometest.js`.

Make sure to make descriptive error messages so people can understand what has failed without having to look at the code.

## Protractor (end-to-end)
For the best introduction to [Protractor](https://angular.github.io/protractor/#/tutorial) read the official documentation. You can also generate tests in your broswer using [Selenium Builder](https://saucelabs.com/builder). If you do make sure you remove the browser specific parts in the beginning of the tests. We're setting those elsewhere.

All configuration to protractor is located at `src/ui/test/protractor.conf.js`

### Example test

```
/*global browser, by */

'use strict';

describe('language selector', function() {
  var selector = element(by.model('userLanguage'));

  it('should list available languages', function() {
    browser.get('/#/login');

    var lang = selector.$('option:checked').getText(),
        options = selector.all(by.tagName('option')).getText();

    expect(lang).toBe('English');
    expect(selector).toBeDefined();
    expect(options).toContain('English');
    expect(options).toContain('Русский');
  });

  it('should change the text of the login button', function() {
    browser.get('/#/login');
    var submit = element(by.name('signin'));

    expect(submit.getAttribute('value')).toBe('Sign in');

    element(by.cssContainingText('option', 'Русский')).click();

    expect(selector.$('option:checked').getText()).toBe('Русский');
    expect(selector.$('option:checked').getText()).toBe('Русский');
    expect(submit.getAttribute('value')).toBe('Войти');
  });
});
```

## Karma (unit)

We're using [Karma](http://karma-runner.github.io/0.13/index.html) and [Jasmine-framework](http://jasmine.github.io/1.3/introduction.html) for unit-testing. For the best introduction read the offficial documentation on Jasmine.

Configuration is stored in `src/ui/test/karma.conf.js`.

### Example Test

```
/*global angular */

'use strict';

describe('Unit: Constants', function() {

  var constants;

  beforeEach(function() {
    // instantiate the app module
    angular.mock.module('app');

    // mock the directive
    angular.mock.inject(function(AppSettings) {
      constants = AppSettings;
    });
  });

  it('should exist', function() {
    expect(constants).toBeDefined();
  });

  it('should have an application name', function() {
    expect(constants.appTitle).toEqual('Sitemaster');
  });

});
```
