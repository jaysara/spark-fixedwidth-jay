package com.fixedwidth.jay.x.ds.metro2;

import org.apache.spark.sql.sources.DataSourceRegister;

/**
 * Defines the "short name" for the data source
 * 
 * @author jsaraiya
 */
public class Metro2DataSourceShortnameAdvertiser
    extends Metro2DataSource
    implements DataSourceRegister {

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.spark.sql.sources.DataSourceRegister#shortName()
   */
  @Override
  public String shortName() {
    return "metro2";
  }

}
