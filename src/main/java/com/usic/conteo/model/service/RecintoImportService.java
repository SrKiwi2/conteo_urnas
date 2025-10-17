package com.usic.conteo.model.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.usic.conteo.model.IService.IDistritoService;
import com.usic.conteo.model.IService.IRecintoService;
import com.usic.conteo.model.entityGeneral.Municipio;
import com.usic.conteo.model.entityGeneral.Recinto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecintoImportService {
    private final IRecintoService recintoRepository;
    private final IDistritoService municipioRepository;

    public static record ImportResult(
        int totalRows, int inserted, int updated, List<String> errors
    ) {}

    @Transactional
    public ImportResult importarExcel(MultipartFile file) throws Exception {
        List<String> errors = new ArrayList<>();
        int inserted = 0, updated = 0, total = 0;

        try (InputStream in = file.getInputStream();
             Workbook wb = WorkbookFactory.create(in)) {

            Sheet sheet = wb.getSheetAt(0);
            if (sheet == null) throw new IllegalArgumentException("El Excel no tiene hojas.");

            // --- 1) Colectar IDs de municipios usados en el Excel ---
            Set<Long> municipioIdsUsados = new HashSet<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Long mid = getLong(row, 2); // Columna C: id_municipio
                if (mid != null) municipioIdsUsados.add(mid);
            }

            // --- 2) Cargar municipios en un mapa para lookup rápido ---
            Map<Long, Municipio> municipios = municipioRepository.findByIdMunicipioIn(municipioIdsUsados)
                .stream().collect(Collectors.toMap(Municipio::getIdMunicipio, m -> m));

            // --- 3) Procesar filas (A=nombre, B=habilitados, C=id_municipio) ---
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                total++;

                try {
                    String nombre = getString(row, 0);       // A
                    String habilitados = getString(row, 1);  // B
                    Long municipioId = getLong(row, 2);      // C

                    if (nombre == null || nombre.isBlank()) {
                        errors.add("Fila " + (i+1) + ": 'nombre' vacío.");
                        continue;
                    }

                    Municipio municipio = null;
                    if (municipioId != null) {
                        municipio = municipios.get(municipioId);
                        if (municipio == null) {
                            errors.add("Fila " + (i+1) + ": id_municipio " + municipioId + " no existe.");
                            continue;
                        }
                    }

                    // Upsert por (nombre, municipio)
                    Optional<Recinto> existingOpt = (municipioId != null)
                        ? recintoRepository.findByNombreAndMunicipio_IdMunicipio(nombre.trim(), municipioId)
                        : recintoRepository.findByNombreAndMunicipio(nombre.trim(), null);

                    Recinto r = existingOpt.orElseGet(Recinto::new);
                    r.setNombre(nombre.trim());
                    r.setHabilitados(habilitados != null ? habilitados.trim() : null);
                    r.setMunicipio(municipio);
                    r.setEstado("ACTIVO"); // refuerzo

                    boolean isNew = (r.getIdRecinto() == null);
                    recintoRepository.save(r);

                    if (isNew) inserted++; else updated++;

                } catch (Exception exRow) {
                    errors.add("Fila " + (i+1) + ": " + exRow.getMessage());
                }
            }
        }

        return new ImportResult(total, inserted, updated, errors);
    }

    // Helpers robustos para leer celdas
    private String getString(Row row, int idx) {
        Cell c = row.getCell(idx);
        if (c == null) return null;
        return switch (c.getCellType()) {
            case STRING   -> c.getStringCellValue();
            case NUMERIC  -> DateUtil.isCellDateFormatted(c)
                ? c.getDateCellValue().toString()
                : normalizeNumber(c.getNumericCellValue());
            case BOOLEAN  -> String.valueOf(c.getBooleanCellValue());
            case FORMULA  -> c.getRichStringCellValue().getString();
            default       -> null;
        };
    }

    private Long getLong(Row row, int idx) {
        Cell c = row.getCell(idx);
        if (c == null) return null;
        return switch (c.getCellType()) {
            case STRING -> {
                String s = c.getStringCellValue();
                if (s == null || s.isBlank()) yield null;
                yield Long.parseLong(s.trim());
            }
            case NUMERIC -> (long) c.getNumericCellValue();
            case FORMULA -> {
                try { yield Long.parseLong(c.getRichStringCellValue().getString().trim()); }
                catch (Exception e) { yield null; }
            }
            default -> null;
        };
    }

    private String normalizeNumber(double v) {
        long lv = (long) v;
        return (v == lv) ? String.valueOf(lv) : String.valueOf(v);
    }
}
