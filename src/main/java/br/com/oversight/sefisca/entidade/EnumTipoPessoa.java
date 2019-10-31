package br.com.oversight.sefisca.entidade;

import br.com.ambientinformatica.util.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumTipoPessoa implements IEnum {

    PF("Pessoa Física"),
    PJ("Pessoa Jurídica");

    private final String descricao;
}