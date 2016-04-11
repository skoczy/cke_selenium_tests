'use strict';

var istanbul = require('browserify-istanbul');
var isparta  = require('isparta');

if (typeof process.env.URL !== "undefined") {
  // Set env.URL to whatever endpoint you want to run karma against.
  var baseurl = process.env.URL;}
else {
  // For local development and testing
  var baseurl = 'http://localhost:3002/';
}

module.exports = function(config) {

  config.set({

    basePath: '../',
    frameworks: ['jasmine', 'browserify'],
    preprocessors: {
      'app/js/**/*.js': ['browserify', 'babel', 'coverage']
    },
    
    browsers: ['PhantomJS'],
    reporters: ['progress', 'coverage', 'junit'],

    autoWatch: true,

    junitReporter: {
      outputDir: 'test/test-results/karma/', // results will be saved as $outputDir/$browserName.xml
      suite: '', // suite will become the package name attribute in xml testsuite element
      useBrowserName: true // add browser name to report and classes names
    },

    browserify: {
      debug: true,
      transform: [
        'bulkify',
        istanbul({
          instrumenter: isparta,
          ignore: ['**/node_modules/**', '**/test/**']
        })
      ]
    },

    proxies: {
      // '/': 'http://localhost:9876/',
      '/': baseurl,
    },

    urlRoot: '/_karma_/',

    files: [
      // app-specific code
      'app/js/main.js',

      // 3rd-party resources
      'node_modules/angular-mocks/angular-mocks.js',

      // test files
      'test/unit/**/*.js',

      // styles
      'build/css/main.css'
    ]

  });

};
