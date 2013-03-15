package scrum.server.project;

import ilarkesto.fp.Predicate;

public class UsabilityMechanismDao extends GUsabilityMechanismDao {

	public UsabilityMechanism getUsabilityMechanismByNumber(final int number, final Project project) {
		return getEntity(new Predicate<UsabilityMechanism>() {

			@Override
			public boolean test(UsabilityMechanism t) {
				return t.isNumber(number) && t.isProject(project);
			}
		});
	}

	@Override
	public UsabilityMechanism newEntityInstance() {
		UsabilityMechanism usm = super.newEntityInstance();
		usm.setLabel("New UsabilityMechanism");
		return usm;
	}

	// --- test data ---

	public UsabilityMechanism postUsabilityMechanism(Project project, String label, String description) {
		UsabilityMechanism usm = newEntityInstance();
		usm.setProject(project);
		usm.setLabel(label);
		usm.setDescription(description);
		saveEntity(usm);
		return usm;
	}
}