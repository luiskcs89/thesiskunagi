package scrum.client.sprint;

import ilarkesto.core.scope.Scope;
import scrum.client.common.TooltipBuilder;
import scrum.client.project.Requirement;
import scrum.client.project.UsabilityRecommendation;
import scrum.client.workspace.ProjectWorkspaceWidgets;

public class CreateTaskWithUsabilityRecommendationAction extends GCreateAcceptanceCriteriaAction {

	private Requirement requirement;
	private UsabilityRecommendation usr;

	public CreateTaskWithUsabilityRecommendationAction(Requirement requirement, UsabilityRecommendation usr) {
		this.requirement = requirement;
		this.usr = usr;
	}

	@Override
	public String getLabel() {
		return "Create";
	}

	@Override
	protected void updateTooltip(TooltipBuilder tb) {
		tb.setText("Create this Task.");
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
		Task task = requirement.createNewTask();
		task.setLabel(usr.getLabel());
		task.setDescription(usr.getLabel());
		requirement.addUsabilityRecommendation(usr);
		Scope.get().getComponent(ProjectWorkspaceWidgets.class).showEntity(task);
	}

}