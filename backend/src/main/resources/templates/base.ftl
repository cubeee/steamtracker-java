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
        <meta name="theme-color" content="#2c3e50" />
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
        <div class="ui inverted vertical segment">
            <div class="ui grid">
                <div class="twelve wide centered column" style="padding-top: 0; padding-bottom: 0;">
                    <div class="ui huge inverted menu">
                        <div class="header item"><a href="<@spring.url '/' />">SteamTracker</a></div>
                        <div class="right menu">
                            <div class="item">
                                <form class="ui icon input" method="post" action="<@spring.url '/search' />">
                                    <div class="ui transparent inverted icon input">
                                        <i class="search icon"></i>
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                        <input type="text" name="identifier" placeholder="Search player">
                                    </div>
                                </form>
                            </div>
                            <#if functions.authenticated()>
                                <#assign identifier="${functions.authenticated_field('principal.identifier')}" />
                                <a class="item" href="<@spring.url '/player/${identifier}/' />">Profile</a>
                                <a class="item" href="<@spring.url '/auth/logout/' />">Log out</a>
                            <#else>
                            <div class="item">
                                <@macros.steam_auth_img />
                            </div>
                            </#if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        </@layout.block>

        <@layout.block name="body"></@layout.block>

        <@layout.block name="footer">
        <div class="ui inverted vertical masthead center aligned segment">
            <!-- TODO: fill -->
            <p>SteamTracker</p>
        </div>
        </@layout.block>
        <@layout.block name="scripts"></@layout.block>
        <@layout.block name="analytics">
            <@macros.google_analytics_script id=googleAnalyticsId />
        </@layout.block>
    </body>
</html>
