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
    <title>管理商品</title>
    <%--静态包含,作为一个servlet--%>
    <%@include file="common/head.jsp" %>
    <%@include file="common/tag.jsp" %>
</head>

<body>
<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading text-center">
            <h2>管理商品</h2>
            <h3><a class="btn btn-info" href="javascript:manager.insert.init()">新增</a></h3>
        </div>
        <div class="panel-body">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>名称</th>
                    <th>库存</th>
                    <th>开始时间</th>
                    <th>结束时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="sk" items="${list}" varStatus="status">
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
                            <fmt:formatDate value="${sk.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                        <td>
                            <a class="btn btn-info"
                               href="javascript:manager.update.init('${sk.seckillId}','${sk.name}','${sk.number}','${sk.startTime}','${sk.endTime}');">修改</a>&nbsp;&nbsp;&nbsp;
                            <a class="btn btn-info"
                               href="javascript:manager.delete.execution('${sk.seckillId}');">删除</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="panel-body">
            <form class="form-horizontal" role="form" id="mainForm" hidden>
                <div class="form-group">
                    <label for="name" class="col-sm-2 control-label">名称</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="name" placeholder="请输入名称">
                    </div>
                </div>
                <div class="form-group">
                    <label for="number" class="col-sm-2 control-label">库存</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="number" placeholder="请输入库存">
                    </div>
                </div>
                <div class="form-group">
                    <label for="startTime" class="col-sm-2 control-label">开始时间</label>
                    <div class="col-sm-10 input-group date" id="startTimePicker">
                        <input type="text" class="form-control" id="startTime" placeholder="请输入开始时间">
                        <span class="input-group-addon">
                        <i class="glyphicon glyphicon-calendar"></i>
                        </span>
                    </div>
                </div>
                <div class="form-group">
                    <label for="endTime" class="col-sm-2 control-label">结束时间</label>
                    <div class="col-sm-10 input-group date" id="endTimePicker">
                        <input type="text" class="form-control" id="endTime" placeholder="请输入结束时间">
                        <span class="input-group-addon">
                        <i class="glyphicon glyphicon-calendar"></i>
                        </span>
                    </div>
                </div>
                <div class="form-group">
                    <input type="text" id="updateId" hidden>
                </div>
            </form>
            <div class="col-sm-offset-2 col-sm-10" hidden id="updateButton">
                <a type="button" class="btn btn-info"
                   href="javascript:manager.update.execute()">修改</a>
            </div>
            <div class="col-sm-offset-2 col-sm-10" hidden id="insertButton">
                <a type="button" class="btn btn-info"
                   href="javascript:manager.insert.execute()">新增</a>
            </div>
        </div>
        <div class="panel-footer">
            <ul class="pagination">
                <li><a href="javascript:manager.list.execute('${page.pageNumber}','1')">首页</a></li>
                <%--'${page.totalPage-1}',而不是'${page.totalPage}-1'--%>
                <li><a href="javascript:manager.list.execute('${page.pageNumber}','${page.currentPage-1}')">上一页</a></li>
                <li><a href="javascript:manager.list.execute('${page.pageNumber}','${page.currentPage+1}')">下一页</a></li>
                <li><a href="javascript:manager.list.execute('${page.pageNumber}','${page.totalPage}')">末页</a></li>
                <li>每页<input type="text" id="pageNumber" value="${page.pageNumber}">条</li>
                <li>
                    当前第<input type="text" id="currentPage" value="${page.currentPage}">页
                    <a href="javascript:manager.list.execute($('#pageNumber').val(),$('#currentPage').val())">转到</a>
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

<script src="/resources/js/manager.js"></script>
<script src="/resources/js/common/dateTime.js"></script>


<script src="https://cdn.bootcss.com/moment.js/2.18.1/moment-with-locales.min.js"></script>
<link href="https://cdn.bootcss.com/bootstrap-datetimepicker/4.17.47/css/bootstrap-datetimepicker.min.css"
      rel="stylesheet">
<script src="https://cdn.bootcss.com/bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"></script>

<script type="text/javascript">
  $(function () {
    manager.init();
  })
</script>


</html>
