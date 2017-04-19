<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@page import="com.elend.gate.channel.constant.BankIdConstant"%>
<%@page import="com.elend.gate.channel.constant.ChannelIdConstant"%>
<%@page import="com.elend.gate.conf.constant.PayLimitType"%>

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
					充值银行列表<small></small>
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
						<i class="icon-reorder"></i> 查询
					</div>
					<div class="tools">
						<a href="javascript:;" class="collapse"></a>
					</div>
				</div>
				<div class="portlet-body">
				<form id="fm" class="form form-horizontal">
					<!-- BEGIN 查询条件 -->
				 	<div class="control-group">
				 		<label class="control-label">银行： </label>
				 		<select id="bankId" name="bankId">
				 			<option value="">--请选择银行--</option>
				 			<%
				 			for(BankIdConstant p : BankIdConstant.values()) {
				 				out.print("<option value=\""+p.getBankId()+"\">"+p.getDesc()+"</option>"); 
				 			}
				 			%>
				 		</select>
				 	</div>
				 	<div class="control-group">
				 		<label class="control-label">渠道： </label>
				 		<select id="channel" name="payChannel">
				 			<option value="">--请选择渠道--</option>
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
				 		<label class="control-label">状态： </label>
				 		<select id="status" name="status">
				 			<option value="0">--请选择状态--</option>
				 			<option value="1">启用</option>
				 			<option value="2">停用</option>
				 		</select>
				 	</div>
				 	<!-- END 查询条件 -->
				 	<div class="form-actions">
                            <a id="J_saveForm" class="btn green btn-primary"style="left: 500px" onclick="searchData()">查询</a>
                    </div>
				</form>
			</div>
			</div>
			<div class="portlet box default">
				<div class="portlet-title">
					<div class="caption">
						<i class="icon-reorder"></i>列表查询 
					</div>
				</div>
				<div class="portlet-body" >
					<table class="table table-bordered table-striped table-hover">
						<thead>
						<tr>
							<th colspan="7" style="text-align: center;" id="userRealName" ></th>
						</tr>
						<tr>
							<th>序号</th>
							<th>银行ID</th>
							<th>银行名称</th>
							<th>渠道</th>
							<th>渠道名称</th>
							<th>渠道银行ID</th>
							<th>状态</th>
						</tr>
					</thead>
					<tbody id="tbody" ms-controller="dataList">
						<tr ms-repeat-item="list">
							<td>{{$index+1}}</td>
							<td>{{item.bankId}}</td>
							<td>{{item.bankName}}</td>
							<td>{{item.payChannel}}</td>
							<td>{{item.channelName}}</td>
							<td>{{item.channelBankId}}</td>
							<td ms-if="item.status == 1">启用</td>
							<td ms-if="item.status != 1">停用</td>
						</tr>
					</tbody>
					</table>
					<!-- 放置分页 -->
					<div id="common_page"></div>
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
<script src="/js/common.js"></script>
<script src="/js/base.js"></script>

<script src="/js/bankConfig/payBankList.js"></script>
<!-- END 页面基本js -->


</body>
</html>