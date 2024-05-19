package com.agile.plugin.excel.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Check error message.
 *
 * @author Huang Z.Y.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {

    /**
     * Line number.
     */
    private Long lineNum;

    /**
     * Error message.
     */
    private Set<String> errors = new HashSet<>();

    public ErrorMessage(Set<String> errors) {
        this.errors = errors;
    }

    public ErrorMessage(String error) {
        HashSet<String> objects = new HashSet<>();
        objects.add(error);
        this.errors = objects;
    }

}
    