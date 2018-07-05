package io.infinite.datasync.dbhelper;


public class DbFactory
{
  public static DbHelper create(String type)
  {
     if (type.toLowerCase().equals("mysql")){
      return new MySqlHelper();
    }
    return null;
  }
}
