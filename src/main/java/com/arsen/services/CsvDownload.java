package com.arsen.services;

import com.arsen.models.Order;
import com.opencsv.CSVWriter;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class CsvDownload {

    public ResponseEntity<byte[]> download(List<Order> orders) {
        StringWriter stringWriter = new StringWriter();
        try (CSVWriter csvWriter = new CSVWriter(stringWriter)) {
            csvWriter.writeNext(new String[]{"ID", "Дата", "Статус", "Имя пользователя",
                    "Наименование товара", "Описание товара", "Стоимость"});
            for (Order order : orders) {
                csvWriter.writeNext(new String[]{
                        order.getId().toString(),
                        order.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        order.getStatus().toString(),
                        order.getUser().getFullName(),
                        order.getProduct().getName(),
                        order.getProduct().getDescription(),
                        order.getProduct().getPrice().toString()
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return convertToByteArray(stringWriter);
    }

    public ResponseEntity<byte[]> convertToByteArray(StringWriter stringWriter) {
        byte[] csvData = stringWriter.toString().getBytes(StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment().filename("orders.csv").build());
        return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
    }

}
