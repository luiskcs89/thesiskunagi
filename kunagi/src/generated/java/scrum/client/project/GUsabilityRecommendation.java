// ----------> GENERATED FILE - DON'T TOUCH! <----------

// generator: ilarkesto.mda.legacy.generator.GwtEntityGenerator










package scrum.client.project;

import java.util.*;
import ilarkesto.core.logging.Log;
import scrum.client.common.*;
import ilarkesto.gwt.client.*;

public abstract class GUsabilityRecommendation
            extends scrum.client.common.AScrumGwtEntity {

    protected scrum.client.Dao getDao() {
        return scrum.client.Dao.get();
    }

    public abstract boolean isEditable();

    public GUsabilityRecommendation() {
    }

    public GUsabilityRecommendation(Map data) {
        super(data);
        updateProperties(data);
    }

    public static final String ENTITY_TYPE = "usabilityRecommendation";

    @Override
    public final String getEntityType() {
        return ENTITY_TYPE;
    }

    // --- project ---

    private String projectId;

    public final scrum.client.project.Project getProject() {
        if (projectId == null) return null;
        return getDao().getProject(this.projectId);
    }

    public final boolean isProjectSet() {
        return projectId != null;
    }

    public final UsabilityRecommendation setProject(scrum.client.project.Project project) {
        String id = project == null ? null : project.getId();
        if (equals(this.projectId, id)) return (UsabilityRecommendation) this;
        this.projectId = id;
        propertyChanged("projectId", this.projectId);
        return (UsabilityRecommendation)this;
    }

    public final boolean isProject(scrum.client.project.Project project) {
        String id = project==null ? null : project.getId();
        return equals(this.projectId, id);
    }

    // --- number ---

    private int number ;

    public final int getNumber() {
        return this.number ;
    }

    public final UsabilityRecommendation setNumber(int number) {
        if (isNumber(number)) return (UsabilityRecommendation)this;
        this.number = number ;
        propertyChanged("number", this.number);
        return (UsabilityRecommendation)this;
    }

    public final boolean isNumber(int number) {
        return equals(this.number, number);
    }

    private transient NumberModel numberModel;

    public NumberModel getNumberModel() {
        if (numberModel == null) numberModel = createNumberModel();
        return numberModel;
    }

    protected NumberModel createNumberModel() { return new NumberModel(); }

    protected class NumberModel extends ilarkesto.gwt.client.editor.AIntegerEditorModel {

        @Override
        public String getId() {
            return "UsabilityRecommendation_number";
        }

        @Override
        public java.lang.Integer getValue() {
            return getNumber();
        }

        @Override
        public void setValue(java.lang.Integer value) {
            setNumber(value);
        }

            @Override
            public void increment() {
                setNumber(getNumber() + 1);
            }

            @Override
            public void decrement() {
                setNumber(getNumber() - 1);
            }

        @Override
        protected void onChangeValue(java.lang.Integer oldValue, java.lang.Integer newValue) {
            super.onChangeValue(oldValue, newValue);
            addUndo(this, oldValue);
        }

    }

    // --- label ---

    private java.lang.String label ;

    public final java.lang.String getLabel() {
        return this.label ;
    }

    public final UsabilityRecommendation setLabel(java.lang.String label) {
        if (isLabel(label)) return (UsabilityRecommendation)this;
        if (ilarkesto.core.base.Str.isBlank(label)) throw new RuntimeException("Field is mandatory.");
        this.label = label ;
        propertyChanged("label", this.label);
        return (UsabilityRecommendation)this;
    }

    public final boolean isLabel(java.lang.String label) {
        return equals(this.label, label);
    }

    private transient LabelModel labelModel;

    public LabelModel getLabelModel() {
        if (labelModel == null) labelModel = createLabelModel();
        return labelModel;
    }

    protected LabelModel createLabelModel() { return new LabelModel(); }

    protected class LabelModel extends ilarkesto.gwt.client.editor.ATextEditorModel {

        @Override
        public String getId() {
            return "UsabilityRecommendation_label";
        }

        @Override
        public java.lang.String getValue() {
            return getLabel();
        }

        @Override
        public void setValue(java.lang.String value) {
            setLabel(value);
        }

        @Override
        public boolean isMandatory() { return true; }

        @Override
        public boolean isEditable() { return GUsabilityRecommendation.this.isEditable(); }
        @Override
        public String getTooltip() { return "The label should be short (as it appears where the Usability Recommendation is referenced), yet give a hint strong enough to make the content of it come to mind."; }

        @Override
        protected void onChangeValue(java.lang.String oldValue, java.lang.String newValue) {
            super.onChangeValue(oldValue, newValue);
            addUndo(this, oldValue);
        }

    }

    // --- creates ---

    private boolean creates ;

    public final boolean isCreates() {
        return this.creates ;
    }

    public final UsabilityRecommendation setCreates(boolean creates) {
        if (isCreates(creates)) return (UsabilityRecommendation)this;
        this.creates = creates ;
        propertyChanged("creates", this.creates);
        return (UsabilityRecommendation)this;
    }

    public final boolean isCreates(boolean creates) {
        return equals(this.creates, creates);
    }

    private transient CreatesModel createsModel;

    public CreatesModel getCreatesModel() {
        if (createsModel == null) createsModel = createCreatesModel();
        return createsModel;
    }

    protected CreatesModel createCreatesModel() { return new CreatesModel(); }

    protected class CreatesModel extends ilarkesto.gwt.client.editor.ABooleanEditorModel {

        @Override
        public String getId() {
            return "UsabilityRecommendation_creates";
        }

        @Override
        public java.lang.Boolean getValue() {
            return isCreates();
        }

        @Override
        public void setValue(java.lang.Boolean value) {
            setCreates(value);
        }

        @Override
        protected void onChangeValue(java.lang.Boolean oldValue, java.lang.Boolean newValue) {
            super.onChangeValue(oldValue, newValue);
            addUndo(this, oldValue);
        }

    }

    // --- modifies ---

    private boolean modifies ;

    public final boolean isModifies() {
        return this.modifies ;
    }

    public final UsabilityRecommendation setModifies(boolean modifies) {
        if (isModifies(modifies)) return (UsabilityRecommendation)this;
        this.modifies = modifies ;
        propertyChanged("modifies", this.modifies);
        return (UsabilityRecommendation)this;
    }

    public final boolean isModifies(boolean modifies) {
        return equals(this.modifies, modifies);
    }

    private transient ModifiesModel modifiesModel;

    public ModifiesModel getModifiesModel() {
        if (modifiesModel == null) modifiesModel = createModifiesModel();
        return modifiesModel;
    }

    protected ModifiesModel createModifiesModel() { return new ModifiesModel(); }

    protected class ModifiesModel extends ilarkesto.gwt.client.editor.ABooleanEditorModel {

        @Override
        public String getId() {
            return "UsabilityRecommendation_modifies";
        }

        @Override
        public java.lang.Boolean getValue() {
            return isModifies();
        }

        @Override
        public void setValue(java.lang.Boolean value) {
            setModifies(value);
        }

        @Override
        protected void onChangeValue(java.lang.Boolean oldValue, java.lang.Boolean newValue) {
            super.onChangeValue(oldValue, newValue);
            addUndo(this, oldValue);
        }

    }

    // --- entityAffected ---

    private int entityAffected ;

    public final int getEntityAffected() {
        return this.entityAffected ;
    }

    public final UsabilityRecommendation setEntityAffected(int entityAffected) {
        if (isEntityAffected(entityAffected)) return (UsabilityRecommendation)this;
        this.entityAffected = entityAffected ;
        propertyChanged("entityAffected", this.entityAffected);
        return (UsabilityRecommendation)this;
    }

    public final boolean isEntityAffected(int entityAffected) {
        return equals(this.entityAffected, entityAffected);
    }

    private transient EntityAffectedModel entityAffectedModel;

    public EntityAffectedModel getEntityAffectedModel() {
        if (entityAffectedModel == null) entityAffectedModel = createEntityAffectedModel();
        return entityAffectedModel;
    }

    protected EntityAffectedModel createEntityAffectedModel() { return new EntityAffectedModel(); }

    protected class EntityAffectedModel extends ilarkesto.gwt.client.editor.AIntegerEditorModel {

        @Override
        public String getId() {
            return "UsabilityRecommendation_entityAffected";
        }

        @Override
        public java.lang.Integer getValue() {
            return getEntityAffected();
        }

        @Override
        public void setValue(java.lang.Integer value) {
            setEntityAffected(value);
        }

            @Override
            public void increment() {
                setEntityAffected(getEntityAffected() + 1);
            }

            @Override
            public void decrement() {
                setEntityAffected(getEntityAffected() - 1);
            }

        @Override
        protected void onChangeValue(java.lang.Integer oldValue, java.lang.Integer newValue) {
            super.onChangeValue(oldValue, newValue);
            addUndo(this, oldValue);
        }

    }

    // --- usabilityMechanism ---

    private String usabilityMechanismId;

    public final scrum.client.project.UsabilityMechanism getUsabilityMechanism() {
        if (usabilityMechanismId == null) return null;
        return getDao().getUsabilityMechanism(this.usabilityMechanismId);
    }

    public final boolean isUsabilityMechanismSet() {
        return usabilityMechanismId != null;
    }

    public final UsabilityRecommendation setUsabilityMechanism(scrum.client.project.UsabilityMechanism usabilityMechanism) {
        String id = usabilityMechanism == null ? null : usabilityMechanism.getId();
        if (equals(this.usabilityMechanismId, id)) return (UsabilityRecommendation) this;
        this.usabilityMechanismId = id;
        propertyChanged("usabilityMechanismId", this.usabilityMechanismId);
        return (UsabilityRecommendation)this;
    }

    public final boolean isUsabilityMechanism(scrum.client.project.UsabilityMechanism usabilityMechanism) {
        String id = usabilityMechanism==null ? null : usabilityMechanism.getId();
        return equals(this.usabilityMechanismId, id);
    }

    // --- update properties by map ---

    public void updateProperties(Map props) {
        projectId = (String) props.get("projectId");
        number  = (Integer) props.get("number");
        label  = (java.lang.String) props.get("label");
        creates  = (Boolean) props.get("creates");
        modifies  = (Boolean) props.get("modifies");
        entityAffected  = (Integer) props.get("entityAffected");
        usabilityMechanismId = (String) props.get("usabilityMechanismId");
        updateLocalModificationTime();
    }

    @Override
    public void storeProperties(Map properties) {
        super.storeProperties(properties);
        properties.put("projectId", this.projectId);
        properties.put("number", this.number);
        properties.put("label", this.label);
        properties.put("creates", this.creates);
        properties.put("modifies", this.modifies);
        properties.put("entityAffected", this.entityAffected);
        properties.put("usabilityMechanismId", this.usabilityMechanismId);
    }

    public final java.util.List<scrum.client.project.Requirement> getRequirements() {
        return getDao().getRequirementsByUsabilityRecommendation((UsabilityRecommendation)this);
    }

    @Override
    public boolean matchesKey(String key) {
        if (super.matchesKey(key)) return true;
        if (matchesKey(getLabel(), key)) return true;
        return false;
    }

}