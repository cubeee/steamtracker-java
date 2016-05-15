'use strict';

var gulp = require('gulp');
var chug = require('gulp-chug');
var runSeq = require('run-sequence');

gulp.task('build_sierra', function() {
    return gulp.src('../../../node_modules/sierra-library/gulpfile.js').pipe(chug());
});

gulp.task('build', function(callback) {
    runSeq('move_sierra', 'build_sierra', 'sass:build', 'js:browserify',  'dist', callback);
});