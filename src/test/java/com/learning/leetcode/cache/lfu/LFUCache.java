package com.learning.cache.lfu;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 最近最不经常使用算法
 * @author lifang
 * @since 2021/12/9
 */
public class LFUCache<K, V> {

    /**
     * 缓存数据大小
     */
    private int cacheSize;

    /**
     * Key和节点对应关系
     */
    private Map<K, Node<K, V>> cacheMap;

    /**
     * 双向链表
     */
    private DoublyLinkedList<K, V> doublyLinkedList;

    public LFUCache(int cacheSize){
        this.cacheSize = cacheSize;
        this.cacheMap = new HashMap<>(cacheSize);
        this.doublyLinkedList = new DoublyLinkedList<>();
    }

    public V get(K key){
        if(!cacheMap.containsKey(key)){
            return null;
        }

        Node<K, V> node = cacheMap.get(key);

        // 使用了，将节点移动到链表前面
        doublyLinkedList.remove(node);

        // 添加到头部位
        doublyLinkedList.addHead(node);

        return node.value;
    }

    public void put(K key, V value){
        if(cacheMap.containsKey(key)){
            // 更新
            Node<K, V> node = cacheMap.get(key);

            node.setValue(value);

            cacheMap.put(key, node);

            doublyLinkedList.remove(node);

            doublyLinkedList.addHead(node);
        }else{

            Node<K, V> node = new Node<>(key, value);

            if(cacheMap.size() == cacheSize){
                // 淘汰最近最少使用
                Node last = doublyLinkedList.getLast();

                // 先移除
                cacheMap.remove(last.key);
                doublyLinkedList.remove(last);

                // 再添加
                cacheMap.put(key, node);
                doublyLinkedList.addHead(node);
            }else{
                cacheMap.put(key, node);

                doublyLinkedList.addHead(node);
            }

        }
    }


    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public Map<K, Node<K, V>> getCacheMap() {
        return cacheMap;
    }

    public void setCacheMap(Map<K, Node<K, V>> cacheMap) {
        this.cacheMap = cacheMap;
    }

    public DoublyLinkedList<K, V> getDoublyLinkedList() {
        return doublyLinkedList;
    }

    public void setDoublyLinkedList(DoublyLinkedList<K, V> doublyLinkedList) {
        this.doublyLinkedList = doublyLinkedList;
    }

    //==================================================================================================================

    static class Node<K, V>{

        /**
         * 缓存的key
         */
        private K key;

        /**
         * 数据
         */
        private V value;


        /**
         * 最后使用时间
         */
        private LocalDateTime lastUseDateTime;

        /**
         * 前置节点
         */
        private Node prev;

        /**
         * 后置节点
         */
        private Node next;

        public Node() {
        }

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }

    static class DoublyLinkedList<K, V>{

        private Node<K, V> head;

        private Node<K, V> tail;

        public DoublyLinkedList(){
            // 头、尾节点为空节点
            this.head = new Node<>();
            this.tail = new Node<>();

            // 初始化节点,首尾相连
            this.head.setNext(this.tail);
            this.tail.setPrev(this.head);
        }

        public void remove(Node<K, V> node){
            Node prev = node.getPrev();
            Node next = node.getNext();

            if(prev != null) {
                prev.setNext(next);
            }
            if(next != null) {
                next.setPrev(prev);
            }
            node.setPrev(null);
            node.setNext(null);
        }

        public void addHead(Node<K, V> node) {
            Node hNext = this.head.getNext();
            node.setNext(hNext);
            hNext.setPrev(node);

            head.setNext(node);
            node.setPrev(head);
        }

        public Node getLast() {
            return tail.prev;
        }

        public Node<K, V> getHead() {
            return head;
        }

        public void setHead(Node<K, V> head) {
            this.head = head;
        }

        public Node<K, V> getTail() {
            return tail;
        }

        public void setTail(Node<K, V> tail) {
            this.tail = tail;
        }
    }

}
