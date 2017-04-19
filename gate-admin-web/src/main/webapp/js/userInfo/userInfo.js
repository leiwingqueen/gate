var customService = avalon.define({
	$id : 'customService',
	list : {}
})
var type_model=avalon.define({
	$id:'type',
	type_value:'realName'
})
var userList = avalon.define({
	$id:'user_list',
	lists:[],
	status:0,
	userName:''
})

function showSelect(obj){
	$(obj).hide().next().show()
}
function changeStatus(obj){
	 $(obj).hide().prev().show()
}
//选择分配客服
function saveSelectCustomService(obj,userId) {
	$.post('/userInfo/saveCustomService.do', {
			'userId' : userId,
			'customServiceId' : obj.value
		}, function(json) {
			if(json){
				var text = $(obj).find('option:selected').html()
				$(obj).next().show()
				$(obj).hide().next().show().html(text)
				return;
			}
			$.dialog({
			    id: 'dialog-skins-demo',
			    width:250,
			    height:80,
			    title: '温馨提示',
			    content: '分配客服出错，请联系管理员',
			    ok:function(){return;}
			});
		}) 
}
function selectType(){
	var type = $('#query_type').val()
	if( type == 'realName'){
		type_model.type_value = 'realName'
		$('#user_email').val('');
		$('#user_passport').val('');
		$('#user_cellphone').val('');
		$('#user_id').val('');
	} 
	if( type == 'passport'){
		type_model.type_value = 'passport'
		$('#user_email').val('');
		$('#user_name').val('');
		$('#user_cellphone').val('');
		$('#user_id').val('');
	}
	if( type == 'cellphone'){
		type_model.type_value = 'cellphone'
		$('#user_email').val('');
		$('#user_passport').val('');
		$('#user_name').val('');
		$('#user_id').val('');
	}
	if( type == 'email'){
		type_model.type_value = 'email'
		$('#user_name').val('');
		$('#user_passport').val('');
		$('#user_cellphone').val('');
		$('#user_id').val('');
	}
	if( type == 'userId'){
		type_model.type_value = 'userId'
		$('#user_name').val('');
		$('#user_passport').val('');
		$('#user_cellphone').val('');
		$('#user_email').val('');
	}
}
function selectCustomService(list){
	if(list.length>0){
		$('#selectCustom').append()
	}
}
function searchData(page, disR) {
	var fn = arguments.callee;
	var req = 'page=' + (page || 1) + '&size=' + (disR = disR || 20);
	$('#fm').find('input,select,textarea').each(function() { // 遍历搜索框输入的内容作为参数
		if(this.value && this.value != '') req += '&' + this.name + '=' + this.value.replace(/\n/g, ',');
	});
	var url = '/userInfo/list.do?'+req;
	$.post(url,function(json) {
		if(!json.success) {
			userList.lists = []
			$.tools.commonPage('#common_page', fn, 0, 0, 0, 0);
			return;
		}
		var count = json.object.count, list = json.object.list, p = json.object.page, pCount = json.object.pageCount;
		userList.lists =  json.object.list;
		$.tools.commonPage('#common_page', fn, disR, count, p, pCount);
	});
}

function getDetail(userId){
	window.location = "/userInfo/userInfoDetail.jspx?userId=" + userId;
}

$(function() {
	App.init();
	searchData();
	customService.list = custom_service_list;
});
