package br.com.oversight.sefisca.entidade;

import br.com.ambientinformatica.util.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumMotivoProcesso implements IEnum {

    PLANEJAMENTO("Planejamento"),
    DENUNCIA("Denúncia");

    private final String descricao;
}