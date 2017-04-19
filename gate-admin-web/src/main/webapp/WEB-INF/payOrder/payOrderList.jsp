<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
					工作台<small></small>
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
						<i class="icon-reorder"></i> 快捷操作
					</div>
					<div class="tools">
						<a href="javascript:;" class="collapse"></a>
					</div>
				</div>
				<div class="portlet-body">
					<button class="btn" type="button"
						onclick="applyPage('/workflow/create.jspx?processDefinitionKey=keyApply')">员工密钥申请</button>
					<!-- <button class="btn" type="button"
						onclick="applyPage('/keyApply/readyEditApply.jspx')">员工密钥变更</button> -->
					<button class="btn" type="button"
						onclick="applyPage('/workflow/create.jspx?processDefinitionKey=serverApply')">服务器权限申请</button>
					<button class="btn" type="button"
						onclick="applyPage('/serverFlow/serverSudoApply.jspx')">sudo权限申请</button>
				</div>
			</div>
			<div class="portlet box default">
				<div class="portlet-title">
					<div class="caption">
						<i class="icon-reorder"></i> 待办任务
					</div>
					<div class="tools">
						<a href="/workflow/taskList.jspx" title="查看更多" class="view-more"></a>
						<a href="javascript:;" class="collapse"></a>
					</div>
				</div>
				<div class="portlet-body">
					<table class="table table-bordered table-striped table-hover">
						<thead>
							<tr>
								<th>流程ID</th>
								<th>摘要信息</th>
								<th>流程类型</th>
								<th>提单人</th>
								<th>提单时间</th>								
								<th>所在步骤</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody id="taskListTbody">
							<script id="taskListTmpl" type="text/html">
										{{each list as v i}}
	                        		<tr>
	                        			<td><div>{{v.task.processInstanceId}}</div></td>
	                        			<td><div>{{v.abstractInfo}}</div></td>
	                        			<td><div>{{v.processDefinition.name}}</div></td>
	                        			<td><div>{{v.historicProcessInstance.startUserNick}}</div></td>	                        			
										<td><div>{{v.historicProcessInstance.startTime}}</div></td>
										<td><div>{{v.task.name}}</div></td>										
										<td>
											<a href="/workflow/traceDetail.jspx?processInstanceId={{v.task.processInstanceId}}" class="btn mini green"><i class="icon-book"></i> 查看</a>											
											<a href="/workflow/complete.jspx?taskId={{v.task.id}}" class="btn mini blue"><i class="icon-pencil"></i> 处理</a>
											<a href="/workflow/trace.jspx?processInstanceId={{v.task.processInstanceId}}" class="btn mini red"><i class="icon-picture"></i> 跟踪</a>
										</td>
	                        		</tr>
										{{/each}}
             						</script>
						</tbody>
					</table>
				</div>
			</div>
			<div class="portlet box default">
				<div class="portlet-title">
					<div class="caption">
						<i class="icon-reorder"></i> 我的任务
					</div>
					<div class="tools">
						<a href="/workflow/myTask.jspx" title="查看更多" class="view-more"></a>
						<a href="javascript:;" class="collapse"></a>
					</div>
				</div>
				<div class="portlet-body">
					<table class="table table-bordered table-striped table-hover"
						id="tb_myTask">
						<thead>
							<tr>
								<th>流程ID</th>
								<th>摘要信息</th>
								<th>流程类型</th>
								<th>提单人</th>
								<th>提单时间</th>
								<th>所在步骤</th>									
								<th>操作</th>
							</tr>
						</thead>
						<tbody id="myTaskTbody">
							<script id="myTaskTmpl" type="text/html">
										{{each list as v i}}
	                        		<tr>
	                        			<td><div>{{v.task.processInstanceId}}</div></td>
										<td><div>{{v.abstractInfo}}</div></td>
	                        			<td><div>{{v.processDefinition.name}}</div></td>
	                        			<td><div>{{v.historicProcessInstance.startUserNick}}</div></td>
										<td><div>{{v.historicProcessInstance.startTime}}</div></td>
										<td><div>{{v.task.name}}</div></td>										
										<td>
											<a href="/workflow/traceDetail.jspx?processInstanceId={{v.task.processInstanceId}}" class="btn mini green"><i class="icon-book"></i> 查看</a>
											<a href="/workflow/complete.jspx?taskId={{v.task.id}}" class="btn mini blue"><i class="icon-pencil"></i> 处理</a>
											<a href="/workflow/trace.jspx?processInstanceId={{v.task.processInstanceId}}" class="btn mini red"><i class="icon-picture"></i> 跟踪</a>
										</td>
	                        		</tr>
										{{/each}}
             						</script>
						</tbody>
					</table>
				</div>
			</div>
			<div class="portlet box default">
				<div class="portlet-title">
					<div class="caption">
						<i class="icon-reorder"></i> 我的申请
					</div>
					<div class="tools">
						<a href="/workflow/myApply.jspx" title="查看更多" class="view-more"></a>
						<a href="javascript:;" class="collapse"></a>
					</div>
				</div>
				<div class="portlet-body">
					<table class="table table-bordered table-striped table-hover"
						id="tb_myApply">
						<thead>
							<tr>
								<th>流程ID</th>
								<th>摘要信息</th>
								<th>流程类型</th>
								<th>提单人</th>
								<th>提单时间</th>
								<th>结单时间</th>								
								<th>当前步骤</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody id="myApplyTbody">
							<script id="myApplyTmpl" type="text/html">
										{{each list as v i}}
	                        		<tr>
	                        			<td><div>{{v.historicProcessInstance.processInstanceId}}</div></td>
	                        			<td><div>{{v.abstractInfo}}</div></td>
	                        			<td><div>{{v.processDefinition.name}}</div></td>
	                        			<td><div>{{v.historicProcessInstance.startUserNick}}</div></td>
	                        			<td><div>{{v.historicProcessInstance.startTime}}</div></td>
	                        			<td><div>{{v.historicProcessInstance.endTime||'--'}}</div></td>
										<td><div>{{v.task.name||'已完成'}}</div></td>
										<td>
											<a href="/workflow/traceDetail.jspx?processInstanceId={{v.historicProcessInstance.processInstanceId}}" class="btn mini green"><i class="icon-book"></i> 查看</a>
											<a href="/workflow/trace.jspx?processInstanceId={{v.historicProcessInstance.processInstanceId}}" class="btn mini red" style="{{v.task.name||'display:none'}}"><i class="icon-picture" ></i> 跟踪</a>
										</td>
	                        		</tr>
										{{/each}}
             						</script>
						</tbody>
					</table>
				</div>
			</div>
			<div class="portlet box default">
				<div class="portlet-title">
					<div class="caption">
						<i class="icon-reorder"></i> 统计信息
					</div>
					<div class="tools">
						<a href="javascript:;" class="collapse"></a>
					</div>
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
<script src="/js/common.js"></script>
<script src="/js/base.js"></script>
<!-- BEGIN 页面基本js -->
<script>

</script>
<!-- END 页面基本js -->
</body>
</html>

