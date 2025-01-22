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
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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

    // Генерация PDF отчета по ножам
    @GetMapping("/knives/pdf")
    public ResponseEntity<byte[]> generateKnivesPdfReport() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Загружаем шрифт Montserrat с поддержкой кириллицы
            PdfFont font = PdfFontFactory.createFont("src/main/resources/fonts/Montserrat-VariableFont_wght.ttf", PdfEncodings.IDENTITY_H, true);
            document.setFont(font);

            // Добавляем заголовок отчета
            Paragraph title = new Paragraph("Отчет о ножах").setBold().setFontSize(20).setUnderline();
            title.setTextAlignment(TextAlignment.CENTER);
            document.add(title);
            document.add(new Paragraph(" ").setTextAlignment(TextAlignment.CENTER));

            // Получаем список всех ножей
            List<Knife> knives = knifeService.getAllKnives();
            BigDecimal totalCost = BigDecimal.ZERO;
            int totalQuantity = 0;

            // Рассчитываем общие показатели
            for (Knife knife : knives) {
                totalCost = totalCost.add(knife.getPrice().multiply(BigDecimal.valueOf(knife.getQuantity())));
                totalQuantity += knife.getQuantity();
            }

            // Отображаем общую информацию
            document.add(new Paragraph("Итоговая информация:").setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Общее количество ножей: " + totalQuantity).setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Общая стоимость всех ножей: " + formatBigDecimal(totalCost)).setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(" ").setTextAlignment(TextAlignment.CENTER));

            // Группируем ножи по категориям и производителям
            Map<String, Map<String, List<Knife>>> groupedKnives = knives.stream()
                    .collect(Collectors.groupingBy(
                            knife -> knife.getCategory() != null ? knife.getCategory().getName() : "Без категории",
                            Collectors.groupingBy(
                                    knife -> knife.getManufacturer() != null ? knife.getManufacturer().getName() : "Без производителя"
                            )
                    ));

            // Вывод данных по категориям и производителям
            for (String category : groupedKnives.keySet()) {
                document.add(new Paragraph("Категория: " + category).setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
                Map<String, List<Knife>> manufacturers = groupedKnives.get(category);

                for (String manufacturer : manufacturers.keySet()) {
                    document.add(new Paragraph("  Производитель: " + manufacturer).setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
                    List<Knife> knivesByManufacturer = manufacturers.get(manufacturer);

                    // Проверяем, есть ли ножи у производителя
                    if (knivesByManufacturer != null && !knivesByManufacturer.isEmpty()) {
                        // Создаем таблицу для ножей
                        Table knifeTable = new Table(5); // Изменено на 5 колонок
                        knifeTable.addHeaderCell("Название ножа").setTextAlignment(TextAlignment.CENTER);
                        knifeTable.addHeaderCell("Количество").setTextAlignment(TextAlignment.CENTER);
                        knifeTable.addHeaderCell("Цена за штуку").setTextAlignment(TextAlignment.CENTER);
                        knifeTable.addHeaderCell("Общая стоимость").setTextAlignment(TextAlignment.CENTER);
                        knifeTable.addHeaderCell("Дата производства").setTextAlignment(TextAlignment.CENTER); // Новая колонка

                        // Заполняем таблицу данными о ножах
                        for (Knife knife : knivesByManufacturer) {
                            knifeTable.addCell(knife.getName()).setTextAlignment(TextAlignment.CENTER);
                            knifeTable.addCell(String.valueOf(knife.getQuantity())).setTextAlignment(TextAlignment.CENTER);
                            knifeTable.addCell(formatBigDecimal(knife.getPrice())).setTextAlignment(TextAlignment.CENTER);
                            knifeTable.addCell(formatBigDecimal(knife.getPrice().multiply(BigDecimal.valueOf(knife.getQuantity())))).setTextAlignment(TextAlignment.CENTER);
                            knifeTable.addCell(knife.getManufactureDate().toString()).setTextAlignment(TextAlignment.CENTER); // Дата производства
                        }

                        document.add(knifeTable); // Добавляем таблицу в документ
                    } else {
                        document.add(new Paragraph("    Ножи отсутствуют").setItalic().setTextAlignment(TextAlignment.CENTER));
                    }
                    document.add(new Paragraph(" ").setTextAlignment(TextAlignment.CENTER));
                }
            }

            document.close();

            // Отправляем PDF в ответе
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "knives_report.pdf");
            return ResponseEntity.ok().headers(headers).body(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при генерации PDF отчета", e);
        }
    }

    // Метод для форматирования BigDecimal
    private String formatBigDecimal(BigDecimal bigDecimal) {
        DecimalFormat df = new DecimalFormat("#,###.00"); // Форматирование с двумя знаками после запятой
        return df.format(bigDecimal);
    }
}
