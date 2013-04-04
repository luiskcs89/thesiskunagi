// ----------> GENERATED FILE - DON'T TOUCH! <----------

// generator: ilarkesto.mda.legacy.generator.EntityGenerator










package scrum.server.project;

import java.util.*;
import ilarkesto.core.logging.Log;
import ilarkesto.persistence.ADatob;
import ilarkesto.persistence.AEntity;
import ilarkesto.persistence.AStructure;
import ilarkesto.auth.AUser;
import ilarkesto.persistence.EntityDoesNotExistException;
import ilarkesto.base.Str;

public abstract class GUsabilityRecommendation
            extends AEntity
            implements ilarkesto.auth.ViewProtected<scrum.server.admin.User>, ilarkesto.search.Searchable, java.lang.Comparable<UsabilityRecommendation> {

    // --- AEntity ---

    public final scrum.server.project.UsabilityRecommendationDao getDao() {
        return usabilityRecommendationDao;
    }

    protected void repairDeadDatob(ADatob datob) {
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

    public int compareTo(UsabilityRecommendation other) {
        return toString().toLowerCase().compareTo(other.toString().toLowerCase());
    }

    public final java.util.Set<scrum.server.project.Requirement> getRequirements() {
        return requirementDao.getRequirementsByUsabilityRecommendation((UsabilityRecommendation)this);
    }

    private static final ilarkesto.core.logging.Log LOG = ilarkesto.core.logging.Log.get(GUsabilityRecommendation.class);

    public static final String TYPE = "usabilityRecommendation";


    // -----------------------------------------------------------
    // - Searchable
    // -----------------------------------------------------------

    public boolean matchesKey(String key) {
        if (super.matchesKey(key)) return true;
        if (matchesKey(getLabel(), key)) return true;
        return false;
    }

    // -----------------------------------------------------------
    // - project
    // -----------------------------------------------------------

    private String projectId;
    private transient scrum.server.project.Project projectCache;

    private void updateProjectCache() {
        projectCache = this.projectId == null ? null : (scrum.server.project.Project)projectDao.getById(this.projectId);
    }

    public final String getProjectId() {
        return this.projectId;
    }

    public final scrum.server.project.Project getProject() {
        if (projectCache == null) updateProjectCache();
        return projectCache;
    }

    public final void setProject(scrum.server.project.Project project) {
        project = prepareProject(project);
        if (isProject(project)) return;
        this.projectId = project == null ? null : project.getId();
        projectCache = project;
        updateLastModified();
        fireModified("project="+project);
    }

    protected scrum.server.project.Project prepareProject(scrum.server.project.Project project) {
        return project;
    }

    protected void repairDeadProjectReference(String entityId) {
        if (this.projectId == null || entityId.equals(this.projectId)) {
            repairMissingMaster();
        }
    }

    public final boolean isProjectSet() {
        return this.projectId != null;
    }

    public final boolean isProject(scrum.server.project.Project project) {
        if (this.projectId == null && project == null) return true;
        return project != null && project.getId().equals(this.projectId);
    }

    protected final void updateProject(Object value) {
        setProject(value == null ? null : (scrum.server.project.Project)projectDao.getById((String)value));
    }

    // -----------------------------------------------------------
    // - number
    // -----------------------------------------------------------

    private int number;

    public final int getNumber() {
        return number;
    }

    public final void setNumber(int number) {
        number = prepareNumber(number);
        if (isNumber(number)) return;
        this.number = number;
        updateLastModified();
        fireModified("number="+number);
    }

    protected int prepareNumber(int number) {
        return number;
    }

    public final boolean isNumber(int number) {
        return this.number == number;
    }

    protected final void updateNumber(Object value) {
        setNumber((Integer)value);
    }

    // -----------------------------------------------------------
    // - label
    // -----------------------------------------------------------

    private java.lang.String label;

    public final java.lang.String getLabel() {
        return label;
    }

    public final void setLabel(java.lang.String label) {
        label = prepareLabel(label);
        if (isLabel(label)) return;
        this.label = label;
        updateLastModified();
        fireModified("label="+label);
    }

    protected java.lang.String prepareLabel(java.lang.String label) {
        // label = Str.removeUnreadableChars(label);
        return label;
    }

    public final boolean isLabelSet() {
        return this.label != null;
    }

    public final boolean isLabel(java.lang.String label) {
        if (this.label == null && label == null) return true;
        return this.label != null && this.label.equals(label);
    }

    protected final void updateLabel(Object value) {
        setLabel((java.lang.String)value);
    }

    // -----------------------------------------------------------
    // - creates
    // -----------------------------------------------------------

    private boolean creates;

    public final boolean isCreates() {
        return creates;
    }

    public final void setCreates(boolean creates) {
        creates = prepareCreates(creates);
        if (isCreates(creates)) return;
        this.creates = creates;
        updateLastModified();
        fireModified("creates="+creates);
    }

    protected boolean prepareCreates(boolean creates) {
        return creates;
    }

    public final boolean isCreates(boolean creates) {
        return this.creates == creates;
    }

    protected final void updateCreates(Object value) {
        setCreates((Boolean)value);
    }

    // -----------------------------------------------------------
    // - modifies
    // -----------------------------------------------------------

    private boolean modifies;

    public final boolean isModifies() {
        return modifies;
    }

    public final void setModifies(boolean modifies) {
        modifies = prepareModifies(modifies);
        if (isModifies(modifies)) return;
        this.modifies = modifies;
        updateLastModified();
        fireModified("modifies="+modifies);
    }

    protected boolean prepareModifies(boolean modifies) {
        return modifies;
    }

    public final boolean isModifies(boolean modifies) {
        return this.modifies == modifies;
    }

    protected final void updateModifies(Object value) {
        setModifies((Boolean)value);
    }

    // -----------------------------------------------------------
    // - entityAffected
    // -----------------------------------------------------------

    private int entityAffected;

    public final int getEntityAffected() {
        return entityAffected;
    }

    public final void setEntityAffected(int entityAffected) {
        entityAffected = prepareEntityAffected(entityAffected);
        if (isEntityAffected(entityAffected)) return;
        this.entityAffected = entityAffected;
        updateLastModified();
        fireModified("entityAffected="+entityAffected);
    }

    protected int prepareEntityAffected(int entityAffected) {
        return entityAffected;
    }

    public final boolean isEntityAffected(int entityAffected) {
        return this.entityAffected == entityAffected;
    }

    protected final void updateEntityAffected(Object value) {
        setEntityAffected((Integer)value);
    }

    // -----------------------------------------------------------
    // - usabilityMechanism
    // -----------------------------------------------------------

    private String usabilityMechanismId;
    private transient scrum.server.project.UsabilityMechanism usabilityMechanismCache;

    private void updateUsabilityMechanismCache() {
        usabilityMechanismCache = this.usabilityMechanismId == null ? null : (scrum.server.project.UsabilityMechanism)usabilityMechanismDao.getById(this.usabilityMechanismId);
    }

    public final String getUsabilityMechanismId() {
        return this.usabilityMechanismId;
    }

    public final scrum.server.project.UsabilityMechanism getUsabilityMechanism() {
        if (usabilityMechanismCache == null) updateUsabilityMechanismCache();
        return usabilityMechanismCache;
    }

    public final void setUsabilityMechanism(scrum.server.project.UsabilityMechanism usabilityMechanism) {
        usabilityMechanism = prepareUsabilityMechanism(usabilityMechanism);
        if (isUsabilityMechanism(usabilityMechanism)) return;
        this.usabilityMechanismId = usabilityMechanism == null ? null : usabilityMechanism.getId();
        usabilityMechanismCache = usabilityMechanism;
        updateLastModified();
        fireModified("usabilityMechanism="+usabilityMechanism);
    }

    protected scrum.server.project.UsabilityMechanism prepareUsabilityMechanism(scrum.server.project.UsabilityMechanism usabilityMechanism) {
        return usabilityMechanism;
    }

    protected void repairDeadUsabilityMechanismReference(String entityId) {
        if (this.usabilityMechanismId == null || entityId.equals(this.usabilityMechanismId)) {
            setUsabilityMechanism(null);
        }
    }

    public final boolean isUsabilityMechanismSet() {
        return this.usabilityMechanismId != null;
    }

    public final boolean isUsabilityMechanism(scrum.server.project.UsabilityMechanism usabilityMechanism) {
        if (this.usabilityMechanismId == null && usabilityMechanism == null) return true;
        return usabilityMechanism != null && usabilityMechanism.getId().equals(this.usabilityMechanismId);
    }

    protected final void updateUsabilityMechanism(Object value) {
        setUsabilityMechanism(value == null ? null : (scrum.server.project.UsabilityMechanism)usabilityMechanismDao.getById((String)value));
    }

    public void updateProperties(Map<?, ?> properties) {
        for (Map.Entry entry : properties.entrySet()) {
            String property = (String) entry.getKey();
            if (property.equals("id")) continue;
            Object value = entry.getValue();
            if (property.equals("projectId")) updateProject(value);
            if (property.equals("number")) updateNumber(value);
            if (property.equals("label")) updateLabel(value);
            if (property.equals("creates")) updateCreates(value);
            if (property.equals("modifies")) updateModifies(value);
            if (property.equals("entityAffected")) updateEntityAffected(value);
            if (property.equals("usabilityMechanismId")) updateUsabilityMechanism(value);
        }
    }

    protected void repairDeadReferences(String entityId) {
        super.repairDeadReferences(entityId);
        repairDeadProjectReference(entityId);
        repairDeadUsabilityMechanismReference(entityId);
    }

    // --- ensure integrity ---

    public void ensureIntegrity() {
        super.ensureIntegrity();
        if (!isProjectSet()) {
            repairMissingMaster();
            return;
        }
        try {
            getProject();
        } catch (EntityDoesNotExistException ex) {
            LOG.info("Repairing dead project reference");
            repairDeadProjectReference(this.projectId);
        }
        try {
            getUsabilityMechanism();
        } catch (EntityDoesNotExistException ex) {
            LOG.info("Repairing dead usabilityMechanism reference");
            repairDeadUsabilityMechanismReference(this.usabilityMechanismId);
        }
    }


    // -----------------------------------------------------------
    // - dependencies
    // -----------------------------------------------------------

    static scrum.server.project.ProjectDao projectDao;

    public static final void setProjectDao(scrum.server.project.ProjectDao projectDao) {
        GUsabilityRecommendation.projectDao = projectDao;
    }

    static scrum.server.project.UsabilityMechanismDao usabilityMechanismDao;

    public static final void setUsabilityMechanismDao(scrum.server.project.UsabilityMechanismDao usabilityMechanismDao) {
        GUsabilityRecommendation.usabilityMechanismDao = usabilityMechanismDao;
    }

    static scrum.server.project.UsabilityRecommendationDao usabilityRecommendationDao;

    public static final void setUsabilityRecommendationDao(scrum.server.project.UsabilityRecommendationDao usabilityRecommendationDao) {
        GUsabilityRecommendation.usabilityRecommendationDao = usabilityRecommendationDao;
    }

    static scrum.server.project.RequirementDao requirementDao;

    public static final void setRequirementDao(scrum.server.project.RequirementDao requirementDao) {
        GUsabilityRecommendation.requirementDao = requirementDao;
    }

}