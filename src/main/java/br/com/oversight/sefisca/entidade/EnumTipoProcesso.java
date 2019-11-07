package br.com.oversight.sefisca.entidade;

import br.com.ambientinformatica.util.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumTipoProcesso implements IEnum {

    ORDINARIO("Ordinário"),
    EXTRAORDINARIO("Extraordinário");

    private final String descricao;
}