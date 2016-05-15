'use strict';

var gulp = require('gulp');

gulp.task('move_sierra', function() {
    var src = __dirname + "/../../sierra/_variables.scss";
    var dest = __dirname + "/../../../node_modules/sierra-library/src";
    return gulp.src(src).pipe(gulp.dest(dest));
});