// ----------> GENERATED FILE - DON'T TOUCH! <----------

// generator: ilarkesto.mda.legacy.generator.DaoGenerator










package scrum.server.project;

import java.util.*;
import ilarkesto.core.logging.Log;
import ilarkesto.auth.Auth;
import ilarkesto.base.Cache;
import ilarkesto.persistence.EntityEvent;
import ilarkesto.fp.Predicate;

public abstract class GUsabilityRecommendationDao
            extends ilarkesto.persistence.ADao<UsabilityRecommendation> {

    public final String getEntityName() {
        return UsabilityRecommendation.TYPE;
    }

    public final Class getEntityClass() {
        return UsabilityRecommendation.class;
    }

    public Set<UsabilityRecommendation> getEntitiesVisibleForUser(final scrum.server.admin.User user) {
        return getEntities(new Predicate<UsabilityRecommendation>() {
            public boolean test(UsabilityRecommendation e) {
                return Auth.isVisible(e, user);
            }
        });
    }

    // --- clear caches ---
    public void clearCaches() {
        usabilityRecommendationsByProjectCache.clear();
        projectsCache = null;
        usabilityRecommendationsByNumberCache.clear();
        numbersCache = null;
        usabilityRecommendationsByLabelCache.clear();
        labelsCache = null;
        usabilityRecommendationsByCreatesCache.clear();
        usabilityRecommendationsByModifiesCache.clear();
        usabilityRecommendationsByEntityAffectedCache.clear();
        entityAffectedsCache = null;
        usabilityRecommendationsByUsabilityMechanismCache.clear();
        usabilityMechanismsCache = null;
    }

    @Override
    public void entityDeleted(EntityEvent event) {
        super.entityDeleted(event);
        if (event.getEntity() instanceof UsabilityRecommendation) {
            clearCaches();
        }
    }

    @Override
    public void entitySaved(EntityEvent event) {
        super.entitySaved(event);
        if (event.getEntity() instanceof UsabilityRecommendation) {
            clearCaches();
        }
    }

    // -----------------------------------------------------------
    // - project
    // -----------------------------------------------------------

    private final Cache<scrum.server.project.Project,Set<UsabilityRecommendation>> usabilityRecommendationsByProjectCache = new Cache<scrum.server.project.Project,Set<UsabilityRecommendation>>(
            new Cache.Factory<scrum.server.project.Project,Set<UsabilityRecommendation>>() {
                public Set<UsabilityRecommendation> create(scrum.server.project.Project project) {
                    return getEntities(new IsProject(project));
                }
            });

    public final Set<UsabilityRecommendation> getUsabilityRecommendationsByProject(scrum.server.project.Project project) {
        return new HashSet<UsabilityRecommendation>(usabilityRecommendationsByProjectCache.get(project));
    }
    private Set<scrum.server.project.Project> projectsCache;

    public final Set<scrum.server.project.Project> getProjects() {
        if (projectsCache == null) {
            projectsCache = new HashSet<scrum.server.project.Project>();
            for (UsabilityRecommendation e : getEntities()) {
                if (e.isProjectSet()) projectsCache.add(e.getProject());
            }
        }
        return projectsCache;
    }

    private static class IsProject implements Predicate<UsabilityRecommendation> {

        private scrum.server.project.Project value;

        public IsProject(scrum.server.project.Project value) {
            this.value = value;
        }

        public boolean test(UsabilityRecommendation e) {
            return e.isProject(value);
        }

    }

    // -----------------------------------------------------------
    // - number
    // -----------------------------------------------------------

    private final Cache<Integer,Set<UsabilityRecommendation>> usabilityRecommendationsByNumberCache = new Cache<Integer,Set<UsabilityRecommendation>>(
            new Cache.Factory<Integer,Set<UsabilityRecommendation>>() {
                public Set<UsabilityRecommendation> create(Integer number) {
                    return getEntities(new IsNumber(number));
                }
            });

    public final Set<UsabilityRecommendation> getUsabilityRecommendationsByNumber(int number) {
        return new HashSet<UsabilityRecommendation>(usabilityRecommendationsByNumberCache.get(number));
    }
    private Set<Integer> numbersCache;

    public final Set<Integer> getNumbers() {
        if (numbersCache == null) {
            numbersCache = new HashSet<Integer>();
            for (UsabilityRecommendation e : getEntities()) {
                numbersCache.add(e.getNumber());
            }
        }
        return numbersCache;
    }

    private static class IsNumber implements Predicate<UsabilityRecommendation> {

        private int value;

        public IsNumber(int value) {
            this.value = value;
        }

        public boolean test(UsabilityRecommendation e) {
            return e.isNumber(value);
        }

    }

    // -----------------------------------------------------------
    // - label
    // -----------------------------------------------------------

    private final Cache<java.lang.String,Set<UsabilityRecommendation>> usabilityRecommendationsByLabelCache = new Cache<java.lang.String,Set<UsabilityRecommendation>>(
            new Cache.Factory<java.lang.String,Set<UsabilityRecommendation>>() {
                public Set<UsabilityRecommendation> create(java.lang.String label) {
                    return getEntities(new IsLabel(label));
                }
            });

    public final Set<UsabilityRecommendation> getUsabilityRecommendationsByLabel(java.lang.String label) {
        return new HashSet<UsabilityRecommendation>(usabilityRecommendationsByLabelCache.get(label));
    }
    private Set<java.lang.String> labelsCache;

    public final Set<java.lang.String> getLabels() {
        if (labelsCache == null) {
            labelsCache = new HashSet<java.lang.String>();
            for (UsabilityRecommendation e : getEntities()) {
                if (e.isLabelSet()) labelsCache.add(e.getLabel());
            }
        }
        return labelsCache;
    }

    private static class IsLabel implements Predicate<UsabilityRecommendation> {

        private java.lang.String value;

        public IsLabel(java.lang.String value) {
            this.value = value;
        }

        public boolean test(UsabilityRecommendation e) {
            return e.isLabel(value);
        }

    }

    // -----------------------------------------------------------
    // - creates
    // -----------------------------------------------------------

    private final Cache<Boolean,Set<UsabilityRecommendation>> usabilityRecommendationsByCreatesCache = new Cache<Boolean,Set<UsabilityRecommendation>>(
            new Cache.Factory<Boolean,Set<UsabilityRecommendation>>() {
                public Set<UsabilityRecommendation> create(Boolean creates) {
                    return getEntities(new IsCreates(creates));
                }
            });

    public final Set<UsabilityRecommendation> getUsabilityRecommendationsByCreates(boolean creates) {
        return new HashSet<UsabilityRecommendation>(usabilityRecommendationsByCreatesCache.get(creates));
    }

    private static class IsCreates implements Predicate<UsabilityRecommendation> {

        private boolean value;

        public IsCreates(boolean value) {
            this.value = value;
        }

        public boolean test(UsabilityRecommendation e) {
            return value == e.isCreates();
        }

    }

    // -----------------------------------------------------------
    // - modifies
    // -----------------------------------------------------------

    private final Cache<Boolean,Set<UsabilityRecommendation>> usabilityRecommendationsByModifiesCache = new Cache<Boolean,Set<UsabilityRecommendation>>(
            new Cache.Factory<Boolean,Set<UsabilityRecommendation>>() {
                public Set<UsabilityRecommendation> create(Boolean modifies) {
                    return getEntities(new IsModifies(modifies));
                }
            });

    public final Set<UsabilityRecommendation> getUsabilityRecommendationsByModifies(boolean modifies) {
        return new HashSet<UsabilityRecommendation>(usabilityRecommendationsByModifiesCache.get(modifies));
    }

    private static class IsModifies implements Predicate<UsabilityRecommendation> {

        private boolean value;

        public IsModifies(boolean value) {
            this.value = value;
        }

        public boolean test(UsabilityRecommendation e) {
            return value == e.isModifies();
        }

    }

    // -----------------------------------------------------------
    // - entityAffected
    // -----------------------------------------------------------

    private final Cache<Integer,Set<UsabilityRecommendation>> usabilityRecommendationsByEntityAffectedCache = new Cache<Integer,Set<UsabilityRecommendation>>(
            new Cache.Factory<Integer,Set<UsabilityRecommendation>>() {
                public Set<UsabilityRecommendation> create(Integer entityAffected) {
                    return getEntities(new IsEntityAffected(entityAffected));
                }
            });

    public final Set<UsabilityRecommendation> getUsabilityRecommendationsByEntityAffected(int entityAffected) {
        return new HashSet<UsabilityRecommendation>(usabilityRecommendationsByEntityAffectedCache.get(entityAffected));
    }
    private Set<Integer> entityAffectedsCache;

    public final Set<Integer> getEntityAffecteds() {
        if (entityAffectedsCache == null) {
            entityAffectedsCache = new HashSet<Integer>();
            for (UsabilityRecommendation e : getEntities()) {
                entityAffectedsCache.add(e.getEntityAffected());
            }
        }
        return entityAffectedsCache;
    }

    private static class IsEntityAffected implements Predicate<UsabilityRecommendation> {

        private int value;

        public IsEntityAffected(int value) {
            this.value = value;
        }

        public boolean test(UsabilityRecommendation e) {
            return e.isEntityAffected(value);
        }

    }

    // -----------------------------------------------------------
    // - usabilityMechanism
    // -----------------------------------------------------------

    private final Cache<scrum.server.project.UsabilityMechanism,Set<UsabilityRecommendation>> usabilityRecommendationsByUsabilityMechanismCache = new Cache<scrum.server.project.UsabilityMechanism,Set<UsabilityRecommendation>>(
            new Cache.Factory<scrum.server.project.UsabilityMechanism,Set<UsabilityRecommendation>>() {
                public Set<UsabilityRecommendation> create(scrum.server.project.UsabilityMechanism usabilityMechanism) {
                    return getEntities(new IsUsabilityMechanism(usabilityMechanism));
                }
            });

    public final Set<UsabilityRecommendation> getUsabilityRecommendationsByUsabilityMechanism(scrum.server.project.UsabilityMechanism usabilityMechanism) {
        return new HashSet<UsabilityRecommendation>(usabilityRecommendationsByUsabilityMechanismCache.get(usabilityMechanism));
    }
    private Set<scrum.server.project.UsabilityMechanism> usabilityMechanismsCache;

    public final Set<scrum.server.project.UsabilityMechanism> getUsabilityMechanisms() {
        if (usabilityMechanismsCache == null) {
            usabilityMechanismsCache = new HashSet<scrum.server.project.UsabilityMechanism>();
            for (UsabilityRecommendation e : getEntities()) {
                if (e.isUsabilityMechanismSet()) usabilityMechanismsCache.add(e.getUsabilityMechanism());
            }
        }
        return usabilityMechanismsCache;
    }

    private static class IsUsabilityMechanism implements Predicate<UsabilityRecommendation> {

        private scrum.server.project.UsabilityMechanism value;

        public IsUsabilityMechanism(scrum.server.project.UsabilityMechanism value) {
            this.value = value;
        }

        public boolean test(UsabilityRecommendation e) {
            return e.isUsabilityMechanism(value);
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

    scrum.server.project.ProjectDao projectDao;

    public void setProjectDao(scrum.server.project.ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    scrum.server.project.UsabilityMechanismDao usabilityMechanismDao;

    public void setUsabilityMechanismDao(scrum.server.project.UsabilityMechanismDao usabilityMechanismDao) {
        this.usabilityMechanismDao = usabilityMechanismDao;
    }

}