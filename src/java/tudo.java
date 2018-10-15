/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Darlan
 */
@WebServlet(urlPatterns = {"/tudo"})
public class tudo extends HttpServlet {
    
    private ResultSet resultado_da_consulta = null;
    private PreparedStatement preparador_da_consulta = null;
    private conexao criador_da_conexao = new conexao();
    private  Connection conexao;
    

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try{
          PrintWriter out = response.getWriter();
          this.conexao = criador_da_conexao.criarconexcao();
          int op = Integer.parseInt(request.getParameter("op"));  
          if(op == 1){
                String sql = "INSERT INTO `ContaCorrente` (`idContaCorrente`,`NumeroConta`,`CPF_Titular`) VALUES (NULL, '" + request.getParameter("conta")+ "', '" + request.getParameter("cpf") + "')";
                preparador_da_consulta = conexao.prepareStatement(sql);
                preparador_da_consulta.execute(sql);
                out.println("<p>Cadastrado.</p><br><a href='/prova'>Voltar");
          }
          
          if(op == 2){
                 int id = 0;         
                 String sql = "SELECT * FROM ContaCorrente WHERE `NumeroConta` = '" +request.getParameter("conta")+ "'";
                 preparador_da_consulta = conexao.prepareStatement(sql);
                 resultado_da_consulta = preparador_da_consulta.executeQuery();
                 while (resultado_da_consulta.next()) {
                    id = resultado_da_consulta.getInt("idContaCorrente");
                 }
              
               	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
                Date dataAtual = new Date();
                 String sql2 = "INSERT INTO `Operacao` (`idOperacao`, `DataOperacao`, `ValorOperacao`, `CPFResponsavelOperacao`, `idContaCorrente`) VALUES"+
                  "(NULL, '" + new java.sql.Date(dataAtual.getTime()) + "', '" + request.getParameter("valor")+ "','" + request.getParameter("cpf")+ "','" + id + "')";
                preparador_da_consulta = conexao.prepareStatement(sql2);
                preparador_da_consulta.execute(sql2);
                out.println("<p>Cadastrado.</p><br><a href='/prova'>Voltar");
               
            
          }
          
          if(op == 3){
                int id = 0;
                String titular_cpf = "";String conta= "";String credito_debito;String soma = "";
                String sql = "SELECT * FROM ContaCorrente WHERE `NumeroConta` = '" +request.getParameter("conta")+ "'";
                 preparador_da_consulta = conexao.prepareStatement(sql);
                 resultado_da_consulta = preparador_da_consulta.executeQuery();
                 while (resultado_da_consulta.next()) {
                    id = resultado_da_consulta.getInt("idContaCorrente");
                    titular_cpf =  resultado_da_consulta.getString("CPF_Titular");
                    conta = resultado_da_consulta.getString("NumeroConta");
            
                                       
                 }
                 String resultado = "<p>Número da conta:"+conta+"</p>";
                        resultado += "<p>cpf:"+titular_cpf+"</p>";

                
                 String sql2 = "SELECT * FROM `Operacao` WHERE `idContaCorrente` ="+id+" ORDER BY `Operacao`.`DataOperacao` ASC";
                 preparador_da_consulta = conexao.prepareStatement(sql2);
                 this.resultado_da_consulta = preparador_da_consulta.executeQuery();
                 
                 while (this.resultado_da_consulta.next()) {
                     credito_debito = (this.resultado_da_consulta.getBigDecimal("ValorOperacao").compareTo(new BigDecimal(0)) < 0) ? "Débito":"Credito";
                     resultado += "<tr>\n" +
                                "    <td>"+this.resultado_da_consulta.getBigDecimal("ValorOperacao")+"</td>\n" +
                                "    <td>"+this.resultado_da_consulta.getString("CPFResponsavelOperacao")+"</td>\n" +
                                "    <td>"+credito_debito +"</td>\n" +
                                "    <td>"+this.resultado_da_consulta.getDate("DataOperacao")+"</td>\n" +
                                "  </tr><br>\n";
                     
                 }
                 
                 String sql3 = "SELECT SUM(ValorOperacao) as soma FROM `Operacao` WHERE `idContaCorrente` = "+id+"";
                 preparador_da_consulta = conexao.prepareStatement(sql3);
                 this.resultado_da_consulta = preparador_da_consulta.executeQuery();
                 
                 while (this.resultado_da_consulta.next()) {
                    soma = this.resultado_da_consulta.getString("soma");
                       
                }
                 
                 
                  out.println(resultado+"<br>Saldo:"+soma+"<br><a href='/prova'>Voltar");
                
          }
          
          
        } catch (SQLException ex) {
            Logger.getLogger(tudo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(tudo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

 

}
