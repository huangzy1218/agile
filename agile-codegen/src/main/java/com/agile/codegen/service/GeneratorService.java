package com.agile.codegen.service;

import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * @author Huang Z.Y.
 */
public interface GeneratorService {

    /**
     * Generate code and write to zip output stream.
     *
     * @param tableId Table ID.
     * @param zip     Output stream.
     */
    void downloadCode(Long tableId, ZipOutputStream zip);

    /**
     * Preview code.
     *
     * @param tableId Table ID.
     * @return List of maps containing template names and rendered results.
     */
    List<Map<String, String>> preview(Long tableId);

    /**
     * Write rendered results to target directory.
     *
     * @param tableId Table ID.
     */
    void generatorCode(Long tableId);

}

