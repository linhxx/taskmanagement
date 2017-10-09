$(function(){
    $("#queryBtnCalculate").click(function(){
        var calculateString = $.trim($('#calculateString').val());
        if(0 >= calculateString.length){
            alert("请输入+-*/()及0-9数字");
            return;
        }
        $.ajax({
            url:"/algorithm/calculate",
            cache: false,
            type:"post",
            data:{"calculateString" : calculateString},
            beforeSend : function(){
                loading = layer.load("正在重新设置信息...");
            },
            success:function(result){
                $("#resultDiv").html(result);
            },error:function(){
                layer.msg('请求错误!', {icon: 7,time: 2000});
            },
            complete : function(){
                layer.close(loading);
            }
        });
    });
});

