package org.apache.tiles.request.collection;

import static org.apache.tiles.request.util.RequestUtil.*;

import java.util.Collection;
import java.util.Map;

import org.apache.tiles.request.collection.extractor.HasRemovableKeys;

public class RemovableKeySet extends KeySet {

    private HasRemovableKeys<?> request;

    public RemovableKeySet(HasRemovableKeys<?> request) {
        super(request);
        this.request = request;
    }

    @Override
    public boolean remove(Object o) {
        String skey = key(o);
        Object previous = request.getValue(skey);
        if (previous != null) {
            request.removeValue(skey);
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean removeAll(Collection<?> c) {
        Collection<Map.Entry<String, Object>> realCollection = (Collection<java.util.Map.Entry<String, Object>>) c;
        boolean retValue = false;
        for (Map.Entry<String, Object> entry : realCollection) {
            retValue |= remove(entry);
        }
        return retValue;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean retainAll(Collection<?> c) {
        Collection<String> realCollection = (Collection<String>) c;
        boolean retValue = false;
        for (String key : realCollection) {
            retValue |= remove(key);
        }
        return retValue;
    }

}
