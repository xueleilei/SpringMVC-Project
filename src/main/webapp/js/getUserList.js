$(function() {



	var app = new Vue({
		el: '#app',
		data: {
			peps: '',

			username:'',
            password:'',
			
			nowIndex:-100,
			nowUserID:'',
			nowUsername:''
		},

		mounted: function() {
			this.getJsonInfo()
		},
		methods: {
			getJsonInfo: function() {
				this.$http.get('/userController/getlistUser.do',{
					//callback 名称,默认callback
					jsonp:'cb',
					//传入参数
					params: {
						wd:"params"
					}
				}).then(function(response){
					//alert(response.data.Code);
					//response.data  是内容,.data是节点
					var resdata = response.data.data;
					this.peps = resdata;
					

				}).catch(function(response){

					console.log(response);
					console.log("居然没有弹窗");
				})
			},
			addUser: function() {
				var username = this.username;
                var password = this.password;
           
                
                if(!username){
                    layer.msg("请填写用户名");
                    return false;
                }
                if(!password){
                    layer.msg("请填写密码");
                    return false;
                }
                
                
                this.$http.get('/userController/insertUser.do',{
					//callback 名称,默认callback
					jsonp:'cb',
					//传入参数
					params: {
						name:username,
						password:password
					}
				}).then(function(response){
					var resCode = response.data;
					
					if(resCode.Code=='0000'){
						swal("添加成功！", "", "success");
						$('#addUserModal').modal('hide');
						this.getJsonInfo();
						
						this.username = '';
		                this.password = '';
					}else if(resCode.Code=='1001'){
						sweetAlert("该用户已经存在，请换一个用户名!", "", "error");
					}else if(resCode.Code=='-1'){
						sweetAlert("错误!", "系统可能出现异常情况！", "error");
					}
					

				}).catch(function(response){

					console.log("出现错误，系统可能出现异常情况！");
				})
	
			},
			setDeleteInfo:function (index,userId,username) {
				this.nowIndex = index;
				this.nowUserID = userId;
				this.nowUsername = username;
				//alert(this.nowIndex+"=="+this.nowUserID+"=="+this.nowUsername);

			},
			deleteMsg:function (nowIndex,nowUserID) {
				//alert(nowIndex+"=="+nowUserID);

				if(nowIndex=="all"){
					this.peps = [];
				}else{
					this.$http.get('/userController/deleteUser.do',{
						//callback 名称,默认callback
						jsonp:'cb',
						//参数
						params: {
							id:nowUserID,
							name:this.nowUsername
						}
					}).then(function(response){
						//alert(response.data.Code);
						//response.data  是内容,.data是节点
						var resdata = response.data.Code;
						if(resdata == "0000"){
							this.peps.splice(nowIndex,1);
						}else{
							layer_error("删除失败!")
						}


					}).catch(function(response){

						console.log(response);
						console.log("居然没有弹窗");
					})


				}
			}

		}
	})



});

