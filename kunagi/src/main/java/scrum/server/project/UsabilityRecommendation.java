package scrum.server.project;

import scrum.client.common.LabelSupport;
import scrum.client.common.ReferenceSupport;
import scrum.server.admin.User;
import scrum.server.common.Numbered;

public class UsabilityRecommendation extends GUsabilityRecommendation implements Numbered, ReferenceSupport,
		LabelSupport {

	public String getReferenceAndLabel() {
		return getReference() + " " + getLabel();
	}

	@Override
	public String getReference() {
		return scrum.client.project.UsabilityRecommendation.REFERENCE_PREFIX + getNumber();
	}

	@Override
	public void updateNumber() {
		if (getNumber() == 0) setNumber(getProject().generateUsabilityRecommendationNumber());
	}

	@Override
	public boolean isVisibleFor(User user) {
		return getProject().isVisibleFor(user);
	}

	public boolean isEditableBy(User user) {
		return getProject().isEditableBy(user);
	}

	@Override
	public void ensureIntegrity() {
		super.ensureIntegrity();
		updateNumber();
	}

	@Override
	public String toString() {
		return getReferenceAndLabel();
	}
}