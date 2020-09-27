package com.hyht.amap_historical_building.entity;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class TDraw {
  private int drawId;
  private int basicId;
  private String fileName;
  private String drawProportion;
  private String drawName;
  private String drawPath;
  private String drawDate;
}
