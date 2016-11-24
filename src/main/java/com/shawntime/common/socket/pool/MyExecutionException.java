package com.shawntime.common.socket.pool;

import java.util.concurrent.ExecutionException;


/**
 * Exception thrown when attempting to retrieve the result of a task
 * that aborted by throwing an exception. This exception can be
 * inspected using the {@link #getCause()} method.
 *
 * @see Future
 * @since 1.5
 * @author Doug Lea
 */
public class MyExecutionException extends ExecutionException {
    
}
