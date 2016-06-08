<#macro hoursPlayed mins>
<#compress>
    <#setting number_format=",##0">
    ${mins/60}
</#compress>
</#macro>

<#macro gameImage id url>
    <#compress>
    <img class="rounded-corners game-image" src="http://media.steampowered.com/steamcommunity/public/images/apps/#{id}/${url}.jpg" />
    </#compress>
</#macro>