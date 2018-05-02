<#assign basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()?c+request.getContextPath()>
<html>
<head>
    <title></title>
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script>
        window.yuenjee={"basePath":"${basePath}"};
    </script>
</head>
<body>
