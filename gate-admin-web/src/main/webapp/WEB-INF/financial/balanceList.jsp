<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@page import="com.elend.gate.order.constant.OrderType"%>
<%@page import="com.elend.gate.channel.constant.ChannelIdConstant"%>
<%@page import="com.elend.gate.channel.constant.WithdrawChannel"%>

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
					账本列表<small></small>
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
						<i class="icon-reorder"></i>总账
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
							<th>用户ID</th>
							<th>账户名称</th>
							<th>收入</th>
							<th>支出</th>
							<th>余额</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody id="tbody" ms-controller="dataList">
						<tr ms-repeat-item="list">
							<td>{{$index+1}}</td>
							<td>{{item.userId}}</td>
							<td>{{item.remark}}</td>
							<td>{{item.income}}</td>
							<td>{{item.outlay}}</td>
							<td>{{item.usableBalance}}</td>
							
							<td>
								<a href="/payOrder/payOrderList.jspx">充值申请</a>
								&nbsp;&nbsp;&nbsp;
								<a href="/order/withdrawRequestList.jspx">提现申请</a>
								&nbsp;&nbsp;&nbsp;
								<a ms-href="/financial/transactionList.jspx?userId={{item.userId}}">资金流水</a>
							</td>
							
						</tr>
					</tbody>
					</table>
					<!-- 放置分页 -->
					<div id="common_page"></div>
				</div>
			</div>
			<div class="portlet box default">
				<div class="portlet-title">
					<div class="caption">
						<i class="icon-reorder"></i>渠道分账
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
							<th>用户ID</th>
							<th>账户名称</th>
							<th>收入</th>
							<th>支出</th>
							<th>余额</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody id="tbody" ms-controller="dataList1">
						<tr ms-repeat-item="list">
							<td>{{$index+1}}</td>
							<td>{{item.userId}}</td>
							<td>{{item.remark}}</td>
							<td>{{item.income}}</td>
							<td>{{item.outlay}}</td>
							<td>{{item.usableBalance}}</td>
							
							<td>
								<span ms-if="item.payChannel != null">
									&nbsp;&nbsp;&nbsp;
									<a ms-href="/payOrder/payOrderList.jspx?channel={{item.payChannel}}">充值申请</a>
								</span>
								<span ms-if="item.withdrawChannel != null">
									&nbsp;&nbsp;&nbsp;
									<a ms-href="/order/withdrawRequestList.jspx?channel={{item.withdrawChannel}}">提现申请</a>
								</span>
								&nbsp;&nbsp;&nbsp;
								<a ms-href="/financial/transactionList.jspx?userId={{item.userId}}">资金流水</a>
							</td>
							
						</tr>
					</tbody>
					</table>
					<!-- 放置分页 -->
					<div id="common_page"></div>
				</div>
			</div>
			<div class="portlet box default">
				<div class="portlet-title">
					<div class="caption">
						<i class="icon-reorder"></i>银行卡分账
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
							<th>用户ID</th>
							<th>账户名称</th>
							<th>收入</th>
							<th>支出</th>
							<th>余额</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody id="tbody" ms-controller="dataList2">
						<tr ms-repeat-item="list">
							<td>{{$index+1}}</td>
							<td>{{item.userId}}</td>
							<td>{{item.remark}}</td>
							<td>{{item.income}}</td>
							<td>{{item.outlay}}</td>
							<td>{{item.usableBalance}}</td>
							
							<td>
								<a ms-href="/financial/transactionList.jspx?userId={{item.userId}}">资金流水</a>
							</td>
							
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
<script src="/js/financial/balanceList.js"></script>
<!-- END 页面基本js -->
</body>
</html>