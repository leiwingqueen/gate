
var balance1 = 0;

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

//验证奖品等级、最大中奖次数
jQuery.validator.addMethod("checkAmount", function(value, element, param) {
	return this.optional(element) || (/^(\-?[0-9]\d*)(\.\d{1,2})?$/).test(value);
}, "金额必须为正数或负数且不超过两位小数");

jQuery.validator.addMethod("checkBalance1", function(value, element, param) {
	return this.optional(element) || (parseFloat(value) + balance1) >= 0;
}, "减少金额的绝对值必须小于等于账户余额");

App.validate('#fm1',function(){
	var params = $('#fm1').serialize();
	var url = '/financial/adjustAccount.do';
	$.post(url, params, function(json) {
		if(!json.success) {
			alert(json.message);
			return ;
		}
		alert(json.message);
		queryBalance1($("#outUserId1").val());
		//window.location.href = '/financial/transfer.jspx';
	});
})

$(function() {
	queryBalance1($("#outUserId1").val());
});

avalon.scan();
