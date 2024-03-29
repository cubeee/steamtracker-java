var webpack = require('webpack'),
    ExtractTextPlugin = require('extract-text-webpack-plugin'),
    OptimizeCssAssetsPlugin = require('optimize-css-assets-webpack-plugin'),
    FailPlugin = require('webpack-fail-plugin'),
    path = require("path");

module.exports = {
    entry: {
        global: ['./src/index.less', './src/index.js']
    },
    resolve: {
        extensions: [
            '.webpack.js',
            '.js',
            '.less'
        ]
    },
    output: {
        path: path.resolve(__dirname, 'build', 'dist', 'static'),
        publicPath: '/',
        filename: '[name].js'
    },
    module: {
        rules: [
            {
                test: /\.less$/,
                loader: ExtractTextPlugin.extract({fallback: 'style-loader', use: 'css-loader!less-loader'})
            },
            {
                test: /\.(png|jpg|gif)$/,
                loader: 'file-loader?name=images/[name].[ext]'
            },
            {
                test: /\.(eot|svg|ttf|otf|woff|woff2)?$/,
                loader: 'file-loader?name=fonts/[name].[ext]'
            }
        ]
    },
    plugins: [
        new ExtractTextPlugin({filename: '[name].css', allChunks: true}),
        new OptimizeCssAssetsPlugin({
            cssProcessorOptions: {
                autoprefixer: {add: true},
                calc: {add: true},
                colormin: {add: true},
                convertValues: {add: true},
                discardDuplicates: {add: true},
                discardEmpty: {add: true},
                discardUnused: {add: true},
                mergeIdents: {add: true},
                mergeLonghand: {add: true},
                mergeRules: {add: true},
                minifyFontValues: {add: true},
                minifyGradients: {add: true},
                minifySelectors: {add: true},
                normalizeCharset: {add: true},
                normalizeUrl: {add: true},
                reduceTransforms: {add: true},
                uniqueSelectors: {add: true},
                zindex: {add: true}
            }
        }),
        new webpack.ProvidePlugin({
            $: 'jquery',
            jQuery: 'jquery',
            'window.jQuery': 'jquery'
        }),
        FailPlugin
    ]
};