package org.apache.tiles.freemarker.servlet;

import org.apache.tiles.freemarker.template.AddAttributeFMModel;
import org.apache.tiles.freemarker.template.AddListAttributeFMModel;
import org.apache.tiles.freemarker.template.DefinitionFMModel;
import org.apache.tiles.freemarker.template.GetAsStringFMModel;
import org.apache.tiles.freemarker.template.ImportAttributeFMModel;
import org.apache.tiles.freemarker.template.InsertAttributeFMModel;
import org.apache.tiles.freemarker.template.InsertDefinitionFMModel;
import org.apache.tiles.freemarker.template.InsertTemplateFMModel;
import org.apache.tiles.freemarker.template.PutAttributeFMModel;
import org.apache.tiles.freemarker.template.PutListAttributeFMModel;
import org.apache.tiles.freemarker.template.SetCurrentContainerFMModel;
import org.apache.tiles.template.AddAttributeModel;
import org.apache.tiles.template.AddListAttributeModel;
import org.apache.tiles.template.AttributeResolver;
import org.apache.tiles.template.DefaultAttributeResolver;
import org.apache.tiles.template.DefinitionModel;
import org.apache.tiles.template.GetAsStringModel;
import org.apache.tiles.template.ImportAttributeModel;
import org.apache.tiles.template.InsertAttributeModel;
import org.apache.tiles.template.InsertDefinitionModel;
import org.apache.tiles.template.InsertTemplateModel;
import org.apache.tiles.template.PutAttributeModel;
import org.apache.tiles.template.PutListAttributeModel;

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

        configuration.setSharedVariable("addAttribute",
                new AddAttributeFMModel(new AddAttributeModel()));
        configuration.setSharedVariable("addListAttribute",
                new AddListAttributeFMModel(new AddListAttributeModel()));
        configuration.setSharedVariable("definition", new DefinitionFMModel(
                new DefinitionModel()));
        AttributeResolver attributeResolver = new DefaultAttributeResolver();
        configuration.setSharedVariable("getAsString", new GetAsStringFMModel(
                new GetAsStringModel(attributeResolver)));
        configuration.setSharedVariable("importAttribute",
                new ImportAttributeFMModel(new ImportAttributeModel()));
        configuration.setSharedVariable("insertAttribute",
                new InsertAttributeFMModel(new InsertAttributeModel(
                        attributeResolver)));
        configuration.setSharedVariable("insertDefinition",
                new InsertDefinitionFMModel(new InsertDefinitionModel()));
        configuration.setSharedVariable("insertTemplate",
                new InsertTemplateFMModel(new InsertTemplateModel()));
        configuration.setSharedVariable("putAttribute",
                new PutAttributeFMModel(new PutAttributeModel()));
        configuration.setSharedVariable("putListAttribute",
                new PutListAttributeFMModel(new PutListAttributeModel()));
        configuration.setSharedVariable("setCurrentContainer",
                new SetCurrentContainerFMModel());
        return configuration;
    }

}
