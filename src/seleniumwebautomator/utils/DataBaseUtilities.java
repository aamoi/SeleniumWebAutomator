/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seleniumwebautomator.utils;
/**
 *
 * @author amoi
 */
import static  seleniumwebautomator.utils.CommonJavaUtilities.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * @author alexander
 */
public class DataBaseUtilities {
   
    private static Logging log=null;
    private static Props props=null;
    private static final String classname = "DataBaseUtilities | ";
    private static  final String logseparator = "| -1 | ";
    
    public DataBaseUtilities( Props properties, Logging log) {
        this.props = properties;
        this.log = log;
    }
    /**
     * gets data from mySQL database based on select query.
     * @param query the SQL select query.
     * @return data set based on query, grouped by table columns in the order 
     * as supplied in the query.
     */
   public  HashMap<Integer, ArrayList<String>> queryFromMysql(
           String db_host, String db_port,String db_name,String db_username,
           String db_password, String query){
       
       String preString=classname+"queryFromMysql()"+logseparator;
       
       log.info(preString+" fetch query:-\""+query+"\"");
       
       String[] columns=processQuery(query);
       
       //Initialise db datasets/data columns.
       HashMap<Integer, ArrayList<String>> queryResult=new HashMap<>();
       
      for(int j=1;j<=columns.length; ++j ){
           ArrayList<String> ColumnData=new ArrayList();
           queryResult.put(j, ColumnData);
        }
       //Load the MySQL driver
       try{
        Class.forName("com.mysql.jdbc.Driver").newInstance();
      
        String connectionString = "jdbc:mysql://" +db_host+":" +db_port + "/" 
                +db_name +"?user=" + db_username + "&password=" + db_password
                + "&autoReconnect=true&characterEncoding=UTF-8";  
        
        log.info(preString+"Mysql db connection string:- \""+connectionString+"\"");
        
        Connection con =DriverManager.getConnection(connectionString);
        PreparedStatement pstm=con.prepareStatement(query);
        ResultSet rrst=pstm.executeQuery();
        int size=0;
        if(rrst.last()){
            size=rrst.getRow();
            rrst.beforeFirst();
        }
        int count=0;
        while(rrst.next()){
            for(int i=0;i<columns.length;i++){
                queryResult.get(i+1).add(rrst.getString(columns[i]));
            }
        }
        log.info(preString+"fetch completed, records fetched:"+queryResult.size());
       }
       catch(SQLException se){
           log.error(preString+"An SQL Exception has occured"+se);
           log.info(preString+"System exiting...");
           System.exit(1);
       }
       catch(ClassNotFoundException cnf){
         log.error(preString+" An ClassNotFoundException has occured.Error: "+cnf); 
         log.info(preString+"System exiting...");
         System.exit(1);
       }
       catch(InstantiationException ie){
         log.error(preString+" An InstantiationException has occured.Error: "+ie); 
         log.info(preString+"System exiting...");
         System.exit(1);
           
       }
       catch(IllegalAccessException e){
         log.error(preString+" An IllegalAccessException has occured.Error: "+e);  
         log.info(preString+"System exiting...");
         System.exit(1);
       }
       catch(Exception e){
         log.error(preString+" An exception has occured.Error: "+e);  
         log.info(preString+"System exiting...");
         System.exit(1);  
       }
       return queryResult;
   }
   /**
    * filters the db query to get tables columns
    * @param query db select query 
    * @return String of table columns.
    */
   private static String[] processQuery(String query){
       //Split string & retrieve substring before "from" keyword. 
       String queryPreString=query.split(" from ")[0];
       
       //remove "select" keyword from the queryPreString.
       String columsString=queryPreString.replaceFirst("select", "");
       
       //remove white space
       columsString=columsString.trim();
       columsString=removeStringSpaces(columsString);
       
       String[] columns=columsString.split(",");
       
       
       String[] formattedColumns=new String[columns.length];
       
       for(int i=0;i<columns.length;i++){
                String tableColumn=null;
                if(columns[i].contains(".")){
                //filter table columnName from table.columnName.
                tableColumn=columns[i].substring(columns[i].indexOf(".")+1); 
                }
                else{ 
                 tableColumn=columns[i];   
                }
                formattedColumns[i]=tableColumn;
            }
       
       return formattedColumns;
   }
   /**
     * gets data from postgeres database based on select query.
     * @param db_host
     * @param db_port
     * @param db_name
     * @param db_username
     * @param db_password
     * @param query the SQL select query.
     * @return data set based on query, grouped by table columns in the order 
     * as supplied in the query.
     */
   public  static HashMap<Integer, ArrayList<String>> fetchDataFromPostgres(
           String db_host, String db_port,String db_name,String db_username,
           String db_password, String query){
       
       String preString=classname+"queryFromMysql()"+logseparator;
       
//       log.info(preString+" fetch query:-\""+query+"\"");
       
       String[] columns=processQuery(query);
       
       //Initialise db datasets/data columns.
       HashMap<Integer, ArrayList<String>> queryResult=new HashMap<>();
       
      for(int j=1;j<=columns.length; ++j ){
           ArrayList<String> ColumnData=new ArrayList();
           queryResult.put(j, ColumnData);
        }
       //Load the MySQL driver
       try{
        Class.forName("org.postgresql.Driver").newInstance();
      
        String connectionString = "jdbc:postgresql://" +db_host+":" +db_port + "/" 
                +db_name +"?user=" + db_username + "&password=" + db_password
                + "&autoReconnect=true&characterEncoding=UTF-8"; 
        
       // log.info(preString+"posgres db connection string:- \""+connectionString+"\"");
        
        Connection con =DriverManager.getConnection(connectionString);
        PreparedStatement pstm=con.prepareStatement(query);
        ResultSet rrst=pstm.executeQuery();
        
        
        int size=0;
        if(rrst.isFirst()){
            size=rrst.getRow();
            System.out.println("Size..:"+size);     
        }
        int count=0;
        while(rrst.next()){
            for(int i=0;i<columns.length;i++){
                queryResult.get(i+1).add(rrst.getString(columns[i]));
            }
        }
       // log.info(preString+"fetch completed, records fetched:"+queryResult.size());
       }
       catch(SQLException se){
          // log.error(preString+"An SQL Exception has occured"+se);
           //log.info(preString+"System exiting...");
           //System.exit(1);
                    System.err.println(se);

       }
       catch(ClassNotFoundException cnf){
        // log.error(preString+" An ClassNotFoundException has occured.Error: "+cnf); 
       //  log.info(preString+"System exiting...");
         //System.exit(1);
                  System.err.println(cnf);

       }
       catch(InstantiationException ie){
//         log.error(preString+" An InstantiationException has occured.Error: "+ie); 
//         log.info(preString+"System exiting...");
         //System.exit(1);
                  System.err.println(ie);

           
       }
       catch(IllegalAccessException e){
//         log.error(preString+" An IllegalAccessException has occured.Error: "+e);  
//         log.info(preString+"System exiting...");
        // System.exit(1);
                 System.err.println(e);

       }
       catch(Exception |Error e){
//         log.error(preString+" An exception has occured.Error: "+e);  
//         log.info(preString+"System exiting...");
         //System.exit(1); 
         System.err.println(e);
       }
       return queryResult;
   }
}
