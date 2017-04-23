<@layout.extends name="base.ftl">
    <#import "spring.ftl" as spring />
    <#import "_macros.ftl" as macros />
    <#import "_functions.ftl" as functions />
    <#assign security=JspTaglibs["/META-INF/security.tld"] />

    <@layout.put block="navigation" type="replace"></@layout.put>

    <@layout.put block="body">
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
                        <@macros.steam_auth_img />
                    </div>
                </#if>
                </div>
            </div>
            <div class="ui text container">
                <h1 class="ui inverted header">SteamTracker</h1>
                <h2>Track your Steam profile progression</h2>
                <div class="ui ten column centered tight grid">
                    <div class="row">
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
                    <div class="row">
                        <div class="ui twelve wide center aligned column">
                            <a class="ui large black image label">
                                ${tracked_players}
                                <div class="detail">Players tracked</div>
                            </a>
                            <a class="ui large black image label">
                                <@macros.timePlayed mins=collective_tracked detailed=false />
                                <div class="detail">Collective hours tracked</div>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="main ui relaxed padded stackable centered grid">
            <div class="four wide column">
                <h3 class="text-medium">Most played in the last 24 hours</h3>
                <#if todays_played?size gt 0>
                    <@macros.commonGameTable fillTables=fillTables maxRows=gamesInTables games=todays_played />
                </#if>
            </div>
            <div class="four wide column">
                <h3 class="text-medium">Most played in the last 7 days</h3>
                <#if weeks_played?size gt 0>
                    <@macros.commonGameTable fillTables=fillTables maxRows=gamesInTables games=weeks_played />
                </#if>
            </div>
            <div class="four wide column">
                <h3 class="text-medium">Most played</h3>
                <#if most_played?size gt 0>
                    <@macros.commonGameTable fillTables=fillTables maxRows=gamesInTables games=most_played />
                </#if>
            </div>
        </div>
    </div>
    </@layout.put>
</@layout.extends>