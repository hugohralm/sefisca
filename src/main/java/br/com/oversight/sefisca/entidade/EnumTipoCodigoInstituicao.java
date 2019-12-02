package br.com.oversight.sefisca.entidade;

import br.com.ambientinformatica.util.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumTipoCodigoInstituicao implements IEnum {

    CNES("CNES"),
    CNPJ("CNPJ");

    @Getter
    private final String descricao;
}