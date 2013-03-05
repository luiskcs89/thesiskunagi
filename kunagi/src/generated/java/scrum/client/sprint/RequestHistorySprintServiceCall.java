// // ----------> GENERATED FILE - DON'T TOUCH! <----------

package scrum.client.sprint;

public class RequestHistorySprintServiceCall extends scrum.client.core.AServiceCall {

    private String sprintId;

    public  RequestHistorySprintServiceCall(String sprintId) {
        this.sprintId = sprintId;
    }

    public void execute(Runnable returnHandler) {
        serviceCaller.onServiceCall(this);
        serviceCaller.getService().requestHistorySprint(serviceCaller.getConversationNumber(), sprintId, new DefaultCallback(this, returnHandler));
    }

    @Override
    public String toString() {
        return "RequestHistorySprint";
    }

}

