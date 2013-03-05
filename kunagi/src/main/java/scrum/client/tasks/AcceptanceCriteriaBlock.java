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
import scrum.client.collaboration.EmoticonsWidget;
import scrum.client.common.ABlockWidget;
import scrum.client.common.AScrumAction;
import scrum.client.common.BlockHeaderWidget;
import scrum.client.common.BlockWidgetFactory;
import scrum.client.dnd.TrashSupport;
import scrum.client.journal.ActivateChangeHistoryAction;
import scrum.client.sprint.AcceptanceCriteria;
import scrum.client.sprint.DeleteAcceptanceCriteriaAction;

import com.google.gwt.user.client.ui.Widget;

public class AcceptanceCriteriaBlock extends ABlockWidget<AcceptanceCriteria> implements TrashSupport {

	private AnchorPanel statusIcon;

	private AcceptanceCriteriaBlockContainer container;

	public AcceptanceCriteriaBlock(AcceptanceCriteriaBlockContainer container) {
		this.container = container;
	}

	@Override
	protected void onInitializationHeader(BlockHeaderWidget header) {
		AcceptanceCriteria acceptanceCriteria = getObject();
		statusIcon = header.addIconWrapper();
		header.addText(acceptanceCriteria.getLabelModel());
		header.appendOuterCell(new EmoticonsWidget(acceptanceCriteria), null, true);
		header.addMenuAction(new DeleteAcceptanceCriteriaAction(acceptanceCriteria));
		header.addMenuAction(new ActivateChangeHistoryAction(acceptanceCriteria));
	}

	@Override
	protected void onUpdateHeader(BlockHeaderWidget header) {
		AcceptanceCriteria acceptanceCriteria = getObject();
		header.setDragHandle(acceptanceCriteria.getReference());
	}

	@Override
	protected Widget onExtendedInitialization() {
		return new AcceptanceCriteriaWidget(getObject());
	}

	@Override
	public AScrumAction getTrashAction() {
		return new DeleteAcceptanceCriteriaAction(getObject());
	}

	public AcceptanceCriteriaBlockContainer getContainer() {
		return this.container;
	}

	public static class AcceptanceCriteriaBlockFactory implements BlockWidgetFactory<AcceptanceCriteria> {

		private AcceptanceCriteriaBlockContainer container;

		public AcceptanceCriteriaBlockFactory(AcceptanceCriteriaBlockContainer container) {
			this.container = container;
		}

		@Override
		public ABlockWidget<AcceptanceCriteria> createBlock() {
			return new AcceptanceCriteriaBlock(container);
		}
	}
}
