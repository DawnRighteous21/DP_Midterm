package notification;

import model.Document;

public class EmailNotificationObserver extends BaseChannelObserver {

    @Override
    public String getChannelName() {
        return "EMAIL";
    }

    @Override
    protected void sendToRecipient(
        String recipientInfo,
        Document doc,
        String newStatus
    ) {
        System.out.println(
            "  [GỬI EMAIL]    -> " +
                recipientInfo +
                ": Hồ sơ [" +
                doc.getId() +
                "] chuyển sang trạng thái " +
                newStatus
        );
    }
}
