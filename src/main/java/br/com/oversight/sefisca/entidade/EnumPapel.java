package br.com.oversight.sefisca.entidade;

import br.com.ambientinformatica.util.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumPapel implements IEnum {
    
    ADMIN("Administrador"),
    COORDENADOR("Coordenador do Departamento de Fiscalização"),
    FISCAL("Enfermeiro Fisca"),
    AUXILIAR_FISCALIZACAO("Auxiliar de fiscalização"),
    AGENTE_ADMINISTRATIVO("Agente Administrativo"),
    USUARIO("Usuário");

    private final String descricao;

}
