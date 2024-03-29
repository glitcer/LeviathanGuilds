package me.khalit.projectleviathan.utils;

import lombok.Data;

@Data
public class KeyPair<K, V> {

    private K valueFirst;
    private V valueSecond;

    public KeyPair() {}

    public KeyPair(K valueFirst, V valueSecond) {
        put(valueFirst, valueSecond);
    }

    public void put(K valueFirst, V valueSecond) {
        this.valueFirst = valueFirst;
        this.valueSecond = valueSecond;
    }

}
