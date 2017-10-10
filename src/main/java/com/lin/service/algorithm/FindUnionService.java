package com.lin.service.algorithm;

/**
 * FindUnion—压缩路径的quick-union算法
 */
public class FindUnionService {
    private Integer count;//当前连接集合数量
    private Integer[] id;//节点
    private Integer[] childNum;//节点的子节点
    private Integer[][] connectPairs;//待处理的连接数组
    public FindUnionService(Integer nodeNum, String strPairs){
        this.count = nodeNum;
        this.id = new Integer[this.count];
        this.childNum = new Integer[this.count];
        createArray();
        dealConnectPairs(strPairs);
    }

    /**
     * 创建初始化节点和子节点数目
     */
    private void createArray(){
        for(int i=0;i<this.count;i++){
            this.id[i] = i;
            this.childNum[i] = 1;
        }
    }
    private void dealConnectPairs(String connectPairs){
        String[] pairs = connectPairs.split("|");
        Integer pairsNum = pairs.length/2;
        this.connectPairs = new Integer[pairsNum][2];
        for(int i=0,j=0;i<pairsNum;i++){
            this.connectPairs[i][0] = Integer.parseInt(pairs[j++]);
            this.connectPairs[i][1] = Integer.parseInt(pairs[j++]);
        }
    }
}
