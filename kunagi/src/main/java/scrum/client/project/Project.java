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

import ilarkesto.core.base.Str;
import ilarkesto.core.base.Utl;
import ilarkesto.core.logging.Log;
import ilarkesto.core.scope.Scope;
import ilarkesto.core.time.Date;
import ilarkesto.core.time.DateAndTime;
import ilarkesto.core.time.Time;
import ilarkesto.gwt.client.AGwtEntity;
import ilarkesto.gwt.client.Gwt;
import ilarkesto.gwt.client.HyperlinkWidget;
import ilarkesto.gwt.client.editor.AEditorModel;
import ilarkesto.gwt.client.editor.AFieldModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import scrum.client.ScrumGwt;
import scrum.client.admin.Auth;
import scrum.client.admin.ProjectUserConfig;
import scrum.client.admin.User;
import scrum.client.collaboration.Comment;
import scrum.client.collaboration.ForumSupport;
import scrum.client.collaboration.Subject;
import scrum.client.collaboration.Wikipage;
import scrum.client.common.ShowEntityAction;
import scrum.client.common.WeekdaySelector;
import scrum.client.dashboard.DashboardWidget;
import scrum.client.files.File;
import scrum.client.impediments.Impediment;
import scrum.client.issues.Issue;
import scrum.client.journal.ProjectEvent;
import scrum.client.pr.BlogEntry;
import scrum.client.release.Release;
import scrum.client.risks.Risk;
import scrum.client.sprint.AcceptanceCriteria;
import scrum.client.sprint.RequirementsOrderComparator;
import scrum.client.sprint.Sprint;
import scrum.client.sprint.SprintReport;
import scrum.client.sprint.Task;

import com.google.gwt.user.client.ui.Widget;

public class Project extends GProject implements ForumSupport {

	private static transient final Log LOG = Log.get(Project.class);

	private static final String effortUnit = "pts";
	public static final String INIT_LABEL = "New Project";

	public transient boolean historyLoaded;
	private transient RequirementsOrderComparator requirementsOrderComparator;
	private transient Comparator<Issue> issuesOrderComparator;

	public Project(User creator) {
		setLabel(INIT_LABEL + " " + DateAndTime.now());
		addParticipant(creator);
		addAdmin(creator);
		addProductOwner(creator);
		addScrumMaster(creator);
		addTeamMember(creator);
		setLastOpenedDateAndTime(DateAndTime.now());
	}

	public Project(Map data) {
		super(data);
	}

	public boolean isInHistory(Requirement requirement) {
		for (SprintReport report : getSprintReports()) {
			if (report.containsCompletedRequirement(requirement)) return true;
			if (report.containsRejectedRequirement(requirement)) return true;
		}
		return false;
	}

	public Set<SprintReport> getSprintReports() {
		Set<SprintReport> reports = new HashSet<SprintReport>();
		for (Sprint sprint : getSprints()) {
			SprintReport report = sprint.getSprintReport();
			if (report != null) reports.add(report);
		}
		return reports;
	}

	public boolean isHomepagePublishingEnabled() {
		return !Str.isBlank(getHomepageDir());
	}

	public int getTotalMisconducts() {
		int sum = 0;
		for (ProjectUserConfig config : getProjectUserConfigs()) {
			sum += config.getMisconducts();
		}
		return sum;
	}

	public Requirement getNextProductBacklogRequirement() {
		List<Requirement> requirements = getProductBacklogRequirements();
		if (requirements.isEmpty()) return null;
		Collections.sort(requirements, getRequirementsOrderComparator());
		return requirements.get(0);
	}

	public List<String> getThemes() {
		Set<String> themes = new HashSet<String>();
		for (Requirement requirement : getRequirements()) {
			if (requirement.isClosed()) continue;
			themes.addAll(requirement.getThemes());
		}
		for (Issue issue : getIssues()) {
			if (issue.isClosed()) continue;
			themes.addAll(issue.getThemes());
		}
		List<String> ret = new ArrayList<String>(themes);
		Collections.sort(ret);
		return ret;
	}

	public void updateRequirementsModificationTimes() {
		for (Requirement requirement : getRequirements()) {
			requirement.updateLocalModificationTime();
		}
	}

	@Override
	public boolean isEditable() {
		User currentUser = Scope.get().getComponent(Auth.class).getUser();
		return currentUser.isAdmin() || isAdmin(currentUser);
	}

	public List<Issue> getClaimedBugs(User user) {
		List<Issue> issues = new ArrayList<Issue>();
		for (Issue issue : getBugs()) {
			if (issue.isOwner(user) && !issue.isFixed()) issues.add(issue);
		}
		return issues;
	}

	public List<Task> getClaimedTasks(User user) {
		List<Task> tasks = new ArrayList<Task>();
		for (Requirement req : getRequirements()) {
			tasks.addAll(req.getClaimedTasks(user));
		}
		return tasks;
	}

	public Float getVelocityFromLastSprint() {
		Sprint latest = getLatestCompletedSprint();
		return latest == null ? null : latest.getVelocity();
	}

	public Sprint getLatestCompletedSprint() {
		Sprint latest = null;
		for (Sprint sprint : getSprints()) {
			if (!sprint.isCompleted()) continue;
			if (latest == null || sprint.getEnd().isAfter(latest.getEnd())) latest = sprint;
		}
		return latest;
	}

	public Set<Sprint> getCompletedSprints() {
		Set<Sprint> ret = new HashSet<Sprint>();
		for (Sprint sprint : getSprints()) {
			if (sprint.isCompleted()) ret.add(sprint);
		}
		return ret;
	}

	public List<Sprint> getCompletedSprintsInOrder() {
		return Utl.sort(getCompletedSprints(), Sprint.END_DATE_COMPARATOR);
	}

	public List<Sprint> getCompletedSprintsInReverseOrder() {
		return Utl.sort(getCompletedSprints(), Sprint.END_DATE_REVERSE_COMPARATOR);
	}

	public List<ProjectEvent> getLatestProjectEvents(int min) {
		List<ProjectEvent> events = getProjectEvents();
		Collections.sort(events, ProjectEvent.DATE_AND_TIME_COMPARATOR);

		DateAndTime deadline = new DateAndTime(Date.today().prevDay(), Time.now());
		List<ProjectEvent> ret = new ArrayList<ProjectEvent>();
		int count = 0;
		for (ProjectEvent event : events) {
			ret.add(event);
			count++;
			DateAndTime dateAndTime = event.getDateAndTime();
			if (count > min && dateAndTime.isBefore(deadline)) break;
		}
		return ret;
	}

	public Wikipage getWikipage(String name) {
		name = name.toLowerCase();
		for (Wikipage page : getDao().getWikipagesByProject(this)) {
			if (page.getName().toLowerCase().equals(name)) return page;
		}
		return null;
	}

	public ProjectUserConfig getUserConfig(User user) {
		for (ProjectUserConfig config : getDao().getProjectUserConfigsByProject(this)) {
			if (config.isUser(user)) return config;
		}
		return null;
	}

	public boolean isScrumTeamMember(User user) {
		return isProductOwner(user) || isScrumMaster(user) || isTeamMember(user);
	}

	public boolean isProductOwnerOrScrumMaster(User user) {
		return isProductOwner(user) || isScrumMaster(user);
	}

	public boolean isProductOwnerOrTeamMember(User user) {
		return isProductOwner(user) || isTeamMember(user);
	}

	public boolean isTeamMember(User user) {
		return getTeamMembers().contains(user);
	}

	public boolean isScrumMaster(User user) {
		return getScrumMasters().contains(user);
	}

	public boolean isProductOwner(User user) {
		return getProductOwners().contains(user);
	}

	public boolean isAdmin(User user) {
		return getAdmins().contains(user);
	}

	public String getUsersRolesAsString(User user, String prefix, String suffix) {
		StringBuilder sb = new StringBuilder();
		List<String> roles = new ArrayList<String>();
		if (isProductOwner(user)) roles.add("PO");
		if (isScrumMaster(user)) roles.add("SM");
		if (isTeamMember(user)) roles.add("T");
		boolean first = true;
		if (!roles.isEmpty()) {
			for (String role : roles) {
				if (first) {
					first = false;
					if (prefix != null) sb.append(prefix);
				} else {
					sb.append(",");
				}
				sb.append(role);
			}
			if (suffix != null) sb.append(suffix);
		}
		return sb.toString();
	}

	private void updateRequirementsOrder() {
		List<Requirement> requirements = getProductBacklogRequirements();
		Collections.sort(requirements, getRequirementsOrderComparator());
		updateRequirementsOrder(requirements);
	}

	public void updateRequirementsOrder(List<Requirement> requirements) {
		setRequirementsOrderIds(Gwt.getIdsAsList(requirements));
		updateRequirementsModificationTimes();
	}

	public void updateUrgentIssuesOrder(List<Issue> issues) {
		setUrgentIssuesOrderIds(Gwt.getIdsAsList(issues));
	}

	public void setParticipantsConfigured(Collection<User> users) {
		users.addAll(getTeamMembers());
		users.addAll(getAdmins());
		users.addAll(getProductOwners());
		users.addAll(getScrumMasters());
		setParticipants(users);
	}

	public String getEffortUnit() {
		return effortUnit;
	}

	public Wikipage createNewWikipage(String name) {
		Wikipage page = new Wikipage(this, name);
		getDao().createWikipage(page);
		return page;
	}

	public Subject createNewSubject() {
		Subject subject = new Subject(this);
		getDao().createSubject(subject);
		return subject;
	}

	public Issue createNewIssue() {
		Issue issue = new Issue(this);
		getDao().createIssue(issue);
		return issue;
	}

	public Impediment createNewImpediment() {
		Impediment impediment = new Impediment(this);
		getDao().createImpediment(impediment);
		return impediment;
	}

	public void deleteImpediment(Impediment impediment) {
		for (Task task : getDao().getTasksByImpediment(impediment)) {
			task.setImpediment(null);
		}
		getDao().deleteImpediment(impediment);
	}

	public void deleteFile(File file) {
		getDao().deleteFile(file);
	}

	public void deleteIssue(Issue issue) {
		getDao().deleteIssue(issue);
	}

	public void deleteRisk(Risk risk) {
		getDao().deleteRisk(risk);
	}

	public Set<ForumSupport> getEntitiesWithComments() {
		Set<ForumSupport> ret = new HashSet<ForumSupport>();
		ret.addAll(getSubjects());
		for (Comment comment : getDao().getComments()) {
			AGwtEntity entity = comment.getParent();
			if (!(entity instanceof ForumSupport)) {
				LOG.error(entity.getClass().getName() + " needs to implement ForumSupport");
				continue;
			}
			ret.add((ForumSupport) comment.getParent());
		}
		return ret;
	}

	public List<Impediment> getOpenImpediments() {
		List<Impediment> ret = new ArrayList<Impediment>();
		for (Impediment impediment : getImpediments()) {
			if (impediment.isClosed()) continue;
			ret.add(impediment);
		}
		return ret;
	}

	public List<Issue> getOpenIssues(boolean includeSuspended) {
		List<Issue> ret = new ArrayList<Issue>();
		for (Issue issue : getIssues()) {
			if (!issue.isOpen()) continue;
			if (!includeSuspended && issue.isSuspended()) continue;
			ret.add(issue);
		}
		return ret;
	}

	public List<Issue> getIdeas() {
		List<Issue> ret = new ArrayList<Issue>();
		for (Issue issue : getIssues()) {
			if (issue.isIdea()) ret.add(issue);
		}
		return ret;
	}

	public List<Issue> getUnclaimedBugs(int severity) {
		List<Issue> ret = new ArrayList<Issue>();
		for (Issue issue : getBugs()) {
			if (issue.getOwner() == null && issue.isSeverity(severity)) ret.add(issue);
		}
		return ret;
	}

	public List<Issue> getBugs() {
		List<Issue> ret = new ArrayList<Issue>();
		for (Issue issue : getIssues()) {
			if (issue.isBug()) ret.add(issue);
		}
		return ret;
	}

	public List<Issue> getFixedBugs() {
		List<Issue> ret = new ArrayList<Issue>();
		for (Issue issue : getIssues()) {
			if (issue.isBug() && issue.isFixed()) ret.add(issue);
		}
		return ret;
	}

	public List<Issue> getClosedIssues() {
		List<Issue> ret = new ArrayList<Issue>();
		for (Issue issue : getIssues()) {
			if (issue.isClosed()) ret.add(issue);
		}
		return ret;
	}

	public List<Release> getReleasedReleases() {
		List<Release> ret = new ArrayList<Release>();
		for (Release release : getReleases()) {
			if (release.isReleased()) ret.add(release);
		}
		return ret;
	}

	public List<Release> getPlannedReleases() {
		List<Release> ret = new ArrayList<Release>();
		for (Release release : getReleases()) {
			if (!release.isReleased()) ret.add(release);
		}
		return ret;
	}

	public List<Risk> getHighestRisks(int max) {
		List<Risk> ret = getRisks();
		Collections.sort(ret, Risk.PRIORITY_COMPARATOR);
		while (ret.size() > max) {
			ret.remove(ret.size() - 1);
		}
		return ret;
	}

	public Risk createNewRisk() {
		Risk risk = new Risk(this);
		getDao().createRisk(risk);
		return risk;
	}

	public BlogEntry createNewBlogEntry() {
		BlogEntry blogEntry = new BlogEntry(this);
		getDao().createBlogEntry(blogEntry);
		return blogEntry;
	}

	public BlogEntry createNewBlogEntry(Release release) {
		BlogEntry blogEntry = new BlogEntry(this);
		blogEntry.setTitle("Release " + release.getLabel());
		String text = release.isReleased() ? release.getReleaseNotes() : release.createIzemizedReleaseNotes();
		blogEntry.setText(text);
		getDao().createBlogEntry(blogEntry);
		return blogEntry;
	}

	public Release createNewRelease(Release parentRelease) {
		Release release = new Release(this);
		Date date = Date.today();
		if (parentRelease != null) {
			release.setParentRelease(parentRelease);
			release.setLabel(parentRelease.getLabel() + " Bugfix " + (parentRelease.getBugfixReleases().size() + 1));
			Date parentDate = parentRelease.getReleaseDate();
			if (parentDate != null && parentDate.isAfter(date)) date = parentDate;
		}
		release.setReleaseDate(date);
		getDao().createRelease(release);
		return release;
	}

	/**
	 * @param relative The story, before which the new story should be placed. Optional.
	 */
	public Requirement createNewRequirement(Requirement relative, boolean before, boolean split) {
		Requirement item = new Requirement(this);

		if (split) {
			String theme = relative.getLabel();
			List<String> themes = relative.getThemes();
			if (!themes.contains(theme)) themes.add(theme);

			relative.setThemes(themes);
			relative.setDirty(true);

			item.setEpic(relative);
			item.setThemes(themes);
			item.setDescription(relative.getDescription());
			item.setTestDescription(relative.getTestDescription());
			item.setQualitys(relative.getQualitys());
		}

		if (relative == null) {
			updateRequirementsOrder();
		} else {
			List<Requirement> requirements = getProductBacklogRequirements();
			requirements.remove(item);
			Collections.sort(requirements, getRequirementsOrderComparator());
			int idx = requirements.indexOf(relative);
			if (!before) idx++;
			requirements.add(idx, item);
			updateRequirementsOrder(requirements);
		}

		getDao().createRequirement(item);
		return item;
	}

	public Quality createNewQuality() {
		Quality item = new Quality(this);
		getDao().createQuality(item);
		return item;
	}

	public UsabilityMechanism createNewUsabilityMechanism() {
		UsabilityMechanism item = new UsabilityMechanism(this);
		getDao().createUsabilityMechanism(item);
		return item;
	}

	public UsabilityRecommendation createNewUsabilityRecommendation() {
		UsabilityRecommendation item = new UsabilityRecommendation(this);
		getDao().createUsabilityRecommendation(item);
		return item;
	}

	public void deleteRequirement(Requirement item) {
		getDao().deleteRequirement(item);
	}

	public void deleteQuality(Quality item) {
		getDao().deleteQuality(item);
	}

	public List<Requirement> getProductBacklogRequirements() {
		List<Requirement> ret = new ArrayList<Requirement>();
		for (Requirement requirement : getRequirements()) {
			if (requirement.isClosed()) continue;
			if (requirement.isInCurrentSprint()) continue;
			ret.add(requirement);
		}
		return ret;
	}

	public List<Task> getTasks() {
		return getDao().getTasks();
	}

	public List<AcceptanceCriteria> getAcceptanceCriterias() {
		return getDao().getAcceptanceCriterias();
	}

	public List<Issue> getIssuesByThemes(Collection<String> themes) {
		List<Issue> ret = new ArrayList<Issue>();
		for (Issue issue : getIssues()) {
			for (String theme : themes) {
				if (issue.containsTheme(theme) && !ret.contains(issue)) ret.add(issue);
			}
		}
		return ret;
	}

	public List<Requirement> getRequirementsByThemes(Collection<String> themes) {
		List<Requirement> ret = new ArrayList<Requirement>();
		for (Requirement requirement : getRequirements()) {
			for (String theme : themes) {
				if (requirement.containsTheme(theme) && !ret.contains(requirement)) ret.add(requirement);
			}
		}
		return ret;
	}

	public List<Requirement> getRequirementsOrdered() {
		List<Requirement> requirements = getRequirements();
		Collections.sort(requirements, getRequirementsOrderComparator());
		return requirements;
	}

	public Sprint createNewSprint() {
		Sprint sprint = new Sprint(this, "New Sprint");
		getDao().createSprint(sprint);
		return sprint;
	}

	public boolean deleteTask(Task task) {
		for (Requirement requirement : getRequirements()) {
			boolean b = requirement.getTasks().remove(task);
			if (b) return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return getLabel();
	}

	public Comparator<Issue> getIssuesOrderComparator() {
		if (issuesOrderComparator == null) issuesOrderComparator = new Comparator<Issue>() {

			@Override
			public int compare(Issue a, Issue b) {
				List<String> order = getUrgentIssuesOrderIds();
				int additional = order.size();
				int ia = order.indexOf(a.getId());
				if (ia < 0) {
					ia = additional;
					additional++;
				}
				int ib = order.indexOf(b.getId());
				if (ib < 0) {
					ib = additional;
					additional++;
				}
				return ia - ib;
			}
		};
		return issuesOrderComparator;
	}

	public RequirementsOrderComparator getRequirementsOrderComparator() {
		if (requirementsOrderComparator == null) requirementsOrderComparator = new RequirementsOrderComparator() {

			@Override
			protected List<String> getOrder() {
				return getRequirementsOrderIds();
			}

		};
		return requirementsOrderComparator;
	}

	public String formatEfford(Float i) {
		if (i == null || i == 0) return "nothing";
		String unit = getEffortUnit();
		if (i == 1) {
			if (unit.endsWith("s")) unit = unit.substring(0, unit.length() - 2);
			return "1 " + unit;
		}
		return i + " " + unit;
	}

	public static final Comparator<Project> LABEL_COMPARATOR = new Comparator<Project>() {

		@Override
		public int compare(Project a, Project b) {
			return a.getLabel().compareTo(b.getLabel());
		}
	};

	public static final Comparator<Project> LAST_OPENED_COMPARATOR = new Comparator<Project>() {

		@Override
		public int compare(Project a, Project b) {
			return Utl.compare(a.getLastOpenedDateAndTime(), b.getLastOpenedDateAndTime()) * -1;
		}
	};

	public Set<User> getUsersSelecting(AGwtEntity entity) {
		Set<User> users = new HashSet<User>();
		for (ProjectUserConfig config : getUserConfigs()) {
			if (config.getSelectedEntitysIds().contains(entity.getId())) users.add(config.getUser());
		}
		return users;
	}

	public List<ProjectUserConfig> getUserConfigs() {
		return getDao().getProjectUserConfigsByProject(this);
	}

	@Override
	public Widget createForumItemWidget() {
		return new HyperlinkWidget(new ShowEntityAction(DashboardWidget.class, this, "Project Dashboard"));
	}

	@Override
	public String getReference() {
		return "prj";
	}

	private transient AFieldModel<String> lastOpenedAgoModel;

	public AFieldModel<String> getLastOpenedAgoModel() {
		if (lastOpenedAgoModel == null) lastOpenedAgoModel = new AFieldModel<String>() {

			@Override
			public String getValue() {
				return ScrumGwt.getPeriodToAsShortestString("last opened ", getLastOpenedDateAndTime(), " ago");
			}
		};
		return lastOpenedAgoModel;
	}

	private transient AEditorModel<WeekdaySelector> freeDaysWeekdaySelectorModel;

	public AEditorModel<WeekdaySelector> getFreeDaysWeekdaySelectorModel() {
		if (freeDaysWeekdaySelectorModel == null) freeDaysWeekdaySelectorModel = new AEditorModel<WeekdaySelector>() {

			@Override
			public WeekdaySelector getValue() {
				return new WeekdaySelector(getFreeDays());
			}

			@Override
			public void setValue(WeekdaySelector value) {
				if (value != null && value.isSelectedAll())
					throw new RuntimeException("At least one work day required.");
				setFreeDays(value == null ? 0 : value.createBitmask());
			}

			@Override
			public String getId() {
				return "freeDaysWeekdaySelector";
			}

		};
		return freeDaysWeekdaySelectorModel;
	}

	// Affected entities: 0=Acceptance Criteria, 1=Dependent Usability Task, 2=Independent Usability Task,
	// 3=User Story

	public final int ACCEPTANCE_CRITERIA = 0;
	public final int DEPENDENT_USABILITY_TASK = 1;
	public final int INDEPENDENT_USABILITY_TASK = 2;
	public final int USER_STORY = 3;

	public void addUsabilityMechanisms() {
		if (getUsabilityMechanisms().isEmpty()) {

			// System Status
			UsabilityMechanism systemStatus = createNewUsabilityMechanism();
			systemStatus.setNumber(1);
			systemStatus.setLabel("System Status");
			systemStatus.setDescription("To inform users about the internal status of the system");

			UsabilityRecommendation systemStatus1 = createNewUsabilityRecommendation();
			systemStatus1.setUsabilityMechanism(systemStatus);
			systemStatus1.setEntityAffected(ACCEPTANCE_CRITERIA);
			systemStatus1.setCreates(false);
			systemStatus1.setModifies(true);
			systemStatus1.setLabel("Check that the confirmation message appears when the operation is succesful");

			UsabilityRecommendation systemStatus2 = createNewUsabilityRecommendation();
			systemStatus2.setUsabilityMechanism(systemStatus);
			systemStatus2.setEntityAffected(ACCEPTANCE_CRITERIA);
			systemStatus2.setCreates(true);
			systemStatus2.setModifies(false);
			systemStatus2
					.setLabel("Check that the error message appears when the operation fails. (One Acceptance Criteria for each possible failure)");

			UsabilityRecommendation systemStatus3 = createNewUsabilityRecommendation();
			systemStatus3.setUsabilityMechanism(systemStatus);
			systemStatus3.setEntityAffected(DEPENDENT_USABILITY_TASK);
			systemStatus3.setCreates(true);
			systemStatus3.setModifies(true);
			systemStatus3.setLabel("Indicate error/confirmation message in the corresponding action");

			// Warning
			UsabilityMechanism warning = createNewUsabilityMechanism();
			warning.setNumber(2);
			warning.setLabel("Warning");
			warning.setDescription("To inform users of any action with important consequences");

			UsabilityRecommendation warning1 = createNewUsabilityRecommendation();
			warning1.setNumber(1);
			warning1.setUsabilityMechanism(warning);
			warning1.setEntityAffected(ACCEPTANCE_CRITERIA);
			warning1.setCreates(false);
			warning1.setModifies(true);
			warning1.setLabel("Check the warning window emergence in the corresponding functionality");

			UsabilityRecommendation warning2 = createNewUsabilityRecommendation();
			warning2.setNumber(2);
			warning2.setUsabilityMechanism(warning);
			warning2.setEntityAffected(ACCEPTANCE_CRITERIA);
			warning2.setCreates(true);
			warning2.setModifies(false);
			warning2.setLabel("Check the obtrusive behavior of the window");

			UsabilityRecommendation warning3 = createNewUsabilityRecommendation();
			warning3.setNumber(3);
			warning3.setUsabilityMechanism(warning);
			warning3.setEntityAffected(ACCEPTANCE_CRITERIA);
			warning3.setCreates(true);
			warning3.setModifies(false);
			warning3.setLabel("Check the functionality of the “Cancel” button of the warning window");

			UsabilityRecommendation warning4 = createNewUsabilityRecommendation();
			warning4.setNumber(4);
			warning4.setUsabilityMechanism(warning);
			warning4.setEntityAffected(ACCEPTANCE_CRITERIA);
			warning4.setCreates(true);
			warning4.setModifies(false);
			warning4.setLabel("Check the functionality of the “Accept” button of the warning window");

			UsabilityRecommendation warning5 = createNewUsabilityRecommendation();
			warning5.setNumber(5);
			warning5.setUsabilityMechanism(warning);
			warning5.setEntityAffected(DEPENDENT_USABILITY_TASK);
			warning5.setCreates(true);
			warning5.setModifies(true);
			warning5.setLabel("Relate warning to the corresponding functionality");

			UsabilityRecommendation warning6 = createNewUsabilityRecommendation();
			warning6.setNumber(6);
			warning6.setUsabilityMechanism(warning);
			warning6.setEntityAffected(USER_STORY);
			warning6.setCreates(true);
			warning6.setModifies(true);
			warning6.setLabel("Implementation of warning window");

			// Long Action
			UsabilityMechanism longAction = createNewUsabilityMechanism();
			longAction.setNumber(3);
			longAction.setLabel("Long Action");
			longAction
					.setDescription("To inform users that the system is processing an action that will take some time to complete");

			UsabilityRecommendation longAction1 = createNewUsabilityRecommendation();
			longAction1.setNumber(7);
			longAction1.setUsabilityMechanism(longAction);
			longAction1.setEntityAffected(ACCEPTANCE_CRITERIA);
			longAction1.setCreates(true);
			longAction1.setModifies(false);
			longAction1.setLabel("Check the emergence of the progress window");

			UsabilityRecommendation longAction2 = createNewUsabilityRecommendation();
			longAction2.setNumber(8);
			longAction2.setUsabilityMechanism(longAction);
			longAction2.setEntityAffected(ACCEPTANCE_CRITERIA);
			longAction2.setCreates(true);
			longAction2.setModifies(false);
			longAction2.setLabel("Check the obtrusiveness of the progress window");

			UsabilityRecommendation longAction3 = createNewUsabilityRecommendation();
			longAction3.setNumber(9);
			longAction3.setUsabilityMechanism(longAction);
			longAction3.setEntityAffected(ACCEPTANCE_CRITERIA);
			longAction3.setCreates(true);
			longAction3.setModifies(false);
			longAction3.setLabel("Check that the progress window works properly");

			UsabilityRecommendation longAction4 = createNewUsabilityRecommendation();
			longAction4.setNumber(10);
			longAction4.setUsabilityMechanism(longAction);
			longAction4.setEntityAffected(ACCEPTANCE_CRITERIA);
			longAction4.setCreates(true);
			longAction4.setModifies(false);
			longAction4
					.setLabel("Check that the progress information disappear when the corresponding functionality finishes");

			UsabilityRecommendation longAction5 = createNewUsabilityRecommendation();
			longAction5.setNumber(11);
			longAction5.setUsabilityMechanism(longAction);
			longAction5.setEntityAffected(DEPENDENT_USABILITY_TASK);
			longAction5.setCreates(true);
			longAction5.setModifies(false);
			longAction5.setLabel("Relate the progress window with the corresponding functionality ");

			UsabilityRecommendation longAction6 = createNewUsabilityRecommendation();
			longAction6.setNumber(12);
			longAction6.setUsabilityMechanism(longAction);
			longAction6.setEntityAffected(USER_STORY);
			longAction6.setCreates(true);
			longAction6.setModifies(true);
			longAction6
					.setLabel("Implementation of the obstrusive progress window that shows the evolution of a long task in order to keep the user informed about the advance of the long action");

			// Long Action + Abort Command
			UsabilityMechanism longActionAbort = createNewUsabilityMechanism();
			longActionAbort.setNumber(4);
			longActionAbort.setLabel("Long Action + Abort Command");

			UsabilityRecommendation longActionAbort1 = createNewUsabilityRecommendation();
			longActionAbort1.setNumber(13);
			longActionAbort1.setUsabilityMechanism(longActionAbort);
			longActionAbort1.setEntityAffected(ACCEPTANCE_CRITERIA);
			longActionAbort1.setCreates(true);
			longActionAbort1.setModifies(false);
			longActionAbort1.setLabel("Check the emergence of the progress window");

			UsabilityRecommendation longActionAbort2 = createNewUsabilityRecommendation();
			longActionAbort2.setNumber(14);
			longActionAbort2.setUsabilityMechanism(longActionAbort);
			longActionAbort2.setEntityAffected(ACCEPTANCE_CRITERIA);
			longActionAbort2.setCreates(true);
			longActionAbort2.setModifies(false);
			longActionAbort2.setLabel("Check the obtrusiveness of the progress window");

			UsabilityRecommendation longActionAbort3 = createNewUsabilityRecommendation();
			longActionAbort3.setNumber(15);
			longActionAbort3.setUsabilityMechanism(longActionAbort);
			longActionAbort3.setEntityAffected(ACCEPTANCE_CRITERIA);
			longActionAbort3.setCreates(true);
			longActionAbort3.setModifies(false);
			longActionAbort3.setLabel("Check that the progress window works properly");

			UsabilityRecommendation longActionAbort4 = createNewUsabilityRecommendation();
			longActionAbort4.setNumber(16);
			longActionAbort4.setUsabilityMechanism(longActionAbort);
			longActionAbort4.setEntityAffected(ACCEPTANCE_CRITERIA);
			longActionAbort4.setCreates(true);
			longActionAbort4.setModifies(false);
			longActionAbort4
					.setLabel("Check that the progress information disappear when the corresponding functionality finishes");

			UsabilityRecommendation longActionAbort5 = createNewUsabilityRecommendation();
			longActionAbort5.setNumber(17);
			longActionAbort5.setUsabilityMechanism(longActionAbort);
			longActionAbort5.setEntityAffected(ACCEPTANCE_CRITERIA);
			longActionAbort5.setCreates(true);
			longActionAbort5.setModifies(false);
			longActionAbort5.setLabel("Check that the usability functionality to abort the operation works properly");

			UsabilityRecommendation longActionAbort6 = createNewUsabilityRecommendation();
			longActionAbort6.setNumber(18);
			longActionAbort6.setUsabilityMechanism(longActionAbort);
			longActionAbort6.setEntityAffected(DEPENDENT_USABILITY_TASK);
			longActionAbort6.setCreates(true);
			longActionAbort6.setModifies(false);
			longActionAbort6.setLabel("Relate the progress window with the corresponding functionality ");

			UsabilityRecommendation longActionAbort7 = createNewUsabilityRecommendation();
			longActionAbort7.setNumber(19);
			longActionAbort7.setUsabilityMechanism(longActionAbort);
			longActionAbort7.setEntityAffected(USER_STORY);
			longActionAbort7.setCreates(true);
			longActionAbort7.setModifies(true);
			longActionAbort7
					.setLabel("Implementation of the obstrusive progress window that shows the evolution of a long task and gives the option to cancel the task in order to keep the user informed about the advance of the long action and let him cancel it if he needs to");

			// Abort Operation
			UsabilityMechanism abortOpertation = createNewUsabilityMechanism();
			abortOpertation.setNumber(5);
			abortOpertation.setLabel("Abort Operation");

			UsabilityRecommendation abortOpertation1 = createNewUsabilityRecommendation();
			abortOpertation1.setNumber(20);
			abortOpertation1.setUsabilityMechanism(abortOpertation);
			abortOpertation1.setEntityAffected(ACCEPTANCE_CRITERIA);
			abortOpertation1.setCreates(true);
			abortOpertation1.setModifies(false);
			abortOpertation1.setLabel("Check that the usability functionality to abort the operation works properly");

			UsabilityRecommendation abortOpertation2 = createNewUsabilityRecommendation();
			abortOpertation2.setNumber(21);
			abortOpertation2.setUsabilityMechanism(abortOpertation);
			abortOpertation2.setEntityAffected(DEPENDENT_USABILITY_TASK);
			abortOpertation2.setCreates(true);
			abortOpertation2.setModifies(true);
			abortOpertation2.setLabel("Add the usability functionality to abort the operation (cancel button)");

			// Go Back
			UsabilityMechanism goBack = createNewUsabilityMechanism();
			goBack.setNumber(6);
			goBack.setLabel("Go back");

			UsabilityRecommendation goBack1 = createNewUsabilityRecommendation();
			goBack1.setNumber(22);
			goBack1.setUsabilityMechanism(goBack);
			goBack1.setEntityAffected(ACCEPTANCE_CRITERIA);
			goBack1.setCreates(true);
			goBack1.setModifies(false);
			goBack1.setLabel("Check that the usability functionality to go back works properly");

			UsabilityRecommendation goBack2 = createNewUsabilityRecommendation();
			goBack2.setNumber(23);
			goBack2.setUsabilityMechanism(goBack);
			goBack2.setEntityAffected(DEPENDENT_USABILITY_TASK);
			goBack2.setCreates(true);
			goBack2.setModifies(true);
			goBack2.setLabel("Add the usability functionality of go back (go back button)");

			// Text Entry
			UsabilityMechanism textEntry = createNewUsabilityMechanism();
			textEntry.setNumber(7);
			textEntry.setLabel("Text Entry");

			UsabilityRecommendation textEntry1 = createNewUsabilityRecommendation();
			textEntry1.setNumber(24);
			textEntry1.setUsabilityMechanism(textEntry);
			textEntry1.setEntityAffected(ACCEPTANCE_CRITERIA);
			textEntry1.setCreates(true);
			textEntry1.setModifies(false);
			textEntry1
					.setLabel("For each text entry field, check the emergence of the facilities that will help the user to avoid mistakes when writing");

			UsabilityRecommendation textEntry2 = createNewUsabilityRecommendation();
			textEntry2.setNumber(25);
			textEntry2.setUsabilityMechanism(textEntry);
			textEntry2.setEntityAffected(ACCEPTANCE_CRITERIA);
			textEntry2.setCreates(true);
			textEntry2.setModifies(false);
			textEntry2.setLabel("For each text entry field, check that the facility works");

			UsabilityRecommendation textEntry3 = createNewUsabilityRecommendation();
			textEntry3.setNumber(26);
			textEntry3.setUsabilityMechanism(textEntry);
			textEntry3.setEntityAffected(INDEPENDENT_USABILITY_TASK);
			textEntry3.setCreates(true);
			textEntry3.setModifies(true);
			textEntry3
					.setLabel("For each text entry field, show the facilities that will help the user to avoid mistakes when writing");

			// Undo
			UsabilityMechanism undo = createNewUsabilityMechanism();
			undo.setNumber(8);
			undo.setLabel("Undo");

			UsabilityRecommendation undo1 = createNewUsabilityRecommendation();
			undo1.setNumber(27);
			undo1.setUsabilityMechanism(undo);
			undo1.setEntityAffected(ACCEPTANCE_CRITERIA);
			undo1.setCreates(true);
			undo1.setModifies(false);
			undo1.setLabel("Check that the undo button is enabled, when there are actions in the stack");

			UsabilityRecommendation undo2 = createNewUsabilityRecommendation();
			undo2.setNumber(28);
			undo2.setUsabilityMechanism(undo);
			undo2.setEntityAffected(ACCEPTANCE_CRITERIA);
			undo2.setCreates(true);
			undo2.setModifies(false);
			undo2.setLabel("Check that the undo button is enabled, when there are no actions in the stack");

			UsabilityRecommendation undo3 = createNewUsabilityRecommendation();
			undo3.setNumber(29);
			undo3.setUsabilityMechanism(undo);
			undo3.setEntityAffected(DEPENDENT_USABILITY_TASK);
			undo3.setCreates(true);
			undo3.setModifies(true);
			undo3.setLabel("Add Undo button to the GUI");

			UsabilityRecommendation undo4 = createNewUsabilityRecommendation();
			undo4.setNumber(30);
			undo4.setUsabilityMechanism(undo);
			undo4.setEntityAffected(INDEPENDENT_USABILITY_TASK);
			undo4.setCreates(true);
			undo4.setModifies(false);
			undo4.setLabel("Update undo stack");

			UsabilityRecommendation undo5 = createNewUsabilityRecommendation();
			undo5.setNumber(31);
			undo5.setUsabilityMechanism(undo);
			undo5.setEntityAffected(USER_STORY);
			undo5.setCreates(true);
			undo5.setModifies(false);
			undo5.setLabel("Undo stack creation");

			UsabilityRecommendation undo6 = createNewUsabilityRecommendation();
			undo6.setNumber(32);
			undo6.setUsabilityMechanism(undo);
			undo6.setEntityAffected(USER_STORY);
			undo6.setCreates(true);
			undo6.setModifies(false);
			undo6.setLabel("Undo operation");

			// Undo Reset
			UsabilityMechanism undoReset = createNewUsabilityMechanism();
			undoReset.setNumber(9);
			undoReset.setLabel("Undo Reset");

			UsabilityRecommendation undoReset1 = createNewUsabilityRecommendation();
			undoReset1.setNumber(33);
			undoReset1.setUsabilityMechanism(undoReset);
			undoReset1.setEntityAffected(ACCEPTANCE_CRITERIA);
			undoReset1.setCreates(true);
			undoReset1.setModifies(false);
			undoReset1.setLabel("Check that the reset functionality is working properly");

			UsabilityRecommendation undoReset2 = createNewUsabilityRecommendation();
			undoReset2.setNumber(34);
			undoReset2.setUsabilityMechanism(undoReset);
			undoReset2.setEntityAffected(DEPENDENT_USABILITY_TASK);
			undoReset2.setCreates(true);
			undoReset2.setModifies(true);
			undoReset2.setLabel("Add the functionality to reset (reset button)");

			// Step by Step
			UsabilityMechanism stepByStep = createNewUsabilityMechanism();
			stepByStep.setNumber(10);
			stepByStep.setLabel("Step by Step");

			UsabilityRecommendation stepByStep1 = createNewUsabilityRecommendation();
			stepByStep1.setNumber(35);
			stepByStep1.setUsabilityMechanism(stepByStep);
			stepByStep1.setEntityAffected(ACCEPTANCE_CRITERIA);
			stepByStep1.setCreates(true);
			stepByStep1.setModifies(false);
			stepByStep1.setLabel("Check the transition after each step (one for each step)");

			UsabilityRecommendation stepByStep2 = createNewUsabilityRecommendation();
			stepByStep2.setNumber(36);
			stepByStep2.setUsabilityMechanism(stepByStep);
			stepByStep2.setEntityAffected(INDEPENDENT_USABILITY_TASK);
			stepByStep2.setCreates(true);
			stepByStep2.setModifies(false);
			stepByStep2.setLabel("Develop each step of the process (one task for each step)");

			// Preferences
			UsabilityMechanism preferences = createNewUsabilityMechanism();
			preferences.setNumber(11);
			preferences.setLabel("Preferences");

			UsabilityRecommendation preferences1 = createNewUsabilityRecommendation();
			preferences1.setNumber(37);
			preferences1.setUsabilityMechanism(preferences);
			preferences1.setEntityAffected(USER_STORY);
			preferences1.setCreates(true);
			preferences1.setModifies(false);
			preferences1.setLabel("As a user I want to set up the preferences");

			// Favorites
			UsabilityMechanism favorites = createNewUsabilityMechanism();
			favorites.setNumber(12);
			favorites.setLabel("Favorites");

			UsabilityRecommendation favorites1 = createNewUsabilityRecommendation();
			favorites1.setNumber(38);
			favorites1.setUsabilityMechanism(favorites);
			favorites1.setEntityAffected(ACCEPTANCE_CRITERIA);
			favorites1.setCreates(true);
			favorites1.setModifies(false);
			favorites1.setLabel("Check that the favourites button is enabled");

			UsabilityRecommendation favorites2 = createNewUsabilityRecommendation();
			favorites2.setNumber(39);
			favorites2.setUsabilityMechanism(favorites);
			favorites2.setEntityAffected(DEPENDENT_USABILITY_TASK);
			favorites2.setCreates(true);
			favorites2.setModifies(true);
			favorites2.setLabel("Add the favourites button in the GUI");

			UsabilityRecommendation favorites3 = createNewUsabilityRecommendation();
			favorites3.setNumber(40);
			favorites3.setUsabilityMechanism(favorites);
			favorites3.setEntityAffected(USER_STORY);
			favorites3.setCreates(true);
			favorites3.setModifies(false);
			favorites3.setLabel("Implementation of favourites list");

			UsabilityRecommendation favorites4 = createNewUsabilityRecommendation();
			favorites4.setNumber(41);
			favorites4.setUsabilityMechanism(favorites);
			favorites4.setEntityAffected(USER_STORY);
			favorites4.setCreates(true);
			favorites4.setModifies(false);
			favorites4.setLabel("Implementation of the use of the favourite links");

			// Help
			UsabilityMechanism help = createNewUsabilityMechanism();
			help.setNumber(13);
			help.setLabel("Help");

			UsabilityRecommendation help1 = createNewUsabilityRecommendation();
			help1.setNumber(38);
			help1.setUsabilityMechanism(help);
			help1.setEntityAffected(ACCEPTANCE_CRITERIA);
			help1.setCreates(true);
			help1.setModifies(false);
			help1.setLabel("Check that the help button is enabled");

			UsabilityRecommendation help2 = createNewUsabilityRecommendation();
			help2.setNumber(41);
			help2.setUsabilityMechanism(help);
			help2.setEntityAffected(DEPENDENT_USABILITY_TASK);
			help2.setCreates(true);
			help2.setModifies(true);
			help2.setLabel("Add Help button to the GUI");

			UsabilityRecommendation help3 = createNewUsabilityRecommendation();
			help3.setNumber(42);
			help3.setUsabilityMechanism(help);
			help3.setEntityAffected(USER_STORY);
			help3.setCreates(true);
			help3.setModifies(false);
			help3.setLabel("Implementation of the help functionality");

			UsabilityRecommendation help4 = createNewUsabilityRecommendation();
			help4.setNumber(43);
			help4.setUsabilityMechanism(help);
			help4.setEntityAffected(USER_STORY);
			help4.setCreates(true);
			help4.setModifies(false);
			help4.setLabel("Implementation of the use of the help functionality");
		}
	}

	public Requirement requirementWithLabel(String label) {
		for (Requirement req : getRequirements()) {
			if (req.isLabel(label)) return req;
		}
		return null;
	}
}
