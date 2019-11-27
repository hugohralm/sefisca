package br.com.oversight.sefisca.entidade;

import br.com.ambientinformatica.util.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumSituacaoInstituicao implements IEnum {

    INDIVIDUAL("Individual"),
    MANTIDA("Mantida");

    private final String descricao;
}