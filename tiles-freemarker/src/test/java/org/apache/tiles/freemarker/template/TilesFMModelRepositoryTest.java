/**
 * 
 */
package org.apache.tiles.freemarker.template;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author antonio
 *
 */
public class TilesFMModelRepositoryTest {

    /**
     * Test method for {@link org.apache.tiles.freemarker.template.TilesFMModelRepository#TilesFMModelRepository()}.
     */
    @Test
    public void testTilesFMModelRepository() {
        TilesFMModelRepository repository = new TilesFMModelRepository();
        assertNotNull(repository.getAddAttribute());
        assertNotNull(repository.getAddListAttribute());
        assertNotNull(repository.getDefinition());
        assertNotNull(repository.getGetAsString());
        assertNotNull(repository.getImportAttribute());
        assertNotNull(repository.getInsertAttribute());
        assertNotNull(repository.getInsertDefinition());
        assertNotNull(repository.getInsertTemplate());
        assertNotNull(repository.getPutAttribute());
        assertNotNull(repository.getPutListAttribute());
        assertNotNull(repository.getSetCurrentContainer());
    }

}
