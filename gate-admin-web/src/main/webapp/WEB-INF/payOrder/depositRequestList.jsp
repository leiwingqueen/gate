<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@page import="com.elend.gate.order.constant.OrderType"%>
<%@page import="com.elend.gate.channel.constant.ChannelIdConstant"%>
<%@page import="com.elend.gate.channel.constant.WithdrawChannel"%>
<%@page import="com.elend.gate.order.constant.RequestStatus"%>

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
					充值申请列表<small></small>
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
				 		<label class="control-label">申请时间： </label>
				 		<input id="j_start_time" name="startDate" class="input-medium J_calendar" type="text" />
				 		~
						<input id="j_end_time" name="endDate" class="input-medium J_calendar" type="text" />
				 	</div>
				 	<div class="control-group">
				 		<label class="control-label">渠道： </label>
				 		<select id="payChannel" name="payChannel">
				 			<option value="">--请选择--</option>
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
				 		<label class="control-label">P2P订单号： </label>
				 		<input id="partnerOrderId" name="partnerTradeNo" class="" type="text" />
				 	</div>
				 	<div class="control-group">
				 		<label class="control-label">支付网关订单号： </label>
				 		<input id="orderId" name="orderId" class="" type="text" />
				 	</div>
				 	<div class="control-group">
				 		<label class="control-label">状态： </label>
				 		<select id="status" name="status">
				 			<option value="">--请选择--</option>
				 			<%
				 			for(RequestStatus s : RequestStatus.values()) {
				 				out.print("<option value=\""+s.getValue()+"\">"+s.getDesc()+"</option>"); 
				 			}
				 			%>
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
							<th>支付网关订单号</th>
							<th>P2P订单号</th>
							<th>渠道</th>
							<th>渠道名称</th>
							<th>渠道订单号</th>
							<th>金额</th>
							<th>支付类型</th>
							<th>状态</th>
							<th>创建时间</th>
							<th>回调时间</th>
						</tr>
					</thead>
					<tbody id="tbody" ms-controller="dataList">
						<tr ms-repeat-item="list">
							<td>{{$index+1}}</td>
							<td>{{item.orderId}}</td>
							<td>{{item.partnerTradeNo}}</td>
							<td>{{item.payChannel}}</td>
							<td>{{item.channelName}}</td>
							<td>{{item.channelTradeNo}}</td>
							<td>{{item.amount}}</td>
							<td ms-if="item.payType == 1">个人支付</td>
							<td ms-if="item.payType == 2">企业支付</td>
							<td>{{item.statusStr}}</td>
							<td>{{item.createTime | date('yyyy-MM-dd &nbsp; HH:mm:ss')}}</td>
							<td>{{item.callBackTime | date('yyyy-MM-dd &nbsp; HH:mm:ss')}}</td>
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

<!-- BEGIN 页面基本js -->
<script type="text/javascript">
	var channel = $.sysop.kit.getUrlValue('channel');
	var partnerOrderId = $.sysop.kit.getUrlValue('partnerOrderId');
	$("#payChannel").val(channel);
	$("#partnerOrderId").val(partnerOrderId);
</script>
<script src="/js/payOrder/depositRequestList.js"></script>
<!-- END 页面基本js -->


</body>
</html>