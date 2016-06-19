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
    <div class="image">
        <div class="rounded-corners game-image" style="background: url(http://media.steampowered.com/steamcommunity/public/images/apps/#{id}/${url}.jpg) no-repeat;"></div>
    </div>
    </#compress>
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