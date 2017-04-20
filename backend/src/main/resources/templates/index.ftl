<@layout.extends name="base.ftl">
    <#import "spring.ftl" as spring />
    <#import "_macros.ftl" as macros />
    <#import "_functions.ftl" as functions />
    <#assign security=JspTaglibs["/META-INF/security.tld"] />

    <@layout.put block="navigation" type="replace"></@layout.put>

    <@layout.put block="body">
    <style>
        .masthead.segment {
            min-height: 10em;
            padding: 1em 0em;
        }
        .masthead .logo.item img {
            margin-right: 1em;
        }
        .masthead .ui.menu .ui.button {
            margin-left: 0.5em;
        }
        .masthead h1.ui.header {
            margin-top: 1em;
            margin-bottom: 0;
            font-size: 4em;
            font-weight: normal;
        }
        .masthead h2 {
            margin-bottom: 1.5em;
            font-size: 1.7em;
            font-weight: normal;
        }
    </style>

    <div class="pusher">
        <div class="ui inverted vertical masthead center aligned segment">
            <div class="ui large secondary inverted pointing menu">
                <div class="ui container">
                <#if functions.authenticated()>
                    <#assign avatar="${functions.authenticated_field('principal.avatarMedium')}" />
                    <#assign identifier="${functions.authenticated_field('principal.identifier')}" />
                    <#assign username="${functions.authenticated_field('principal.username')}" />
                    <!--<a class="item" href="/">Home</a>

                    <div class="right item">
                        <a class="item" href="<@spring.url '/player/${identifier}/' />">Profile</a>
                        <a class="item" href="<@spring.url '/auth/logout/' />">Sign out</a>
                    </div>-->
                    <div class="ui inverted menu">
                        <a class="active item">Home</a>
                        <div class="right menu">
                            <a class="ui item">Sign out</a>
                        </div>
                    </div>
                <#else>
                    <div class="right item">
                        <@macros.steam_auth_button />
                    </div>
                </#if>
                </div>
            </div>
            <div class="ui text container">
                <h1 class="ui inverted header">SteamTracker</h1>
                <h2>Track your Steam profile progression</h2>
                <div class="ui ten column centered grid">
                    <div class="ui twelve wide column">
                        <form class="ui form" method="post" action="<@spring.url '/search' />">
                                <div class="fields">
                                    <div class="sixteen wide centered field">
                                    <div class="ui fluid big icon input">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                        <input class="prompt" name="identifier" type="text" placeholder="Steam profile url, id or custom name">
                                        <i class="search icon"></i>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <div class="ui vertical segment">
            <div class="ui container">
                <div class="ui center aligned equal width grid">
                    <div class="ui centered column">
                        <a class="ui large grey image label">
                            ${tracked_players}
                            <div class="detail">Players tracked</div>
                        </a>
                    </div>
                    <div class="ui centered column">
                        <a class="ui large grey image label">
                            <@macros.timePlayed mins=collective_tracked detailed=false />
                            <div class="detail">Collective hours tracked</div>
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <div class="main ui relaxed padded stackable centered grid">
            <div class="four wide column">
                <h3 class="text-medium">Most played in the last 24 hours</h3>
                <#if todays_played?size gt 0>
                    <table class="ui celled table game-table main-game-table">
                        <tbody>
                            <@macros.filledTable fillEmpty=fillTables columns=1 rows=gamesInTables curRows=todays_played?size>
                                <#list todays_played as game>
                                <tr>
                                    <td>
                                        <h4 class="ui image header">
                                            <!--<img src="/images/avatar2/small/lena.png" class="ui mini rounded image">-->
                                            <@macros.gameImg id=game.game.appId url=game.game.iconUrl classes="ui mini rounded image" />
                                            <div class="content">
                                            ${game.game.name}
                                                <div class="sub header" title="${game.minutesPlayed} minutes">
                                                    <@macros.timePlayed mins=game.minutesPlayed />
                                                </div>
                                            </div>
                                        </h4>
                                    </td>
                                </tr>
                                </#list>
                            </@macros.filledTable>
                        </tbody>
                    </table>
                </#if>
            </div>
            <div class="four wide column">
                <h3 class="text-medium">Most played in the last 7 days</h3>
                <#if weeks_played?size gt 0>
                <table class="ui celled table game-table main-game-table">
                    <tbody>
                        <@macros.filledTable fillEmpty=fillTables columns=2 rows=gamesInTables curRows=weeks_played?size fillEmpty=fillTables>
                            <#list weeks_played as game>
                            <tr>
                                <td>
                                    <h4 class="ui image header">
                                        <!--<img src="/images/avatar2/small/lena.png" class="ui mini rounded image">-->
                                        <@macros.gameImg id=game.game.appId url=game.game.iconUrl classes="ui mini rounded image" />
                                        <div class="content">
                                        ${game.game.name}
                                            <div class="sub header" title="${game.minutesPlayed} minutes">
                                                <@macros.timePlayed mins=game.minutesPlayed />
                                            </div>
                                        </div>
                                    </h4>
                                </td>
                            </tr>
                            </#list>
                        </@macros.filledTable>
                    </tbody>
                </table>
                </#if>
            </div>
            <div class="four wide column">
                <h3 class="text-medium">Most played</h3>
                <#if most_played?size gt 0>
                <table class="ui celled table game-table">
                    <tbody>
                        <@macros.fixedSizeTable columns=2 rows=gamesInTables curRows=most_played?size>
                            <#list most_played as game>
                            <tr>
                                <td>
                                    <h4 class="ui image header">
                                        <!--<img src="/images/avatar2/small/lena.png" class="ui mini rounded image">-->
                                        <@macros.gameImg id=game.game.appId url=game.game.iconUrl classes="ui mini rounded image" />
                                        <div class="content">
                                            ${game.game.name}
                                            <div class="sub header" title="${game.minutesPlayed} minutes">
                                                <@macros.timePlayed mins=game.minutesPlayed />
                                            </div>
                                        </div>
                                    </h4>
                                </td>
                            </tr>
                            </#list>
                        </@macros.fixedSizeTable>
                    </tbody>
                </table>
                </#if>
            </div>
        </div>
    </div>
    <div class="ui inverted vertical masthead center aligned segment">
        <!-- TODO: fill -->
        <p>SteamTracker</p>
    </div>
    </@layout.put>

</@layout.extends>