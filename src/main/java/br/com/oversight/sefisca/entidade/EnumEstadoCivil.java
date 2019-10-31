package br.com.oversight.sefisca.entidade;

import br.com.ambientinformatica.util.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumEstadoCivil implements IEnum {

    SOLTEIRO("Solteiro(a)"),
    CASADO("Casado(a)"),
    VIUVO("Viúvo(a)"),
    SEPARADO("Separado(a) judicialmente"),
    DIVORCIADO("Divorciado(a)");

    @Getter
    private final String descricao;
}