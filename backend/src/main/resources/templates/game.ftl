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
                                        <div class="value">${(stats.day)!"N/A"} <@macros.statistic_arrow "n/a" /></div>
                                        <div class="label">Last 24 hours</div>
                                    </div>
                                </div>
                                <div class="ui sixteen wide row">
                                    <div class="ui inverted tiny statistic">
                                        <div class="value">${(stats.week)!"N/A"} <@macros.statistic_arrow "n/a" /></div>
                                        <div class="label">Last 7 days</div>
                                    </div>
                                </div>
                                <div class="ui sixteen wide row">
                                    <div class="ui inverted tiny statistic">
                                        <div class="value">${(stats.alltime)!"N/A"}</div>
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
            <div class="twelve wide column">
                <div class="ui grid">
                    <!-- Reserved for graphs and tables -->
                </div>
            </div>
        </div>
    </div>
    </@layout.put>

    <@layout.put block="footer" type="replace"></@layout.put>
</@layout.extends>