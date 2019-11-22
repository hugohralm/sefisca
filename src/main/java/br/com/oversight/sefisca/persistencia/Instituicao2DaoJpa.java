package br.com.oversight.sefisca.persistencia;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.ambientinformatica.jpa.persistencia.PersistenciaJpa;
import br.com.oversight.sefisca.entidade.Atividade;
import br.com.oversight.sefisca.entidade.EnumSituacaoInstituicao;
import br.com.oversight.sefisca.entidade.EnumTipoGestao;
import br.com.oversight.sefisca.entidade.EnumTipoPessoa;
import br.com.oversight.sefisca.entidade.Instituicao2;
import br.com.oversight.sefisca.entidade.MotivoDesativacao;
import br.com.oversight.sefisca.entidade.NaturezaJuridica;
import br.com.oversight.sefisca.entidade.TipoInstituicao;
import br.com.oversight.sefisca.entidade.TipoUnidade;
import br.com.oversight.sefisca.entidade.TurnoAtendimento;
import br.com.oversight.sefisca.util.UtilSefisca;

@Repository("instituicao2Dao")
public class Instituicao2DaoJpa extends PersistenciaJpa<Instituicao2> implements Instituicao2Dao {

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

    private static final String SAMPLE_CSV_FILE_PATH = "/tbEstabelecimento.csv";

    @Override
    @Transactional
    public void atualizarInstituicaoCsv() throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (Reader reader = Files.newBufferedReader(Paths.get(classloader.getResource(SAMPLE_CSV_FILE_PATH).toURI()));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.EXCEL.withDelimiter(';').withQuote('"')
                        .withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            int i = 0;
            for (CSVRecord csvRecord : csvParser) {
                em.persist(criarObjeto(csvRecord));
                i++;
                if (i == 19) {
                    i = 0;
                    em.flush();
                    em.clear();
                }
            }
            em.getTransaction().commit();
            em.close();
            System.out.println(csvParser.getHeaderMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Instituicao2 criarObjeto(CSVRecord csvRecord) {
        Instituicao2 instituicao2 = new Instituicao2();
        String cnes = csvRecord.get("CO_CNES").trim();
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

        instituicao2.setCnes(UtilSefisca.isNullOrEmpty(cnes) ? null : cnes);
        instituicao2.setRazaoSocial(UtilSefisca.isNullOrEmpty(razaoSocial) ? null : razaoSocial);
        instituicao2.setNomeFantasia(UtilSefisca.isNullOrEmpty(nomeFantasia) ? null : nomeFantasia);
        instituicao2.setTelefone(UtilSefisca.isNullOrEmpty(telefone) ? null : telefone);
        instituicao2.setFax(UtilSefisca.isNullOrEmpty(fax) ? null : fax);
        instituicao2.setEmail(UtilSefisca.isNullOrEmpty(email) ? null : email);
        instituicao2.setCpf(UtilSefisca.isNullOrEmpty(cpf) ? null : cpf);
        instituicao2.setCnpj(UtilSefisca.isNullOrEmpty(cnpj) ? null : cnpj);
        instituicao2.setUrl(UtilSefisca.isNullOrEmpty(url) ? null : url);
        instituicao2.setCnpjMantenedora(UtilSefisca.isNullOrEmpty(cnpjMantenedora) ? null : cnpjMantenedora);
        instituicao2.setCpfDiretor(UtilSefisca.isNullOrEmpty(cpfDiretor) ? null : cpfDiretor);
        instituicao2.setConselhoClasseDiretor(
                UtilSefisca.isNullOrEmpty(conselhoClasseDiretor) ? null : conselhoClasseDiretor);
        instituicao2.setCodigoUnidade(UtilSefisca.isNullOrEmpty(codigoUnidade) ? null : codigoUnidade);

        instituicao2.setTipoPessoa(tipoPessoa.equals("1") ? EnumTipoPessoa.PF : EnumTipoPessoa.PJ);
        instituicao2.setSituacaoInstituicao(
                situacaoInstituicao.equals("1") ? EnumSituacaoInstituicao.INDIVIDUAL : EnumSituacaoInstituicao.MANTIDA);
        instituicao2.setTipoGestao(EnumTipoGestao.valueOf(tipoGestao));

        if (!UtilSefisca.isNullOrEmpty(atividadeCsv)) {
            Atividade atividade = atividadeDao.consultar(Integer.parseInt(atividadeCsv) + 1);
            instituicao2.setAtividadePrincipal(atividade);
        }

        if (!UtilSefisca.isNullOrEmpty(tipoUnidadeCsv)) {
            TipoUnidade tipoUnidade = tipoUnidadeDao.consultar(Integer.parseInt(tipoUnidadeCsv));
            instituicao2.setTipoUnidade(tipoUnidade);
        }

        if (!UtilSefisca.isNullOrEmpty(turnoAtendimentoCsv)) {
            TurnoAtendimento turnoAtendimento = turnoAtendimentoDao.consultar(Integer.parseInt(turnoAtendimentoCsv));
            instituicao2.setTurnoAtendimento(turnoAtendimento);
        }

        if (!UtilSefisca.isNullOrEmpty(naturezaJuridicaCsv)) {
            List<NaturezaJuridica> naturezaJuridica = naturezaJuridicaDao.naturezaJuridicaPorCodigo(naturezaJuridicaCsv);
            instituicao2.setNaturezaJuridica(naturezaJuridica.size() > 0 ? naturezaJuridica.get(0) : null);
        }

        if (!UtilSefisca.isNullOrEmpty(tipoInstituicaoCsv)) {
            TipoInstituicao tipoInstituicao = tipoInstituicaoDao.consultar(Integer.parseInt(tipoInstituicaoCsv));
            instituicao2.setTipoInstituicao(tipoInstituicao);
        }

        if (!UtilSefisca.isNullOrEmpty(motivoDesativacaoCsv)) {
            MotivoDesativacao motivoDesativacao = motivoDesativacaoDao
                    .consultar(Integer.parseInt(motivoDesativacaoCsv));
            instituicao2.setMotivoDesativacao(motivoDesativacao);
        }
        instituicao2.setInstituicaoSempreAberta(instituicaoSempreAberta.equals("S"));
        instituicao2.setContratoFormalizadoSus(contratoFormalizadoSus.equals("S"));
        
        instituicao2.getEndereco().setLogradouro(UtilSefisca.isNullOrEmpty(logradouro) ? null : logradouro);
        instituicao2.getEndereco().setNumero(UtilSefisca.isNullOrEmpty(numero) ? null : numero);
        instituicao2.getEndereco().setComplemento(UtilSefisca.isNullOrEmpty(complemento) ? null : complemento);
        instituicao2.getEndereco().setBairro(UtilSefisca.isNullOrEmpty(bairro) ? null : bairro);
        instituicao2.getEndereco().setCep(UtilSefisca.isNullOrEmpty(cep) ? null : cep);
        instituicao2.getEndereco().setLatitude(UtilSefisca.isNullOrEmpty(latitude) ? null : latitude);
        instituicao2.getEndereco().setLongitude(UtilSefisca.isNullOrEmpty(longitude) ? null : longitude);
        return instituicao2;
    }
}
