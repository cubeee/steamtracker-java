'use strict';

var gulp = require('gulp');
var runSeq = require('run-sequence');
var which = require('which').sync;
var spawn = require('child_process').spawn;

gulp.task('sierra:build', function(callback) {
    var sierraDir = __dirname + '/../../../node_modules/sierra-library';
    spawn(which('npm'), ['--prefix', sierraDir, 'install', sierraDir], { stdio: 'inherit' }).on('exit', function(failed) {
        if (!failed) {
            spawn(which('gulp'), ['--cwd', sierraDir, 'build'], { stdio: 'inherit' }).on('exit', callback);
        }
    });
});

gulp.task('build', function(callback) {
    runSeq('sierra:build', 'sierra:move', 'sass:build', 'js:browserify',  'dist', callback);
});