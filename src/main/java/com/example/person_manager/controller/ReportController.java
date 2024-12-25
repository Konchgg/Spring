package com.example.person_manager.controller;

import com.example.person_manager.model.Knife;
import com.example.person_manager.service.KnifeService;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final KnifeService knifeService;

    @Autowired
    public ReportController(KnifeService knifeService) {
        this.knifeService = knifeService;
    }

    @GetMapping("/knives/pdf")
    public ResponseEntity<byte[]> generateKnivesPdfReport() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Загружаем шрифт Montserrat с поддержкой кириллицы
            PdfFont font = PdfFontFactory.createFont("src/main/resources/fonts/Montserrat-VariableFont_wght.ttf", PdfEncodings.IDENTITY_H, true);
            document.setFont(font);

            // Заголовок отчета
            document.add(new Paragraph("Отчет о ножах").setBold().setFontSize(18).setUnderline());
            document.add(new Paragraph(" "));

            // Список всех ножей
            List<Knife> knives = knifeService.getAllKnives();
            BigDecimal totalCost = BigDecimal.ZERO;
            int totalQuantity = 0;

            for (Knife knife : knives) {
                totalCost = totalCost.add(knife.getPrice().multiply(BigDecimal.valueOf(knife.getQuantity())));
                totalQuantity += knife.getQuantity();
            }

            // Общая информация
            Paragraph totalInfo = new Paragraph("Итоговая информация:").setBold().setFontSize(14);
            document.add(totalInfo);
            document.add(new Paragraph("Общее количество ножей: " + totalQuantity).setBold());
            document.add(new Paragraph("Общая стоимость всех ножей: " + totalCost).setBold());
            document.add(new Paragraph(" "));

            // Группировка ножей по категориям и производителям
            Map<String, Map<String, List<Knife>>> groupedKnives = knives.stream()
                    .collect(Collectors.groupingBy(
                            knife -> knife.getCategory() != null ? knife.getCategory().getName() : "Без категории",
                            Collectors.groupingBy(
                                    knife -> knife.getManufacturer() != null ? knife.getManufacturer().getName() : "Без производителя"
                            )
                    ));

            // Вывод данных по категориям и производителям
            for (String category : groupedKnives.keySet()) {
                document.add(new Paragraph("Категория: " + category).setBold().setFontSize(14));
                Map<String, List<Knife>> manufacturers = groupedKnives.get(category);

                for (String manufacturer : manufacturers.keySet()) {
                    document.add(new Paragraph("  Производитель: " + manufacturer).setBold().setFontSize(12));
                    List<Knife> knivesByManufacturer = manufacturers.get(manufacturer);

                    // Проверка, содержит ли список ножей
                    if (knivesByManufacturer != null && !knivesByManufacturer.isEmpty()) {
                        // Таблица для ножей
                        Table knifeTable = new Table(2);
                        knifeTable.addHeaderCell("Название ножа");
                        knifeTable.addHeaderCell("Количество");

                        for (Knife knife : knivesByManufacturer) {
                            knifeTable.addCell(knife.getName());
                            knifeTable.addCell(String.valueOf(knife.getQuantity())); // Добавляем количество ножей
                        }

                        document.add(knifeTable);
                    } else {
                        document.add(new Paragraph("    Ножи отсутствуют").setItalic());
                    }
                    document.add(new Paragraph(" "));
                }
            }

            document.close();

            // Возврат PDF-файла как ответа
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "knives_report.pdf");
            return ResponseEntity.ok().headers(headers).body(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при генерации PDF отчета", e);
        }
    }
}