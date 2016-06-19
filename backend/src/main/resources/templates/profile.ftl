<@layout.extends name="base.ftl">
    <#import "spring.ftl" as spring />
    <#import "_macros.ftl" as macros />
    <#assign pageTitle="${player.getDisplayName()}" />

    <@layout.put block="body">
    <div class="section background-dark">
        <div class="container">
            <div class="row">
                <div class="col-md-2 col-xs-12">
                    <img class="avatar" src="${player.avatarFull}" alt="Avatar" />
                </div>
                <div class="player-stats col-md-8 col-xs-12">
                    <ul>
                        <li><h3 class="text-big text-with-subtitle">${player.getDisplayName()}</h3></li>
                        <#if player.identifier == '76561198045967568' >
                            <!-- todo: implement titles? -->
                        <li><span class="text-small text-rating4">Founder / Developer</span></li>
                        </#if>
                        <li>
                            <#if player.countryCode??>
                                <h4 class="country-block text-small">
                                    <img class="country-flag" src="https://steamcommunity-a.akamaihd.net/public/images/countryflags/${player.countryCode?lower_case}.gif" />
                                    <#if player.getCountry()??>
                                    ${player.getCountry()}
                                    </#if>
                                </h4>
                            </#if>
                        </li>
                        <li>Tracked since <span class="text-gray">${(player.getFormattedCreationTime('MMMM dd yyyy'))!"N/A"}</span></li>
                        <!-- x minutes/hours/days ago? -->
                        <li>Last updated <span class="text-gray">${(player.getFormattedLastUpdate('MMMM dd yyyy HH:mm:ss O'))!"N/A"}</span></li>
                    </ul>
                </div>
                <div class="player-links col-md-2 col-xs-12 align-right">
                    <ul>
                        <li><a class="button button-small" href="https://steamcommunity.com/profiles/${player.identifier}/">View on Steam</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <div class="container section">
        <div class="row">
            <div class="col-md-7 col-xs-12">
                <h3 class="text-medium">Most played in the last 24 hours</h3>
                <table class="table game-table">
                    <thead>
                    <tr class="text-small align-left">
                        <th>Game</th>
                        <th>Time played</th>
                    </tr>
                    </thead>
                    <tbody>
                        <@macros.fixedSizeTable columns=2 rows=gamesInTables curRows=todays_played?size>
                            <#list todays_played as game>
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

            <div class="col-md-4 col-xs-12 col-md-offset-1">
                <h3 class="text-medium">Most played games</h3>
                <table class="table game-table">
                    <thead>
                    <tr class="text-small align-left">
                        <th>Game</th>
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

            <div class="col-md-7 col-xs-12">
                <h3 class="text-medium">Most played in the last 7 days</h3>
                <table class="table game-table">
                    <thead>
                    <tr class="text-small align-left">
                        <th>Most played games</th>
                        <th>Time played</th>
                    </tr>
                    </thead>
                    <tbody>
                        <@macros.fixedSizeTable columns=2 rows=gamesInTables curRows=weeks_played?size>
                            <#list weeks_played as game>
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
        </div>
    </div>
    </@layout.put>
</@layout.extends>