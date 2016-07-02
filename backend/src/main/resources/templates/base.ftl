<#import "spring.ftl" as spring />
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
        <link rel="stylesheet" type="text/css" href="<@spring.url '/css/sierra.min.css' />" />
        <link rel="stylesheet" type="text/css" href="<@spring.url '/css/site.css' />" />
        </@layout.block>
        <@layout.block name="header-scripts">

        </@layout.block>
        <script src="//use.fontawesome.com/89317662ce.js"></script>
    </head>
    <body>
        <@layout.block name="navigation">
        <div class="navigation">
            <div class="row">
                <div class="col-xs-12 col-md-9">
                    <a class="text-medium text-white text-logo" href="/">Steam Tracker</a>
                    <div class="tabs tabs-compact nav">
                        <@layout.block name="navigation-links">

                        </@layout.block>
                    </div>
                </div>
                <div class="search-container col-xs-12 col-md-3 form-row">
                    <form class="form-collapse" method="post" action="<@spring.url '/search' />">
                        <div class="input input-with-icon full-width search">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            <input class="search-field" name="identifier" placeholder="Search profile" type="text">
                            <button class="input-icon fa fa-search fa-lg" type="submit"></button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        </@layout.block>
        <main class="main">
        <@layout.block name="body"></@layout.block>
        </main>
    <@layout.block name="scripts">

    </@layout.block>
    </body>
</html>
