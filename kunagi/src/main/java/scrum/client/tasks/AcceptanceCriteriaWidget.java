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

import ilarkesto.gwt.client.Gwt;
import ilarkesto.gwt.client.TableBuilder;
import scrum.client.ScrumGwt;
import scrum.client.collaboration.CommentsWidget;
import scrum.client.common.AScrumWidget;
import scrum.client.journal.ChangeHistoryWidget;
import scrum.client.sprint.AcceptanceCriteria;

import com.google.gwt.user.client.ui.Widget;

public class AcceptanceCriteriaWidget extends AScrumWidget {

	private AcceptanceCriteria acceptanceCriteria;

	public AcceptanceCriteriaWidget(AcceptanceCriteria acceptanceCriteria) {
		this.acceptanceCriteria = acceptanceCriteria;
	}

	@Override
	protected Widget onInitialization() {
		boolean inCurrentSprint = acceptanceCriteria.isInCurrentSprint();

		TableBuilder tb = ScrumGwt.createFieldTable();

		if (inCurrentSprint) tb.addFieldRow("Label", acceptanceCriteria.getLabelModel(), 3);

		tb.addFieldRow("Description", acceptanceCriteria.getDescriptionModel(), 3);

		if (inCurrentSprint) appendCurrentSprintFields(tb);

		Widget comments = inCurrentSprint ? ScrumGwt.createEmoticonsAndComments(acceptanceCriteria)
				: new CommentsWidget(acceptanceCriteria);
		ChangeHistoryWidget changeHistory = new ChangeHistoryWidget(acceptanceCriteria);
		return Gwt.createFlowPanel(tb.createTable(), comments, Gwt.createSpacer(1, 10), changeHistory);
	}

	private void appendCurrentSprintFields(TableBuilder tb) {}
}
