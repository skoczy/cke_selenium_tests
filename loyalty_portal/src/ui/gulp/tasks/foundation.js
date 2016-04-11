'use strict';

var config         = require('../config');
var gulp           = require('gulp');
var gulpif         = require('gulp-if');
var uglify         = require('gulp-uglify');
var concat         = require('gulp-concat');

// Foundation task
gulp.task('foundation', function() {
  // Create the dependency file that needs to be included manually
  gulp.src(config.foundation.dependencies.src)
    .pipe(concat('dependencies.js'))
    .pipe(gulpif(global.isProd, uglify()))
    .pipe(gulp.dest(config.foundation.dependencies.dest));

  // Copy all component files
  return gulp.src(config.foundation.components.src)
    .pipe(gulp.dest(config.foundation.components.dest));
});
