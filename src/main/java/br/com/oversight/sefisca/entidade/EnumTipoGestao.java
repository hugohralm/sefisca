package br.com.oversight.sefisca.entidade;

import br.com.ambientinformatica.util.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumTipoGestao implements IEnum {

    M("Municipal"),
    E("Estadual"),
    D("Dupla"),
    S("Sem gestão");

    private final String descricao;
}