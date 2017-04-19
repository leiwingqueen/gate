
var balance1 = 0;
var balance2 = 0;

function queryBalance1(userId) {
	if(userId == '') {
		$("#usableBalance1").html(0);
		balance1 = 0;
		return ;
	}
	
	var url = '/financial/queryUserBalance.do?userId=' + userId;
	$.post(url, function(json) {
		if(!json.success) {
			$("#usableBalance1").html(0);
			balance1 = 0;
			return ;
		}
		balance1 = parseFloat(json.object);
		$("#usableBalance1").html(json.object);
	});
}

function queryBalance2(userId) {
	if(userId == '') {
		$("#usableBalance2").html(0);
		balance2 = 0;
		return ;
	}
	
	var url = '/financial/queryUserBalance.do?userId=' + userId;
	$.post(url, function(json) {
		if(!json.success) {
			$("#usableBalance2").html(0);
			balance2 = 0;
			return ;
		}
		balance2 = parseFloat(json.object);
		$("#usableBalance2").html(json.object);
	});
}

//验证奖品等级、最大中奖次数
jQuery.validator.addMethod("checkAmount", function(value, element, param) {
	return this.optional(element) || (/^([0-9]\d*)(\.\d{1,2})?$/).test(value);
}, "金额必须为正数且不超过两位小数");

jQuery.validator.addMethod("checkBalance1", function(value, element, param) {
	return this.optional(element) || parseFloat(value) <= balance1;
}, "金额必须小于等于转出账户余额");

jQuery.validator.addMethod("checkBalance2", function(value, element, param) {
	return this.optional(element) || parseFloat(value) <= balance2;
}, "金额必须小于等于转出账户余额");

App.validate('#fm1',function(){
	var params = $('#fm1').serialize();
	var url = '/financial/transferOut.do';
	$.post(url, params, function(json) {
		if(!json.success) {
			alert(json.message);
			return ;
		}
		alert(json.message);
		queryBalance1($("#outUserId1").val());
		queryBalance2($("#outUserId2").val());
		//window.location.href = '/financial/transfer.jspx';
	});
})

App.validate('#fm2',function(){
	var params = $('#fm2').serialize();
	var url = '/financial/transferIn.do';
	$.post(url, params, function(json) {
		if(!json.success) {
			alert(json.message);
			return ;
		}
		alert(json.message);
		queryBalance1($("#outUserId1").val());
		queryBalance2($("#outUserId2").val());
		//window.location.href = '/financial/transfer.jspx';
	});
})

$(function() {
	queryBalance1($("#outUserId1").val());
	queryBalance2($("#outUserId2").val());
});

avalon.scan();
