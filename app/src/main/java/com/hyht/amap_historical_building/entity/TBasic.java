package com.hyht.amap_historical_building.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TBasic {
  private int basicId;
  private String cityType;
  private String buildingNumber;
  private String buildingName;
  private String buildingAddress;
  private String positionCoordinates;
  private String architecturalAge;
  private String buildingCategory;
  private String buildingDescription;
  private String historicalEvolution;
  private String architectName;
  private String valueElements;
  private String statusFunction;
  private String structureType;
  private String buildingFloors;
  private String buildingArea;
  private String areaCovered;
  private String statusDescription;
  private String naturalFactor;
  private String humanFactor;
  private String propertyType;
  private String propertyName;
  private String userName;
  private String propertyDescription;
}
