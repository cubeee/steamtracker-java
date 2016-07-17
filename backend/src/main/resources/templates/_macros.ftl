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