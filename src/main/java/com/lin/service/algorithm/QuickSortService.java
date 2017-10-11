package com.lin.service.algorithm;

public class QuickSortService {
    private Integer[] nums;
    public QuickSortService(String str){splitString(str);}
    private void splitString(String str){
        String[] stringArray = str.split("\\|");
        nums = new Integer[stringArray.length];
        for(int i=0;i<stringArray.length;i++){
            nums[i] = Integer.parseInt(stringArray[i]);
        }
    }
    public String getSotredNums(){
        String res = "";
        for(Integer i:nums){
            res += i + "|";
        }
        return res;
    }

}
