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
package scrum.client.project;

import ilarkesto.gwt.client.AnchorPanel;
import scrum.client.common.ABlockWidget;
import scrum.client.common.AScrumAction;
import scrum.client.common.BlockHeaderWidget;
import scrum.client.common.BlockWidgetFactory;
import scrum.client.dnd.TrashSupport;
import scrum.client.journal.ActivateChangeHistoryAction;
import scrum.client.sprint.AcceptanceCriteria;
import scrum.client.sprint.Sprint;
import scrum.client.tasks.AcceptanceCriteriaWidget;

import com.google.gwt.user.client.ui.Widget;

public class AcceptanceCriteriaInHistoryBlock extends ABlockWidget<AcceptanceCriteria> implements TrashSupport {

	private Sprint sprint;
	private AnchorPanel statusIcon;

	public AcceptanceCriteriaInHistoryBlock(Sprint sprint) {
		super();
		this.sprint = sprint;
	}

	@Override
	protected void onInitializationHeader(BlockHeaderWidget header) {
		AcceptanceCriteria acc = getObject();
		header.addText(acc.getLabelModel());
		header.setDragHandle(acc.getReference());
		header.addMenuAction(new ActivateChangeHistoryAction(acc));
	}

	@Override
	protected void onUpdateHeader(BlockHeaderWidget header) {}

	@Override
	protected Widget onExtendedInitialization() {
		return new AcceptanceCriteriaWidget(getObject());
	}

	@Override
	public AScrumAction getTrashAction() {
		return null;
	}

	public static final BlockWidgetFactory<AcceptanceCriteria> createFactory(final Sprint sprint) {
		return new BlockWidgetFactory<AcceptanceCriteria>() {

			@Override
			public AcceptanceCriteriaInHistoryBlock createBlock() {
				return new AcceptanceCriteriaInHistoryBlock(sprint);
			}
		};
	}

}
