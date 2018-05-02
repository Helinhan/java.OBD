package com.hantong.model;

public class Pair<K,V> {
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

    private K key;
    private V value;
    public Pair(K k,V v) {
        this.key = k;
        this.value = v;
    }
}
