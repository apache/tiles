package org.apache.tiles.request.collection;

import java.util.Enumeration;
import java.util.Map;

import org.apache.tiles.request.collection.extractor.HasKeys;

public abstract class AbstractEnumerationMap<V> implements Map<String, V>{

    protected HasKeys<V> request;

    public AbstractEnumerationMap(HasKeys<V> request) {
        this.request = request;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        HasKeys<V> otherRequest = ((AbstractEnumerationMap<V>) o).request;
        boolean retValue = true;
        for (Enumeration<String> attribs = request.getKeys(); attribs
                .hasMoreElements()
                && retValue;) {
            String parameterName = attribs.nextElement();
            retValue = request.getValue(parameterName).equals(
                    otherRequest.getValue(parameterName));
        }

        return retValue;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int retValue = 0;
        for (Enumeration<String> attribs = request.getKeys(); attribs
                .hasMoreElements();) {
            String parameterName = attribs.nextElement();
            V value = request.getValue(parameterName);
            retValue += parameterName.hashCode() ^ (value == null ? 0 : value.hashCode());
        }
        return retValue;
    }
}
