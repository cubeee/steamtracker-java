<@layout.extends name="base.ftl">
    <#import "spring.ftl" as spring />
    <#assign pageTitle>${(player.getDisplayName())!"No player found"}</#assign>

    <@layout.put block="body">
    <div class="container section">
    <#if player??>
        <p>Profile of player '${player.getDisplayName()}'</p>
        <p>Games: ${player.gameCount}</p>
        <p>Creation date: ${player.creationTime}</p>
        <p>Last updated: ${player.lastUpdated}</p>
        <img src="${player.avatarFull}" alt="Avatar" />
    <#else>
        <p>No profile found for player</p>
    </#if>
    </div>
    </@layout.put>
</@layout.extends>