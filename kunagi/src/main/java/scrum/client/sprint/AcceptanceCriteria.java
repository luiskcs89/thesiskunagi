package scrum.client.sprint;

import ilarkesto.core.scope.Scope;
import ilarkesto.gwt.client.EntityDoesNotExistException;
import ilarkesto.gwt.client.HyperlinkWidget;

import java.util.Map;

import scrum.client.ScrumGwt;
import scrum.client.admin.Auth;
import scrum.client.collaboration.ForumSupport;
import scrum.client.common.LabelSupport;
import scrum.client.common.ReferenceSupport;
import scrum.client.common.ShowEntityAction;
import scrum.client.project.Project;
import scrum.client.project.Requirement;
import scrum.client.tasks.WhiteboardWidget;

import com.google.gwt.user.client.ui.Widget;

public class AcceptanceCriteria extends GAcceptanceCriteria implements ReferenceSupport, LabelSupport, ForumSupport {

	public static final String REFERENCE_PREFIX = "acc";

	public Sprint getSprint() {
		return getRequirement().getSprint();
	}

	public AcceptanceCriteria(Requirement requirement) {
		setRequirement(requirement);
	}

	public AcceptanceCriteria(Map data) {
		super(data);
	}

	public boolean isInCurrentSprint() {
		return getRequirement().isInCurrentSprint();
	}

	@Override
	public void updateLocalModificationTime() {
		super.updateLocalModificationTime();
		try {
			Requirement requirement = getRequirement();
			if (requirement != null) requirement.updateLocalModificationTime();
		} catch (EntityDoesNotExistException ex) {
			return;
		}
	}

	public String getLongLabel(boolean showOwner, boolean showRequirement) {
		StringBuilder sb = new StringBuilder();
		sb.append(getLabel());
		if (showRequirement) {
			Requirement requirement = getRequirement();
			sb.append(" (").append(requirement.getReference()).append(" ").append(requirement.getLabel()).append(')');
		}
		return sb.toString();
	}

	@Override
	public String getReference() {
		return REFERENCE_PREFIX + getNumber();
	}

	@Override
	public String toHtml() {
		return ScrumGwt.toHtml(this, getLabel());
	}

	@Override
	public String toString() {
		return getReference();
	}

	public Project getProject() {
		return getRequirement().getProject();
	}

	@Override
	public boolean isEditable() {
		if (!isInCurrentSprint()) return false;
		if (!getProject().isTeamMember(Scope.get().getComponent(Auth.class).getUser())) return false;
		return true;
	}

	@Override
	public Widget createForumItemWidget() {
		return new HyperlinkWidget(new ShowEntityAction(WhiteboardWidget.class, this, getLabel()));
	}
}