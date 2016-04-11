'use strict';

var gulp            = require('gulp');
var protractor      = require('gulp-protractor').protractor;
var config          = require('../config');

gulp.task('protractor', ['prod', 'server'], function() {

  return gulp.src('test/e2e/**/*.js')
    .pipe(protractor({
        configFile: config.test.protractor,
    }))
    .on('end', function() {
        console.log('E2E Testing complete');
        process.exit();
    })
    .on('error', function(error) {
        console.log('E2E Tests failed');
        process.exit(1);
    });
});
