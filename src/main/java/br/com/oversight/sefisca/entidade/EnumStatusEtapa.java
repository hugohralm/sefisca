package br.com.oversight.sefisca.entidade;

import br.com.ambientinformatica.util.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumStatusEtapa implements IEnum {

    ATIVO("Ativo"),
    SUSPENSO("Suspenso"),
    CANCELADO("Cancelado"),
    ADMITIDO("Admitido");

    private final String descricao;
}