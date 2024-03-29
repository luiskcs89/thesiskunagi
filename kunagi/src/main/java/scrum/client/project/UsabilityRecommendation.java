package scrum.client.project;

import ilarkesto.core.base.Utl;
import ilarkesto.core.scope.Scope;

import java.util.Comparator;
import java.util.Map;

import scrum.client.ScrumGwt;
import scrum.client.admin.Auth;
import scrum.client.collaboration.ForumSupport;
import scrum.client.common.LabelSupport;
import scrum.client.common.ReferenceSupport;

import com.google.gwt.user.client.ui.Widget;

public class UsabilityRecommendation extends GUsabilityRecommendation implements ReferenceSupport, LabelSupport,
		ForumSupport, Comparable<UsabilityRecommendation> {

	public static final String REFERENCE_PREFIX = "usr";

	public UsabilityRecommendation(Project project) {
		setProject(project);
	}

	public UsabilityRecommendation(Map data) {
		super(data);
	}

	@Override
	public String getReference() {
		return REFERENCE_PREFIX + getNumber();
	}

	@Override
	public String toHtml() {
		String html = "";
		if (this.isCreates()) html += "Create ";
		if (this.isModifies()) html += "Modify ";
		return html + "- " + ScrumGwt.escapeHtml(getLabel());
	}

	@Override
	public String toString() {
		return getReference() + " " + getLabel();
	}

	@Override
	public boolean isEditable() {
		if (!getProject().isProductOwner(Scope.get().getComponent(Auth.class).getUser())) return false;
		return true;
	}

	@Override
	public Widget createForumItemWidget() {
		return null;// new HyperlinkWidget(new ShowEntityAction(UsabilityMechanismBacklogWidget.class, this,
					// getLabel()));
	}

	@Override
	public int compareTo(UsabilityRecommendation o) {
		return Utl.compare(getNumber(), o.getNumber());
	}

	public static final Comparator<UsabilityRecommendation> LABEL_COMPARATOR = new Comparator<UsabilityRecommendation>() {

		@Override
		public int compare(UsabilityRecommendation a, UsabilityRecommendation b) {
			return Utl.compare(a.getNumber(), b.getNumber());
		}
	};

}