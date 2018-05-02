<#include "header.ftl">
<!--
${context.contextPath}
${context.getRequestUri()}
${request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()}
${basePath}
${springMacroRequestContext.contextPath}
-->

<div id="DD"></div><br/>
<div id="DIV"></div>

<#include "footer.ftl">

<script>
    function success(data) {
        console.log(JSON.stringify(data));
    }

    function error(data) {
        console.log(JSON.stringify(data));
    }

    function complete(data1,data2) {
        console.log(JSON.stringify(data1));
        console.log(JSON.stringify(data2));
    }

    $(function () {

        $("#DD").html($.yuenjee.url("a","b","c","d")+"<br/>" + $.yuenjee.api("{0}/{1}/fdsfd/{2}","b","c","d"));

        if ($.yuenjee.isNull($.session.get('key'))){
            $("#DIV").html("no session,addit");
            $.session.set('key', 'a value');
        } else {
            $("#DIV").html("hav session:"+$.session.get('key'));
        }

        $.yuenjee.ajax("https://localhost:8443/api/v1/system/info","GET",null,success,error,complete);
    });
</script>

