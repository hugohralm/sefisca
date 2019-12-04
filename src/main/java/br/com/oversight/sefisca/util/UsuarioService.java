package br.com.oversight.sefisca.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.ambientinformatica.util.UtilLog;

public class UsuarioService implements UserDetailsService {

    private DataSource dataSource;

    private static void registrarHistoricoLogin(Connection con, String cpf) throws SQLException {
        Date agora = new Date();
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE Usuario ");
        sql.append("SET dataultimoacesso = ? ");
        sql.append("FROM Pessoa p ");
        sql.append("WHERE (p.id = pessoa_id) ");
        sql.append("AND p.cpf = ? ");

        PreparedStatement pstmtUsuario = con.prepareStatement(sql.toString());
        pstmtUsuario.setTimestamp(1, new Timestamp(agora.getTime()));
        pstmtUsuario.setString(2, cpf);
        pstmtUsuario.execute();
        pstmtUsuario.close();

        sql = new StringBuilder();
        sql.append("INSERT INTO HistoricoLogin (id, data, usuario_id) ");
        sql.append("values ((SELECT nextval('historico_login_seq')), ?, ");
        sql.append("(SELECT u.id ");
        sql.append("FROM Usuario u ");
        sql.append("INNER JOIN Pessoa p ");
        sql.append("ON p.id = u.pessoa_id ");
        sql.append("WHERE p.cpf = ?)) ");

        PreparedStatement pstmtHistorico = con.prepareStatement(sql.toString());
        pstmtHistorico.setTimestamp(1, new Timestamp(agora.getTime()));
        pstmtHistorico.setString(2, cpf);
        pstmtHistorico.execute();
        pstmtHistorico.close();
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        try {
            Connection con = dataSource.getConnection();

            try {
                StringBuilder sql = new StringBuilder();
                sql.append("SELECT ");
                sql.append("p.cpf AS username, ");
                sql.append("u.senha as password, ");
                sql.append("u.confirmado AS enabled ");
                sql.append("FROM usuario u ");
                sql.append("INNER JOIN Pessoa p ");
                sql.append("ON p.id = u.pessoa_id ");
                sql.append("WHERE p.cpf = ? ");
                PreparedStatement pstmt = con.prepareStatement(sql.toString());
                
                try {
                    pstmt.setString(1, username);
                    ResultSet rs = pstmt.executeQuery();

                    try {
                        if (rs.next()) {
                            List<GrantedAuthority> papeis = new ArrayList<>();
                            sql = new StringBuilder();
                            sql.append("SELECT ");
                            sql.append("p.papel as authority ");
                            sql.append("FROM PapelUsuario p ");
                            sql.append("WHERE p.usuario_id = ");
                            sql.append("(SELECT u.id ");
                            sql.append("FROM Usuario u ");
                            sql.append("INNER JOIN Pessoa p ");
                            sql.append("ON p.id = u.pessoa_id ");
                            sql.append("where p.cpf = ?) ");
                            PreparedStatement pstmtPapeis = con.prepareStatement(sql.toString());
                            try {
                                pstmtPapeis.setString(1, username);
                                ResultSet rsPapeis = pstmtPapeis.executeQuery();
                                try {
                                    UserDetails user;
                                    try {
                                        while (rsPapeis.next()) {
                                            papeis.add(new SimpleGrantedAuthority(rsPapeis.getString("authority")));
                                        }
                                        papeis.add(new SimpleGrantedAuthority("USUARIO"));

                                        user = new UsuarioImpl(username, rs.getString("password"),
                                                rs.getBoolean("enabled"), true, true, true, papeis);

                                        registrarHistoricoLogin(con, username);
                                    } finally {
                                        rsPapeis.close();
                                    }
                                    return user;
                                } finally {
                                    rsPapeis.close();
                                }
                            } finally {
                                pstmtPapeis.close();
                            }
                        } else {
                            throw new UsernameNotFoundException("Usuário " + username + " não encontrado");
                        }
                    } finally {
                        rs.close();
                    }
                } finally {
                    pstmt.close();
                }
            } finally {
                con.close();
            }
        } catch (RuntimeException e) {
            UtilLog.getLog().error(e.getMessage(), e);
            throw new DataAccessExceptionImpl(e.getMessage(), e);
        } catch (SQLException e) {
            UtilLog.getLog().error(e.getMessage(), e);
            throw new DataAccessExceptionImpl(e.getMessage(), e);
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static class DataAccessExceptionImpl extends DataAccessException {

        private static final long serialVersionUID = 1L;

        public DataAccessExceptionImpl(String msg, Exception e) {
            super(msg, e);
        }

    }

}
