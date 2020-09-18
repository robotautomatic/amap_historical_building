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
@Table(name = "t_draw")
public class TDraw {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO   ,generator = "JDBC")
  private int drawId;
  private int basicId;
  private String fileName;
  private String drawProportion;
  private String drawName;
  private String drawPath;
  @Column
  @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
  private Date drawDate;


}
