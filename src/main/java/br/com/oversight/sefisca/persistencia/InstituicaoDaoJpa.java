package br.com.oversight.sefisca.persistencia;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import br.com.oversight.sefisca.entidade.Atividade;
import br.com.oversight.sefisca.entidade.EnumSituacaoInstituicao;
import br.com.oversight.sefisca.entidade.EnumTipoCodigoInstituicao;
import br.com.oversight.sefisca.entidade.EnumTipoGestao;
import br.com.oversight.sefisca.entidade.EnumTipoPessoa;
import br.com.oversight.sefisca.entidade.Instituicao;
import br.com.oversight.sefisca.entidade.MotivoDesativacao;
import br.com.oversight.sefisca.entidade.NaturezaJuridica;
import br.com.oversight.sefisca.entidade.TipoInstituicao;
import br.com.oversight.sefisca.entidade.TipoUnidade;
import br.com.oversight.sefisca.entidade.TurnoAtendimento;
import br.com.oversight.sefisca.entidade.Usuario;
import br.com.oversight.sefisca.util.UtilSefisca;

@Repository("instituicaoDao")
public class InstituicaoDaoJpa extends PersistenciaJpa<Instituicao> implements InstituicaoDao {

    private static final long serialVersionUID = 1L;

    @Autowired
    private EntityManagerFactory emf;

    @Autowired
    private AtividadeDao atividadeDao;

    @Autowired
    private TipoUnidadeDao tipoUnidadeDao;

    @Autowired
    private TurnoAtendimentoDao turnoAtendimentoDao;

    @Autowired
    private NaturezaJuridicaDao naturezaJuridicaDao;

    @Autowired
    private TipoInstituicaoDao tipoInstituicaoDao;

    @Autowired
    private MotivoDesativacaoDao motivoDesativacaoDao;

    @Override
    public int atualizarInstituicaoCsv(UploadedFile file, Date ultimaDataAtualizacao, Usuario usuario) throws Exception {
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
                        break;
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

    private Instituicao criarObjeto(CSVRecord csvRecord, Date ultimaDataAtualizacao, Usuario usuario) throws ParseException {
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
            String situacaoInstituicao = csvRecord.get("NIVEL_DEP").trim();
            String tipoGestao = csvRecord.get("TP_GESTAO").trim();
            String razaoSocial = csvRecord.get("NO_RAZAO_SOCIAL").trim();
            String nomeFantasia = csvRecord.get("NO_FANTASIA").trim();
            String telefone = csvRecord.get("NU_TELEFONE").trim();
            String fax = csvRecord.get("NU_FAX").trim();
            String email = csvRecord.get("NO_EMAIL").trim();
            String cpf = csvRecord.get("NU_CPF").trim();
            String cnpj = csvRecord.get("NU_CNPJ").trim();
            String url = csvRecord.get("NO_URL").trim();
            String latitude = csvRecord.get("NU_LATITUDE").trim();
            String longitude = csvRecord.get("NU_LONGITUDE").trim();
            String atividadeCsv = csvRecord.get("CO_ATIVIDADE_PRINCIPAL").trim();
            String tipoUnidadeCsv = csvRecord.get("TP_UNIDADE").trim();
            String turnoAtendimentoCsv = csvRecord.get("CO_TURNO_ATENDIMENTO").trim();
            String naturezaJuridicaCsv = csvRecord.get("CO_NATUREZA_JUR").trim();
            String tipoInstituicaoCsv = csvRecord.get("CO_TIPO_ESTABELECIMENTO").trim();
            String motivoDesativacaoCsv = csvRecord.get("CO_MOTIVO_DESAB").trim();
            String contratoFormalizadoSus = csvRecord.get("ST_CONTRATO_FORMALIZADO").trim();
            String instituicaoSempreAberta = csvRecord.get("TP_ESTAB_SEMPRE_ABERTO").trim();
            String cnpjMantenedora = csvRecord.get("NU_CNPJ_MANTENEDORA").trim();
            String logradouro = csvRecord.get("NO_LOGRADOURO").trim();
            String numero = csvRecord.get("NU_ENDERECO").trim();
            String complemento = csvRecord.get("NO_COMPLEMENTO").trim();
            String bairro = csvRecord.get("NO_BAIRRO").trim();
            String cep = csvRecord.get("CO_CEP").trim();
            String codigoUnidade = csvRecord.get("CO_UNIDADE").trim();
            String cpfDiretor = csvRecord.get("CO_CPFDIRETORCLN").trim();
            String conselhoClasseDiretor = csvRecord.get("REG_DIRETORCLN").trim();
            String dataAtualizacaoGeoCsv = csvRecord.get("TO_CHAR(DT_ATU_GEO,'DD/MM/YYYY')").trim();
            Date dataAtualizacaoGeo = null;
            if (!UtilSefisca.isNullOrEmpty(dataAtualizacaoGeoCsv)) {
                dataAtualizacaoGeo = new SimpleDateFormat("dd/MM/yyyy").parse(dataAtualizacaoGeoCsv);
            }
            
            instituicao.setCnes(UtilSefisca.isNullOrEmpty(cnes) ? null : cnes);
            instituicao.setTipoPessoa(tipoPessoa.equals("1") ? EnumTipoPessoa.PF : EnumTipoPessoa.PJ);
            instituicao.setSituacaoInstituicao(situacaoInstituicao.equals("1") ? EnumSituacaoInstituicao.INDIVIDUAL
                    : EnumSituacaoInstituicao.MANTIDA);
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
            instituicao.setTelefone(UtilSefisca.isNullOrEmpty(telefone) ? null : telefone);
            instituicao.setFax(UtilSefisca.isNullOrEmpty(fax) ? null : fax);
            instituicao.setEmail(UtilSefisca.isNullOrEmpty(email) ? null : email);

            instituicao.setUrl(UtilSefisca.isNullOrEmpty(url) ? null : url);
            instituicao.setCnpjMantenedora(UtilSefisca.isNullOrEmpty(cnpjMantenedora) ? null
                    : UtilSefisca.formatStringMask(cnpjMantenedora, "##.###.###/####-##"));
            instituicao.setCpfDiretor(UtilSefisca.isNullOrEmpty(cpfDiretor) ? null : cpfDiretor);
            instituicao.setConselhoClasseDiretor(
                    UtilSefisca.isNullOrEmpty(conselhoClasseDiretor) ? null : conselhoClasseDiretor);
            instituicao.setCodigoUnidade(UtilSefisca.isNullOrEmpty(codigoUnidade) ? null : codigoUnidade);

            instituicao.setTipoGestao(EnumTipoGestao.valueOf(tipoGestao));

            if (!UtilSefisca.isNullOrEmpty(atividadeCsv)) {
                Atividade atividade = atividadeDao.consultar(Integer.parseInt(atividadeCsv) + 1);
                instituicao.setAtividadePrincipal(atividade);
            }

            if (!UtilSefisca.isNullOrEmpty(tipoUnidadeCsv)) {
                TipoUnidade tipoUnidade = tipoUnidadeDao.consultar(Integer.parseInt(tipoUnidadeCsv));
                instituicao.setTipoUnidade(tipoUnidade);
            }

            if (!UtilSefisca.isNullOrEmpty(turnoAtendimentoCsv)) {
                TurnoAtendimento turnoAtendimento = turnoAtendimentoDao
                        .consultar(Integer.parseInt(turnoAtendimentoCsv));
                instituicao.setTurnoAtendimento(turnoAtendimento);
            }

            if (!UtilSefisca.isNullOrEmpty(naturezaJuridicaCsv)) {
                List<NaturezaJuridica> naturezaJuridica = naturezaJuridicaDao
                        .naturezaJuridicaPorCodigo(naturezaJuridicaCsv);
                instituicao.setNaturezaJuridica(naturezaJuridica.size() > 0 ? naturezaJuridica.get(0) : null);
            }

            if (!UtilSefisca.isNullOrEmpty(tipoInstituicaoCsv)) {
                TipoInstituicao tipoInstituicao = tipoInstituicaoDao.consultar(Integer.parseInt(tipoInstituicaoCsv));
                instituicao.setTipoInstituicao(tipoInstituicao);
            }

            if (!UtilSefisca.isNullOrEmpty(motivoDesativacaoCsv)) {
                MotivoDesativacao motivoDesativacao = motivoDesativacaoDao
                        .consultar(Integer.parseInt(motivoDesativacaoCsv));
                instituicao.setMotivoDesativacao(motivoDesativacao);
            }
            instituicao.setInstituicaoSempreAberta(instituicaoSempreAberta.equals("S"));
            instituicao.setContratoFormalizadoSus(contratoFormalizadoSus.equals("S"));

            instituicao.getEndereco().setLogradouro(UtilSefisca.isNullOrEmpty(logradouro) ? null : logradouro);
            instituicao.getEndereco().setNumero(UtilSefisca.isNullOrEmpty(numero) ? null : numero);
            instituicao.getEndereco().setComplemento(UtilSefisca.isNullOrEmpty(complemento) ? null : complemento);
            instituicao.getEndereco().setBairro(UtilSefisca.isNullOrEmpty(bairro) ? null : bairro);
            instituicao.getEndereco().setCep(UtilSefisca.isNullOrEmpty(cep) ? null : cep);
            instituicao.getEndereco().setLatitude(UtilSefisca.isNullOrEmpty(latitude) ? null : latitude);
            instituicao.getEndereco().setLongitude(UtilSefisca.isNullOrEmpty(longitude) ? null : longitude);

            instituicao.setDataAtualizacao(UtilSefisca.isNullOrEmpty(dataAtualizacao) ? null : dataAtualizacao);
            instituicao.getEndereco()
                    .setDataAtualizacaoGeo(UtilSefisca.isNullOrEmpty(dataAtualizacaoGeo) ? null : dataAtualizacaoGeo);
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
