package br.com.oversight.sefisca.persistencia;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.persistencia.PersistenciaJpa;
import br.com.ambientinformatica.util.UtilLog;
import br.com.oversight.sefisca.entidade.EnumTipoCodigoInstituicao;
import br.com.oversight.sefisca.entidade.EnumTipoPessoa;
import br.com.oversight.sefisca.entidade.Instituicao;
import br.com.oversight.sefisca.entidade.TipoUnidade;
import br.com.oversight.sefisca.entidade.Usuario;
import br.com.oversight.sefisca.util.UtilSefisca;

@Repository("instituicaoDao")
public class InstituicaoDaoJpa extends PersistenciaJpa<Instituicao> implements InstituicaoDao {

    private static final long serialVersionUID = 1L;

    @Autowired
    private EntityManagerFactory emf;

    @Autowired
    private TipoUnidadeDao tipoUnidadeDao;

    @Override
    public int atualizarInstituicaoCsv(UploadedFile file, Date ultimaDataAtualizacao, Usuario usuario)
            throws Exception {
        try (Reader reader = new InputStreamReader(file.getInputstream());) {
            return atualizarInstituicao(reader, ultimaDataAtualizacao, usuario);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional
    private int atualizarInstituicao(Reader reader, Date ultimaDataAtualizacao, Usuario usuario) {
        try (CSVParser csvParser = new CSVParser(reader, CSVFormat.EXCEL.withDelimiter(';').withQuote('"')
                .withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            int i = 0;
            int instituicaoAtualizada = 0;
            for (CSVRecord csvRecord : csvParser) {
                Instituicao instituicao = criarObjeto(csvRecord, ultimaDataAtualizacao, usuario);
                if (!UtilSefisca.isNullOrEmpty(instituicao)) {
                    em.persist(instituicao);
                    i++;
                    instituicaoAtualizada++;
                    if (i == 100) {
                        i = 0;
                        em.flush();
                        em.clear();
                    }
                }
            }
            em.getTransaction().commit();
            em.close();
            return instituicaoAtualizada;
        } catch (IOException e) {
            UtilLog.getLog().error(e.getMessage(), e);
        } catch (ParseException e) {
            UtilLog.getLog().error(e.getMessage(), e);
        }
        return 0;
    }

    private Instituicao criarObjeto(CSVRecord csvRecord, Date ultimaDataAtualizacao, Usuario usuario)
            throws ParseException {
        String dataAtualizacaoCsv = csvRecord.get("TO_CHAR(DT_ATUALIZACAO,'DD/MM/YYYY')").trim();
        Date dataAtualizacao = null;
        if (!UtilSefisca.isNullOrEmpty(dataAtualizacaoCsv)) {
            dataAtualizacao = new SimpleDateFormat("dd/MM/yyyy").parse(dataAtualizacaoCsv);
        }

        if (UtilSefisca.isNullOrEmpty(ultimaDataAtualizacao) || dataAtualizacao.after(ultimaDataAtualizacao)) {
            String cnes = csvRecord.get("CO_CNES").trim();
            if (!UtilSefisca.isNullOrEmpty(instituicaoPorCnesCpnj(cnes, EnumTipoCodigoInstituicao.CNES))) {
                return null;
            }

            Instituicao instituicao = new Instituicao();
            String tipoPessoa = csvRecord.get("TP_PFPJ").trim();
            String razaoSocial = csvRecord.get("NO_RAZAO_SOCIAL").trim();
            String nomeFantasia = csvRecord.get("NO_FANTASIA").trim();
            String cpf = csvRecord.get("NU_CPF").trim();
            String cnpj = csvRecord.get("NU_CNPJ").trim();
            String tipoUnidadeCsv = csvRecord.get("TP_UNIDADE").trim();
            instituicao.setCnes(UtilSefisca.isNullOrEmpty(cnes) ? null : cnes);
            instituicao.setTipoPessoa(tipoPessoa.equals("1") ? EnumTipoPessoa.PF : EnumTipoPessoa.PJ);
            if (instituicao.getTipoPessoa().equals(EnumTipoPessoa.PF)) {
                instituicao.setCpfCnpj(
                        UtilSefisca.isNullOrEmpty(cpf) ? null : UtilSefisca.formatStringMask(cpf, "###.###.###-##"));
            }
            if (instituicao.getTipoPessoa().equals(EnumTipoPessoa.PJ)) {
                instituicao.setCpfCnpj(UtilSefisca.isNullOrEmpty(cnpj) ? null
                        : UtilSefisca.formatStringMask(cnpj, "##.###.###/####-##"));
            }
            instituicao.setRazaoSocial(UtilSefisca.isNullOrEmpty(razaoSocial) ? null : razaoSocial);
            instituicao.setNomeFantasia(UtilSefisca.isNullOrEmpty(nomeFantasia) ? null : nomeFantasia);
            if (!UtilSefisca.isNullOrEmpty(tipoUnidadeCsv)) {
                TipoUnidade tipoUnidade = tipoUnidadeDao.consultar(Integer.parseInt(tipoUnidadeCsv));
                instituicao.setTipoUnidade(tipoUnidade);
            }
            instituicao.setUsuario(usuario);
            return instituicao;
        }
        return null;
    }

    @Override
    public Instituicao instituicaoPorCnesCpnj(String cnesCnpj, EnumTipoCodigoInstituicao tipoCodigo)
            throws PersistenciaException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select distinct i from Instituicao i where 1 = 1");

            if (tipoCodigo.equals(EnumTipoCodigoInstituicao.CNES)) {
                sql.append(" and i.cnes = :cnes");
            }

            if (tipoCodigo.equals(EnumTipoCodigoInstituicao.CNPJ)) {
                sql.append(" and i.cpfCnpj = :cnpj");
            }

            TypedQuery<Instituicao> query = em.createQuery(sql.toString(), Instituicao.class);

            if (tipoCodigo.equals(EnumTipoCodigoInstituicao.CNES)) {
                query.setParameter("cnes", cnesCnpj);
            }
            if (tipoCodigo.equals(EnumTipoCodigoInstituicao.CNPJ)) {
                query.setParameter("cnpj", cnesCnpj);
            }

            return query.getSingleResult();
        } catch (NoResultException ne) {
            return null;
        } catch (Exception e) {
            UtilLog.getLog().error(e.getMessage(), e);
            throw new PersistenciaException("Erro ao consultar instituição por cnes/cnpj.", e);
        }
    }

    @Override
    public Date ultimaDataAtualizacao() {
        try {
            String sql = "select max(i.dataAtualizacao) from Instituicao as i";

            TypedQuery<Date> query = em.createQuery(sql.toString(), Date.class);

            return query.getSingleResult();
        } catch (NoResultException ne) {
            return null;
        } catch (Exception e) {
            UtilLog.getLog().error(e.getMessage(), e);
            throw new PersistenciaException("Erro ao consultar ultima data de atualização.", e);
        }
    }
}
