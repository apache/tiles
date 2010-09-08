package org.apache.tiles.freemarker;

import org.apache.tiles.freemarker.template.TilesFMModelRepository;
import org.apache.tiles.request.freemarker.servlet.SharedVariableFactory;

import freemarker.ext.beans.BeanModel;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateModel;

public class TilesSharedVariableFactory implements SharedVariableFactory {

    @Override
    public TemplateModel create() {
        return new BeanModel(new TilesFMModelRepository(),
                BeansWrapper.getDefaultInstance());
    }

}
