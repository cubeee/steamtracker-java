<@layout.extends name="base.ftl">
    <#import "spring.ftl" as spring />
    <#import "_macros.ftl" as macros />
    <#import "_functions.ftl" as functions />
    <#assign pageTitle="${player.getDisplayName()}" />

    <@layout.put block="body">
    <div class="main ui relaxed padded stackable centered grid">
        <div class="twelve wide column">
            <div class="ui grid">
                <div class="ui player info row">
                    <div class="fourteen wide computer sixteen wide tablet column">
                        <div class="ui items">
                            <div class="item">
                                <div class="image">
                                    <img src="${player.avatarFull}" alt="Avatar">
                                </div>
                                <div class="content">
                                    <a class="header"><@macros.cutText text="${player.getDisplayName()}" len=18 /></a>
                                    <#if player.identifier == '76561198045967568'>
                                        <div class="meta">
                                            <span>Founder / Developer</span>
                                        </div>
                                    </#if>
                                    <div class="description">
                                        <p class="country">
                                            <img class="country-flag" src="//steamcommunity-a.akamaihd.net/public/images/countryflags/${player.countryCode?lower_case}.gif" />
                                            <#if player.getCountry()??>
                                            ${player.getCountry()}
                                            </#if>
                                        </p>
                                    </div>
                                    <div class="extra">
                                        <br />
                                        <p>Tracked since ${(player.getFormattedCreationTime('MMMM dd yyyy'))!"N/A"}</p>
                                        <p>Last updated ${(player.getFormattedLastUpdate('MMMM dd yyyy HH:mm:ss O'))!"N/A"}</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="two wide column">
                        <a target="_blank" href="https://steamcommunity.com/profiles/${player.identifier}/" class="ui secondary button">View on Steam</a>
                    </div>
                </div>
                <div class="ui row">
                    <div class="six wide column">
                        <h3>Most played in the last 24 hours</h3>
                        <#if todays_played?size == 0>
                            <p>No statistics for the last 24 hours</p>
                        <#else>
                            <@macros.commonGameTable fillTables=fillTables maxRows=gamesInTables games=todays_played />
                        </#if>
                    </div>
                    <div class="six wide column">
                        <h3>Most played in the last 7 days</h3>
                        <#if weeks_played?size == 0>
                            <p>No statistics for the last 7 days</p>
                        <#else>
                            <@macros.commonGameTable fillTables=fillTables maxRows=gamesInTables games=weeks_played />
                        </#if>
                    </div>
                    <div class="four wide column">
                        <h3 class="text-medium">Most played games</h3>
                        <@macros.commonGameTable fillTables=false maxRows=gamesInTables games=most_played />
                    </div>
                </div>
            </div>
        </div>
    </div>
    </@layout.put>

    <@layout.put block="footer" type="replace"></@layout.put>
</@layout.extends>