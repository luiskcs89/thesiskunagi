package scrum.client.sprint;

import scrum.client.common.TooltipBuilder;

public class DeleteAcceptanceCriteriaAction extends GDeleteAcceptanceCriteriaAction {

	public DeleteAcceptanceCriteriaAction(AcceptanceCriteria acceptanceCriteria) {
		super(acceptanceCriteria);
	}

	@Override
	public String getLabel() {
		return "Delete";
	}

	@Override
	protected void updateTooltip(TooltipBuilder tb) {
		tb.setText("Delete this Acceptance Criteria permanently.");
		if (!getCurrentProject().isTeamMember(getCurrentUser())) {
			tb.addRemark(TooltipBuilder.NOT_TEAM);
		}
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
		acceptanceCriteria.getRequirement().deleteAcceptanceCriteria(acceptanceCriteria);
		addUndo(new Undo());
	}

	class Undo extends ALocalUndo {

		@Override
		public String getLabel() {
			return "Undo Delete " + acceptanceCriteria.getReference() + " " + acceptanceCriteria.getLabel();
		}

		@Override
		protected void onUndo() {
			getDao().createAcceptanceCriteria(acceptanceCriteria);
		}

	}

}