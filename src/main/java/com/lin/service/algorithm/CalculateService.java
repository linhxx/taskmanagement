package com.lin.service.algorithm;

import java.util.HashSet;
import java.util.Stack;

/**
 * Dijkstra双栈四则运算
 */
public class CalculateService{
    private Stack<Double> doubleStack = new Stack<>();
    private Stack<Character> charStack = new Stack<>();
    private String strCalcu;
    private Integer strLength;
    private static final HashSet<Character> numStringSet = new HashSet<Character>(){
        {add('0'); add('1'); add('2'); add('3'); add('4'); add('5'); add('6'); add('7'); add('8'); add('9');}
    };
    private static final HashSet<Character> operatorSet = new HashSet<Character>(){
        {add('+'); add('-'); add('*'); add('/');}
    };
    public CalculateService(String strCalcu){
        this.strCalcu = strCalcu;
        this.strLength = strCalcu.length();
    }
    private void pushDouble(Double num){
        doubleStack.push(num);
    }
    private void pushChar(Character str){
        charStack.push(str);
    }
    private Double popDouble(){
        return doubleStack.pop();
    }
    private Character popChar(){
        return charStack.pop();
    }
    private Boolean needCalculate(Character ch){
        Boolean res = false;
        if(operatorSet.contains(ch)){
            this.pushChar(ch);//运算符
        }else if(numStringSet.contains(ch)){
            this.pushDouble(Double.valueOf(String.valueOf(ch)));//数字
        }else if(ch.equals(')')){
            res = true;//要计算的情况
        }
        return res;
    }
    private void calculateMiddle(){
        Double sum = this.popDouble();
        Character ch = this.popChar();
        Double num = this.popDouble();
        if(ch.equals('+')){
            sum += num;
        }else if(ch.equals('-')){
            sum = num - sum;
        }else if(ch.equals('*')){
            sum *= num;
        }else if(ch.equals('/') && 0 == sum){
            sum = num / 1;
        }else{
            sum = num / sum;
        }
        this.pushDouble(sum);
    }
    public Double dealCalculate(){
        for(int i=0; i<this.strLength; i++){
            Character ch = this.strCalcu.charAt(i);
            if(this.needCalculate(ch)){
                this.calculateMiddle();
            }
        }
        return doubleStack.pop();
    }
}
