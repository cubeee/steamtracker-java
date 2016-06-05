<@layout.extends name="base.ftl">
    <#import "spring.ftl" as spring />
    <#assign pageTitle = "${player.identifier}" />

    <@layout.put block="body">
    <div class="container section">
        <p>Profile of player '${player.identifier}'</p>

        <#if player??>
            <p>player found! woo</p>
            <p>Games: ${player.gameCount}</p>
        <#else>
            <p>No profile found for player</p>
        </#if>
    </div>
    </@layout.put>

</@layout.extends>