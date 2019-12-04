package br.com.oversight.sefisca.entidade;

import br.com.ambientinformatica.util.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumStatusEtapa implements IEnum {

    ATIVA("Ativa"),
    SUSPENSA("Suspensa"),
    CANCELADA("Cancelada"),
    ADMITIDA("Admitida");

    private final String descricao;
}