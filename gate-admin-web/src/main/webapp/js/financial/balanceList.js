var dataList = avalon.define({
	$id : 'dataList',
	list : []
});

function searchData(page, disR) {
	
	var url = '/financial/balanceListData.do?type=TOTAL_ACCOUNT';
	$.post(url,function(json) {
		if(!json.success) {
			dataList.list = [];
		}
		dataList.list =  json.object;
	});
}

var dataList1 = avalon.define({
	$id : 'dataList1',
	list : []
});

function searchData1(page, disR) {
	
	var url = '/financial/balanceListData.do?type=CHANNEL_ACCOUNT';
	$.post(url,function(json) {
		if(!json.success) {
			dataList1.list = [];
		}
		dataList1.list =  json.object;
	});
}

var dataList2 = avalon.define({
	$id : 'dataList2',
	list : []
});

function searchData2(page, disR) {
	
	var url = '/financial/balanceListData.do?type=BANK_ACCOUNT';
	$.post(url,function(json) {
		if(!json.success) {
			dataList2.list = [];
		}
		dataList2.list =  json.object;
	});
}


$(function() {
	searchData();
	searchData1();
	searchData2();
});

avalon.scan();

