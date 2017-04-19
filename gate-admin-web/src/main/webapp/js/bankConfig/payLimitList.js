var dataList = avalon.define({
	$id : 'dataList',
	list : []
});

function searchData(page, disR) {
	var fn = arguments.callee;
	var req = 'page=' + (page || 1) + '&size=' + (disR = disR || 20);
	$('#fm').find('input,select,textarea').each(function() { // 遍历搜索框输入的内容作为参数
		if(this.value && this.value != '') req += '&' + this.name + '=' + this.value.replace(/\n/g, ',');
	});
	var url = '/bank/payLimitList.do?'+req;
	$.post(url,function(json) {
		if(!json.success) {
			dataList.lists = []
			$.tools.commonPage('#common_page', fn, 0, 0, 0, 0);
		}
		var count = json.object.count, list = json.object.list, p = json.object.page, pCount = json.object.pageCount;
		dataList.list =  json.object.list;
		$.tools.commonPage('#common_page', fn, disR, count, p, pCount);
	});
}

//初始化时间
function initDate() {
	document.getElementById("j_end_time").value = $.sysop.kit.date
			.getDateStr(0)
			+ " 23:59:59";
	
	var date = new Date();
	var year = date.getFullYear();
    var month = date.getMonth()+1;
    if(date.getDate() == 1){
		month = month - 1;
	}
    if (month<10){
        month = "0"+month;
    }
    var firstDay = year + '-' + month + '-01'+ " 00:00:00"  ;
	document.getElementById("j_start_time").value = firstDay;
	
}

//编辑记录
function edit(id) {
	window.location.href = "/bank/payLimitAdd.jspx?id=" + id;
}

function deleteRec(id) {
	if(window.confirm('你确定要删除该限额？')){
		var url = '/bank/payLimitDelete.do?id=' + id;
		$.post(url,function(json) {
			if(!json.success) {
				alert("删除失败, " + json.message);
			}
			alert(json.message);
			searchData();
		});
	}
}


$(function() {
	initDate();
	searchData();
});

avalon.scan();
