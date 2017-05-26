	$(document).ready(function() {
		var controller = new Vue({
            el:"#box",
            data:{
                myData:[
//                    {name:"张三",age:22},
//                    {name:"李四",age:32},
//                    {name:"王五",age:23},
//                    {name:"赵六",age:26}
                ],

                searchKey:'',
                now:-1

            },
            mounted: function () { //
                //在这里写试试
            	this.$http.jsonp('/maven/file/listFile.do',{
                    //callback 名称,默认callback
                    jsonp:'cb',
                    //参数
                    params: {
                        wd:"d"
                    }
                }).then(function(res){
                    this.myData = res.data.s;
                },function(res){
                    alert(res.status);
                });
            },
            methods:{
                get:function (ev) {
                    //判断是否按上下键,如果按了上下键,不再执行查找
                    if(ev.keyCode == 38 || ev.keyCode == 40)return;

                    //当按下回车键时,跳转页面搜索
                    if(ev.keyCode == 13){
                        this.searchBtn();
                    }

                    this.$http.jsonp('https://sp0.baidu.com/5a1Fazu8AA54nxGko9WTAnF6hhy/su',{
                        //callback 名称,默认callback
                        jsonp:'cb',
                        //参数
                        params: {
                            wd:this.searchKey
                        }
                    }).then(function(res){
                        this.myData = res.data.s;
                    },function(res){
                        alert(res.status);
                    });
                },
                searchBtn:function(){
                    window.open('https://www.baidu.com/s?wd='+this.searchKey);
                    this.searchKey = '';
                },
                changeDown:function () { //按了下键
                    this.now++;
                    if(this.now == this.myData.length)this.now = 0;
                    this.searchKey = this.myData[this.now];
                },
                changeUp:function () { //按了上键
                    this.now--;
                    if(this.now == -1)this.now = this.myData.length-1;
                    this.searchKey = this.myData[this.now];
                }
            }
        });
		
		
		
		
		//删除一个用户
		$("body").on('click','.J_delete',function() {
			var _thisfilename = $(this).attr('id');
			layer.confirm('您确定要删除吗？确认后将不可恢复', {btn: ['删除','取消']}, function(){
				//-------------
				$.ajax({
					type : "GET",
					url : "${webSiteDomain}/file/delete.do",
					data : {filename : _thisfilename},
					timeout: 5000,
					dataType: "jsonp",//"xml", "html", "script", "json", "jsonp", "text".
					jsonp: "callback",
					beforeSend: function () {
						layer.msg('正在处理…');
					},
					success : function(json) {
						console.log(json);
						if(json.Code=="0000"){
							layer.msg('删除成功');
							$('#J_'+_thisfilename).remove();
						}else if(json.Code=="-1"){
							layer.msg('删除失败');
						}else{
							layer.msg('服务器出现错误');
						}
					},
					error: function(XMLHttpRequest, textStatus, errorThrown){
						layer.msg('出现错误，系统可能出现异常情况！');
						console.log("出现错误，系统可能出现异常情况！");
					}
				});
				//------------------
			});
		});

		






	});
	
	