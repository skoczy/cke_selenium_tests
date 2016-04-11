'use strict';

var config         = require('../config');
var gulp           = require('gulp');
var gulpif       = require('gulp-if');
var browserSync    = require('browser-sync');

gulp.task('language', function() {
  gulp.src(config.language.src)
    .pipe(gulp.dest(config.language.dest))
    .pipe(gulpif(browserSync.active, browserSync.reload({ stream: true })));
});
