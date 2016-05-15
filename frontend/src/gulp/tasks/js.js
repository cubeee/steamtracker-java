'use strict';

var config = require('../config.js'),
    browserify = require('browserify'),
    buffer = require('vinyl-buffer'),
    es = require('event-stream'),
    glob = require('glob'),
    gulp = require('gulp'),
    path = require('path'),
    source = require('vinyl-source-stream'),
    uglify = require('gulp-uglify');

gulp.task('js:browserify', function(done) {
    glob(config.js.src, function(err, files) {
        if (err) {
            done(err);
        }

        var tasks = files.map(function(entry) {
            return browserify({
                entries: [entry]
            })
            .bundle()
            .pipe(source(path.basename(entry)))
            .pipe(buffer())
            .pipe(uglify({
                compress: true,
                mangle: true
            }))
            .pipe(gulp.dest(config.js.dest));
        });
        es.merge(tasks).on('end', done);
    });
});

gulp.task('js:watch', function() {
    return gulp.watch(config.js.src, ['js:browserify']);
});