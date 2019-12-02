package br.com.oversight.sefisca.util;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.MaskFormatter;
import javax.xml.soap.SOAPBody;

import org.apache.commons.lang.RandomStringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import br.com.ambientinformatica.util.UtilException;
import br.com.ambientinformatica.util.UtilLog;
import br.com.ambientinformatica.util.UtilRecurso;
import br.com.ambientinformatica.util.UtilTexto;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

public class UtilSefisca {

    private UtilSefisca() {
    }

    public static boolean isPessoaFisica(String cpfCnpj) {
        return cpfCnpj.length() == 14;
    }

    public static String formatStringMask(String string, String mascara) {
        try {
            MaskFormatter mask = new MaskFormatter(mascara);
            mask.setValueContainsLiteralCharacters(false);
            return mask.valueToString(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSenhaAleatoria(int qtdDigito) {
        String alfabeto = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder conteudo = new StringBuilder();
        for (int i = 0; i < qtdDigito; i++) {
            int indice = (int) (Math.random() * alfabeto.length());
            indice = indice < alfabeto.length() ? indice : 0;
            conteudo.append(alfabeto.charAt(indice));
        }
        return conteudo.toString();
    }

    public static boolean isCPFComDigitosIguais(String cpf) {
        switch (cpf) {
        case "000.000.000-00":
        case "111.111.111-11":
        case "222.222.222-22":
        case "333.333.333-33":
        case "444.444.444-44":
        case "555.555.555-55":
        case "666.666.666-66":
        case "777.777.777-77":
        case "888.888.888-88":
        case "999.999.999-99":
            return true;
        default:
            break;
        }
        return false;
    }

    /**
     * @param parametro = texto para concatenação.
     * @param tamanho   = quantidade de caracteres randomicos.
     * @param ProximoID = Inteiro para evitar códigos iguais.
     * @return String concatenada.
     */
    public static String gerarCodigoAleatorio(int tamanho, Integer proximoID) {
        String id = Integer.toHexString(proximoID).toUpperCase();
        return RandomStringUtils.randomAlphanumeric(tamanho - id.length()).toUpperCase() + id;
    }

    /**
     * @param tamanho = quantidade de caracteres randomicos.
     * @return String aleatoria.
     */
    public static String gerarCodigoAleatorio(int tamanho) {
        return RandomStringUtils.randomAlphanumeric(tamanho).toUpperCase();
    }

    public static boolean isNullOrEmpty(Object o) {
        if (o != null) {
            if (o instanceof String) {
                return ((String) o).isEmpty();
            } else if (o instanceof List && ((List<?>) o).size() == 0) {
                return true;
            } else if (o instanceof Set && ((Set<?>) o).size() == 0) {
                return true;
            }
            return false;
        }
        return true;
    }

    public static boolean validarMenorIdade16(Date dataNascimento) {
        boolean deMaior = false;
        Calendar calDataNascimento = Calendar.getInstance();
        calDataNascimento.setTime(dataNascimento);
        calDataNascimento.add(Calendar.YEAR, 16);
        Calendar calDataAtual = Calendar.getInstance();
        if (calDataNascimento.after(calDataAtual)) {
            deMaior = true;
        }
        return deMaior;
    }

    /**
     * @param conteudoPdf = Conteu em byte do pdf.
     */
    public static void visualizarPdf(byte[] conteudoPdf) {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();

        response.reset();
        response.setContentType("application/pdf");
        response.setContentLength(conteudoPdf.length);
        response.setHeader("Content-disposition", "inline; filename=licenca.pdf");

        try {
            response.getOutputStream().write(conteudoPdf);
            response.getOutputStream().flush();
            response.getOutputStream().close();
            fc.responseComplete();
        } catch (Exception e) {
            UtilLog.getLog().error(e.getMessage(), e);
        }
    }

    public static void visualizarDadosPdf(byte[] conteudoPdf, String nomeArquivo) {

        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();

        response.reset();
        response.setContentType("application/pdf");
        response.setContentLength(conteudoPdf.length);
        response.setHeader("Content-disposition",
                "inline; filename=" + UtilTexto.removerCaracteresNaoAlfaNumericos(nomeArquivo) + ".pdf");

        try {
            response.getOutputStream().write(conteudoPdf);
            response.getOutputStream().flush();
            response.getOutputStream().close();
            fc.responseComplete();
        } catch (Exception e) {
            UtilLog.getLog().error(e.getMessage(), e);
        }
    }

    public static void visualizarHtml(String html) {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
        response.reset();
        response.setContentLength(html.length());
        response.setHeader("Content-disposition", "inline");
        try {
            response.getOutputStream().write(html.getBytes());
            response.getOutputStream().flush();
            response.getOutputStream().close();
            fc.responseComplete();
        } catch (Exception e) {
            UtilLog.getLog().error(e.getMessage(), e);
        }
    }

    public static String getDataStringFormatadaMask(Date data, String mask) {
        String dataString = "";
        if (data != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(mask);
            dataString = sdf.format(data);
        }
        return dataString;
    }

    public static String limparArrayDeString(String texto) {
        String textoFormatado = UtilTexto.truncarTamanhoMaximo(texto.toString(), 1024);
        textoFormatado = textoFormatado.replace("[", "");
        textoFormatado = textoFormatado.replace("]", "");
        textoFormatado = textoFormatado.replace(",,", ".");
        textoFormatado = textoFormatado.replace(".,", ".");
        textoFormatado = textoFormatado.replace(":,", ":");
        textoFormatado = textoFormatado.replace(",", ", ");
        return UtilTexto.truncarTamanhoMaximo(textoFormatado.toString(), 1024);
    }

    public static String limparMascara(String texto) {
        return texto.replaceAll("[^0-9]", "");
    }

    @SuppressWarnings("rawtypes")
    public static byte[] gerarStreamRelatorio(String caminhoRelatorio, Object dados, Map<String, Object> parametros)
            throws UtilException {
        JRPdfExporter exporter = new JRPdfExporter();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JasperPrint impressao = null;

        String diretorioRelatorio = caminhoRelatorio.substring(0, caminhoRelatorio.indexOf("/"));

        try {
            URL urlDir = UtilRecurso.getClassLoader().getResource(diretorioRelatorio);

            if (urlDir == null) {
                UtilLog.getLog()
                        .error("Recurso: " + diretorioRelatorio + " inexistente no diretório de classes do projeto");
                throw new UtilException(String.format("ERRO AO GERAR RELATORIO [Recurso: %s INEXISTENTE]",
                        new Object[] { diretorioRelatorio }));
            }

            parametros.put("SUBREPORT_DIR", urlDir.getPath() + "/");
            InputStream is = UtilRecurso.getClassLoader().getResourceAsStream(caminhoRelatorio);

            if (is == null) {
                UtilLog.getLog()
                        .error(String.format("Recurso: %s INEXISTENTE", new Object[] { "/" + caminhoRelatorio }));
                throw new UtilException(String.format("ERRO AO GERAR RELATORIO [Recurso: %s INEXISTENTE]",
                        new Object[] { caminhoRelatorio }));
            }
            if (dados instanceof Connection) {
                impressao = JasperFillManager.fillReport(is, parametros, (Connection) dados);
            } else if (dados instanceof Collection) {
                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource((Collection) dados);
                impressao = JasperFillManager.fillReport(is, parametros, dataSource);
            } else if (dados instanceof String) {
                ByteArrayInputStream streamDados = new ByteArrayInputStream(((String) dados).getBytes());
                JRCsvDataSource dataSource = new JRCsvDataSource(streamDados);
                dataSource.setUseFirstRowAsHeader(true);
                impressao = JasperFillManager.fillReport(is, parametros, dataSource);
            } else {
                String msg = "o parametro dados deve ser do tipo String(csv), Collection ou Connection";
                UtilLog.getLog().error(msg);
                throw new UtilException(msg);
            }

            exporter.setExporterInput(new SimpleExporterInput(impressao));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            exporter.setConfiguration(configuration);

            exporter.exportReport();

            UtilLog.getLog().info(String.format("Gerando relatorio. Caminho: %s Parametros: %s",
                    new Object[] { caminhoRelatorio, parametros }, parametros));
            return baos.toByteArray();
        } catch (RuntimeException e) {
            UtilLog.getLog().error(String.format("ERRO AO GERAR RELATORIO : %s %s", "",
                    new Object[] { "/" + caminhoRelatorio, e.getMessage() }), e);
            throw new UtilException(String.format("ERRO AO GERAR RELATORIO: %s]", new Object[] { caminhoRelatorio }),
                    e);
        } catch (JRException e) {
            UtilLog.getLog().error(String.format("ERRO AO GERAR RELATORIO : %s %s", "",
                    new Object[] { "/" + caminhoRelatorio, e.getMessage() }), e);
            throw new UtilException(String.format("ERRO AO GERAR RELATORIO: %s]", new Object[] { caminhoRelatorio }),
                    e);
        }
    }

    public static String getCRC32(byte[] bytes) {
        Checksum checksum = new CRC32();
        checksum.update(bytes, 0, bytes.length);
        long checksumValue = checksum.getValue();
        return Long.toHexString(checksumValue).toUpperCase();
    }

    public static String getSha256(byte[] bytes) {
        MessageDigest digest;
        byte[] hash = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(bytes);
            return new String(Hex.encode(hash));
        } catch (NoSuchAlgorithmException e) {
            UtilLog.getLog().error("Erro ao calcular o checksum do documento", e);
        }
        return null;
    }

    public static String getIpUsuario() {
        HttpServletRequest req = null;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            req = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        }
        String ip = req.getHeader("X-FORWARDED-FOR");
        if (ip != null) {
            ip = ip.replaceFirst(",.*", "");
        } else {
            ip = req.getRemoteAddr();
        }
        return ip;
    }

    public static byte[] converterHtmlParaPdf(String htmlPath) {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, byteOutput);
            writer.setInitialLeading(12.5f);
            document.open();
            CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(false);
            HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
            htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
            htmlContext
                    .setImageProvider(new Base64ImageProvider(Constantes.Sistema.URL_SISTEMA + "/resources/imagens/"));
            PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
            HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
            CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
            XMLWorker worker = new XMLWorker(css, true);
            XMLParser converter = new XMLParser(worker);
            converter.parse(new ByteArrayInputStream(htmlPath.getBytes()));
            document.close();
        } catch (Exception e) {
            UtilLog.getLog().error("Erro ao converter o html para pdf!", e);
        }
        return byteOutput.toByteArray();
    }

    public static byte[] adicionarNumeracaoNasPaginasDoPdf(byte[] bytePdf) {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        PdfReader reader;
        PdfStamper stamper;
        PdfContentByte pagecontent;
        Phrase phrase;
        float eixoX, eixoY = 15, rotation = 0;
        try {
            reader = new PdfReader(bytePdf);
            int n = reader.getNumberOfPages();
            stamper = new PdfStamper(reader, byteOutput);
            for (int i = 0; i < n;) {
                pagecontent = stamper.getOverContent(++i);
                phrase = new Phrase(String.format("%s de %s", i, n));
                eixoX = reader.getPageSize(i).getWidth() / 2;
                ColumnText.showTextAligned(pagecontent, Element.ALIGN_CENTER, phrase, eixoX, eixoY, rotation);
            }
            stamper.close();
            reader.close();
        } catch (IOException | DocumentException e) {
            UtilLog.getLog().error("Erro ao adicionar a numeração nas páginas do pdf!", e);
        }
        return byteOutput.toByteArray();
    }

    public static String criarBarcodeQRCodeBase64(String texto, Integer widthQRCode, Integer heightQRCode) {
        BarcodeQRCode qrcode = new BarcodeQRCode(texto, widthQRCode, heightQRCode, null);
        final double percentCrop = 0.78;
        String qrcodeBase64 = "";
        try {
            Image imageQRCode = qrcode.createAwtImage(Color.BLACK, Color.WHITE);
            BufferedImage bufferedImage = new BufferedImage(imageQRCode.getWidth(null), imageQRCode.getHeight(null),
                    BufferedImage.TYPE_INT_RGB);
            bufferedImage.getGraphics().drawImage(imageQRCode, 0, 0, null);
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            int targetWidth = (int) (width * percentCrop);
            int targetHeight = (int) (height * percentCrop);
            int x = (width - targetWidth) / 2;
            int y = (height - targetHeight) / 2;
            BufferedImage cropImage = bufferedImage.getSubimage(x, y, targetWidth, targetHeight);
            ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
            ImageIO.write(cropImage, "png", bytesOut);
            bytesOut.flush();
            byte[] pngImageData = bytesOut.toByteArray();
            qrcodeBase64 = Base64.getEncoder().encodeToString(pngImageData);
        } catch (IOException e) {
            UtilLog.getLog().error("Erro ao criar o QRCode!", e);
        }
        return qrcodeBase64;
    }

    public static String getRealPath() {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
                .getContext();
        return servletContext.getRealPath(File.separator);
    }

    public static String getElementsByTagNameXML(SOAPBody soapBody, String tagName) {
        NodeList nodes = soapBody.getElementsByTagName(tagName);
        Node node = nodes.item(0);
        return node != null ? node.getTextContent() : "";
    }
}
