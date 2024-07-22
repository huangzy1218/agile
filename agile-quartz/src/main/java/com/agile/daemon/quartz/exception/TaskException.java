package com.agile.daemon.quartz.exception;

/**
 * Scheduled task exception.
 *
 * @author Huang Z.Y.
 */
public class TaskException extends Exception {

    public TaskException() {
        super();
    }

    public TaskException(String msg) {
        super(msg);
    }

}
