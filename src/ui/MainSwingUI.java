package ui;

import extraction.ContentExtractorFactory;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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
    private JTable table;

    // Bảng màu chuẩn Academic Light Theme
    private static final Color BG_APP = new Color(243, 244, 246);       // Gray 100
    private static final Color BG_CARD = new Color(255, 255, 255);      // White
    private static final Color PRIMARY = new Color(79, 70, 229);        // Indigo 600
    private static final Color PRIMARY_HOVER = new Color(67, 56, 202);  // Indigo 700
    private static final Color SECONDARY = new Color(229, 231, 235);    // Gray 200
    private static final Color SECONDARY_HOVER = new Color(209, 213, 219); // Gray 300
    private static final Color TEXT_MAIN = new Color(17, 24, 39);       // Gray 900
    private static final Color TEXT_MUTED = new Color(107, 114, 128);   // Gray 500

    public MainSwingUI(DocumentProcessor processor) {
        this.processor = processor;
        setTitle("Hệ Thống Quản Lý Hồ Sơ Điện Tử V2.0");
        setSize(1050, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_APP);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(24, 32, 24, 32));

        // 1. Thẻ chính (Main Card) chứa toàn bộ giao diện với bóng đổ
        ModernCard mainCard = new ModernCard(16);
        mainCard.setLayout(new BorderLayout());

        // 2. Thanh công cụ (Top Bar)
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(BG_CARD);
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)),
            new EmptyBorder(16, 24, 16, 24)
        ));

        JLabel lblBrand = new JLabel("Danh sách Hồ sơ");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblBrand.setForeground(TEXT_MAIN);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        actionPanel.setBackground(BG_CARD);

        ModernButton btnSwitchDb = new ModernButton("Demo: Chuyển DB sang MySQL", SECONDARY, TEXT_MAIN, SECONDARY_HOVER);
        ModernButton btnAdd = new ModernButton("+ Khởi tạo Hồ Sơ", PRIMARY, Color.WHITE, PRIMARY_HOVER);

        btnSwitchDb.addActionListener(e -> {
            processor.setStorageRepository(new MySqlDatabaseStorageRepository());
            JOptionPane.showMessageDialog(this, "Đã chuyển chiến lược lưu trữ sang MySQL Database!",
                "Chuyển đổi Storage", JOptionPane.INFORMATION_MESSAGE);
        });

        btnAdd.addActionListener(e -> {
            new AddDocumentDialog(this, processor, null).setVisible(true);
        });

        actionPanel.add(btnSwitchDb);
        actionPanel.add(btnAdd);

        topBar.add(lblBrand, BorderLayout.WEST);
        topBar.add(actionPanel, BorderLayout.EAST);

        // 3. Bảng dữ liệu (Data Table)
        String[] cols = {"Mã Hồ Sơ", "Người Nộp", "Email", "Loại", "Định Dạng", "Trạng Thái"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        styleModernTable(table);

        // Sự kiện Double-Click để mở lại bản nháp
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    int row = table.getSelectedRow();
                    String docId = tableModel.getValueAt(row, 0).toString().replace("#", "");
                    Document selectedDoc = documentList.stream()
                        .filter(d -> d.getId().equals(docId))
                        .findFirst().orElse(null);

                    if (selectedDoc != null && "LUU_NHAP".equals(selectedDoc.getStatus())) {
                        new AddDocumentDialog(MainSwingUI.this, processor, selectedDoc).setVisible(true);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        scrollPane.getViewport().setBackground(BG_CARD);

        mainCard.add(topBar, BorderLayout.NORTH);
        mainCard.add(scrollPane, BorderLayout.CENTER);

        add(mainCard, BorderLayout.CENTER);
    }

    private void styleModernTable(JTable table) {
        table.setRowHeight(48);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(243, 244, 246));
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(238, 242, 255)); // Indigo 50
        table.setSelectionForeground(PRIMARY);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setForeground(new Color(55, 65, 81)); // Gray 700

        JTableHeader header = table.getTableHeader();
        header.setBackground(BG_CARD);
        header.setForeground(TEXT_MUTED);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 44));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(229, 231, 235)));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        // Thêm padding cho các ô
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(new EmptyBorder(0, 24, 0, 24));
                return c;
            }
        });
    }

    public void addOrUpdateDocumentInList(Document doc) {
        documentList.removeIf(d -> d.getId().equals(doc.getId()));
        documentList.add(doc);
        refreshTable();
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Document d : documentList) {
            tableModel.addRow(new Object[]{
                "#" + d.getId(), d.getApplicantName(), d.getApplicantEmail(),
                d.getDocumentType(), d.getFileExtension().toUpperCase(), d.getStatus()
            });
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Font baseFont = new Font("Segoe UI", Font.PLAIN, 14);
            UIManager.put("Label.font", baseFont);
            UIManager.put("OptionPane.messageFont", baseFont);
            UIManager.put("OptionPane.buttonFont", baseFont);
        } catch (Exception e) {}

        ContentExtractorFactory extractorFactory = new ContentExtractorFactory();
        ValidationPipeline pipeline = new ValidationPipeline();
        pipeline.addStation(new BasicFormatValidator(Set.of("txt", "pdf", "jpg", "png")));
        pipeline.addStation(new AntivirusScanValidator());
        pipeline.addStation(new DuplicateSubmissionValidator(new HashSet<>()));

        NotificationCenter notificationCenter = new NotificationCenter();
        EmailNotificationObserver emailObs = new EmailNotificationObserver();
        emailObs.setRoleSubscription(RecipientRole.APPLICANT, true);
        emailObs.setRoleSubscription(RecipientRole.OFFICER, true);

        SmsNotificationObserver smsObs = new SmsNotificationObserver();
        smsObs.setRoleSubscription(RecipientRole.APPLICANT, true);

        AppPushNotificationObserver appPushObs = new AppPushNotificationObserver();
        appPushObs.setRoleSubscription(RecipientRole.OFFICER, true);

        notificationCenter.subscribe(emailObs);
        notificationCenter.subscribe(smsObs);
        notificationCenter.subscribe(appPushObs);

        DocumentStorageRepository storage = new LocalJsonStorageRepository();
        DocumentProcessor processor = new DocumentProcessor(pipeline, extractorFactory, storage, notificationCenter);

        SwingUtilities.invokeLater(() -> new MainSwingUI(processor).setVisible(true));
    }
}

/* =========================================================================
 * CUSTOM UI COMPONENTS (Vẽ bằng Graphics2D)
 * ========================================================================= */

class ModernCard extends JPanel {
    private final int radius;

    public ModernCard(int radius) {
        this.radius = radius;
        setOpaque(false);
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(12, 12, 16, 12)); // Chừa không gian cho shadow
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ 4 lớp bóng đổ bán trong suốt giả lập độ sâu (Elevation)
        g2.setColor(new Color(0, 0, 0, 4));
        g2.fillRoundRect(4, 6, getWidth() - 8, getHeight() - 10, radius, radius);
        g2.setColor(new Color(0, 0, 0, 6));
        g2.fillRoundRect(3, 4, getWidth() - 6, getHeight() - 8, radius, radius);
        g2.setColor(new Color(0, 0, 0, 8));
        g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 6, radius, radius);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 6, radius, radius);

        // Viền siêu mỏng
        g2.setColor(new Color(229, 231, 235));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 6, radius, radius);

        g2.dispose();
        super.paintComponent(g);
    }
}

class ModernButton extends JButton {
    private final Color bgColor;
    private final Color hoverColor;
    private boolean isHovered = false;

    public ModernButton(String text, Color bgColor, Color fgColor, Color hoverColor) {
        super(text);
        this.bgColor = bgColor;
        this.hoverColor = hoverColor;

        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(fgColor);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(new EmptyBorder(10, 20, 10, 20));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { isHovered = true; repaint(); }
            @Override
            public void mouseExited(MouseEvent e) { isHovered = false; repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(isHovered ? hoverColor : bgColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        g2.dispose();
        super.paintComponent(g);
    }
}
