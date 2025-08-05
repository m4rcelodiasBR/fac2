package mb.cpo.facdigital.util;

import mb.cpo.facdigital.dto.avaliacao.DadosXmlDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class UtilitarioParserXML {

    public DadosXmlDTO extrairDadosDoXml(MultipartFile arquivo) throws Exception {
        try (InputStream inputStream = arquivo.getInputStream()) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document documento = dBuilder.parse(inputStream);
            documento.getDocumentElement().normalize();

            Element raiz = documento.getDocumentElement();

            // Extrai dados do avaliador
            String nipAvaliador = raiz.getAttribute("nip_avaliador");
            String nomeAvaliador = getValorTag(raiz, "nome_avaliador");
            String postoAvaliador = getValorTag(raiz, "posto_avaliador");
            String quadroAvaliador = getValorTag(raiz, "cqd_avaliador");

            // Extrai dados do evento
            String dataLimite = getValorTag(raiz, "data_limite_remessa");
            Element eventoElement = (Element) raiz.getElementsByTagName("evento").item(0);
            int eventoCodigo = Integer.parseInt(eventoElement.getTextContent());
            String eventoDataDescritiva = eventoElement.getAttribute("data");
            String situacaoPromocao = getValorTag(raiz, "situacao_promocao");
            int aditamento = Integer.parseInt(getValorTag(raiz, "adto"));

            // Extrai dados dos avaliados
            List<DadosXmlDTO.AvaliadoXmlDTO> avaliados = new ArrayList<>();
            NodeList listaAvaliados = documento.getElementsByTagName("avaliado");

            for (int i = 0; i < listaAvaliados.getLength(); i++) {
                Node nodeAvaliado = listaAvaliados.item(i);
                if (nodeAvaliado.getNodeType() == Node.ELEMENT_NODE) {
                    Element elementAvaliado = (Element) nodeAvaliado;
                    avaliados.add(new DadosXmlDTO.AvaliadoXmlDTO(
                            elementAvaliado.getAttribute("nip"),
                            getValorTag(elementAvaliado, "nome"),
                            getValorTag(elementAvaliado, "nome_de_guerra"),
                            Integer.parseInt(getValorTag(elementAvaliado, "sequencia")),
                            getValorTag(elementAvaliado, "ecd_esp"),
                            getValorTag(elementAvaliado, "antiguidade"),
                            getValorTag(elementAvaliado, "posto"),
                            null, // Quadro do avaliado não está no XML, será mapeado
                            ((Element) elementAvaliado.getElementsByTagName("om").item(0)).getAttribute("sigla"),
                            getValorTag(elementAvaliado, "fotobase64")
                    ));
                }
            }

            return new DadosXmlDTO(nipAvaliador, nomeAvaliador, postoAvaliador, quadroAvaliador, dataLimite,
                    eventoCodigo, eventoDataDescritiva, situacaoPromocao, aditamento, avaliados);
        }
    }

    private String getValorTag(Element elemento, String nomeTag) {
        NodeList nodeList = elemento.getElementsByTagName(nomeTag);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            if (node != null) {
                return node.getTextContent();
            }
        }
        return "";
    }
}