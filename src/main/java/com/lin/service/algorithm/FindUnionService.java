package com.lin.service.algorithm;

/**
 * FindUnion—压缩路径的quick-union算法
 */
public class FindUnionService {
    private Integer count;//当前连接集合数量
    private Integer[] id;//节点父连接数组
    private Integer[] childNum;//节点的子节点
    private Integer[][] connectPairs;//待处理的连接数组
    public FindUnionService(Integer nodeNum, String strPairs){
        this.count = nodeNum;
        this.id = new Integer[this.count];
        this.childNum = new Integer[this.count];
        createArray();
        dealConnectPairs(strPairs);
        getUnion();
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
    /**
     * 创建待连接的数字
     */
    private void dealConnectPairs(String connectPairs){
        //如果以竖线为分隔符，则split的时候需要加上两个斜杠【\\】进行转义
        String[] pairs = connectPairs.split("\\|");
        Integer pairsNum = pairs.length/2;
        this.connectPairs = new Integer[pairsNum][2];
        for(int i=0,j=0;i<pairsNum;i++){
            this.connectPairs[i][0] = Integer.parseInt(pairs[j++]);
            this.connectPairs[i][1] = Integer.parseInt(pairs[j++]);
        }
    }
    public Integer getCount() {
        return count;
    }
    public Integer[] getId() {
        return id;
    }
    public Integer[] getChildNum() {
        return childNum;
    }
    private Integer find(int currentIndex){
        //循环查找父节点
        int parentValue = currentIndex;
        while(parentValue != this.id[currentIndex]){
            parentValue = this.id[currentIndex];
        }
        //路径压缩，将节点的全部父节点都指向r，把路径压缩成深度是1的树
        int parentIndex = currentIndex, tmp;
        while(parentIndex != parentValue){
            tmp = this.id[parentIndex];
            this.id[parentIndex] = parentValue;
            parentIndex = tmp;
        }
        return parentValue;
    }
    public Boolean connected(int p, int q){
        return find(p) == find(q);
    }
    private void getUnion(){
        for (Integer[] connectPair : this.connectPairs) {
            this.union(connectPair[0], connectPair[1]);
        }
    }
    private void union(int p, int q){
        Integer i = this.find(p);
        Integer j = this.find(q);
        if(!i.equals(j)){
            if(this.childNum[i] > this.childNum[j]){
                id[j] = i;
                this.childNum[i] += this.childNum[j];
            }else{
                id[i] = j;
                this.childNum[j] += this.childNum[i];
            }
            this.count--;
        }
    }

}
