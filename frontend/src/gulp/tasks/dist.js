'use strict';

var gulp = require('gulp'),
    fs = require('fs'),
    config = require('../config.js');

var filesToMove = {
    '../../../node_modules/sierra-library/dist/sierra.min.css': '../../../' + config.sass.dest
};

gulp.task('dist', function() {
    for(var key in filesToMove) {
        var src = __dirname + "/" + key;
        gulp.src(src)
            .pipe(gulp.dest(__dirname + "/" + filesToMove[key]));
    }
});