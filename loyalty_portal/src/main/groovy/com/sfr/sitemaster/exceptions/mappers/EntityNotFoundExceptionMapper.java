package com.sfr.sitemaster.exceptions.mappers;

import com.sfr.apicore.exceptions.pojo.ErrorMessage;
import com.sfr.sitemaster.exceptions.EntityNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by piotr on 26/01/16.
 */
public class EntityNotFoundExceptionMapper implements ExceptionMapper<EntityNotFoundException> {

    public Response toResponse(final EntityNotFoundException exception) {
        final int statusCode = 404;
        final StringWriter errorStackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(errorStackTrace));
        final ErrorMessage errorMessage = new ErrorMessage(statusCode, exception.getMessage(), errorStackTrace.toString());
        return Response.status(statusCode).entity(errorMessage).type("application/json").build();
    }

}
