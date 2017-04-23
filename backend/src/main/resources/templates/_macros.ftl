<#macro timePlayed mins detailed=true>
<#compress>
    <#assign hours=(mins/60)?floor>
    <#setting number_format=",##0">
    <#if detailed>
        <#if mins lt 61>
            ${mins} minute<#if mins != 1>s</#if>
        <#else>
            ${hours} hour<#if hours != 1>s</#if>
        </#if>
    <#else>
        ${hours}
    </#if>
</#compress>
</#macro>

<#macro gameImage id url>
    <#compress>
    <div class="rounded-corners image">
        <div class="rounded-corners game-image" style="background: url(http://media.steampowered.com/steamcommunity/public/images/apps/#{id}/${url}.jpg) no-repeat;"></div>
    </div>
    </#compress>
</#macro>

<#macro commonGameTable fillTables maxRows games>
    <@gameTable>
        <@filledTable fillEmpty=fillTables columns=1 rows=maxRows curRows=games?size>
            <#list games as game>
            <tr>
                <td>
                    <h4 class="ui image header">
                        <@gameImg id=game.game.appId url=game.game.iconUrl classes="ui mini rounded image" />
                        <div class="content">
                        ${game.game.name}
                            <div class="sub header" title="${game.minutesPlayed} minutes">
                                <@timePlayed mins=game.minutesPlayed />
                            </div>
                        </div>
                    </h4>
                </td>
            </tr>
            </#list>
        </@filledTable>
    </@gameTable>
</#macro>

<#macro gameImg id url classes>
    <img src="//media.steampowered.com/steamcommunity/public/images/apps/#{id}/${url}.jpg" class="${classes}" />
</#macro>

<#macro gameTable>
<table class="ui celled table game-table main-game-table">
    <tbody>
        <#nested>
    </tbody>
</table>
</#macro>

<#macro fixedSizeTable columns rows curRows>
    <#nested>
    <#if curRows lt rows>
        <#list 1..(rows-curRows) as i>
        <tr>
            <#list 1..columns as j>
            <td class="empty">&nbsp;</td>
            </#list>
        </tr>
        </#list>
    </#if>
</#macro>

<#macro filledTable fillEmpty columns rows curRows>
    <#if fillEmpty>
        <@fixedSizeTable columns=columns rows=rows curRows=curRows>
            <#nested>
        </@fixedSizeTable>
    <#else>
        <#nested>
    </#if>
</#macro>

<#macro avatar url size>
    <#compress>
    <div class="avatar ${size}">
        <div class="player-avatar ${size}" style="background: url(${url}) no-repeat;"></div>
    </div>
    </#compress>
</#macro>

<#macro cutText text len>
    <#assign strLen=text?length>
    <#if strLen &lt; len>
        ${text}
    <#else>
        <#assign overflow=(strLen)-len />
        ${text?substring(0,strLen-overflow)}...
    </#if>
</#macro>

<#macro steam_auth_img>
    <a href="<@spring.url '/auth/steam/' />">
        <img src="https://steamcommunity-a.akamaihd.net/public/images/signinthroughsteam/sits_01.png" alt="Sign in" />
    </a>
</#macro>

<#macro steam_auth_button>
<a href="<@spring.url '/auth/steam/' />" class="ui inverted button">Sign in with Steam</a>
</#macro>