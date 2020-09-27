package com.hyht.amap_historical_building.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TImage {
  private int imageId;
  private int basicId;
  private String imageName;
  private String photoName;
  private String imagePath;
  private String imageDate;
}
