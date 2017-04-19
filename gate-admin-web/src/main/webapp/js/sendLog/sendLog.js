var sendLog = avalon.define({
	$id:'sendLog',
	list:[]
})

//查询列表
function searchData(page, disR) {
	var fn = arguments.callee;
	var req = 'page=' + (page || 1) + '&size=' + (disR = disR || 20);
	$('#fm').find('input,select,textarea').each(function() { // 遍历搜索框输入的内容作为参数
		if(this.value && this.value != '') req += '&' + this.name + '=' + this.value.replace(/\n/g, ',');
	});
	var url = '/sendLog/list.do';
	$.getJSON(url, req, function(json) {
		if(!json.success) {
			alert(json.message);
			return;
		}
		var count = json.object.count, list = json.object.list, p = json.object.page, pCount = json.object.pageCount;
		sendLog.list = list
		// 分页
		$.tools.commonPage('#common_page', fn, disR, count, p, pCount);
	});
}

//查询短信剩余条数
function getMsgCount(){
	$.post("/sendLog/getMsgCount.do",function(json){
		if(!json.success){
			alert(data.message);
			return;
		}
		document.getElementById("zsmsgCount").innerHTML = json.object[0];
		document.getElementById("v6msgCount").innerHTML = json.object[1];
		document.getElementById("mcwxmsgCount").innerHTML = json.object[2];
	})
}

//初始化时间
function initDate() {
	document.getElementById("endCreateTime").value = $.sysop.kit.date
			.getDateStr(0)
			+ " 23:59:59";
	document.getElementById("startCreateTime").value = $.sysop.kit.date
			.getDateStr(7)
			+ " 00:00:00";
}

$(function() {
	App.init();
	initDate();
	searchData();
	getMsgCount();
});

avalon.scan()