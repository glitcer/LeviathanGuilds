package me.khalit.projectleviathan.data.sql;

public interface Callback<V> {

    void result(V param) throws Exception;

}
