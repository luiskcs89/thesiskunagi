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

public class UsabilityMechanism extends GUsabilityMechanism implements ReferenceSupport, LabelSupport, ForumSupport,
		Comparable<UsabilityMechanism> {

	public static final String REFERENCE_PREFIX = "usm";

	public UsabilityMechanism(Project project) {
		setProject(project);
	}

	public UsabilityMechanism(Map data) {
		super(data);
	}

	@Override
	public String getReference() {
		return REFERENCE_PREFIX + getNumber();
	}

	@Override
	public String toHtml() {
		return getReference() + " " + ScrumGwt.escapeHtml(getLabel());
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
	public int compareTo(UsabilityMechanism o) {
		return Utl.compare(getNumber(), o.getNumber());
	}

	public static final Comparator<UsabilityMechanism> LABEL_COMPARATOR = new Comparator<UsabilityMechanism>() {

		@Override
		public int compare(UsabilityMechanism a, UsabilityMechanism b) {
			return Utl.compare(a.getNumber(), b.getNumber());
		}
	};

}