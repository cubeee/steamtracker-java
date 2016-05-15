'use strict';

module.exports = {
    sass: {
        src: './src/sass/**/*.scss',
        dest: './build/dist/static/css',
        includePaths: [
            './src/css/'
        ]
    },
    js: {
        src: './src/js/**/*.js',
        dest: './build/dist/static/js'
    },
    css: {
        dest: './build/dist/static/css'
    }
};