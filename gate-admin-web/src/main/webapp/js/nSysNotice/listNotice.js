function searchData(page, disR) {
	var fn = arguments.callee;
	var req = 'page=' + (page || 1) + '&size=' + (disR = disR || 100);
	$('.search_box').find('input,select,textarea').each(function() { // 遍历搜索框输入的内容作为参数
		if(this.value && this.value != '') req += '&' + this.name + '=' + this.value.replace(/\n/g, ',');
	});
	var url = '/nSysNotice/list.do';
	$.getJSON(url, req, function(json) {
		if(!json.success) {
			alert(json.message);
			return;
		}
		var count = json.object.count, list = json.object.list, p = json.object.page, pCount = json.object.pageCount;
		noticeList.lists = list
		// 分页
		$.tools.commonPage('#common_page', fn, disR, count, p, pCount);
	});
}

function delNotice(id){
	$.getJSON('/nSysNotice/delete.do?id='+id,function(data){
		if(data.success){
			$.dialog({
			    id: 'dialog-skins-demo',
			    width:250,
			    height:80,
			    title: '温馨提示',
			    content: '删除成功！',
			    ok:function(){return;}
			});
			searchData();
		 return
		}
	})
}
$(function() {
	searchData();
});
var noticeList = avalon.define({
	$id:'notice_list',
	lists:[]
})