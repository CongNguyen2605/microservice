package org.example.identityservice.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BaseRedisService {
    void set(String key, String value);

    void setTimeToLive(String key, Long timeToLive);

    void hashSet(String key, String field, Object value);

    Boolean hashExists(String key, String field);

    Object get(String key);

    public Map<String, Object> getFields(String key);

    Object hashGet(String key, String field);

    List<Object> hashGetByFieldsPrefix(String key, String field);

    Set<String> getFieldPrefixes(String key);

    void delete(String key);

    void delete(String key, String field);

    void delete(String key, List<String> fields);

}
