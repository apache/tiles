package org.apache.tiles.freemarker.servlet;

import org.apache.tiles.freemarker.template.TilesFMModelRepository;

import freemarker.ext.beans.BeanModel;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.template.Configuration;

public class TilesFreemarkerServlet extends FreemarkerServlet {

    /*
     * (non-Javadoc)
     * 
     * @see freemarker.ext.servlet.FreemarkerServlet#createConfiguration()
     */
    @Override
    protected Configuration createConfiguration() {
        Configuration configuration = super.createConfiguration();

        BeanModel tilesBeanModel = new BeanModel(new TilesFMModelRepository(),
                BeansWrapper.getDefaultInstance());
        configuration.setSharedVariable("tiles", tilesBeanModel);
        return configuration;
    }

}
