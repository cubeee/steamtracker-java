'use strict';

var config = require('../config.js'),
    gulp = require('gulp'),
    sass = require('gulp-sass'),
    minify = require('gulp-minify-css');

gulp.task('sass:build', function() {
   return gulp.src(config.sass.src)
       .pipe(sass().on('error', sass.logError))
       .pipe(minify({
           processImport: false
       }))
       .pipe(gulp.dest(config.sass.dest));
});

gulp.task('sass:watch', function() {
   return gulp.watch(config.sass.src, ['sass:build']);
});