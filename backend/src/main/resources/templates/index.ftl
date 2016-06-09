<@layout.extends name="base.ftl">
    <#import "spring.ftl" as spring />
    <#import "_macros.ftl" as macros />

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
                <!-- todays most played games and players -->
                <div class="col-md-7 col-xs-12">
                    <h3 class="text-medium">Most played in the last 24 hours</h3>
                    <table class="table game-table main-game-table">
                        <thead>
                            <tr class="text-small align-left">
                                <th>Game</th>
                                <th>Hours</th>
                            </tr>
                        </thead>
                        <tbody>
                        <#list todays_played as game>
                            <tr>
                                <td><@macros.gameImage id=game.game.appId url=game.game.iconUrl /> ${game.game.name}</td>
                                <td title="${game.minutesPlayed} minutes"><@macros.hoursPlayed mins=game.minutesPlayed /></td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>

                    <h3 class="text-medium">Most played in the last 7 days</h3>
                    <table class="table game-table main-game-table">
                        <thead>
                            <tr class="text-small align-left">
                                <th>Game</th>
                                <th>Hours</th>
                            </tr>
                        </thead>
                        <tbody>
                            <#list weeks_played as game>
                            <tr>
                                <td><@macros.gameImage id=game.game.appId url=game.game.iconUrl /> ${game.game.name}</td>
                                <td title="${game.minutesPlayed} minutes"><@macros.hoursPlayed mins=game.minutesPlayed /></td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>

                <!-- sidebar -->
                <div class="col-md-4 col-xs-12 col-md-offset-1">
                    <!-- stats: tracked users, collective hours played in # games -->
                    <p class="text-medium text-with-subtitle text-center">Collective hours played</p>
                    <h3 class="text-big text-code text-center"><strong title="${collective_played} minutes"><@macros.hoursPlayed mins=collective_played /></strong></h3>
                    <p class="text-medium text-with-subtitle text-center">Players tracked</p>
                    <h3 class="text-big text-code text-center"><strong>${tracked_players}</strong></h3>

                    <table class="table game-table">
                        <thead>
                            <tr class="text-small align-left">
                                <th>Most played games</th>
                                <th>Hours played</th>
                            </tr>
                        </thead>
                        <tbody>
                            <#list most_played as game>
                            <tr>
                                <td><@macros.gameImage id=game.game.appId url=game.game.iconUrl /> ${game.game.name}</td>
                                <td title="${game.minutesPlayed} minutes"><@macros.hoursPlayed mins=game.minutesPlayed /></td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    </@layout.put>

</@layout.extends>