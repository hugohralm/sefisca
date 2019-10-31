package br.com.oversight.sefisca.controle;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import br.com.ambientinformatica.util.UtilLog;
import br.com.oversight.sefisca.util.StringXorCriptografia;

public class ConfirmacaoCadastro extends HttpServlet{

   private static final long serialVersionUID = 1L;

   private static StringXorCriptografia criptografia = new StringXorCriptografia();

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
      String cpf = req.getParameter("x");
      String pagina = "erroConfirmacao.ovs";
      cpf = criptografia.decode(cpf, StringXorCriptografia.getChave());
      try {
         if(confirmarUsuario(cpf)){
            req.getSession().setAttribute("_cpf", cpf);
            pagina = "confirmacao.ovs";
         }
         resp.sendRedirect(pagina);
      } catch (SQLException e) {
         UtilLog.getLog().error(e.getMessage(), e);
      } catch (UnknownHostException  e) {
         UtilLog.getLog().error(e.getMessage(), e);
      } catch (IOException e) {
         UtilLog.getLog().error(e.getMessage(), e);
      }
   }

   private static boolean confirmarUsuario(String cpf) throws SQLException  {
      PreparedStatement pstmt = null;
      Connection con = null;

      try {
         InitialContext ctx = new InitialContext();
         DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/sefisca");
         con = ds.getConnection();
         pstmt = con.prepareStatement("update usuario set confirmado = true where cpf = ?");
         pstmt.setString(1, cpf);
         int qtd = pstmt.executeUpdate();

         if(qtd == 1){
            return true;
         } 

      } catch (Exception e) {
         UtilLog.getLog().error(e.getMessage(), e);
      } finally {
         if (pstmt != null) { 
            pstmt.close();
         }
         if (con != null) { 
            con.close();
         }
      }
      return false;
   }

}
