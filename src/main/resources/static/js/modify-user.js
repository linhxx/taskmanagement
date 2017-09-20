$(function(){
    $("#btnReturn").click(function(){
        window.location.href = "/index";
    });
    $("#btnConfirm").click(function(){
        var newusername = $.trim($('#username').val());
        var oldusername = $.trim($('#hiddenUsername').text());
        var oldpwd = $.trim($('#oldpwd').val());
        var newpwd = $.trim($('#newpwd').val());
        var newconfirmpwd = $.trim($('#newconfirmpwd').val());
        if(!checkInput(newusername, oldusername, oldpwd, newpwd, newconfirmpwd)){
            return;
        }
        $.ajax({
            url:"/resetUserInfo",
            cache: false,
            type:"post",
            data:{"newUsername" : newusername, "newPwd" : newpwd},
            beforeSend : function(){
                loading = layer.load("正在重新设置信息...");
            },
            success:function(result){
                if(1 == result.msg){
                    alert("修改名字成功！");
                    window.location.href = "/index";
                }else if(2 == result.msg){
                    alert("密码修改成功，请重新登录！");
                    window.location.href = "/index";
                }else{
                    layer.msg('设置失败!', {icon: 7,time: 2000});
                }
            },error:function(){
                layer.msg('请求错误!', {icon: 7,time: 2000});
            },
            complete : function(){
                layer.close(loading);
            }
        });
    });
});

function checkInput(newusername, oldusername, oldpwd, newpwd, newconfirmpwd){
    if(newusername == oldusername && ('' == oldpwd || '' == newpwd || '' == newconfirmpwd)){
        layer.msg('没有任何改动，不需要保存！', {icon: 7,time: 2000}); //2秒关闭（如果不配置，默认是3秒）
        return false;
    }
    if('' == newusername){
        layer.msg('用户名不能为空！', {icon: 7,time: 2000});
        return false;
    }
    if(newpwd != newconfirmpwd){
        layer.msg('两次输入的新密码不一致！', {icon: 7,time: 2000});
        return false;
    }
    var pwdNum = 0;
    if('' != oldpwd){
        pwdNum = pwdNum +1;
    }
    if('' != newpwd){
        pwdNum = pwdNum +1;
    }
    if('' != newconfirmpwd){
        pwdNum = pwdNum +1;
    }
    if(pwdNum >0 && pwdNum <3){
        layer.msg('要重置的密码不能是空，如果不需要重置密码请把三个密码输入框都清空！', {icon: 7,time: 3000});
        return false;
    }
    if(3 == pwdNum){
        var loading;
        var isCorrected = false;
        $.ajax({
            url:"/checkPwd",
            cache: false,
            type:"post",
            data:{"oldPwd" : oldpwd},
            async:false,
            beforeSend : function(){
                loading = layer.load("正在确认您的密码...");
            },
            success:function(result){
                if(result.validMsg){
                    isCorrected = true;
                }else{
                    layer.msg('旧密码错误，修改密码失败!', {icon: 7,time: 2000});
                    isCorrected = false;
                }
            },error:function(){
                layer.msg('请求错误!', {icon: 7,time: 2000});
                isCorrected = false;
            },
            complete : function(){
                layer.close(loading);
            }
        });
        return isCorrected;
    }
    return true;
}









