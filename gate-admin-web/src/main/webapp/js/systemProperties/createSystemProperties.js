var systemPropertiesList = avalon.define({
	$id:'systemProperties_list',
	property_key:'',
	lists:[]
})
var propertyKey=$.sysop.kit.getUrlValue('propertyKey');
//验证
function validate(obj,message){
	var flag = true
	if( obj.length <= 0 ){
		 $.dialog({
			    id: 'dialog-skins-demo',
			    width:250,
			    height:80,
			    title: '温馨提示',
			    content: message,
			    ok:function(){
			    		flag = false;
			    	}
			});
		 flag = false
	}
	return flag
}

//保存提交
function op(){
	
	var flag = validate($('#propertyKey').val(),'配置id不能为空！')
	if( !flag ) return
	flag = validate($('#propertyName').val(),'配置名称不能为空！')
	if( !flag ) return
	flag = validate($('#propertyValue').val(),'配置值不能为空！')
	if( !flag ) return
	//var propertyKey = $('#propertyKey').val()
	var params = $('#fm').serialize();
	console.log(params)
	if(systemPropertiesList.property_key!= ''){
		$.post('/systemProperties/save.do', params, function(data) {
			$.alert(data.message);
			searchData();
		}); 
	}
}

//查询列表
function searchData(page, disR) {
	var fn = arguments.callee;
	var req = 'page=' + (page || 1) + '&size=' + (disR = disR || 100);
	$('.search_box').find('input,select,textarea').each(function() { // 遍历搜索框输入的内容作为参数
		if(this.value && this.value != '') req += '&' + this.name + '=' + this.value.replace(/\n/g, ',');
	});
	var url = '/systemProperties/list.do';
	$.getJSON(url, req, function(json) {
		if(!json.success) {
			alert(json.message);
			return;
		}
		var count = json.object.count, list = json.object.list, p = json.object.page, pCount = json.object.pageCount;
		systemPropertiesList.lists = list
		// 分页
		$.tools.commonPage('#common_page', fn, disR, count, p, pCount);
	});
}

//删除
function delSystemProperties(propertyKey){
	$.getJSON('/systemProperties/delete.do?propertyKey='+propertyKey,function(data){
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

//获取数据
function getEdit(){
	
	if (!propertyKey) {
		return;
	}
	$.post('/systemProperties/get.do', 'propertyKey=' + propertyKey , function(data) {
		if (!data.success) {
			$.alert(data.message);
			return false;
		} 
		if( data.object.propertyKey != '' ){
			$('#propertyKey').val(data.object.propertyKey)
			$('#propertyKey').attr('readonly','readonly')
		}
		if( data.object.propertyName != '' ){
			$('#propertyName').val(data.object.propertyName)
		}
		if( data.object.propertyValue != '' ){
			$('#propertyValue').val(data.object.propertyValue)
		}
		
	}); 
}

$(function() {
	App.init();
	searchData();
	getEdit();
	
});

avalon.scan()