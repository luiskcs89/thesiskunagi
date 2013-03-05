package scrum.server.sprint;

import ilarkesto.fp.Predicate;

import java.util.Set;

import scrum.server.project.Project;

public class AcceptanceCriteriaDao extends GAcceptanceCriteriaDao {

	public Set<AcceptanceCriteria> getAcceptanceCriteriaByProject(final Project project) {
		return getEntities(new Predicate<AcceptanceCriteria>() {

			@Override
			public boolean test(AcceptanceCriteria a) {
				return a.isProject(project);
			}
		});
	}

	public AcceptanceCriteria getAcceptanceCriteriaByNumber(final int number, final Project project) {
		return getEntity(new Predicate<AcceptanceCriteria>() {

			@Override
			public boolean test(AcceptanceCriteria a) {
				return a.isNumber(number) && a.isProject(project);
			}
		});
	}

	@Override
	public AcceptanceCriteria newEntityInstance() {
		AcceptanceCriteria acceptanceCriteria = super.newEntityInstance();
		return acceptanceCriteria;
	}

	public Set<AcceptanceCriteria> getTasksBySprint(final Sprint sprint) {
		return getEntities(new Predicate<AcceptanceCriteria>() {

			@Override
			public boolean test(AcceptanceCriteria acceptanceCriteria) {
				return acceptanceCriteria.isSprint(sprint);
			}
		});
	}
}