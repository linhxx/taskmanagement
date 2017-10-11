$(function(){
    /**
     * dijkstra四则运算
     */
    $("#queryBtnCalculate").click(function(){
        var calculateString = $.trim($('#calculateString').val());
        if(checkNotEmpty(calculateString, "请输入+-*/()及0-9数字")){
            ajaxToController("/algorithm/calculate", {"calculateString" : calculateString});
        }
    });
    /**
     * Find-Union
     */
    $("#queryBtnUnion").click(function(){
        var nodeNum = $.trim($('#countNum').val());
        var connectPairs = $.trim($('#comparePairs').val());
        if(checkNotEmpty(connectPairs, "连接内容不能是空，以|隔开") && checkGreaterThan(nodeNum, 0, "节点要大于1个")){
            ajaxToController("/algorithm/findUnion", {"nodeNum" : nodeNum, "connectPairs" : connectPairs});
        }
    });
    /**
     * 快速排序
     */
    $("#queryBtnSort").click(function(){
        var quickSortInput = $.trim($('#quickSortInput').val());
        if(checkNotEmpty(quickSortInput, "数字不能空，以|隔开")){
            ajaxToController("/algorithm/quickSort", {"quickSortInput" : quickSortInput});
        }
    });
});

function ajaxToController(url, params){
    $.ajax({
        url:url,
        cache: false,
        type:"post",
        data:params,
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
}

function checkNotEmpty(str, msg) {
    if(1 >= str.length){
        alert(msg);
        return false;
    }
    return true;
}

function checkGreaterThan(num, compare, msg) {
    if(compare >= num){
        alert(msg);
        return false;
    }
    return true;
}

