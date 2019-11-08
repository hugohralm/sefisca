package br.com.oversight.sefisca.entidade;

import br.com.ambientinformatica.util.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumEtapaProcesso implements IEnum {

    ABERTURA("Abertura do processo"),
    DESIGNACAO("Designação"),
    FISCALIZACAO("Fiscalização"),
    AVALIACAO("Avaliação técnica da Ação");

    private final String descricao;
}