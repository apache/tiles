package org.apache.tiles.velocity.template;

import javax.servlet.ServletContext;

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

public class TilesVelocityRepository {

    private AddAttributeVModel addAttribute;

    private AddListAttributeVModel addListAttribute;

    private DefinitionVModel definition;

    private GetAsStringVModel getAsString;

    private ImportAttributeVModel importAttribute;

    private InsertAttributeVModel insertAttribute;

    private InsertDefinitionVModel insertDefinition;

    private InsertTemplateVModel insertTemplate;

    private PutAttributeVModel putAttribute;

    private PutListAttributeVModel putListAttribute;

    public TilesVelocityRepository(ServletContext servletContext) {
        AttributeResolver attributeResolver = new DefaultAttributeResolver();

        addAttribute = new AddAttributeVModel(new AddAttributeModel());
        addListAttribute = new AddListAttributeVModel(
                new AddListAttributeModel());
        definition = new DefinitionVModel(new DefinitionModel(), servletContext);
        getAsString = new GetAsStringVModel(new GetAsStringModel(
                attributeResolver), servletContext);
        importAttribute = new ImportAttributeVModel(new ImportAttributeModel(),
                servletContext);
        insertAttribute = new InsertAttributeVModel(new InsertAttributeModel(
                attributeResolver), servletContext);
        insertDefinition = new InsertDefinitionVModel(
                new InsertDefinitionModel(), servletContext);
        insertTemplate = new InsertTemplateVModel(new InsertTemplateModel(),
                servletContext);
        putAttribute = new PutAttributeVModel(new PutAttributeModel(),
                servletContext);
        putListAttribute = new PutListAttributeVModel(
                new PutListAttributeModel(), servletContext);
    }

    public AddAttributeVModel getAddAttribute() {
        return addAttribute;
    }

    public AddListAttributeVModel getAddListAttribute() {
        return addListAttribute;
    }

    public DefinitionVModel getDefinition() {
        return definition;
    }

    public GetAsStringVModel getGetAsString() {
        return getAsString;
    }

    public ImportAttributeVModel getImportAttribute() {
        return importAttribute;
    }

    public InsertAttributeVModel getInsertAttribute() {
        return insertAttribute;
    }

    public InsertDefinitionVModel getInsertDefinition() {
        return insertDefinition;
    }

    public InsertTemplateVModel getInsertTemplate() {
        return insertTemplate;
    }

    public PutAttributeVModel getPutAttribute() {
        return putAttribute;
    }

    public PutListAttributeVModel getPutListAttribute() {
        return putListAttribute;
    }
}
