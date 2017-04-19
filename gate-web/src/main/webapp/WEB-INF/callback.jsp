<%@ page language="java" contentType="text/html;charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>广州e贷支付网关回调</title>
<script language="javascript" src="/assets/plugins/jquery-1.10.1.min.js"></script>
<script type="text/javascript">
	$(document).ready(function()
	{
		//var t=setTimeout("$('#form1').submit()",20000)
		$("#form1").submit();
	});
</script>
</head>
<body>
	<%=request.getAttribute("form")%>
</body>
</html>