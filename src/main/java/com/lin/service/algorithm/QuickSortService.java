package com.lin.service.algorithm;

public class QuickSortService {
    private Integer[] nums;
    private Integer numLength;
    public QuickSortService(String str){
        splitString(str);
        startQuickSort(nums, 0, nums.length-1);
    }
    public Integer[] getNums() {
        return nums;
    }
    public Integer getNumLength() {
        return numLength;
    }
    private void splitString(String str){
        String[] stringArray = str.split("\\|");
        numLength = stringArray.length;
        nums = new Integer[numLength];
        for(int i=0;i<stringArray.length;i++){
            nums[i] = Integer.parseInt(stringArray[i]);
        }
    }
    public String getSortedNums(){
        StringBuilder res = new StringBuilder();
        for(Integer i:nums){
            res.append(i).append("|");
        }
        return res.toString();
    }
    private static Boolean less(Comparable a, Comparable b, Boolean equals){
        Integer compare = a.compareTo(b);
        Boolean res = false;
        if(compare.equals(-1) || (equals && compare.equals(0))){
            res = true;
        }
        return res;
    }
    private static void exchange(Comparable[] a, int p, int q){
        if(a.length <= p || a.length <= q){
            return;
        }
        Comparable tmp = a[p];
        a[p] = a[q];
        a[q] = tmp;
    }
    /**
     * java中的引用传递——static
     * 插入排序，数组长度5以内采用此方法
     */
    private static void insertSort(Comparable[] a){
        int n = a.length;
        for(int i=1;i<n;i++){
            for(int j=i;j>0 && less(a[j], a[j-1], false);j--){
                exchange(a, j, j-1);
            }
        }
    }
    /**
     * 获取快速排序的切分元素，并进行部分排序，保证切分元素左侧元素都小，右侧都大
     */
    private static int partition(Comparable[] a, int low, int high){
        int i = low - 1, j = high + 1;//左右扫描指针
        int randomIndex = (int)(low + Math.random()*(high - low + 1));
        Comparable v = a[randomIndex];//切分元素
        while(true){
            //左边找到比v大的元素
            while(less(a[++i], v, true)){
                if(i >= high) break;
            }
            //右边找到比v小的元素
            while(less(v, a[--j], true)){
                if(j <= low) break;
            }
            //扫描结束退出条件
            if(i >= j) break;
            //交换左右两边找到的元素，保证相对有序
            exchange(a, i, j);
        }
        //将切分元素换到中间
        exchange(a, randomIndex, j);
        return j;
    }
    /**
     * 快速排序
     */
    private static void startQuickSort(Comparable[] a, int low, int high){
        if(a.length <= 5 || high < low + 5){
            insertSort(a);//数组长度5以内采用插入排序
            return;
        }
        int partitionNum = partition(a, low, high);
        startQuickSort(a, low, partitionNum-1);
        startQuickSort(a, partitionNum+1, high);
    }
    /**
     * 三取样切分
     */
    private static void start3WayQuickSort(Comparable[] a, int low, int high){
        if(a.length <= 5 || high < low + 5){
            insertSort(a);//数组长度5以内采用插入排序
            return;
        }
        //equalLeft~equalRight区间是等值的情况,low~equal~equalLeft是小的
        int equalLeft = low, equalRight = high, i = equalLeft +1;
        Comparable v = a[low];
        while(i <= equalRight){
            int cmp = a[i].compareTo(v);
            if(0 < cmp) exchange(a, i, equalRight--);//a[i]>v，交换i和当前最后一个元素，并将最后一个元素-1
            else if(0 > cmp) exchange(a, equalLeft++, i++);//a[i]<v，交换i和左边的元素，并且指针往后
            else i++;//相同的情况，则直接比较下一个元素
        }
        start3WayQuickSort(a, low, equalLeft-1);
        start3WayQuickSort(a, equalRight+1, high);
    }
    /**
     * 求任意数组的第k小元素（不需要排序）
     */
    public Integer getMinK(Comparable[] a, int k){
        int low = 0, high = a.length-1;
        while (low < high){
            int j = partition(a, low, high);
            if(j == k) return (Integer) a[k];
            else if(j > k) high = j - 1;
            else if(j < k) low = j + 1;
        }
        return (Integer) a[k];
    }
}
