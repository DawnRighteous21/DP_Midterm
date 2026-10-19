package ui;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import model.Document;
import service.DocumentProcessor;

public class AddDocumentDialog extends JDialog {

    private final MainSwingUI parent;
    private final DocumentProcessor processor;
    private final Document.DocumentBuilder draftBuilder;

    private JTextField txtName, txtEmail, txtPhone, txtSig;
    private JLabel lblFile;
    private File selectedFile;

    public AddDocumentDialog(
        MainSwingUI parent,
        DocumentProcessor processor,
        Document existingDraft
    ) {
        super(parent, "Tạo hồ sơ mới", true);
        this.parent = parent;
        this.processor = processor;

        this.draftBuilder =
            existingDraft != null
                ? existingDraft.toBuilder()
                : Document.builder();

        setSize(400, 300);
        setLocationRelativeTo(parent);
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridLayout(6, 2, 5, 5));

        add(new JLabel("Họ tên:"));
        txtName = new JTextField();
        add(txtName);

        add(new JLabel("Email (@):"));
        txtEmail = new JTextField();
        add(txtEmail);

        add(new JLabel("Số điện thoại:"));
        txtPhone = new JTextField();
        add(txtPhone);

        add(new JLabel("Chữ ký số:"));
        txtSig = new JTextField();
        add(txtSig);

        JButton btnChooseFile = new JButton("Chọn tệp (TXT, PDF, JPG)");
        lblFile = new JLabel("Chưa chọn file");
        btnChooseFile.addActionListener(e -> {
            JFileChooser jfc = new JFileChooser();
            if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedFile = jfc.getSelectedFile();
                lblFile.setText(selectedFile.getName());
            }
        });
        add(btnChooseFile);
        add(lblFile);

        JButton btnSaveDraft = new JButton("Lưu Nháp (Bỏ qua lỗi)");
        JButton btnSubmit = new JButton("Nộp Hồ Sơ");

        btnSaveDraft.addActionListener(e -> saveDraftAction());
        btnSubmit.addActionListener(e -> submitAction());

        add(btnSaveDraft);
        add(btnSubmit);
    }

    private void saveDraftAction() {
        draftBuilder.withApplicantInfo(
            txtName.getText(),
            txtEmail.getText(),
            txtPhone.getText()
        );
        if (selectedFile != null) draftBuilder.withAttachmentFile(selectedFile);

        Document draftDoc = draftBuilder.buildDraft();
        processor.saveDraft(draftDoc);

        parent.addOrUpdateDocumentInList(draftDoc);
        dispose();
    }

    private void submitAction() {
        draftBuilder.withApplicantInfo(
            txtName.getText(),
            txtEmail.getText(),
            txtPhone.getText()
        );
        if (selectedFile != null) draftBuilder.withAttachmentFile(selectedFile);
        draftBuilder.withDigitalSignature(txtSig.getText());

        Document doc = draftBuilder.buildSubmitted();

        boolean success = processor.process(doc);
        parent.addOrUpdateDocumentInList(doc);

        if (success) {
            JOptionPane.showMessageDialog(
                this,
                "Hồ sơ đã được duyệt và trích xuất thành công!"
            );
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Hồ sơ bị TỪ CHỐI! Xem console để biết chi tiết lỗi."
            );
        }
    }
}
