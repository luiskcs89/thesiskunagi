/*
 * Copyright 2011 Witoslaw Koczewsi <wi@koczewski.de>, Artjom Kochtchi
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero
 * General Public License as published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package scrum.client.tasks;

import ilarkesto.gwt.client.AnchorPanel;
import ilarkesto.gwt.client.ButtonWidget;
import ilarkesto.gwt.client.Gwt;
import scrum.client.collaboration.EmoticonsWidget;
import scrum.client.common.ABlockWidget;
import scrum.client.common.BlockHeaderWidget;
import scrum.client.common.BlockListWidget;
import scrum.client.common.BlockWidgetFactory;
import scrum.client.img.Img;
import scrum.client.journal.ActivateChangeHistoryAction;
import scrum.client.journal.ChangeHistoryWidget;
import scrum.client.project.CloseRequirementAction;
import scrum.client.project.FixRequirementAction;
import scrum.client.project.RejectRequirementAction;
import scrum.client.project.RemoveRequirementFromSprintAction;
import scrum.client.project.ReopenRequirementAction;
import scrum.client.project.Requirement;
import scrum.client.project.RequirementWidget;
import scrum.client.sprint.AcceptanceCriteria;
import scrum.client.sprint.AcceptanceCriteriaInRequirementBlock;
import scrum.client.sprint.CreateAcceptanceCriteriaAction;
import scrum.client.sprint.CreateTaskAction;
import scrum.client.sprint.RequirementWorkIndicatorBarWidget;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class RequirementInWhiteboardBlock extends ABlockWidget<Requirement> {

	private AnchorPanel statusIcon;
	private RequirementWidget requirementWidget;
	private BlockListWidget<AcceptanceCriteria> acceptanceCriteriaList;
	private ChangeHistoryWidget changeHistoryWidget;

	@Override
	protected void onInitializationHeader(BlockHeaderWidget header) {
		Requirement requirement = getObject();
		statusIcon = header.addIconWrapper();
		header.addText(requirement.getLabelModel());
		header.addText(requirement.getThemesAsStringModel(), true, false);
		header.addText(requirement.getTaskStatusLabelModel(), true);
		header.appendOuterCell(new EmoticonsWidget(requirement), null, true);
		header.appendOuterCell(new RequirementWorkIndicatorBarWidget(requirement), "150px", true);
		header.addMenuAction(new RejectRequirementAction(requirement));
		header.addMenuAction(new FixRequirementAction(requirement));
		header.addMenuAction(new CloseRequirementAction(requirement));
		header.addMenuAction(new ReopenRequirementAction(requirement));
		header.addMenuAction(new RemoveRequirementFromSprintAction(requirement));
		header.addMenuAction(new ActivateChangeHistoryAction(requirement));
		header.addMenuAction(new CreateTaskAction(requirement));
		header.addMenuAction(new CreateAcceptanceCriteriaAction(requirement));
	}

	@Override
	protected void onUpdateHeader(BlockHeaderWidget header) {
		Requirement requirement = getObject();
		header.setDragHandle(requirement.getReference());
		Image statusImage = null;
		if (requirement.isRejected()) {
			statusImage = Img.bundle.reqRejected().createImage();
			statusImage.setTitle("Rejected.");
		} else if (requirement.isClosed()) {
			statusImage = Img.bundle.reqClosed().createImage();
			statusImage.setTitle("Accepted.");
		} else if (requirement.isTasksClosed()) {
			statusImage = Img.bundle.reqTasksClosed().createImage();
			statusImage.setTitle("All tasks done.");
		} else if (requirement.isBlocked()) {
			statusImage = Img.bundle.tskBlocked().createImage();
			statusImage.setTitle("Blocked by " + requirement.getImpediment().getReferenceAndLabel() + ".");
		}
		statusIcon.setWidget(statusImage);
	}

	@Override
	protected boolean isUpdateRequired() {
		return true;
	}

	@Override
	protected Widget onExtendedInitialization() {
		Requirement requirement = getObject();

		requirementWidget = new RequirementWidget(getObject(), true, false, true, true, false, false, true);

		acceptanceCriteriaList = new BlockListWidget<AcceptanceCriteria>(AcceptanceCriteriaInRequirementBlock.FACTORY);
		acceptanceCriteriaList.setAutoSorter(requirement.getAcceptanceCriteriaOrderComparator());

		createAcceptanceCriteriaButton = new ButtonWidget(new CreateAcceptanceCriteriaAction(requirement));

		changeHistoryWidget = new ChangeHistoryWidget(requirement);

		FlowPanel panel = new FlowPanel();
		panel.add(requirementWidget);
		panel.add(acceptanceCriteriaList);
		panel.add(createAcceptanceCriteriaButton);
		panel.add(changeHistoryWidget);
		return panel;
	}

	@Override
	protected void onUpdateBody() {
		requirementWidget.update();
		acceptanceCriteriaList.setObjects(getObject().getAcceptanceCriterias());
		acceptanceCriteriaList.update();
		Gwt.update(createAcceptanceCriteriaButton);
		changeHistoryWidget.update();
	}

	public boolean selectAcceptanceCriteria(AcceptanceCriteria acceptanceCriteria) {
		return acceptanceCriteriaList.showObject(acceptanceCriteria);
	}

	public static final BlockWidgetFactory<Requirement> FACTORY = new BlockWidgetFactory<Requirement>() {

		@Override
		public RequirementInWhiteboardBlock createBlock() {
			return new RequirementInWhiteboardBlock();
		}
	};

	private ButtonWidget createAcceptanceCriteriaButton;
}
