<#assign security=JspTaglibs["/META-INF/security.tld"] />

<#function authenticated_field field>
    <@security.authorize access="isAuthenticated()">
        <#assign principalField>
            <@security.authentication property=field />
        </#assign>
        <#return principalField />
    </@security.authorize>
</#function>

<#function authenticated_field_alt field alt>
    <@security.authorize access="isAuthenticated()">
        <#assign principalField>
            <@security.authentication property=field />
        </#assign>
        <#return principalField />
    </@security.authorize>
    <@security.authorize access="!isAuthenticated()">
        <#return alt />
    </@security.authorize>
</#function>

<#function authenticated>
    <@security.authorize access="isAuthenticated()">
        <#return true />
    </@security.authorize>
    <@security.authorize access="!isAuthenticated()">
        <#return false />
    </@security.authorize>
</#function>