/* BEGIN 页面基本js */
	var id=$.sysop.kit.getUrlValue('id');
	var editor;
	$(document).ready(function() {
		KindEditor.ready(function(K) {
			editor = K.create('textarea[name="content"]', {
				cssPath : '/kindeditor/plugins/code/prettify.css',
				uploadJson : '/file/uploadFile.do',
				fileManagerJson : '/file/uploadFile.do',
				allowFileManager : true,
				afterCreate : function() {
					var self = this;
					K.ctrl(document, 13, function() {
						self.sync();
					});
					K.ctrl(self.edit.doc, 13, function() {
						self.sync();
					});
				}
			});
			prettyPrint();
			
		});		
	});
	
	//文件上传
	$('#uploadFileInput').on('change',function(){
		uploadFile('/file/common/uploadFile.do','uploadFileInput',function(list){
			if(list.length <= 0 ){
				$('#img_show').attr('src','');
				$('#digestImg').val('')
				return
			}
			for( var i = 0 ; i < list.length; i++ ){
				$('#digestImg').val(list[i])
				$('#img_show').attr('src',list[i]);
			}
		})
	})
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
	function countChar(textareaNamezzjs,spanName){
		
		if(document.getElementById(spanName).innerHTML == 0 ){
			$.dialog({
				id: 'dialog-skins-demo',
				width:250,
				height:80,
				title: '温馨提示',
				content: '亲，摘要最多只能输入100个字符，别太贪心哦！',
				ok:function(){
					return ;
				}
			});
			return;
		}
		 document.getElementById(spanName).innerHTML=100-document.getElementById(textareaNamezzjs).value.length;
	}
	
	
	function op(){
		$('#content').val(escape2Html(editor.html()))
		var flag = validate($('.J_calendar').val(),'发布有效起止时间不能为空！')
		if( !flag ) return
		flag = validate($('#title').val(),'标题不能为空！')
		if( !flag ) return
		flag = validate($('#digest').val(),'内容摘要不能为空！')
		if( !flag ) return
		flag = validate( $('#content').val(),'公告内容不能为空！')
		if( !flag ) return
		
		var params = $('#fm').serialize()
		if(id!=null&&id.length!=0){
			$.post('/nSysNotice/save.do?id='+id, params, function(data) {
				alert(data.message);
			}); 
		}else{
			$.post('/nSysNotice/save.do', params, function(data) {
				alert(data.message);
			}); 
		}
	} 
	
	function escape2Html(str) {
 		var arrEntities={'lt':'<','gt':'>','nbsp':' ','amp':'&','quot':'"'};
 		return str.replace(/&(lt|gt|nbsp|amp|quot);/ig,function(all,t){return arrEntities[t];});
	}
	
	var checkForm = function(){
		
	}
	
	$('#noticeType').change(function(){
		if($('#noticeType').val() == 'NOTIC'){
			$('#notice-div').show()
		}else{
			$('#notice-div').hide()
			$('#instationContent').val('');
			$('#phoneContent').val('');
			$('#emailContent').val('');
		}
	})
	$('#instation').click(function(){
		if($('#instation').attr('checked')){
			$('.instationContent').css('display','block');
		}else{
			$('.instationContent').css('display','none')
			$('#instationContent').val('');
		}
	})
	$('#cellphone').click(function(){
		if($('#cellphone').attr('checked')){
			$('.phoneContent').css('display','block');
		}else{
			$('.phoneContent').css('display','none');
			$('#phoneContent').val('');
		}
	})
	$('#email').click(function(){
		if($('#email').attr('checked')){
			$('.emailContent').css('display','block');
		}else{
			$('.emailContent').css('display','none');
			$('#emailContent').val('');
		}
	})
	