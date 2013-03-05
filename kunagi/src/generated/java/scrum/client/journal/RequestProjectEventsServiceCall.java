// // ----------> GENERATED FILE - DON'T TOUCH! <----------

package scrum.client.journal;

public class RequestProjectEventsServiceCall extends scrum.client.core.AServiceCall {

    public  RequestProjectEventsServiceCall() {
    }

    public void execute(Runnable returnHandler) {
        serviceCaller.onServiceCall(this);
        serviceCaller.getService().requestProjectEvents(serviceCaller.getConversationNumber(), new DefaultCallback(this, returnHandler));
    }

    @Override
    public String toString() {
        return "RequestProjectEvents";
    }

}

