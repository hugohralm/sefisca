package br.com.oversight.sefisca.entidade;

import br.com.ambientinformatica.util.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumTipoDocumento implements IEnum {

    INTERNO("Interno"),
    EXTERNO("Externo");

    private final String descricao;
}