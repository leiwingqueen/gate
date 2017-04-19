<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@page import="com.elend.gate.channel.constant.BankIdConstant"%>
<%@page import="com.elend.gate.channel.constant.ChannelIdConstant"%>
<%@page import="com.elend.gate.conf.constant.PayLimitType"%>


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
					添加/修改支付限额<small></small>
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
		<div class="row-fluid"  ms-controller="dataList">
			<div class="portlet box default">
				<div class="portlet-title">
					<div class="caption">
						<i class="icon-reorder"></i> 请填写支付限额信息
					</div>
					<div class="tools">
						<a href="javascript:;" class="collapse"></a>
					</div>
				</div>
				<div class="portlet-body">
				<form id="fm" class="form form-horizontal" action="#">
				 	<div class="control-group">
				 		<label class="control-label"><b class="required">*</b>银行： </label>
				 		<input id="id" name="id" value="0" style="display: none;" ms-attr-value="data.id">
				 		<select id="bankId" name="bankId" required ms-attr-value="data.bankId">
				 			<option value="">--请选择银行--</option>
				 			<%
				 			for(BankIdConstant p : BankIdConstant.values()) {
				 				out.print("<option value=\""+p.getBankId()+"\">"+p.getDesc()+"</option>"); 
				 			}
				 			%>
				 		</select>
				 	</div>
				 	<div class="control-group">
				 		<label class="control-label"><b class="required">*</b>支付渠道： </label>
				 		<select id="channel" name="channel" required  ms-attr-value="data.channel">
				 			<option value="">--请选择支付渠道--</option>
				 			<%
				 			for(ChannelIdConstant p : ChannelIdConstant.values()) {
				 			   if(p.getBalanceUserId() != 0) {
				 			   		out.print("<option value=\""+p.name()+"\">"+p.getDesc()+"</option>"); 
				 			   }
				 			}
				 			%>
				 		</select>
				 	</div>
				 	<div class="control-group">
				 		<label class="control-label"><b class="required">*</b>支付类型： </label>
				 		<input id="userType" name="userType" class="input-medium" type="text" style="width: 205px;"  required  ms-attr-value="data.userType"/>
				 	</div>
				 	<div class="control-group">
				 		<label class="control-label"><b class="required">*</b>单笔限额类型： </label>
				 		<select id="singleLimitType" name="singleLimitType" required  ms-attr-value="data.singleLimitType">
				 			<option value="">--限额类型--</option>
				 			<%
				 			for(PayLimitType p : PayLimitType.values()) {
				 				out.print("<option value=\""+p.getType()+"\">"+p.getDesc()+"</option>"); 
				 			}
				 			%>
				 		</select>
				 	</div>
				 	<div class="control-group">
				 		<label class="control-label"><b class="required">*</b>单笔限额： </label>
				 		<input id="singleLimit" name="singleLimit" class="input-medium" type="text" style="width: 205px;" required checkAmount=true  ms-attr-value="data.singleLimit"/>
				 	</div>
				 	<div class="control-group">
				 		<label class="control-label"><b class="required">*</b>单日限额类型： </label>
				 		<select id="dayLimitType" name="dayLimitType" required   ms-attr-value="data.dayLimitType">
				 			<option value="">--限额类型--</option>
				 			<%
				 			for(PayLimitType p : PayLimitType.values()) {
				 				out.print("<option value=\""+p.getType()+"\">"+p.getDesc()+"</option>"); 
				 			}
				 			%>
				 		</select>
				 	</div>
				 	<div class="control-group">
				 		<label class="control-label"><b class="required">*</b>单日限额： </label>
				 		<input id="dayLimit" name="dayLimit" class="input-medium" type="text" style="width: 205px;"  required checkAmount=true  ms-attr-value="data.dayLimit"/>
				 	</div>
				 	<div class="control-group">
				 		<label class="control-label"><b class="required">*</b>单月限额类型： </label>
				 		<select id="monthLimitType" name="monthLimitType" required   ms-attr-value="data.monthLimitType">
				 			<option value="">--限额类型--</option>
				 			<%
				 			for(PayLimitType p : PayLimitType.values()) {
				 				out.print("<option value=\""+p.getType()+"\">"+p.getDesc()+"</option>"); 
				 			}
				 			%>
				 		</select>
				 	</div>
				 	<div class="control-group">
				 		<label class="control-label"><b class="required">*</b>单月限额： </label>
				 		<input id="monthLimit" name="monthLimit" class="input-medium" type="text" style="width: 205px;"  required checkAmount=true   ms-attr-value="data.monthLimit"/>
				 	</div>
				 	<div class="control-group">
				 		<label class="control-label"><b class="required">*</b>备注： </label>
				 		<textarea id="remark" name="remark" rows="3" cols="10" required  ms-attr-value="data.remark"></textarea>
				 	</div>
				 	<!-- END 查询条件 -->
				 	<div class="form-actions">
                    	<input id="J_saveForm1" type="submit" class="btn green btn-primary" style="left: 500px" value="确认提交">
                    	&nbsp;&nbsp;&nbsp;&nbsp;
                    	<a class="btn green btn-primary" href="/bank/payLimitList.jspx">返回列表</a>
                    </div>
				</form>
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
<script src="/js/bankConfig/payLimitAdd.js"></script>
<!-- END 页面基本js -->
</body>
</html>