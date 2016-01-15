package ru.goodfil.catalog.mann.merge;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.utils.AssertException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: sazonovkirill@gmail.com
 * Date: 09.12.12
 */
@Lazy(true)
@Component
public class MergeContext {
    private Map<Class, Map<String, Map<Long, Long>>> context = new HashMap<Class, Map<String, Map<Long, Long>>>();
    private Map<Class, Map<String, Map<String, String>>> stringContext = new HashMap<Class, Map<String, Map<String, String>>>();

    public void add(Class klass, String source, Long sourceId, Long destId) {
        if (!context.containsKey(klass)) {
            context.put(klass, new HashMap<String, Map<Long, Long>>());
        }

        Map<String, Map<Long, Long>> klassSubcontext = context.get(klass);
        if (!klassSubcontext.containsKey(source)) {
            klassSubcontext.put(source, new HashMap<Long, Long>());
        }

        Map<Long, Long> sourceSubcontext = klassSubcontext.get(source);
        if (sourceSubcontext.containsKey(sourceId)) {
            Long oldValue = sourceSubcontext.get(sourceId);
            throw new AssertException("Merge context already contains mapping for " + klass.getSimpleName() +
                    ", " + source + ", " + sourceId + " -> " + oldValue + "; new value: " + destId);
        }

        sourceSubcontext.put(sourceId, destId);
    }

    public Long getDestBySource(Class klass, String source, Long sourceId) {
        Map<String, Map<Long, Long>> klassSubcontext = context.get(klass);
        if (klassSubcontext != null) {
            Map<Long, Long> sourceSubcontext = klassSubcontext.get(source);
            if (sourceSubcontext != null) {
                return sourceSubcontext.get(sourceId);
            }
        }

        return null;
    }

    public void add(Class klass, String source, String sourceId, String destId) {
        if (!stringContext.containsKey(klass)) {
            stringContext.put(klass, new HashMap<String, Map<String, String>>());
        }

        Map<String, Map<String, String>> klassSubcontext = stringContext.get(klass);
        if (!klassSubcontext.containsKey(source)) {
            klassSubcontext.put(source, new HashMap<String, String>());
        }

        Map<String, String> sourceSubcontext = klassSubcontext.get(source);
        if (sourceSubcontext.containsKey(sourceId)) {
            String oldValue = sourceSubcontext.get(sourceId);
            throw new AssertException("Merge context already contains mapping for " + klass.getSimpleName() +
                    ", " + source + ", " + sourceId + " -> " + oldValue + "; new value: " + destId);
        }

        sourceSubcontext.put(sourceId, destId);
    }

    public String getDestBySource(Class klass, String source, String sourceId) {
        Map<String, Map<String, String>> klassSubcontext = stringContext.get(klass);
        if (klassSubcontext != null) {
            Map<String, String> sourceSubcontext = klassSubcontext.get(source);
            if (sourceSubcontext != null) {
                return sourceSubcontext.get(sourceId);
            }
        }

        return null;
    }
}
