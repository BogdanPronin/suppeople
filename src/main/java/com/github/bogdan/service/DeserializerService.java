package com.github.bogdan.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.bogdan.exception.WebException;

import java.time.LocalDate;

public class DeserializerService {

    public static String checkNullStringFieldValue(JsonNode node, String field){
        checkForExplicitlyNullField(node.get(field),"Necessary field \""+field+ "\" can't be null");
        if(node.get(field) == null){
            throw new WebException("Necessary field \""+field+ "\" can't be null",400);
        }else if(node.get(field).asText()==""){
            throw new WebException("Necessary field \""+field+ "\" can't be null",400);
        } else return node.get(field).asText();
    }

    public static String getNullableStringFieldValue(JsonNode node, String field){
        if(node instanceof NullNode){
            return null;
        }else if(node.get(field) == null){
            return null;
        }else if(node.get(field).asText()==""){
            return null;
        } else return node.get(field).asText();
    }

    public static int getNullableIntFieldValue(JsonNode node, String field){
        if(node instanceof NullNode){
            return -1;
        }else if(node.get(field) == null){
            return -1;
        }else if(node.get(field).asText()==""){
            return -1;
        } else return node.get(field).asInt();
    }

    public static int getIntFieldValue(JsonNode node, String field){
        checkForExplicitlyNullField(node.get(field),"Necessary field \""+field+ "\" can't be null");
        if(node.get(field) == null){
            throw new WebException("Necessary field \""+field+ "\" can't be null",400);
        }else if(node.get(field).asText()==""){
            throw new WebException("Necessary field \""+field+ "\" can't be null",400);
        }else return node.get(field).asInt();
    }

    public static boolean checkNullBooleanFieldValue(JsonNode node,String field){
        checkForExplicitlyNullField(node.get(field),"Necessary field \""+field+ "\" can't be null");
        if(node.get(field) == null){
            throw new WebException("Necessary field \""+field+ "\" can't be null",400);
        }else if(node.get(field).asText()==""){
            throw new WebException("Necessary field \""+field+ "\" can't be null",400);
        } else return node.get(field).asBoolean();
    }

    public static void checkForExplicitlyNullField(JsonNode node, String exceptionMessage){
        if (node instanceof NullNode) {
            throw new WebException(exceptionMessage,400);
        }
    }
}
