package org.apache.tiles.freemarker.template;

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

public class TilesFMModelRepository {

    private AddAttributeFMModel addAttribute;

    private AddListAttributeFMModel addListAttribute;

    private DefinitionFMModel definition;

    private GetAsStringFMModel getAsString;

    private ImportAttributeFMModel importAttribute;

    private InsertAttributeFMModel insertAttribute;

    private InsertDefinitionFMModel insertDefinition;

    private InsertTemplateFMModel insertTemplate;

    private PutAttributeFMModel putAttribute;

    private PutListAttributeFMModel putListAttribute;

    private SetCurrentContainerFMModel setCurrentContainer;

    public TilesFMModelRepository() {
        addAttribute = new AddAttributeFMModel(new AddAttributeModel());
        addListAttribute = new AddListAttributeFMModel(
                new AddListAttributeModel());
        definition = new DefinitionFMModel(new DefinitionModel());
        AttributeResolver attributeResolver = new DefaultAttributeResolver();
        getAsString = new GetAsStringFMModel(new GetAsStringModel(
                attributeResolver));
        importAttribute = new ImportAttributeFMModel(new ImportAttributeModel());
        insertAttribute = new InsertAttributeFMModel(new InsertAttributeModel(
                attributeResolver));
        insertDefinition = new InsertDefinitionFMModel(
                new InsertDefinitionModel());
        insertTemplate = new InsertTemplateFMModel(new InsertTemplateModel());
        putAttribute = new PutAttributeFMModel(new PutAttributeModel());
        putListAttribute = new PutListAttributeFMModel(
                new PutListAttributeModel());
        setCurrentContainer = new SetCurrentContainerFMModel();
    }

    /**
     * @return the addAttribute
     */
    public AddAttributeFMModel getAddAttribute() {
        return addAttribute;
    }

    /**
     * @return the addListAttribute
     */
    public AddListAttributeFMModel getAddListAttribute() {
        return addListAttribute;
    }

    /**
     * @return the definition
     */
    public DefinitionFMModel getDefinition() {
        return definition;
    }

    /**
     * @return the getAsString
     */
    public GetAsStringFMModel getGetAsString() {
        return getAsString;
    }

    /**
     * @return the importAttribute
     */
    public ImportAttributeFMModel getImportAttribute() {
        return importAttribute;
    }

    /**
     * @return the insertAttribute
     */
    public InsertAttributeFMModel getInsertAttribute() {
        return insertAttribute;
    }

    /**
     * @return the insertDefinition
     */
    public InsertDefinitionFMModel getInsertDefinition() {
        return insertDefinition;
    }

    /**
     * @return the insertTemplate
     */
    public InsertTemplateFMModel getInsertTemplate() {
        return insertTemplate;
    }

    /**
     * @return the putAttribute
     */
    public PutAttributeFMModel getPutAttribute() {
        return putAttribute;
    }

    /**
     * @return the putListAttribute
     */
    public PutListAttributeFMModel getPutListAttribute() {
        return putListAttribute;
    }

    /**
     * @return the setCurrentContainer
     */
    public SetCurrentContainerFMModel getSetCurrentContainer() {
        return setCurrentContainer;
    }
}
