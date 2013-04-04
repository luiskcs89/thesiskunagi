package scrum.client.sprint;

import ilarkesto.core.scope.Scope;
import scrum.client.common.TooltipBuilder;
import scrum.client.project.UsabilityRecommendation;
import scrum.client.workspace.ProjectWorkspaceWidgets;

public class ModifyAcceptanceCriteriaWithUsabilityRecommendationAction extends GCreateAcceptanceCriteriaAction {

	private AcceptanceCriteria acceptanceCriteria;
	private UsabilityRecommendation usr;

	public ModifyAcceptanceCriteriaWithUsabilityRecommendationAction(AcceptanceCriteria acceptanceCriteria,
			UsabilityRecommendation usr) {
		this.acceptanceCriteria = acceptanceCriteria;
		this.usr = usr;
	}

	@Override
	public String getLabel() {
		return acceptanceCriteria.getLabel();
	}

	@Override
	protected void updateTooltip(TooltipBuilder tb) {
		// tb.setText("Create this Acceptance Criteria.");
		// if (!getCurrentProject().isTeamMember(getCurrentUser())) tb.addRemark(TooltipBuilder.NOT_TEAM);
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
		if (acceptanceCriteria.getDescription() != null)
			acceptanceCriteria.setDescription(acceptanceCriteria.getDescription() + "\n" + usr.getLabel());
		else acceptanceCriteria.setDescription(usr.getLabel());
		acceptanceCriteria.getRequirement().addUsabilityRecommendation(usr);
		Scope.get().getComponent(ProjectWorkspaceWidgets.class).showEntity(acceptanceCriteria);
	}

}