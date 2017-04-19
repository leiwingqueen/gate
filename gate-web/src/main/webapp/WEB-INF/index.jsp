<%@ page language="java" contentType="text/html;charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>广州易贷支付网关</title>
</head>
<body>
	<h2>欢迎来到易贷支付网关系统</h2>	
		<hr>
		<h3>介绍</h3>
		易贷支付网关系统，作为第三方支付渠道和业务系统的对接桥梁，屏蔽第三方支付渠道的差异。
		<p>
		<a href="#d1">支付接口说明</a></p>
		<a href="#d2">返回码说明</a></p>
		<a href="#d3">银行编号定义</a></p>
		<hr>
		<div id="d1"><b>支付接口说明</b>
			<ul>
				<li>接入方调用此接口进行充值操作</li>
				<li>正式环境请求地址：http://gate.gzdai.com/pay.jspx</li>
			</ul>
			<p>
			<span>请求参数</span>
			<center>
			<table border="1" width="80%">
				<tr>
					<th>参数名称</th>
					<th>参数含义</th>
					<th>是否必填</th>
					<th>参数说明</th>
					<th>签名顺序</th>
				</tr>
				<tr>
					<td>partnerId</td>
					<td>接入方ID</td>
					<td>是</td>
					<td>接入方的唯一标识</td>
					<td>1</td>
				</tr>
				<tr>
					<td>partnerOrderId</td>
					<td>接入方订单号</td>
					<td>是</td>
					<td>接入方的订单号</td>
					<td>2</td>
				</tr>
				<tr>
					<td>amount</td>
					<td>金额</td>
					<td>是</td>
					<td>充值金额</td>
					<td>3</td>
				</tr>
				<tr>
					<td>payChannel</td>
					<td>第三方支付渠道</td>
					<td>是</td>
					<td>目前只支持易宝，"YEEPAY"</td>
					<td>4</td>
				</tr>
				<tr>
					<td>redirectUrl</td>
					<td>回调url</td>
					<td>是</td>
					<td>充值回调地址，参数以post方式回调</td>
					<td>5</td>
				</tr>
				<tr>
					<td>notifyUrl</td>
					<td>点对点通知地址</td>
					<td>是</td>
					<td>接入方收到请求后必须返回"SUCCESS"字符串，否则支付网关会继续尝试通知</td>
					<td>6</td>
				</tr>
				<tr>
					<td>bankId</td>
					<td>银行编号</td>
					<td>否</td>
					<td>详见 "银行编号"。为空表示到支付渠道页面进行银行选择。假如该支付渠道不支持银行编号，会去到支付渠道重新选择银行</td>
					<td></td>
				</tr>
				<tr>
					<td>timeStamp</td>
					<td>请求生成时间戳</td>
					<td>是</td>
					<td>防止重放攻击</td>
					<td>7</td>
				</tr>
				<tr>
					<td>ip</td>
					<td>请求IP</td>
					<td>是</td>
					<td>保证用户ip来源的一致性</td>
					<td>8</td>
				</tr>
				<tr>
					<td>hmac</td>
					<td>加密签名</td>
					<td>是</td>
					<td>根据签名顺序和用户秘钥生成，用gate-sdk生成</td>
					<td></td>
				</tr>
			</table>
			</center>
			<span>回调参数</span>
			<center>
			<table border="1" width="80%">
				<tr>
					<th>参数名称</th>
					<th>参数含义</th>
					<th>参数说明</th>
					<th>签名顺序</th>
				</tr>
				<tr>
					<td>partnerId</td>
					<td>接入方ID</td>
					<td>接入方的唯一标识</td>
					<td>1</td>
				</tr>
				<tr>
					<td>resultCode</td>
					<td>返回码</td>
					<td>支付返回码</td>
					<td>2</td>
				</tr>
				<tr>
					<td>message</td>
					<td>返回信息</td>
					<td>返回信息</td>
					<td>3</td>
				</tr>
				<tr>
					<td>partnerOrderId</td>
					<td>接入方订单号</td>
					<td>接入方订单号</td>
					<td>4</td>
				</tr>
				<tr>
					<td>orderId</td>
					<td>支付网关订单号</td>
					<td>支付网关订单号</td>
					<td>5</td>
				</tr>
				<tr>
					<td>amount</td>
					<td>实际到账金额</td>
					<td>实际到账金额</td>
					<td>6</td>
				</tr>
				<tr>
					<td>channelId</td>
					<td>支付渠道ID</td>
					<td>支付渠道ID，目前只支持易宝，"YEEPAY"</td>
					<td>7</td>
				</tr>
				<tr>
					<td>channelOrderId</td>
					<td>支付渠道订单号</td>
					<td>支付渠道订单号</td>
					<td>8</td>
				</tr>
				<tr>
					<td>bankId</td>
					<td>银行编号</td>
					<td>银行编号</td>
					<td>9</td>
				</tr>
				<tr>
					<td>channelPayTime</td>
					<td>支付时间</td>
					<td>支付时间</td>
					<td>10</td>
				</tr>
				<tr>
					<td>channelNoticeTime</td>
					<td>支付渠道通知时间</td>
					<td>支付渠道通知时间</td>
					<td>11</td>
				</tr>
				<tr>
					<td>gateNoticeTime</td>
					<td>支付网关通知时间</td>
					<td>支付网关通知时间</td>
					<td>12</td>
				</tr>
				<tr>
					<td>isNotify</td>
					<td>是否点对点通知</td>
					<td>是否点对点通知</td>
					<td></td>
				</tr>
				<tr>
					<td>bankId</td>
					<td>银行编号</td>
					<td>详见 "银行编号"定义。</td>
					<td></td>
				</tr>
				<tr>
					<td>hmac</td>
					<td>加密签名</td>
					<td>根据签名顺序和用户秘钥生成，用gate-sdk生成</td>
					<td></td>
				</tr>
			</table>
			</center>
		</div>
		<p>
		<hr>
		<div id="d2"><b>返回码说明</b>
		<p>
			<center>
			<table border="1" width="80%">
				<tr>
					<th width="20%">返回码</th>
					<th width="80%">说明</td>
				</tr>
				<tr>
					<td width="20%">000</td>
					<td width="80%">支付成功</td>
				</tr>
				<tr>
					<td>101</th>
					<td>接入方不存在</td>
				</tr>
				<tr>
					<td>500</td>
					<td>支付失败</td>
				</tr>
				<tr>
					<td>...</td>
					<td>待完善...</td>
				</tr>
			</table>
			</center>
		
		</div>
		<hr>
		<div id="d3"><b>银行编号定义</b>
		<p>
			银行编号唯一，跟具体的支付网关无关。
			<center>
			<table border="1" width="80%">
				<tr>
					<th width="20%">银行编号</th>
					<th width="80%">银行名称</td>
				</tr>
				<tr>
					<td width="20%">ICBC</td>
					<td width="80%">工商银行</td>
				</tr>
				<tr>
					<td width="20%">CMBC</td>
					<td width="80%">招商银行</td>
				</tr>
				<tr>
					<td width="20%">ABC</td>
					<td width="80%">中国农业银行</td>
				</tr>
				<tr>
					<td width="20%">CCB</td>
					<td width="80%">建设银行</td>
				</tr>
				<tr>
					<td width="20%">BCCB</td>
					<td width="80%">北京银行</td>
				</tr>
				<tr>
					<td width="20%">BOCO</td>
					<td width="80%">交通银行</td>
				</tr>
				<tr>
					<td width="20%">CIB</td>
					<td width="80%">兴业银行</td>
				</tr>
				<tr>
					<td width="20%">CEB</td>
					<td width="80%">光大银行</td>
				</tr>
				<tr>
					<td width="20%">BOC</td>
					<td width="80%">中国银行</td>
				</tr>
				<tr>
					<td width="20%">PINGAN</td>
					<td width="80%">平安银行</td>
				</tr>
				<tr>
					<td width="20%">ECITIC</td>
					<td width="80%">中信银行</td>
				</tr>
				<tr>
					<td width="20%">SDB</td>
					<td width="80%">深圳发展银行</td>
				</tr>
				<tr>
					<td width="20%">GDB</td>
					<td width="80%">广发银行</td>
				</tr>
				<tr>
					<td width="20%">SHB</td>
					<td width="80%">上海银行</td>
				</tr>
				<tr>
					<td width="20%">SPDB</td>
					<td width="80%">上海浦东发展银行</td>
				</tr>
				<tr>
					<td width="20%">POST</td>
					<td width="80%">中国邮政</td>
				</tr>
				<tr>
					<td width="20%">BJRCB</td>
					<td width="80%">北京农村商业银行</td>
				</tr>
				<tr>
					<td width="20%">HXB</td>
					<td width="80%">华夏银行</td>
				</tr>
			</table>
			</center>
		
		</div>
	</body>
</body>
</html>