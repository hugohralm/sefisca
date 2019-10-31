package br.com.oversight.sefisca.entidade;

import br.com.ambientinformatica.util.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumPapel implements IEnum {

    /**
     * ADMIN: Usuários administrador do sistema, com permissão total; 
     * GERENTE: Usuário administrador da área finalistica, tem acesso a consulta de todas as funcionalidades;
     * ORGAO : Usuário que com acesso restrito a atualização das informções dos órgãos e casdastro das vagas.;
     * USUÁRIO: Usuário comum;
     **/

    ADMIN("Administrador"),
    GERENTE("Gerente"),
    ORGAO("Órgão Externo - Acordo de Cooperação Técnica."),
    USUARIO("Usuário");

    private final String descricao;

}
