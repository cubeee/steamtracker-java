'use strict';

var gulp = require('gulp');

var src = __dirname + "/../../sierra/**";
var dest = __dirname + "/../../../node_modules/sierra-library/src";

gulp.task('sierra:move', function() {
    return gulp.src(src).pipe(gulp.dest(dest));
});

gulp.task('sierra:watch', function() {
    return gulp.watch(src, ['sierra:move', 'sierra:build', 'dist']);
});