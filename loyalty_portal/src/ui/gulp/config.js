'use strict';

module.exports = {

  'browserport'  : 3000,
  'uiport'       : 3001,
  'serverport'   : 3002,

  'styles': {
    'src' : 'app/styles/*.scss',
    'dest': 'build/css',
    'cssIncludes': [
      'node_modules/angular-ui-grid',
      'node_modules/angular-busy/dist'
    ],
    'includePaths': [
      'node_modules/foundation-apps/scss',
      'node_modules/pikaday/scss',
      'node_modules/angular-ui-grid'
    ]
  },

  'scripts': {
    'src' : 'app/js/**/*.js',
    'dest': 'build/js'
  },

  'foundation': {
    'components': {
      'src': 'node_modules/foundation-apps/js/angular/components/**/*',
      'dest': 'build/components'
    },
    'dependencies': {
      'src': [
        'node_modules/tether/dist/js/tether.js',
        'node_modules/hammerjs/hammer.js'
      ],
      'dest': 'build/js'
    }
  },

  'data': {
    'src': 'data/**/*',
    'dest': 'build/data'
  },

  'language': {
    'src': 'app/language/*.json',
    'dest': 'build/language'
  },

  'images': {
    'src' : 'app/images/**/*',
    'dest': 'build/images'
  },

  'fonts': {
    'src' : [
      'app/fonts/**/*',
      'node_modules/angular-ui-grid/*.{eot,svg,ttf,woff}'
    ],
    'dest': 'build/fonts'
  },

  'views': {
    'watch': [
      'app/index.html',
      'app/views/**/*.html'
    ],
    'src': 'app/views/**/*.html',
    'dest': 'app/js'
  },

  'gzip': {
    'src': 'build/**/*.{html,xml,json,css,js,js.map}',
    'dest': 'build/',
    'options': {}
  },

  'dist': {
    'root'  : 'build'
  },

  'browserify': {
    'entries'   : ['./app/js/main.js'],
    'bundleName': 'main.js',
    'sourcemap' : true
  },

  'test': {
    'karma': 'test/karma.conf.js',
    'protractor': 'test/protractor.conf.js'
  }
};
