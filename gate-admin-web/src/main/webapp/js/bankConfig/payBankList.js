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
	var url = '/bank/payBankList.do?'+req;
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
	searchData();
});

avalon.scan();
