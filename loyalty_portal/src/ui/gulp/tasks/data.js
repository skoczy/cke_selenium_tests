'use strict';

var gulp        = require('gulp');
var config      = require('../config');
var gulpif      = require('gulp-if');
var browserSync = require('browser-sync');

gulp.task('data', function() {

  // Copy all data files
  return gulp.src(config.data.src)
    .pipe(gulp.dest(config.data.dest))
    .pipe(gulpif(browserSync.active, browserSync.reload({ stream: true })));


});
