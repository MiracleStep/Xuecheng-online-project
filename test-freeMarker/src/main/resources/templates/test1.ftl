<!DOCTYPE html>
<html>
<head>
    <meta charset="utf‐8">
    <title>Hello World!</title>
</head>
<body>
<#list stus as stu>
    <tr>
        <td <#if stu.name =="小明">style="background:#e2dae3;"</#if>>${stu.name}</td>
        <#if stu.name == "小明">哈哈哈</#if>
        <td>${stu.age}</td>
    </tr>
</#list>

</body>
</html>