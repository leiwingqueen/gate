
var dataList = avalon.define({
	$id : 'dataList',
	data : {
		id : 0
		}
});

//验证奖品等级、最大中奖次数
jQuery.validator.addMethod("checkAmount", function(value, element, param) {
	return this.optional(element) || (/^(\-?[0-9]\d*)(\.\d{1,2})?$/).test(value);
}, "金额必须为正数或负数且不超过两位小数");


App.validate('#fm',function(){
	var params = $('#fm').serialize();
	var url = '/bank/payLimitAdd.do';
	$.post(url, params, function(json) {
		if(!json.success) {
			alert(json.message);
			return ;
		}
		alert(json.message);
		//window.location.href = '/financial/transfer.jspx';
	});
});

//获取id
var id = $.sysop.kit.getUrlValue('id');

function initData(id) {
	if(id != null && id != '') {
		var params = $('#fm').serialize();
		var url = '/bank/payLimitGet.do?id=' + id;
		$.post(url, params, function(json) {
			if(!json.success) {
				alert(json.message);
				return ;
			}
			
			//初始化数据
			dataList.data = json.object;
			
		});
	}
}

$(function() {
	initData(id);
});

avalon.scan();
