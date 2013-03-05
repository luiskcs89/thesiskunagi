// ----------> GENERATED FILE - DON'T TOUCH! <----------

// generator: ilarkesto.mda.legacy.generator.GwtActionGenerator










package scrum.client.sprint;

import java.util.*;

public abstract class GDeleteAcceptanceCriteriaAction
            extends scrum.client.common.AScrumAction {

    protected scrum.client.sprint.AcceptanceCriteria acceptanceCriteria;

    public GDeleteAcceptanceCriteriaAction(scrum.client.sprint.AcceptanceCriteria acceptanceCriteria) {
        this.acceptanceCriteria = acceptanceCriteria;
    }

    @Override
    public boolean isExecutable() {
        return true;
    }

    @Override
    public String getId() {
        return ilarkesto.core.base.Str.getSimpleName(getClass()) + '_' + ilarkesto.core.base.Str.toHtmlId(acceptanceCriteria);
    }

}