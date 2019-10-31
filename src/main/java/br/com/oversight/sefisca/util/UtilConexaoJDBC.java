package br.com.oversight.sefisca.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import br.com.ambientinformatica.util.UtilLog;

public abstract class UtilConexaoJDBC {

   public static Connection getConnection() {
      DataSource ds = null;
      try {
         InitialContext ctx = new InitialContext();
         ds = (DataSource) ctx.lookup("java:comp/env/jdbc/sefisca");
         return ds.getConnection();
      } catch (NamingException e) {
         UtilLog.getLog().error(e.getMessage(), e);
      } catch (SQLException e) {
         UtilLog.getLog().error(e.getMessage(), e);
      }
      return null;
   }
}