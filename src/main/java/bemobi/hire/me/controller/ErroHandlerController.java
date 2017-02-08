package bemobi.hire.me.controller;

import bemobi.hire.me.domain.Constants;
import bemobi.hire.me.exception.AliasAlreadyExistsException;
import bemobi.hire.me.exception.ShortenedUrlNotFoundException;
import bemobi.hire.me.response.ErrorResponse;
import com.google.gson.Gson;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created by rrodovalho on 08/02/17.
 */
@RestControllerAdvice
public class ErroHandlerController {

    @Autowired
    Gson gson;

    @ExceptionHandler(value = {AliasAlreadyExistsException.class})
    public ResponseEntity aliasAlreadyExistsException(AliasAlreadyExistsException ex){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(gson.toJson(ErrorResponse.builder()
                        .alias(ex.getMessage())
                        .errorCode(Constants.ERROR_INFO.CODE_ALIAS_ALREADY_USED)
                        .description(Constants.ERROR_INFO.DESCRIPTION_ALIAS_ALREADY_USED)
                        .build())
                );
    }

    @ExceptionHandler(value = {ShortenedUrlNotFoundException.class})
    public ResponseEntity shortenedUrlNotFoundException(ShortenedUrlNotFoundException ex){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(gson.toJson(ErrorResponse.builder()
                        .errorCode(Constants.ERROR_INFO.CODE_URL_NOT_FOUND)
                        .alias(ex.getMessage())
                        .description(Constants.ERROR_INFO.DESCRIPTION_URL_NOT_FOUND)
                        .build())
                );
    }

    @ExceptionHandler(value = {JSONException.class})
    public ResponseEntity invalidContent(JSONException ex){

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(gson.toJson(ErrorResponse.builder()
                        .errorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .alias(null)
                        .description(ex.getMessage())
                        .build())
                );

    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity internalError(Exception ex){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(gson.toJson(ErrorResponse.builder()
                        .errorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .description("Something went wrong :  " + ex.getMessage())
                        .build())
                );
    }



}
