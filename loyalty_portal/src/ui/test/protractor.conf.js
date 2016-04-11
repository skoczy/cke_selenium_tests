'use strict';

// Load env variables to use right endpoint
// Set env.URL to whatever endpoint you want to run protractor against.
if (typeof process.env.URL !== "undefined") {
    var baseurl = process.env.URL;
    var seleniumaddress = 'http://selenium-hub.docker:4444/wd/hub';
}
else {
    var baseurl = 'http://localhost:3002/';
    var seleniumaddress = "http://localhost:4444/wd/hub";
}

exports.config = {

    onPrepare: function () {
        var reporters = require('jasmine-reporters');
        var junitReporter = new reporters.JUnitXmlReporter({
            savePath: 'test-results/protractor/',
            consolidateAll: false
        });
        jasmine.getEnv().addReporter(junitReporter);
    },

    seleniumAddress: seleniumaddress,
    baseUrl: baseurl,

    allScriptsTimeout: 110000,
    capabilities: {
        browserName: 'chrome', // or firefox
        version: '',
        platform: 'ANY'
    },

    framework: 'jasmine',

    jasmineNodeOpts: {
        isVerbose: false,
        showColors: true,
        includeStackTrace: true,
        defaultTimeoutInterval: 300000
    },

    specs: [
        'e2e/01_login_to_LOYALTY.js',
		//'e2e/31_order_new_card.js',
		//'e2e/32_block_card.js',
		//'e2e/05_opening_hours.js',
		//'e2e/05_1_opening_hours.js',
		//'e2e/05_2_opening_hours_temp.js',
		//'e2e/07_changelog.js',
		//'e2e/08_XY_coordinates.js',
		//'e2e/11_adding_new_service.js',
		//'e2e/15_adding_new_fuel.js',
    ]

};
