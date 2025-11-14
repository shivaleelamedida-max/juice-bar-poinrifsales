package projectjuice;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BillGeneratorFrame {

    public static void generateBill(String customerName, DefaultTableModel tableModel) {
        try {
            // ✅ Step 1: Create Desktop folder if not exists
            String userHome = System.getProperty("user.home");
            File billFolder = new File(userHome + "\\Desktop\\JuiceBar_Bills");
            if (!billFolder.exists()) {
                billFolder.mkdir();
            }

            // ✅ Step 2: Unique file name using timestamp
            String fileName = "Bill_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
            String filePath = billFolder.getAbsolutePath() + "\\" + fileName;

            // ✅ Step 3: Create Document
            Document document = new Document(PageSize.A4, 36, 36, 80, 36);
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // ✅ Step 4: Add Logo (optional)
            try {
                Image logo = Image.getInstance("C:\\Users\\home\\Desktop\\cjtts\\programs\\eclipseprograms\\juicebar\\assets\\logo.png");
                logo.scaleToFit(100, 100);
                logo.setAlignment(Element.ALIGN_CENTER);
                document.add(logo);
            } catch (Exception ex) {
                System.out.println("⚠️ Logo not found. Skipping logo step...");
            }

            // ✅ Step 5: Add Header
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 28, Font.BOLD, BaseColor.ORANGE);
            Font subFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.DARK_GRAY);
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);

            Paragraph header = new Paragraph("🍹 Shiva Juice Bar 🍹", titleFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            Paragraph subHeader = new Paragraph("Fresh Juices & Smoothies\nContact: +91 98765 43210\n\n", subFont);
            subHeader.setAlignment(Element.ALIGN_CENTER);
            document.add(subHeader);

            document.add(new Paragraph("Customer Name: " + customerName, boldFont));
            document.add(new Paragraph("Date: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()) + "\n\n", subFont));

            // ✅ Step 6: Create Table
            PdfPTable pdfTable = new PdfPTable(4);
            pdfTable.setWidthPercentage(100);
            pdfTable.setSpacingBefore(10f);
            pdfTable.setWidths(new int[]{3, 1, 1, 2});

            // Table headers
            String[] headers = {"Juice Name", "Size", "Quantity", "Price (₹)"};
            for (String col : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(col, new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.WHITE)));
                headerCell.setBackgroundColor(BaseColor.ORANGE);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setPadding(8);
                pdfTable.addCell(headerCell);
            }

            double grandTotal = 0;

            // ✅ Fill table rows
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String juice = safeGet(tableModel, i, 0);
                String size = safeGet(tableModel, i, 1);
                String qty = safeGet(tableModel, i, 2);
                String price = safeGet(tableModel, i, 3);

                pdfTable.addCell(new Phrase(juice));
                pdfTable.addCell(new Phrase(size));
                pdfTable.addCell(new Phrase(qty));
                pdfTable.addCell(new Phrase(price));

                try {
                    grandTotal += Double.parseDouble(price);
                } catch (NumberFormatException ex) {
                    System.out.println("⚠️ Invalid price for " + juice + ": " + price);
                }
            }

            document.add(pdfTable);

            // ✅ Step 7: Add Total and Footer
            Paragraph totalPara = new Paragraph("\nTotal Amount: ₹" + String.format("%.2f", grandTotal),
                    new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.BLACK));
            totalPara.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalPara);

            Paragraph thanks = new Paragraph("\nThank you for visiting Shiva Juice Bar!",
                    new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC, BaseColor.GRAY));
            thanks.setAlignment(Element.ALIGN_CENTER);
            document.add(thanks);

            document.close();

            // ✅ Step 8: Show confirmation
            JOptionPane.showMessageDialog(null, "✅ Bill saved successfully at:\n" + filePath);

            // ✅ Step 9: Open PDF automatically if supported
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(new File(filePath));
                } else {
                    JOptionPane.showMessageDialog(null, "Bill generated but auto-open not supported on this system.");
                }
            } catch (Exception ex) {
                System.out.println("⚠️ Could not open PDF automatically.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ Error generating bill: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ✅ Safe getter for table cells
    private static String safeGet(DefaultTableModel model, int row, int col) {
        Object val = model.getValueAt(row, col);
        return (val == null) ? "" : val.toString();
    }
}
