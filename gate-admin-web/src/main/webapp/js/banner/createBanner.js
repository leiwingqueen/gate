//文件上传
$('#uploadFileInput').on('change',function(){
		uploadFile('/file/common/uploadFile.do','uploadFileInput',function(list){
			if(list.length <= 0 ){
				$('#img_show').attr('src','');
				$('#imgUrl').val('')
				return
			}
			for( var i = 0 ; i < list.length; i++ ){
				$('#imgUrl').val(list[i])
				$('#img_show').attr('src',list[i]);
			}
		})
	})
	
//输入验证
function doCheck() {
     var flag = true;
     //验证时间不能为空
     var efficientTimeString = $.trim(document.getElementById('efficientTimeString').value) ;
     var disabledTimeString = $.trim(document.getElementById('disabledTimeString').value);
     var imgUrl = $.trim(document.getElementById('imgUrl').value);
     var url = $.trim(document.getElementById('url').value);
     if (efficientTimeString.length == 0 | disabledTimeString.length == 0  ){
         flag = false;
         alert("有效、起止时间都不能为空！");
     }
     
    // 图片链接不能为空
     else if(imgUrl.length == 0){
    	 flag = false;
         alert("图片链接不能为空");
     }
     
     //地址链接不能为空
     else if(url.length == 0){
    	 flag = false;
         alert("地址链接不能为空");
     }
     return flag;
     
 }