<@layout.extends name="base.ftl">
    <#import "spring.ftl" as spring />
    <#import "_macros.ftl" as macros />
    <#import "_functions.ftl" as functions />
    <#assign security=JspTaglibs["/META-INF/security.tld"] />

    <@layout.put block="navigation" type="replace"></@layout.put>

    <@layout.put block="body">
    <div class="container">
        <div class="section">
            <div class="container text-center">
                <h3 class="text-huge text-white text-with-subtitle">Steam Tracker</h3>
                <h4 class="text-big text-gray">Track your Steam profile progression</h4>
                <div class="row aligner-center-horitzontal">
                    <div class="col-md-6 col-xs-12">
                        <form class="form-collapse" method="post" action="<@spring.url '/search' />">
                            <div class="input item item-main">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                <input id="search" name="identifier" placeholder="Steam profile url, id or custom name" type="text">
                            </div>
                            <button type="submit" class="item button button-primary">
                                <i class="fa fa-search fa-lg"></i>
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <div class="container">
            <div class="row">
                <div class="col-md-4 col-xs-12 col-md-push-8">
                    <p class="text-medium text-with-subtitle text-center">Players tracked</p>
                    <h3 class="text-big text-code text-center text-with-subtitle"><strong>${tracked_players}</strong></h3>
                    <p class="text-medium text-with-subtitle text-center">Collective hours tracked</p>
                    <h3 class="text-big text-code text-center"><strong title="${collective_tracked} minutes"><@macros.timePlayed mins=collective_tracked detailed=false /></strong></h3>

                    <#if functions.authenticated()>
                    <div class="message-bar player-bit">
                        <#assign avatar="${functions.authenticated_field('principal.avatarMedium')}" />
                        <#assign identifier="${functions.authenticated_field('principal.identifier')}" />
                        <#assign username="${functions.authenticated_field('principal.username')}" />

                        <#if avatar??>
                            <@macros.avatar url="${avatar}" size="medium" />
                        </#if>
                        <div class="links">
                            <p class="text-medium text-with-subtitle"><@macros.cutText text=username len=17 /></p>
                            <span class="text-small">
                                <a href="<@spring.url '/player/${identifier}/' />">Profile</a> | <a href="<@spring.url '/auth/logout/' />">Logout</a>
                            </span>
                        </div>
                    </div>
                    <#else>
                    <p class="text-center">
                        <@macros.steam_auth_img />
                    </p>
                    </#if>

                    <table class="table game-table">
                        <thead>
                        <tr class="text-small align-left">
                            <th>Most played games</th>
                            <th>Time played</th>
                        </tr>
                        </thead>
                        <tbody>
                            <@macros.fixedSizeTable columns=2 rows=gamesInTables curRows=most_played?size>
                                <#list most_played as game>
                                <tr>
                                    <td>
                                        <div><@macros.gameImage id=game.game.appId url=game.game.iconUrl /> ${game.game.name}</div>
                                    </td>
                                    <td title="${game.minutesPlayed} minutes"><div><@macros.timePlayed mins=game.minutesPlayed /></div></td>
                                </tr>
                                </#list>
                            </@macros.fixedSizeTable>
                        </tbody>
                    </table>
                </div>
                <div class="col-md-8 col-xs-12 col-md-pull-4">
                    <h3 class="text-medium">Most played in the last 24 hours</h3>
                    <table class="table game-table main-game-table">
                        <thead>
                            <tr class="text-small align-left">
                                <th>Game</th>
                                <th>Time played</th>
                            </tr>
                        </thead>
                        <tbody>
                        <@macros.filledTable fillEmpty=fillTables columns=2 rows=gamesInTables curRows=todays_played?size>
                            <#list todays_played as game>
                            <tr>
                                <td><div><@macros.gameImage id=game.game.appId url=game.game.iconUrl /> ${game.game.name}</div></td>
                                <td title="${game.minutesPlayed} minutes"><div><@macros.timePlayed mins=game.minutesPlayed detailed=true /></div></td>
                            </tr>
                            </#list>
                        </@macros.filledTable>
                        </tbody>
                    </table>

                    <h3 class="text-medium">Most played in the last 7 days</h3>
                    <table class="table game-table main-game-table">
                        <thead>
                            <tr class="text-small align-left">
                                <th>Game</th>
                                <th>Time played</th>
                            </tr>
                        </thead>
                        <tbody>
                        <@macros.filledTable fillEmpty=fillTables columns=2 rows=gamesInTables curRows=weeks_played?size fillEmpty=fillTables>
                            <#list weeks_played as game>
                            <tr>
                                <td><div><@macros.gameImage id=game.game.appId url=game.game.iconUrl /> ${game.game.name}</div></td>
                                <td title="${game.minutesPlayed} minutes"><div><@macros.timePlayed mins=game.minutesPlayed detailed=true /></div></td>
                            </tr>
                            </#list>
                        </@macros.filledTable>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    </@layout.put>

</@layout.extends>