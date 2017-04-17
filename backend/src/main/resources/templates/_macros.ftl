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

<#macro gameTable>
<table class="ui celled table game-table">
    <thead>
    <tr>
        <th>Game</th>
        <th class="four wide">Time played</th>
    </tr>
    </thead>
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