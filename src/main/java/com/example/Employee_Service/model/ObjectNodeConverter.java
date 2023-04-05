package com.example.Employee_Service.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ObjectNodeConverter implements AttributeConverter<ObjectNode, String> {
  private final static ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(ObjectNode attribute) {
    if (attribute == null) {
      return null;
    }
    try {
      return objectMapper.writeValueAsString(attribute);
    } catch (Exception e) {
      throw new IllegalArgumentException("Error converting ObjectNode to String: " + e.getMessage());
    }
  }

  @Override
  public ObjectNode convertToEntityAttribute(String dbData) {
    if (dbData == null) {
      return null;
    }
    try {
      return objectMapper.readValue(dbData, ObjectNode.class);
    } catch (Exception e) {
      throw new IllegalArgumentException("Error converting String to ObjectNode: " + e.getMessage());
    }
  }

  public static AttributeConverter<ObjectNode, String> getConverter() {
    return new ObjectNodeConverter();
  }
}
