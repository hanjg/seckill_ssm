<%--
  Created by IntelliJ IDEA.
  User: hjg
  Date: 2018/1/28
  Time: 15:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>秒杀列表</title>
    <%--静态包含,作为一个servlet--%>
    <%@include file="common/head.jsp" %>
    <%@include file="common/tag.jsp" %>
</head>

<body>
<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading text-center">
            <h2>秒杀列表</h2>
        </div>
        <div class="panel-body">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>名称</th>
                    <th>库存</th>
                    <th>开始时间</th>
                    <th>结束时间</th>
                    <th>详情页</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="sk" items="${list}">
                    <tr>
                        <td>${sk.name}</td>
                        <td>${sk.number}</td>
                        <td>
                            <fmt:formatDate value="${sk.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                        <td>
                            <fmt:formatDate value="${sk.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                        <td>
                            <a class="btn btn-info" href="/seckill/${sk.seckillId}/detail" target="_blank">link</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="panel-footer">
            <ul class="pagination">
                <li><a href="javascript:seckill.list.execute('${page.pageNumber}','1')">首页</a></li>
                <%--'${page.totalPage-1}',而不是'${page.totalPage}-1'--%>
                <li><a href="javascript:seckill.list.execute('${page.pageNumber}','${page.currentPage-1}')">上一页</a></li>
                <li><a href="javascript:seckill.list.execute('${page.pageNumber}','${page.currentPage+1}')">下一页</a></li>
                <li><a href="javascript:seckill.list.execute('${page.pageNumber}','${page.totalPage}')">末页</a></li>
                <li>每页<input type="text" id="pageNumber" value="${page.pageNumber}">条</li>
                <li>
                    当前第<input type="text" id="currentPage" value="${page.currentPage}">页
                    <a href="javascript:seckill.list.execute($('#pageNumber').val(),$('#currentPage').val())">转到</a>
                </li>
                <li>共${page.totalPage}页</li>
            </ul>
        </div>
    </div>
</div>
</body>

<!-- 新 Bootstrap 核心 CSS 文件 -->
<link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">

<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="https://cdn.bootcss.com/jquery/2.1.1/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<script src="/resources/js/seckill.js"></script>
<script src="/resources/js/common/dateTime.js"></script>


</html>
