$(function(){
    /**
     * dijkstra四则运算
     */
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
                if(result.flag){
                    $("#resultDiv").html(result.message);
                }else{
                    alert(result.message);
                    $("#resultDiv").html("");
                }
            },error:function(){
                layer.msg('请求错误!', {icon: 7,time: 2000});
            },
            complete : function(){
                layer.close(loading);
            }
        });
    });
    /**
     * Find-Union
     */
    $("#queryBtnUnion").click(function(){
        var nodeNum = $.trim($('#countNum').val());
        if(0 >= nodeNum){
            alert("节点要大于1个");
            return;
        }
        var connectPairs = $.trim($('#comparePairs').val());
        if(1 >= connectPairs.length){
            alert("连接内容不能是空，以|隔开");
            return;
        }
        $.ajax({
            url:"/algorithm/findUnion",
            cache: false,
            type:"post",
            data:{"nodeNum" : nodeNum, "connectPairs" : connectPairs},
            beforeSend : function(){
                loading = layer.load("正在重新设置信息...");
            },
            success:function(result){
                if(result.flag){
                    $("#resultDiv").html(result.message);
                }else{
                    alert(result.message);
                    $("#resultDiv").html("");
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

