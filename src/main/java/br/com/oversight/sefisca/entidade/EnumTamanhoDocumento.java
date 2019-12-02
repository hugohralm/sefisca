package br.com.oversight.sefisca.entidade;

import br.com.ambientinformatica.util.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumTamanhoDocumento implements IEnum {

    TREZENTOS_KB("300 kilobyte (kB)", 307200), UM_MEGA("1 megabyte (MB)", 1048576),
    DOIS_MEGA("2 megabyte (MB)", 2097152), TRES_MEGA("3 megabyte (MB)", 3145728),
    QUATRO_MEGA("4 megabyte (MB)", 4194304), CINCO_MEGA("5 megabyte (MB)", 5242880);

    private final String descricao;

    private final int tamanho;
}
