<#macro timePlayed mins detailed=true>
<#compress>
    <#setting number_format=",##0">
    <#assign hours=mins/60>
    <#if detailed>
        <#if mins lt 60>
        ${mins} minute<#if mins gt 0>s</#if>
        <#else>
        ${hours} hour<#if hours gt 0>s</#if>
        </#if>
    <#else>
        ${hours}
    </#if>
</#compress>
</#macro>

<#macro gameImage id url>
    <#compress>
    <img class="rounded-corners game-image" src="http://media.steampowered.com/steamcommunity/public/images/apps/#{id}/${url}.jpg" />
    </#compress>
</#macro>