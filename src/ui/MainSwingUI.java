package ui;

import extraction.ContentExtractorFactory;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Document;
import notification.AppPushNotificationObserver;
import notification.EmailNotificationObserver;
import notification.NotificationCenter;
import notification.RecipientRole;
import notification.SmsNotificationObserver;
import service.DocumentProcessor;
import storage.DocumentStorageRepository;
import storage.LocalJsonStorageRepository;
import storage.MySqlDatabaseStorageRepository;
import validation.AntivirusScanValidator;
import validation.BasicFormatValidator;
import validation.DuplicateSubmissionValidator;
import validation.ValidationPipeline;

public class MainSwingUI extends JFrame {

    private final DocumentProcessor processor;
    private final List<Document> documentList = new ArrayList<>();
    private DefaultTableModel tableModel;

    public MainSwingUI(DocumentProcessor processor) {
        this.processor = processor;
        setTitle("Hệ Thống Quản Lý Hồ Sơ Điện Tử V2.0");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("Nộp Hồ Sơ / Lưu Nháp");
        JButton btnSwitchDb = new JButton("Chuyển sang MySQL");

        btnAdd.addActionListener(e -> {
            AddDocumentDialog dialog = new AddDocumentDialog(
                this,
                processor,
                null
            );
            dialog.setVisible(true);
        });

        btnSwitchDb.addActionListener(e -> {
            processor.setStorageRepository(
                new MySqlDatabaseStorageRepository()
            );
            JOptionPane.showMessageDialog(
                this,
                "Đã chuyển chiến lược lưu trữ sang MySQL Database!"
            );
        });

        topPanel.add(btnAdd);
        topPanel.add(btnSwitchDb);

        String[] cols = {
            "ID",
            "Người nộp",
            "Email",
            "Loại",
            "File",
            "Trạng thái",
        };
        tableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(tableModel);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void addOrUpdateDocumentInList(Document doc) {
        documentList.removeIf(d -> d.getId().equals(doc.getId()));
        documentList.add(doc);
        refreshTable();
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Document d : documentList) {
            tableModel.addRow(new Object[] {
                d.getId(),
                d.getApplicantName(),
                d.getApplicantEmail(),
                d.getDocumentType(),
                d.getFileExtension().toUpperCase(),
                d.getStatus(),
            });
        }
    }

    public static void main(String[] args) {
        ContentExtractorFactory extractorFactory =
            new ContentExtractorFactory();

        ValidationPipeline pipeline = new ValidationPipeline();
        pipeline.addStation(
            new BasicFormatValidator(Set.of("txt", "pdf", "jpg", "png"))
        );
        pipeline.addStation(new AntivirusScanValidator());
        pipeline.addStation(new DuplicateSubmissionValidator(new HashSet<>()));

        NotificationCenter notificationCenter = new NotificationCenter();

        EmailNotificationObserver emailObs = new EmailNotificationObserver();
        emailObs.setRoleSubscription(RecipientRole.APPLICANT, true);
        emailObs.setRoleSubscription(RecipientRole.OFFICER, true);

        SmsNotificationObserver smsObs = new SmsNotificationObserver();
        smsObs.setRoleSubscription(RecipientRole.APPLICANT, true);

        AppPushNotificationObserver appPushObs =
            new AppPushNotificationObserver();
        appPushObs.setRoleSubscription(RecipientRole.OFFICER, true);

        notificationCenter.subscribe(emailObs);
        notificationCenter.subscribe(smsObs);
        notificationCenter.subscribe(appPushObs);

        DocumentStorageRepository storage = new LocalJsonStorageRepository();

        DocumentProcessor processor = new DocumentProcessor(
            pipeline,
            extractorFactory,
            storage,
            notificationCenter
        );

        SwingUtilities.invokeLater(() ->
            new MainSwingUI(processor).setVisible(true)
        );
    }
}
