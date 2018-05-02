<#include "../header.ftl">
I'm the test page.
<div id="DD">

</div>
<#include "../footer.ftl">
<script>
    $(function () {
        var path = $.yuenjee.url("{0}/{1}","aa","bb");
        $("#DD").html(path);
    });
</script>
