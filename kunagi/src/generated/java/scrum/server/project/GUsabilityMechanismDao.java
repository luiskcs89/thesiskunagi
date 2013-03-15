// ----------> GENERATED FILE - DON'T TOUCH! <----------

// generator: ilarkesto.mda.legacy.generator.DaoGenerator










package scrum.server.project;

import java.util.*;
import ilarkesto.core.logging.Log;
import ilarkesto.auth.Auth;
import ilarkesto.base.Cache;
import ilarkesto.persistence.EntityEvent;
import ilarkesto.fp.Predicate;

public abstract class GUsabilityMechanismDao
            extends ilarkesto.persistence.ADao<UsabilityMechanism> {

    public final String getEntityName() {
        return UsabilityMechanism.TYPE;
    }

    public final Class getEntityClass() {
        return UsabilityMechanism.class;
    }

    public Set<UsabilityMechanism> getEntitiesVisibleForUser(final scrum.server.admin.User user) {
        return getEntities(new Predicate<UsabilityMechanism>() {
            public boolean test(UsabilityMechanism e) {
                return Auth.isVisible(e, user);
            }
        });
    }

    // --- clear caches ---
    public void clearCaches() {
        usabilityMechanismsByProjectCache.clear();
        projectsCache = null;
        usabilityMechanismsByNumberCache.clear();
        numbersCache = null;
        usabilityMechanismsByLabelCache.clear();
        labelsCache = null;
        usabilityMechanismsByDescriptionCache.clear();
        descriptionsCache = null;
    }

    @Override
    public void entityDeleted(EntityEvent event) {
        super.entityDeleted(event);
        if (event.getEntity() instanceof UsabilityMechanism) {
            clearCaches();
        }
    }

    @Override
    public void entitySaved(EntityEvent event) {
        super.entitySaved(event);
        if (event.getEntity() instanceof UsabilityMechanism) {
            clearCaches();
        }
    }

    // -----------------------------------------------------------
    // - project
    // -----------------------------------------------------------

    private final Cache<scrum.server.project.Project,Set<UsabilityMechanism>> usabilityMechanismsByProjectCache = new Cache<scrum.server.project.Project,Set<UsabilityMechanism>>(
            new Cache.Factory<scrum.server.project.Project,Set<UsabilityMechanism>>() {
                public Set<UsabilityMechanism> create(scrum.server.project.Project project) {
                    return getEntities(new IsProject(project));
                }
            });

    public final Set<UsabilityMechanism> getUsabilityMechanismsByProject(scrum.server.project.Project project) {
        return new HashSet<UsabilityMechanism>(usabilityMechanismsByProjectCache.get(project));
    }
    private Set<scrum.server.project.Project> projectsCache;

    public final Set<scrum.server.project.Project> getProjects() {
        if (projectsCache == null) {
            projectsCache = new HashSet<scrum.server.project.Project>();
            for (UsabilityMechanism e : getEntities()) {
                if (e.isProjectSet()) projectsCache.add(e.getProject());
            }
        }
        return projectsCache;
    }

    private static class IsProject implements Predicate<UsabilityMechanism> {

        private scrum.server.project.Project value;

        public IsProject(scrum.server.project.Project value) {
            this.value = value;
        }

        public boolean test(UsabilityMechanism e) {
            return e.isProject(value);
        }

    }

    // -----------------------------------------------------------
    // - number
    // -----------------------------------------------------------

    private final Cache<Integer,Set<UsabilityMechanism>> usabilityMechanismsByNumberCache = new Cache<Integer,Set<UsabilityMechanism>>(
            new Cache.Factory<Integer,Set<UsabilityMechanism>>() {
                public Set<UsabilityMechanism> create(Integer number) {
                    return getEntities(new IsNumber(number));
                }
            });

    public final Set<UsabilityMechanism> getUsabilityMechanismsByNumber(int number) {
        return new HashSet<UsabilityMechanism>(usabilityMechanismsByNumberCache.get(number));
    }
    private Set<Integer> numbersCache;

    public final Set<Integer> getNumbers() {
        if (numbersCache == null) {
            numbersCache = new HashSet<Integer>();
            for (UsabilityMechanism e : getEntities()) {
                numbersCache.add(e.getNumber());
            }
        }
        return numbersCache;
    }

    private static class IsNumber implements Predicate<UsabilityMechanism> {

        private int value;

        public IsNumber(int value) {
            this.value = value;
        }

        public boolean test(UsabilityMechanism e) {
            return e.isNumber(value);
        }

    }

    // -----------------------------------------------------------
    // - label
    // -----------------------------------------------------------

    private final Cache<java.lang.String,Set<UsabilityMechanism>> usabilityMechanismsByLabelCache = new Cache<java.lang.String,Set<UsabilityMechanism>>(
            new Cache.Factory<java.lang.String,Set<UsabilityMechanism>>() {
                public Set<UsabilityMechanism> create(java.lang.String label) {
                    return getEntities(new IsLabel(label));
                }
            });

    public final Set<UsabilityMechanism> getUsabilityMechanismsByLabel(java.lang.String label) {
        return new HashSet<UsabilityMechanism>(usabilityMechanismsByLabelCache.get(label));
    }
    private Set<java.lang.String> labelsCache;

    public final Set<java.lang.String> getLabels() {
        if (labelsCache == null) {
            labelsCache = new HashSet<java.lang.String>();
            for (UsabilityMechanism e : getEntities()) {
                if (e.isLabelSet()) labelsCache.add(e.getLabel());
            }
        }
        return labelsCache;
    }

    private static class IsLabel implements Predicate<UsabilityMechanism> {

        private java.lang.String value;

        public IsLabel(java.lang.String value) {
            this.value = value;
        }

        public boolean test(UsabilityMechanism e) {
            return e.isLabel(value);
        }

    }

    // -----------------------------------------------------------
    // - description
    // -----------------------------------------------------------

    private final Cache<java.lang.String,Set<UsabilityMechanism>> usabilityMechanismsByDescriptionCache = new Cache<java.lang.String,Set<UsabilityMechanism>>(
            new Cache.Factory<java.lang.String,Set<UsabilityMechanism>>() {
                public Set<UsabilityMechanism> create(java.lang.String description) {
                    return getEntities(new IsDescription(description));
                }
            });

    public final Set<UsabilityMechanism> getUsabilityMechanismsByDescription(java.lang.String description) {
        return new HashSet<UsabilityMechanism>(usabilityMechanismsByDescriptionCache.get(description));
    }
    private Set<java.lang.String> descriptionsCache;

    public final Set<java.lang.String> getDescriptions() {
        if (descriptionsCache == null) {
            descriptionsCache = new HashSet<java.lang.String>();
            for (UsabilityMechanism e : getEntities()) {
                if (e.isDescriptionSet()) descriptionsCache.add(e.getDescription());
            }
        }
        return descriptionsCache;
    }

    private static class IsDescription implements Predicate<UsabilityMechanism> {

        private java.lang.String value;

        public IsDescription(java.lang.String value) {
            this.value = value;
        }

        public boolean test(UsabilityMechanism e) {
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

    scrum.server.project.ProjectDao projectDao;

    public void setProjectDao(scrum.server.project.ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

}