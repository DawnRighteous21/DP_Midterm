package ui;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.Document;
import service.DocumentProcessor;

public class AddDocumentDialog extends JDialog {

    private final MainSwingUI parent;
    private final DocumentProcessor processor;
    private final Document.DocumentBuilder draftBuilder;
    private final Document existingDraft;

    private ModernTextField txtName, txtEmail, txtPhone, txtSig;
    private JLabel lblFile;
    private File selectedFile;

    private static final Color PRIMARY = new Color(79, 70, 229);
    private static final Color PRIMARY_HOVER = new Color(67, 56, 202);
    private static final Color SECONDARY = new Color(243, 244, 246);
    private static final Color SECONDARY_HOVER = new Color(229, 231, 235);
    private static final Color TEXT_MAIN = new Color(17, 24, 39);
    private static final Color TEXT_MUTED = new Color(107, 114, 128);
    private static final String REQ_MARK = " <font color='#DC2626'>*</font>";

    public AddDocumentDialog(MainSwingUI parent, DocumentProcessor processor, Document existingDraft) {
        super(parent, existingDraft != null ? "Chỉnh Sửa Bản Nháp" : "Khởi Tạo Hồ Sơ", true);
        this.parent = parent;
        this.processor = processor;
        this.existingDraft = existingDraft;
        this.draftBuilder = existingDraft != null ? existingDraft.toBuilder() : Document.builder();

        setSize(520, 680);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(Color.WHITE);

        setLayout(new BorderLayout());
        initComponents();

        if (existingDraft != null) {
            txtName.setText(existingDraft.getApplicantName());
            txtEmail.setText(existingDraft.getApplicantEmail());
            txtPhone.setText(existingDraft.getApplicantPhone());
            txtSig.setText(existingDraft.getDigitalSignature());
            if (existingDraft.getFilePath() != null && !existingDraft.getFilePath().isEmpty()) {
                selectedFile = new File(existingDraft.getFilePath());
                lblFile.setText(selectedFile.getName());
                lblFile.setForeground(new Color(5, 150, 105));
            }
        }
    }

    private void initComponents() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(32, 32, 16, 32));

        JLabel lblTitle = new JLabel(existingDraft != null ? "Tiếp Tục Hoàn Thiện Hồ Sơ" : "Nhập Thông Tin Hồ Sơ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(TEXT_MAIN);

        JLabel lblSub = new JLabel("<html>Vui lòng điền đầy đủ các thông tin bắt buộc (<font color='#DC2626'>*</font>) dưới đây.</html>");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(TEXT_MUTED);
        lblSub.setBorder(new EmptyBorder(8, 0, 0, 0));

        headerPanel.add(lblTitle, BorderLayout.CENTER);
        headerPanel.add(lblSub, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(0, 32, 20, 32));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        txtName = new ModernTextField();
        txtEmail = new ModernTextField();
        txtPhone = new ModernTextField();
        txtSig = new ModernTextField();

        int yPos = 0;
        yPos = addStackedField(formPanel, gbc, yPos, "<html>Họ và tên người nộp" + REQ_MARK + "</html>", txtName);
        yPos = addStackedField(formPanel, gbc, yPos, "<html>Địa chỉ Email" + REQ_MARK + "</html>", txtEmail);
        yPos = addStackedField(formPanel, gbc, yPos, "<html>Số điện thoại liên hệ" + REQ_MARK + "</html>", txtPhone);
        yPos = addStackedField(formPanel, gbc, yPos, "<html>Chữ ký số điện tử (Token)" + REQ_MARK + "</html>", txtSig);

        gbc.gridy = yPos++;
        gbc.insets = new Insets(16, 0, 8, 0);
        JLabel fileLabel = new JLabel("<html>Tài liệu đính kèm (TXT, PDF, JPG)" + REQ_MARK + "</html>");
        fileLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        fileLabel.setForeground(new Color(55, 65, 81));
        formPanel.add(fileLabel, gbc);

        gbc.gridy = yPos++;
        gbc.insets = new Insets(0, 0, 16, 0);

        JPanel fileWrapper = new JPanel(new BorderLayout(16, 0));
        fileWrapper.setBackground(Color.WHITE);

        ModernButton btnChooseFile = new ModernButton("Tải Tệp Lên", SECONDARY, TEXT_MAIN, SECONDARY_HOVER);
        lblFile = new JLabel("Chưa có tệp nào được chọn.");
        lblFile.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblFile.setForeground(new Color(156, 163, 175));

        btnChooseFile.addActionListener(e -> {
            FileDialog fd = new FileDialog(this, "Chọn tệp đính kèm", FileDialog.LOAD);
            fd.setVisible(true);
            if (fd.getFile() != null) {
                selectedFile = new File(fd.getDirectory(), fd.getFile());
                lblFile.setText(selectedFile.getName());
                lblFile.setForeground(new Color(5, 150, 105));
                lblFile.setFont(new Font("Segoe UI", Font.BOLD, 14));
            }
        });

        fileWrapper.add(btnChooseFile, BorderLayout.WEST);
        fileWrapper.add(lblFile, BorderLayout.CENTER);
        formPanel.add(fileWrapper, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 20));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(243, 244, 246)),
            new EmptyBorder(0, 16, 0, 16)
        ));

        ModernButton btnSaveDraft = new ModernButton("Lưu Nháp", SECONDARY, TEXT_MAIN, SECONDARY_HOVER);
        ModernButton btnSubmit = new ModernButton("Nộp Hồ Sơ", PRIMARY, Color.WHITE, PRIMARY_HOVER);

        btnSaveDraft.addActionListener(e -> saveDraftAction());
        btnSubmit.addActionListener(e -> submitAction());

        actionPanel.add(btnSaveDraft);
        actionPanel.add(btnSubmit);
        add(actionPanel, BorderLayout.SOUTH);
    }

    private int addStackedField(JPanel panel, GridBagConstraints gbc, int y, String labelText, ModernTextField field) {
        gbc.gridy = y++;
        gbc.insets = new Insets(12, 0, 6, 0);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(55, 65, 81));
        panel.add(label, gbc);

        gbc.gridy = y++;
        gbc.insets = new Insets(0, 0, 4, 0);
        panel.add(field, gbc);
        return y;
    }

    private void saveDraftAction() {
        draftBuilder.withApplicantInfo(txtName.getText(), txtEmail.getText(), txtPhone.getText());
        if (selectedFile != null) draftBuilder.withAttachmentFile(selectedFile);

        Document draftDoc = draftBuilder.buildDraft();
        processor.saveDraft(draftDoc);
        parent.addOrUpdateDocumentInList(draftDoc);
        dispose();
    }

    private void submitAction() {
        draftBuilder.withApplicantInfo(txtName.getText(), txtEmail.getText(), txtPhone.getText());
        if (selectedFile != null) draftBuilder.withAttachmentFile(selectedFile);
        draftBuilder.withDigitalSignature(txtSig.getText());

        Document doc = draftBuilder.buildSubmitted();
        boolean success = processor.process(doc);
        parent.addOrUpdateDocumentInList(doc);

        if (success) {
            JOptionPane.showMessageDialog(this, "Hồ sơ đã được duyệt và trích xuất thành công!",
                "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Hồ sơ bị TỪ CHỐI! Xem console để biết chi tiết lỗi.",
                "Từ chối", JOptionPane.ERROR_MESSAGE);
        }
    }
}

class ModernTextField extends JTextField {
    private Shape shape;

    public ModernTextField() {
        setOpaque(false);
        setBorder(new EmptyBorder(12, 16, 12, 16));
        setFont(new Font("Segoe UI", Font.PLAIN, 15));
        setForeground(new Color(17, 24, 39));
        setCaretColor(new Color(79, 70, 229));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isFocusOwner()) {
            g2.setColor(new Color(99, 102, 241));
            g2.setStroke(new BasicStroke(2.0f));
        } else {
            g2.setColor(new Color(209, 213, 219));
            g2.setStroke(new BasicStroke(1.0f));
        }

        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
        g2.dispose();
    }

    @Override
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
        }
        return shape.contains(x, y);
    }
}
