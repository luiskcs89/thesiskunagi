package scrum.client.sprint;

import ilarkesto.core.scope.Scope;
import scrum.client.common.TooltipBuilder;
import scrum.client.project.Requirement;
import scrum.client.workspace.ProjectWorkspaceWidgets;

public class CreateAcceptanceCriteriaAction extends GCreateAcceptanceCriteriaAction {

	private Requirement requirement;

	public CreateAcceptanceCriteriaAction(Requirement requirement) {
		this.requirement = requirement;
	}

	@Override
	public String getLabel() {
		return "Create Acceptance Criteria";
	}

	@Override
	protected void updateTooltip(TooltipBuilder tb) {
		tb.setText("Create a new Acceptance Criteria for this Story.");
		if (!getCurrentProject().isTeamMember(getCurrentUser())) tb.addRemark(TooltipBuilder.NOT_TEAM);
	}

	@Override
	public boolean isExecutable() {
		return true;
	}

	@Override
	public boolean isPermitted() {
		if (!getCurrentProject().isTeamMember(getCurrentUser())) return false;
		return true;
	}

	@Override
	protected void onExecute() {
		AcceptanceCriteria acceptanceCriteria = requirement.createNewAcceptanceCriteria();
		Scope.get().getComponent(ProjectWorkspaceWidgets.class).showEntity(acceptanceCriteria);
	}

}