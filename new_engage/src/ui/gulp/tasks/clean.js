'use strict';

var config = require('../config');
var gulp   = require('gulp');
var del    = require('del');
var rimraf = require('rimraf');

gulp.task('clean', function(cb) {

  rimraf(config.dist.root, cb);

});
