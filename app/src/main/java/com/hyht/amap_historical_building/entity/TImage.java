package com.hyht.amap_historical_building.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "t_image")
public class TImage {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO   ,generator = "JDBC")
  private int imageId;
  private int basicId;
  private String imageName;
  private String photoName;
  private String imagePath;
  @Column
  @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
  private Date imageDate;

}
