<@layout.extends name="base.ftl">
    <#import "spring.ftl" as spring />
    <#import "_macros.ftl" as macros />
    <#import "_functions.ftl" as functions />
    <#assign pageTitle="${player.getDisplayName()}" />

    <@layout.put block="body">
    <div class="five wide computer sixteen wide tablet column">
        <div class="ui card">
            <a class="image" href="#">
                <img class="ui image" src="${player.avatarFull}" alt="Avatar">
            </a>
            <div class="content">
                <span class="header"><@macros.cutText text="${player.getDisplayName()}" len=18 /></span>
                <div class="meta">
                    <#if player.identifier == '76561198045967568'>
                    <p class="title">Founder / Developer</p>
                    </#if>
                    <p class="country">
                        <img class="country-flag" src="https://steamcommunity-a.akamaihd.net/public/images/countryflags/${player.countryCode?lower_case}.gif" />
                        <#if player.getCountry()??>
                        ${player.getCountry()}
                        </#if>
                    </p>
                    <p>Tracked since ${(player.getFormattedCreationTime('MMMM dd yyyy'))!"N/A"}</p>
                    <p>Last updated ${(player.getFormattedLastUpdate('MMMM dd yyyy HH:mm:ss O'))!"N/A"}</p>
                    <p><a target="_blank" href="https://steamcommunity.com/profiles/${player.identifier}/">View on Steam</a></p>
                </div>
            </div>
        </div>
        <!-- player's own profile controls go here -->
        <!-- most played + most tracked -->
        <!--
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
        -->
    </div>
    <div class="eleven wide computer sixteen wide tablet column">
        <#if todays_played?size == 0 && weeks_played?size == 0>
            <h1>Recently added account</h1>
            <p>Nothing tracked yet, go play some games and check back later!</p>
        <#else>
        <h2>Most played in the last 24 hours</h2>
        <@macros.gameTable>
            <@macros.filledTable fillEmpty=fillTables columns=2 rows=gamesInTables curRows=todays_played?size>
                <#list todays_played as game>
                <tr>
                    <td>
                        <div><@macros.gameImage id=game.game.appId url=game.game.iconUrl /> ${game.game.name}</div>
                    </td>
                    <td title="${game.minutesPlayed} minutes"><@macros.timePlayed mins=game.minutesPlayed /></td>
                </tr>
                </#list>
            </@macros.filledTable>
        </@macros.gameTable>

        <h2>Most played in the last 7 days</h2>
        <@macros.gameTable>
            <@macros.filledTable fillEmpty=fillTables columns=2 rows=gamesInTables curRows=weeks_played?size>
                <#list weeks_played as game>
                    <tr>
                        <td>
                            <div><@macros.gameImage id=game.game.appId url=game.game.iconUrl /> ${game.game.name}</div>
                        </td>
                        <td title="${game.minutesPlayed} minutes"><@macros.timePlayed mins=game.minutesPlayed /></td>
                    </tr>
                </#list>
            </@macros.filledTable>
        </@macros.gameTable>
        </#if>
    </div>
    </@layout.put>
</@layout.extends>