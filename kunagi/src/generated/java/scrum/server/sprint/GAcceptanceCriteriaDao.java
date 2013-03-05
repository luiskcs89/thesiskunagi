// ----------> GENERATED FILE - DON'T TOUCH! <----------

// generator: ilarkesto.mda.legacy.generator.DaoGenerator










package scrum.server.sprint;

import java.util.*;
import ilarkesto.core.logging.Log;
import ilarkesto.auth.Auth;
import ilarkesto.base.Cache;
import ilarkesto.persistence.EntityEvent;
import ilarkesto.fp.Predicate;

public abstract class GAcceptanceCriteriaDao
            extends ilarkesto.persistence.ADao<AcceptanceCriteria> {

    public final String getEntityName() {
        return AcceptanceCriteria.TYPE;
    }

    public final Class getEntityClass() {
        return AcceptanceCriteria.class;
    }

    public Set<AcceptanceCriteria> getEntitiesVisibleForUser(final scrum.server.admin.User user) {
        return getEntities(new Predicate<AcceptanceCriteria>() {
            public boolean test(AcceptanceCriteria e) {
                return Auth.isVisible(e, user);
            }
        });
    }

    // --- clear caches ---
    public void clearCaches() {
        acceptanceCriteriasByRequirementCache.clear();
        requirementsCache = null;
        acceptanceCriteriasByNumberCache.clear();
        numbersCache = null;
        acceptanceCriteriasByLabelCache.clear();
        labelsCache = null;
        acceptanceCriteriasByDescriptionCache.clear();
        descriptionsCache = null;
    }

    @Override
    public void entityDeleted(EntityEvent event) {
        super.entityDeleted(event);
        if (event.getEntity() instanceof AcceptanceCriteria) {
            clearCaches();
        }
    }

    @Override
    public void entitySaved(EntityEvent event) {
        super.entitySaved(event);
        if (event.getEntity() instanceof AcceptanceCriteria) {
            clearCaches();
        }
    }

    // -----------------------------------------------------------
    // - requirement
    // -----------------------------------------------------------

    private final Cache<scrum.server.project.Requirement,Set<AcceptanceCriteria>> acceptanceCriteriasByRequirementCache = new Cache<scrum.server.project.Requirement,Set<AcceptanceCriteria>>(
            new Cache.Factory<scrum.server.project.Requirement,Set<AcceptanceCriteria>>() {
                public Set<AcceptanceCriteria> create(scrum.server.project.Requirement requirement) {
                    return getEntities(new IsRequirement(requirement));
                }
            });

    public final Set<AcceptanceCriteria> getAcceptanceCriteriasByRequirement(scrum.server.project.Requirement requirement) {
        return new HashSet<AcceptanceCriteria>(acceptanceCriteriasByRequirementCache.get(requirement));
    }
    private Set<scrum.server.project.Requirement> requirementsCache;

    public final Set<scrum.server.project.Requirement> getRequirements() {
        if (requirementsCache == null) {
            requirementsCache = new HashSet<scrum.server.project.Requirement>();
            for (AcceptanceCriteria e : getEntities()) {
                if (e.isRequirementSet()) requirementsCache.add(e.getRequirement());
            }
        }
        return requirementsCache;
    }

    private static class IsRequirement implements Predicate<AcceptanceCriteria> {

        private scrum.server.project.Requirement value;

        public IsRequirement(scrum.server.project.Requirement value) {
            this.value = value;
        }

        public boolean test(AcceptanceCriteria e) {
            return e.isRequirement(value);
        }

    }

    // -----------------------------------------------------------
    // - number
    // -----------------------------------------------------------

    private final Cache<Integer,Set<AcceptanceCriteria>> acceptanceCriteriasByNumberCache = new Cache<Integer,Set<AcceptanceCriteria>>(
            new Cache.Factory<Integer,Set<AcceptanceCriteria>>() {
                public Set<AcceptanceCriteria> create(Integer number) {
                    return getEntities(new IsNumber(number));
                }
            });

    public final Set<AcceptanceCriteria> getAcceptanceCriteriasByNumber(int number) {
        return new HashSet<AcceptanceCriteria>(acceptanceCriteriasByNumberCache.get(number));
    }
    private Set<Integer> numbersCache;

    public final Set<Integer> getNumbers() {
        if (numbersCache == null) {
            numbersCache = new HashSet<Integer>();
            for (AcceptanceCriteria e : getEntities()) {
                numbersCache.add(e.getNumber());
            }
        }
        return numbersCache;
    }

    private static class IsNumber implements Predicate<AcceptanceCriteria> {

        private int value;

        public IsNumber(int value) {
            this.value = value;
        }

        public boolean test(AcceptanceCriteria e) {
            return e.isNumber(value);
        }

    }

    // -----------------------------------------------------------
    // - label
    // -----------------------------------------------------------

    private final Cache<java.lang.String,Set<AcceptanceCriteria>> acceptanceCriteriasByLabelCache = new Cache<java.lang.String,Set<AcceptanceCriteria>>(
            new Cache.Factory<java.lang.String,Set<AcceptanceCriteria>>() {
                public Set<AcceptanceCriteria> create(java.lang.String label) {
                    return getEntities(new IsLabel(label));
                }
            });

    public final Set<AcceptanceCriteria> getAcceptanceCriteriasByLabel(java.lang.String label) {
        return new HashSet<AcceptanceCriteria>(acceptanceCriteriasByLabelCache.get(label));
    }
    private Set<java.lang.String> labelsCache;

    public final Set<java.lang.String> getLabels() {
        if (labelsCache == null) {
            labelsCache = new HashSet<java.lang.String>();
            for (AcceptanceCriteria e : getEntities()) {
                if (e.isLabelSet()) labelsCache.add(e.getLabel());
            }
        }
        return labelsCache;
    }

    private static class IsLabel implements Predicate<AcceptanceCriteria> {

        private java.lang.String value;

        public IsLabel(java.lang.String value) {
            this.value = value;
        }

        public boolean test(AcceptanceCriteria e) {
            return e.isLabel(value);
        }

    }

    // -----------------------------------------------------------
    // - description
    // -----------------------------------------------------------

    private final Cache<java.lang.String,Set<AcceptanceCriteria>> acceptanceCriteriasByDescriptionCache = new Cache<java.lang.String,Set<AcceptanceCriteria>>(
            new Cache.Factory<java.lang.String,Set<AcceptanceCriteria>>() {
                public Set<AcceptanceCriteria> create(java.lang.String description) {
                    return getEntities(new IsDescription(description));
                }
            });

    public final Set<AcceptanceCriteria> getAcceptanceCriteriasByDescription(java.lang.String description) {
        return new HashSet<AcceptanceCriteria>(acceptanceCriteriasByDescriptionCache.get(description));
    }
    private Set<java.lang.String> descriptionsCache;

    public final Set<java.lang.String> getDescriptions() {
        if (descriptionsCache == null) {
            descriptionsCache = new HashSet<java.lang.String>();
            for (AcceptanceCriteria e : getEntities()) {
                if (e.isDescriptionSet()) descriptionsCache.add(e.getDescription());
            }
        }
        return descriptionsCache;
    }

    private static class IsDescription implements Predicate<AcceptanceCriteria> {

        private java.lang.String value;

        public IsDescription(java.lang.String value) {
            this.value = value;
        }

        public boolean test(AcceptanceCriteria e) {
            return e.isDescription(value);
        }

    }

    // --- valueObject classes ---
    @Override
    protected Set<Class> getValueObjectClasses() {
        Set<Class> ret = new HashSet<Class>(super.getValueObjectClasses());
        return ret;
    }

    @Override
    public Map<String, Class> getAliases() {
        Map<String, Class> aliases = new HashMap<String, Class>(super.getAliases());
        return aliases;
    }

    // --- dependencies ---

    scrum.server.project.RequirementDao requirementDao;

    public void setRequirementDao(scrum.server.project.RequirementDao requirementDao) {
        this.requirementDao = requirementDao;
    }

}