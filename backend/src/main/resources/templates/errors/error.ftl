<@layout.extends name="base.ftl">
    <#assign pageTitle = "Page not found" />
    <@layout.put block="body">
    <div class="container">
        <div class="section">
            <h1 class="text-huge text-center text-with-subtitle">${status}</h1>
            <h2 class="text-big text-center">Something went wrong with your request</h2>
        </div>
    </div>
    </@layout.put>
</@layout.extends>