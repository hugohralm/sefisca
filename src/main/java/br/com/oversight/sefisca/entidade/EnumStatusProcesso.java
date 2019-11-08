package br.com.oversight.sefisca.entidade;

import br.com.ambientinformatica.util.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumStatusProcesso implements IEnum {

    ATIVO("Ativo"),
    SUSPENSO("Suspenso"),
    CANCELADO("Cancelado");

    private final String descricao;
}