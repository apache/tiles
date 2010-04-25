package org.apache.tiles.request.collection;

import static org.apache.tiles.request.util.RequestUtil.*;

import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

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
        Collection<String> realCollection = (Collection<String>) c;
        boolean retValue = false;
        for (String entry : realCollection) {
            retValue |= remove(entry);
        }
        return retValue;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean retainAll(Collection<?> c) {
        Collection<String> realCollection = (Collection<String>) c;
        boolean retValue = false;
        Set<String> keysToRemove = new LinkedHashSet<String>();
        for (Enumeration<String> keys = request.getKeys(); keys.hasMoreElements(); ) {
            String key = keys.nextElement();
            if (!realCollection.contains(key)) {
                retValue = true;
                keysToRemove.add(key);
            }
        }
        for (String key : keysToRemove) {
            request.removeValue(key);
        }
        return retValue;
    }

}
