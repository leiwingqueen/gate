<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@page import="com.elend.gate.order.constant.OrderType"%>
<%@page import="com.elend.gate.channel.constant.ChannelIdConstant"%>
<%@page import="com.elend.gate.channel.constant.WithdrawChannel"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 

<jsp:include page="/include/hd.jsp"></jsp:include>
<!-- BEGIN PAGE -->
<div class="page-content">
	<!-- BEGIN 右边容器-->
	<div class="container-fluid">
		<!-- BEGIN 右容器头部-->
		<div class="row-fluid">
			<div class="span12">
				<!-- BEGIN 页面标题和面包屑导航 -->
				<h3 class="page-title">
					内部转账<small></small>
				</h3>
				<ul class="breadcrumb">
					<li><i class="icon-home"></i> <a href="/">Home</a>
					</li>
				</ul>
				<!-- END 页面标题和面包屑导航 -->
			</div>
		</div>
		<!-- END 右容器头部-->
		<!-- BEGIN 右容器 main-->
		<div class="row-fluid">
			<div class="portlet box default">
				<div class="portlet-title">
					<div class="caption">
						<i class="icon-reorder"></i> 渠道提现到银行卡
					</div>
					<div class="tools">
						<a href="javascript:;" class="collapse"></a>
					</div>
				</div>
				<div class="portlet-body">
				<form id="fm1" class="form form-horizontal" action="https://www.baidu.com">
				 	<div class="control-group">
				 		<label class="control-label"><b class="required">*</b>转出账号： </label>
				 		<select id="outUserId1" name="outUserId" required onchange="queryBalance1(this.value);">
				 			<option value="">--请选择--</option>
				 			<c:forEach var="b" items="${channelList }">
					 			<option value="${b.userId }">${b.remark }</option>
				 			</c:forEach>
				 		</select>
				 		&nbsp;&nbsp;
				 		余额
				 		&nbsp;&nbsp;
				 		<span id="usableBalance1">0</span>
				 		&nbsp;&nbsp;
				 		元
				 	</div>
				 	<div class="control-group">
				 		<label class="control-label"><b class="required">*</b>转入账号： </label>
				 		<select id="inUserId1" name="inUserId" required>
				 			<option value="">--请选择--</option>
				 			<c:forEach var="b" items="${bankList }">
					 			<option value="${b.userId }">${b.remark }</option>
				 			</c:forEach>
				 		</select>
				 	</div>
				 	<div class="control-group">
				 		<label class="control-label"><b class="required">*</b>转账金额： </label>
				 		<input id="amount1" name="amount" class="input-medium" type="text" required checkAmount=true checkBalance1=true/>
				 	</div>
				 	<!-- END 查询条件 -->
				 	<div class="form-actions">
                    	<input id="J_saveForm1" type="submit" class="btn green btn-primary" style="left: 500px" value="确认转账">
                    </div>
				</form>
			</div>
		</div>
		<div class="row-fluid">
			<div class="portlet box default">
				<div class="portlet-title">
					<div class="caption">
						<i class="icon-reorder"></i> 资金转入到渠道
					</div>
					<div class="tools">
						<a href="javascript:;" class="collapse"></a>
					</div>
				</div>
				<div class="portlet-body">
				<form id="fm2" class="form form-horizontal">
				 	<div class="control-group">
				 		<label class="control-label">转出账号： </label>
				 		<select id="outUserId2" name="outUserId" required  onchange="queryBalance2(this.value);">
				 			<option value="">--请选择--</option>
				 			<c:forEach var="b" items="${bankList }">
					 			<option value="${b.userId }">${b.remark }</option>
				 			</c:forEach>
				 		</select>
				 		&nbsp;&nbsp;
				 		余额
				 		&nbsp;&nbsp;
				 		<span id="usableBalance2">0</span>
				 		&nbsp;&nbsp;
				 		元
				 	</div>
				 	<div class="control-group">
				 		<label class="control-label">转入账号： </label>
				 		<select id="inUserId2" name="inUserId" required>
				 			<option value="">--请选择--</option>
				 			<c:forEach var="b" items="${channelList }">
					 			<option value="${b.userId }">${b.remark }</option>
				 			</c:forEach>
				 		</select>
				 	</div>
				 	<div class="control-group">
				 		<label class="control-label">转账金额： </label>
				 		<input id="amount2" name="amount" class="input-medium" type="text" value="${userId }" required checkAmount=true checkBalance2=true/>
				 	</div>

				 	<div class="form-actions">
                            <input type="submit" id="J_saveForm2" class="btn green btn-primary"style="left: 500px" value="确认转账" />
                    </div>
				</form>
			</div>
			</div>
		</div>
		<!-- END 右容器 main-->
	</div>
	<!-- END 右边容器-->
</div>
<!-- END PAGE -->
</div>
<!-- END 页面主容器 -->

<jsp:include page="/include/ft.jsp"></jsp:include>

<!-- BEGIN 页面基本js -->
<script src="http://static.gzdai.com/sysop_fed_lib/assets/plugins/jquery-validation/dist/jquery.validate.js"></script>
<script src="http://static.gzdai.com/sysop_fed_lib/assets/plugins/jquery-validation/dist/additional-methods.js"></script>
<script src="/js/common.js"></script>
<script src="/js/base.js"></script>

<!-- BEGIN 页面基本js -->
<script src="/js/financial/transfer.js"></script>
<!-- END 页面基本js -->
</body>
</html>