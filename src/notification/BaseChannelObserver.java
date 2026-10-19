package notification;

import java.util.EnumSet;
import java.util.Set;
import model.Document;

public abstract class BaseChannelObserver implements DocumentObserver {

    private final Set<RecipientRole> subscribedRoles = EnumSet.noneOf(
        RecipientRole.class
    );

    public void setRoleSubscription(RecipientRole role, boolean enabled) {
        if (enabled) {
            subscribedRoles.add(role);
        } else {
            subscribedRoles.remove(role);
        }
    }

    public boolean isSubscribed(RecipientRole role) {
        return subscribedRoles.contains(role);
    }

    @Override
    public void onStatusChanged(
        Document doc,
        String oldStatus,
        String newStatus
    ) {
        if (isSubscribed(RecipientRole.APPLICANT)) {
            sendToRecipient(
                "Người nộp (" + doc.getApplicantEmail() + ")",
                doc,
                newStatus
            );
        }
        if (isSubscribed(RecipientRole.OFFICER)) {
            sendToRecipient(
                "Cán bộ xử lý (" + doc.getOfficerEmail() + ")",
                doc,
                newStatus
            );
        }
    }

    protected abstract void sendToRecipient(
        String recipientInfo,
        Document doc,
        String newStatus
    );
}
