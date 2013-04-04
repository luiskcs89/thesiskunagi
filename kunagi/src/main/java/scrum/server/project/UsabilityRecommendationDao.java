package scrum.server.project;

import ilarkesto.fp.Predicate;

public class UsabilityRecommendationDao extends GUsabilityRecommendationDao {

	public UsabilityRecommendation getUsabilityRecommendationByNumber(final int number, final Project project) {
		return getEntity(new Predicate<UsabilityRecommendation>() {

			@Override
			public boolean test(UsabilityRecommendation t) {
				return t.isNumber(number) && t.isProject(project);
			}
		});
	}

	@Override
	public UsabilityRecommendation newEntityInstance() {
		UsabilityRecommendation usr = super.newEntityInstance();
		usr.setLabel("New UsabilityRecommendation");
		return usr;
	}

	// --- test data ---

	public UsabilityRecommendation postUsabilityRecommendation(Project project, String label, String description,
			boolean creates, boolean modifies, int entityAffected, UsabilityMechanism usm) {
		UsabilityRecommendation usr = newEntityInstance();
		usr.setProject(project);
		usr.setLabel(label);
		usr.setCreates(creates);
		usr.setEntityAffected(entityAffected);
		usr.setModifies(modifies);
		usr.setUsabilityMechanism(usm);
		saveEntity(usr);
		return usr;
	}
}