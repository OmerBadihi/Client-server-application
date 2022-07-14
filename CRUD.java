package com.srccodes;

import java.io.IOException;

public interface CRUD<K, D> extends AutoCloseable {
    K create(D data) throws IOException;

    D read(K key) throws IOException;

    void update(K key, D data) throws IOException;

    void delete(K key) throws IOException;
}
