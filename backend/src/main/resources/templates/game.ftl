<@layout.extends name="base.ftl">
    <#import "spring.ftl" as spring />
    <#import "_macros.ftl" as macros />
    <#assign pageTitle>${(game.name)!"No game found"}</#assign>

    <@layout.put block="body">
    <style>
        .game-background {
            background: url(//steamcdn-a.akamaihd.net/steam/apps/#{game.appId}/page_bg_generated_v6b.jpg) center 30% !important;
            background-repeat: no-repeat !important;
            border-bottom: 1px solid #333 !important;
            background-color: #252627 !important;
            background-blend-mode: luminosity;
            height: 300px;
        }
    </style>

    <div class="pusher">
        <div class="ui vertical aligned segment game-background">
            <div class="main ui relaxed padded stackable centered grid">
                <div class="twelve wide column">
                    <div class="ui grid">
                        <div class="left floated twelve wide computer sixteen wide tablet column">
                            <h1 class="ui inverted header game-title">${game.name}</h1>
                        </div>
                        <div class="right floated four wide computer sixteen wide tablet column game-stats">
                            <div class="ui padded grid">
                                <div class="ui sixteen wide row">
                                    <div class="ui inverted tiny statistic">
                                        <div class="value">
                                        <#if tracked_times.last24Hours??>
                                            <@macros.timePlayed mins=tracked_times.last24Hours />
                                        <#else>
                                            N/A
                                        </#if>
                                            <@macros.statistic_arrow "n/a" />
                                        </div>
                                        <div class="label">Last 24 hours</div>
                                    </div>
                                </div>
                                <div class="ui sixteen wide row">
                                    <div class="ui inverted tiny statistic">
                                        <div class="value">
                                            <#if tracked_times.last7Days??>
                                                <@macros.timePlayed mins=tracked_times.last7Days />
                                            <#else>
                                                N/A
                                            </#if>
                                            <@macros.statistic_arrow "n/a" />
                                        </div>
                                        <div class="label">Last 7 days</div>
                                    </div>
                                </div>
                                <div class="ui sixteen wide row">
                                    <div class="ui inverted tiny statistic">
                                        <div class="value">
                                            <#if tracked_times.allTime??>
                                                <@macros.timePlayed mins=tracked_times.allTime />
                                            <#else>
                                                N/A
                                            </#if>
                                            <@macros.statistic_arrow "n/a" />
                                        </div>
                                        <div class="label">All time</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="main ui relaxed padded stackable centered grid">
            <div class="four wide column">
                <h3 class="text-medium">Most played in the last 24 hours</h3>
                <#if tracked_players.last24Hours?size gt 0>
                    <@macros.commonPlayerTable fillTables=fillTables maxRows=10 players=tracked_players.last24Hours />
                </#if>
            </div>
            <div class="four wide column">
                <h3 class="text-medium">Most played in the last 7 days</h3>
                <#if tracked_players.last7Days?size gt 0>
                    <@macros.commonPlayerTable fillTables=fillTables maxRows=10 players=tracked_players.last7Days />
                </#if>
            </div>
            <div class="four wide column">
                <h3 class="text-medium">Most played</h3>
                <#if tracked_players.allTime?size gt 0>
                    <@macros.commonPlayerTable fillTables=fillTables maxRows=10 players=tracked_players.allTime />
                </#if>
            </div>
        </div>
    </div>
    </@layout.put>

    <@layout.put block="footer" type="replace"></@layout.put>
</@layout.extends>