<#import "spring.ftl" as spring />
<#import "_macros.ftl" as macros />
<#import "_functions.ftl" as functions />
<#assign security=JspTaglibs["/META-INF/security.tld"] />
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta http-equiv="X-UA-COMPATIBLE" content="IE=Edge" />
        <meta name="application-name" content="Steam Tracker" />
        <meta name="author" content="cubeee" />
        <meta name="rating" content="general" />
        <meta name="keywords" content="Steam, playtime, tracker, tracking" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=yes" />
        <meta name="apple-mobile-web-app-capable" content="yes" />
        <meta name="apple-mobile-web-app-status-bar-style" content="blue-translucent" />
        <meta property="og:title" content="Steam Tracker" />
        <meta property="og:site_name" content="Steam Tracker" />
        <meta property="og:description" content="Steam user tracking site" />
        <meta property="og:type" content="website" />
        <title><#if pageTitle??>${pageTitle} - Steam Tracker<#else>Steam Tracker</#if></title>
        <@layout.block name="stylesheets">
        <link rel="stylesheet" type="text/css" href="<@spring.url '/global.css' />" />
        </@layout.block>
        <script type="application/javascript" src="<@spring.url '/global.js' />"></script>
        <@layout.block name="header-scripts">

        </@layout.block>
    </head>
    <body>
        <@layout.block name="navigation">
        <div class="ui grid">
            <!-- todo: mobile navbar? -->
            <div class="row">
                <div class="ui inverted fixed menu navbar page grid">
                    <a href="/" class="brand item">Steam Tracker</a>
                    <div class="right menu">
                        <div class="item">
                            <form class="ui icon input" method="post" action="<@spring.url '/search' />">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                <input class="search-field" name="identifier" placeholder="Search profile" type="text">
                                <i class="search icon"></i>
                            </form>
                        </div>
                        <!-- todo: profile dropdown -->
                    </div>
                </div>
            </div>
        </div>
        </@layout.block>
        <@layout.block name="body"></@layout.block>
    <@layout.block name="scripts">

    </@layout.block>
    </body>
</html>
