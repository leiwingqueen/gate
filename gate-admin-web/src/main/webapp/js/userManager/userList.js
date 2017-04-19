var customService = avalon.define({
	$id : 'customService',
	list : {}
})
var type_model=avalon.define({
	$id:'type',
	type_value:'realName'
})
var loanList = avalon.define({
	$id:'loan_list',
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
	$.post('/userManager/saveCustomService.do', {
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
	$('.search_box').find('input,select,textarea').each(function() { // 遍历搜索框输入的内容作为参数
		if(this.value && this.value != '') req += '&' + this.name + '=' + this.value.replace(/\n/g, ',');
	});
	var url = '/userManager/list.do';
	$.post(url, req, function(json) {
		if(!json.success) {
			loanList.lists = []
			$.tools.commonPage('#common_page', fn, 0, 0, 0, 0);
			return;
		}
		var count = json.object.count, list = json.object.list, p = json.object.page, pCount = json.object.pageCount;
		loanList.lists =  json.object.list;
		$.tools.commonPage('#common_page', fn, disR, count, p, pCount);
	});
}

function getDetail(userId){
	window.location = "/userManager/userInfoDetail.jspx?userId=" + userId;
}
var hightChartStatistics = function () {
$.post('/userManager/getUserStatistics.do',function(data){
		if(data.success){
		    $('#statisticsChart').highcharts({
		        chart: {
		            type: 'column'
		        },
		        title: {
		            text: '用户认证情况统计'
		        },
		        subtitle: {
		            text: ''
		        },
		        xAxis: {
		            categories: [
		                '总人数',
		                'VIP人数',
		                '实名认证人数',
		                '银行卡认证',
		                '第三方托管授权',
		                '当月有投资记录',
		                '当月无投资记录'
		                
		            ]
		        },
		        yAxis: {
		            min: 0,
		            title: {
		                text: '人数(人)'
		            }
		        },
		        tooltip: {
		            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
		            pointFormat: '<tr><td style="color:{series.color};padding:5">{series.name}: </td>' +
		                '<td style="padding:0"><b>{point.y:.1f} 人</b></td></tr>',
		            footerFormat: '</table>',
		            shared: true,
		            useHTML: true
		        },
		        plotOptions: {
		            column: {
		                pointPadding: 0.2,
		                borderWidth: 0
		            }
		        },
		        series: [{
		            name: '总人数',
		            data: [data.object.allUserNum]

		        }, {
		            name: 'VIP人数',
		            data: [data.object.vipUserNum]

		        }, {
		            name: '实名认证人数',
		            data: [data.object.realNameAuthNum]

		        }, {
		            name: '银行卡认证',
		            data: [data.object.bankAuthNum]

		        },{
		            name: '第三方托管授权',
		            data: [data.object.thirdPartyNum]

		        },{
		            name: '当月有投资记录',
		            data: [data.object.investRecordNum]

		        },{
		            name: '当月无投资记录',
		            data: [data.object.noInvestRecordNum]

		        }]
		    });
		}
	})

}
$(function() {
	searchData();
	hightChartStatistics();
	customService.list = custom_service_list 
});
