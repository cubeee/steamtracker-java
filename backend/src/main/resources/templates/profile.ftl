<@layout.extends name="base.ftl">
    <#import "spring.ftl" as spring />
    <#assign pageTitle = "${identifier}" />

    <@layout.put block="body">
    <div class="container section">
        <p>Profile of player '${identifier}'</p>

        <#if player??>
            <p>player found! woo</p>
            <p>Games: ${player.gameCount}</p>
            <#list player.games as game>
                <p>
                    <img src="http://media.steampowered.com/steamcommunity/public/images/apps/#{game.appId}/${game.logoUrl}.jpg" /> ${game.name}
                </p>
            </#list>
        <#else>
            <p>No profile found for player</p>
        </#if>
    </div>
    </@layout.put>

</@layout.extends>