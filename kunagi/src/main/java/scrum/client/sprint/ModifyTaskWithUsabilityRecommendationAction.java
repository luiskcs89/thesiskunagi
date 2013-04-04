package scrum.client.sprint;

import ilarkesto.core.scope.Scope;
import ilarkesto.gwt.client.Gwt;
import scrum.client.common.TooltipBuilder;
import scrum.client.project.UsabilityRecommendation;
import scrum.client.workspace.ProjectWorkspaceWidgets;

public class ModifyTaskWithUsabilityRecommendationAction extends GCreateAcceptanceCriteriaAction {

	private Task task;
	private UsabilityRecommendation usr;

	public ModifyTaskWithUsabilityRecommendationAction(Task task, UsabilityRecommendation usr) {
		this.task = task;
		this.usr = usr;
	}

	@Override
	public String getLabel() {
		return task.getLabel();
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
		boolean alreadyHas = task.getRequirement().containsUsabilityRecommendation(usr);
		if (alreadyHas) {
			if (!Gwt.confirm("This Usability Recommendation was already added. Add again?")) return;
		}
		if (task.getDescription() != null)
			task.setDescription(task.getDescription() + "\n" + usr.getLabel());
		else task.setDescription(usr.getLabel());
		task.getRequirement().addUsabilityRecommendation(usr);
		Scope.get().getComponent(ProjectWorkspaceWidgets.class).showEntity(task);
	}

}