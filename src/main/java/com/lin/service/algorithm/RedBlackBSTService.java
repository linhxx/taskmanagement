package com.lin.service.algorithm;

import com.sun.org.apache.regexp.internal.RE;

/**
 * 红黑树
 */
public class RedBlackBSTService<Key extends Comparable<Key>, Value> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private class Node{
        Key key;//节点键
        Value value;//节点值
        Node left, right;//节点左右节点
        int N;//节点子节点数量
        boolean color;//节点与其父节点连接的颜色
        Node(Key key, Value value, int N, boolean color){
            this.key = key;
            this.value = value;
            this.N = N;
            this.color = color;
        }
    }
    private Node root;
    //查看节点是否是红色（空节点默认是黑色）
    private boolean isRed(Node x) {
        return null != x && x.color == RED;
    }
    //查看节点子节点数目
    private int size() {
        return size(root);
    }
    private int size(Node h) {
        if (h == null) return 0;
        return h.N;
    }
    private int getTotalSize(Node headNode){
        return 1 + size(headNode.left) + size(headNode.right);
    }
    //获取树的最小、最大节点
    public Key getMinKey(){
        return minKey(root).key;
    }
    public Key getMaxKey(){
        return maxKey(root).key;
    }
    private Node minKey(Node node){
        if(null == node.left) return node;
        return minKey(node.left);
    }
    private Node maxKey(Node node){
        if(null == node.right) return node;
        return maxKey(node.right);
    }
    //左旋(当节点与其右子节点连接是红色时)
    private Node rotateLeft(Node headNode){
        //节点互换
        Node leftNode = headNode.right;
        headNode.right = leftNode.left;
        leftNode.left = headNode;
        //颜色转换
        leftNode.color = headNode.color;
        headNode.color = RED;
        //节点数量重新获取
        leftNode.N = headNode.N;
        headNode.N = getTotalSize(headNode);
        return leftNode;
    }
    //右旋(当节点与父节点连线是红色，且与其左子节点连线也是红色时)
    private Node rotateRight(Node headNode){
        Node rightNode = headNode.left;
        headNode.left = rightNode.right;
        rightNode.right = headNode;
        rightNode.color = headNode.color;
        headNode.color = RED;
        rightNode.N = headNode.N;
        headNode.N = getTotalSize(headNode);
        return rightNode;
    }
    //颜色调整(当节点与其左右子节点连线都是红色时)
    private void flipColor(Node headNode){
        headNode.color = RED;
        headNode.left.color = BLACK;
        headNode.right.color = BLACK;
    }
    //插入节点
    private Node addNode(Node headNode, Key key, Value value){
        //空时新建根节点
        if(null == headNode) return  new Node(key, value, 1, RED);
        //比较待插入节点和当前节点的key
        int cmp = key.compareTo(headNode.key);
        if(0 > cmp) headNode.left = addNode(headNode.left, key, value);
        else if(0 < cmp) headNode.right = addNode(headNode.right, key, value);
        else headNode.value = value;
        //根据插入后的情况判断是否需要旋转或颜色调整
        if(isRed(headNode.right) && !isRed(headNode.left)) rotateLeft(headNode);
        if(isRed(headNode.left) && isRed(headNode.left.left)) rotateRight(headNode);
        if(isRed(headNode.right) && isRed(headNode.left)) flipColor(headNode);
        //节点大小重新获取
        headNode.N = getTotalSize(headNode);
        return headNode;
    }
    public void addNode(Key key, Value value){
        root = addNode(root, key, value);
        root.color = BLACK;
    }

}
