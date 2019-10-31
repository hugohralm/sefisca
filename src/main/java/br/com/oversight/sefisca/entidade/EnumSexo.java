package br.com.oversight.sefisca.entidade;

import java.util.ArrayList;
import java.util.List;

import br.com.ambientinformatica.util.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumSexo implements IEnum {

     M("Masculino", true), F("Feminino", true);
    
    private final String descricao;
    private final boolean visivel;
    
    public static EnumSexo[] valuesVisivel() {
        List<EnumSexo> lista = new ArrayList<>();
        for (EnumSexo v : values()) {
            if (v.isVisivel()) {
                lista.add(v);
            }
        }
        return lista.toArray(new EnumSexo[lista.size()]);
    }
}