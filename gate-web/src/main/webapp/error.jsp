<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
 	String error = (String)request.getAttribute("error")==null?(String)request.getParameter("error"):(String)request.getAttribute("error");
 	
 	request.setAttribute("error", error);
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>广州e贷 服务器异常</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta content="广州e贷" name="description">
<meta content="广州e贷,P2P,借贷" name="keywords">

<style type="text/css">
article, aside, details, figcaption, figure, footer, header, main, nav, section {display: block}
audio, canvas, video {display: inline-block;*display:inline;*zoom:1}
audio:not([controls]) {display: none;height: 0}
[hidden] {display:none}
html {font-size: 100%;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%}
img {width: auto\9;height: auto;vertical-align: middle;border: 0;-ms-interpolation-mode: bicubic}
body {margin: 0;font: 14px/1.5 arial, "Hiragino Sans GB", "Microsoft Yahei", sans-serif;color: #8c8c8c;background-color: #fff}
a:hover, a:focus {color: #595757;text-decoration: none}
ul,li{margin: 0;padding: 0;list-style: none;}
.error {width:520px;height:530px;position: absolute;left: 50%;top:45%;margin-left: -260px;margin-top: -265px;text-align: center;}
.error-pic {background: url(/assets/img/error.jpg) no-repeat center;width:600px;height:650px;margin: 0 auto; position:relative}
.error-logo{ position:absolute;top:0;left:196px}
.error-logo a{ font-size:0;width:247px;height:78px; display:block}
.error ul { padding-top:120px}
.error li{font-size:18px;color: #838383;line-height:35px}
.error-btn {display: inline-block;margin-bottom: 0;font-size: 18px;height:40px;line-height:40px;text-align: center;cursor: pointer;background-color: #975197;color: #fff;padding: 0;width: 150px;text-decoration: none;border-radius: 3px;}
.error-btn:hover{background-color: #800073;color: #fff;}
</style>
</head>
<body>
<div class="error">
  <div class="error-pic"><div class="error-logo"><a class="" href="http://www.gzdai.com">广州e贷首页</a></div>
  <ul>
  <li><c:out value="${error}"/></li>
  <li><input type="button" class="button" value="返回首页" onclick="javascript:window.location.href='http://www.gzdai.com'" /></li>
  <!-- 
    <li>投资就像一场马拉松</li>
    <li>不如趁广州e贷服务器出错的时候，你也思考一下再继续？</li>
     -->
  </ul>
</div></div>
</body>
</html>
